package com.chess.openings;

import com.chess.engine.board.Board;
import com.chess.engine.moves.Move;
import com.chess.pgn.PGNUtilities;
import com.mongodb.client.*;
import org.bson.Document;

import java.util.*;

public class OpeningBook {
    private static final String PATH = "mongodb://localhost:27017/";
    private static final char[] SECTION_OPTIONS = {'A','B','C','D','E'};
    private final MongoClient client;
    private final MongoDatabase db;
    private MongoCollection<Document> currentCollection;

    public OpeningBook() {
        this.client = MongoClients.create(PATH);
        this.db = client.getDatabase("chess");
    }

    public void setCurrentCollection(char sectionLetter){
        this.currentCollection = db.getCollection("section"+ sectionLetter);
    }

    public void setRandomCollection(){
        Random random = new Random();
        char chosenCollection = SECTION_OPTIONS[random.nextInt(SECTION_OPTIONS.length)];
        this.setCurrentCollection(chosenCollection);
    }

    public Move getNextMove(Board board, List<Move> moveHistory){
        List<Document> shuffledDocuments = new ArrayList<>();
        MongoCursor<Document> cursor = currentCollection.find().iterator();
        Document getDocument;
        while(cursor.hasNext()){
            getDocument = cursor.next();
            shuffledDocuments.add(getDocument);
        }
        Collections.shuffle(shuffledDocuments);
        int comparisonResult;
        for(Document document: shuffledDocuments){
            comparisonResult = PGNUtilities.containsPGNString
                    ( moveHistory,document.getString("pgn").split(" "));
            if(comparisonResult == -1){
                continue;
            }
            return PGNUtilities.createMove(board,
                    PGNUtilities.convertDatabasePGN(document.getString("pgn").split(" "))[comparisonResult]);
        }
        return null; // meaning we are out of theory
    }

    public Move getFirstMove(Board board){
        this.setRandomCollection();
        AggregateIterable<Document> randomDocument = currentCollection.aggregate(
                Arrays.asList(
                        new Document("$sample", new Document("size", 1))
                )
        );
        for(Document document: randomDocument){
            return PGNUtilities.createMove(board, document.getString("pgn").split(" ")[1]);
        }
        return null;
    }

    public void closeDBConnection(){
        this.client.close();
    }
}
