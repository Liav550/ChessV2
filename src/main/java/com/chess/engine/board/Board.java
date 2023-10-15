package com.chess.engine.board;

import com.chess.engine.Alliance;
import com.chess.engine.pieces.*;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.*;

/**
 * @author liavb
 * the Board class represents a chess board. (NOTE: this is not the gui chess board. this class is for the logic
 * and validations)
 */

public class Board {
    private final List<Tile> gameBoard; // the representation of the board as a list of 64 tiles
    private final Collection<Piece> activeWhitePieces; // stores all the white active pieces
    private final Collection<Piece> activeBlackPieces; // stores all the black active pieces
    private final WhitePlayer whitePlayer; // represents the white player on the board
    private final BlackPlayer blackPlayer; // represents the black player on the board
    private final Player currentPlayer; // the player who possesses the right to play right now
    private final Pawn enPassentPawn; // the pawn that just moved 2 squares on the previous board (if exists)

    private Board(Builder builder){
        this.gameBoard = createGameBoard(builder);

        this.activeWhitePieces = calculateActivePieces(this.gameBoard,Alliance.WHITE);
        this.activeBlackPieces = calculateActivePieces(this.gameBoard,Alliance.BLACK);

        this.enPassentPawn = builder.enPassentPawn;

        Collection<Move> whiteStandardLegalMoves = calculateLegalMoves(this.activeWhitePieces); // calculating white's
                                                                                                // standard legal moves
        Collection<Move> blackStandardLegalMoves = calculateLegalMoves(this.activeBlackPieces); // calculating black's
                                                                                                // standard legal moves

        this.whitePlayer = new WhitePlayer(this,whiteStandardLegalMoves,blackStandardLegalMoves);
        this.blackPlayer = new BlackPlayer(this,whiteStandardLegalMoves,blackStandardLegalMoves);

        this.currentPlayer = builder.currentMoveMaker.choosePlayer(this.whitePlayer,this.blackPlayer);
    }

    /**
     @param gameBoard: the board that the calculations be executed on (since the method is static we can't access
                       this.gameBoard).
     @param alliance: the color of the active pieces we want to find.

     @return: a collection of all active pieces in the color the method is given.
    */
    private static Collection<Piece> calculateActivePieces(List<Tile> gameBoard, Alliance alliance) {
        List<Piece> activePieces = new ArrayList<>();
        for(Tile tile: gameBoard){
            if(!tile.isTileOccupied()){ // if the tile is empty, there is no piece in there, so we can continue on
                continue;
            }
            if(tile.getPiece().getPieceAlliance() == alliance){ // if the piece is with the same alliance as the given
                                                                // one, we can add it to our list
                activePieces.add(tile.getPiece());
            }
        }
        return ImmutableList.copyOf(activePieces); // returning an immutable copy of the list
    }

    /**
     @param pieces: the collection of the pieces that their legal moves need to be calculated.
     @return: a collection of the legal moves of all pieces.
     */
    private Collection<Move> calculateLegalMoves(Collection<Piece> pieces){
        List<Move> legalMoves = new ArrayList<>();
        for(Piece piece: pieces){
            legalMoves.addAll(piece.calculateLegalMoves(this)); // add the list of the current piece's legal moves
                                                                      // to the main list
        }
        return ImmutableList.copyOf(legalMoves); // returning an immutable copy of the list
    }

    /**
        @return: the legal moves for both sides in a given board.
     */
    public Iterable<Move> getAllLegalMoves() {
        return Iterables.unmodifiableIterable
                (Iterables.concat(whitePlayer.getLegalMoves(), blackPlayer.getLegalMoves()));
    }

     /**
        @param builder: a state of a certain board.
        @return: a chess board created with the help of the builder.
     */
    public static List<Tile> createGameBoard(Builder builder){
        final Tile[] tiles = new Tile[BoardUtils.NUMBER_OF_TILES];
        for(int i =0;i<BoardUtils.NUMBER_OF_TILES;i++){
            tiles[i] = Tile.createTile(i, builder.boardConfig.get(i)); // creating a tile with a piece matching to the
                                                                       // builder's boardConfig map
        }
        return ImmutableList.copyOf(tiles); // returning an immutable copy of the list
    }

    /**
        @return: a standard chess board (a white rook stands in the bottom left corner
        with a knight next to it, and so on).
     */
    public static Board createStandardBoard(){
        Builder builder = new Builder();
        int i;

        builder.setPiece(new Rook(0, Alliance.BLACK));
        builder.setPiece(new Knight(1, Alliance.BLACK));
        builder.setPiece(new Bishop(2, Alliance.BLACK));
        builder.setPiece(new Queen(3, Alliance.BLACK));
        builder.setPiece(new King(4, Alliance.BLACK));
        builder.setPiece(new Bishop(5, Alliance.BLACK));
        builder.setPiece(new Knight(6, Alliance.BLACK));
        builder.setPiece(new Rook(7, Alliance.BLACK));

        for (i=8;i < 16; i++) {
            builder.setPiece(new Pawn(i,Alliance.BLACK)); // setting black's pawns in their places
        }

        for(i= 48;i<56;i++){
            builder.setPiece(new Pawn(i,Alliance.WHITE)); // setting white's pawns in their places
        }

        builder.setPiece(new Rook(56,Alliance.WHITE));
        builder.setPiece(new Knight(57,Alliance.WHITE));
        builder.setPiece(new Bishop(58,Alliance.WHITE));
        builder.setPiece(new Queen(59,Alliance.WHITE));
        builder.setPiece(new King(60,Alliance.WHITE));
        builder.setPiece(new Bishop(61,Alliance.WHITE));
        builder.setPiece(new Knight(62,Alliance.WHITE));
        builder.setPiece(new Rook(63,Alliance.WHITE));

        builder.setMoveMaker(Alliance.WHITE); // the first move belongs to white

        return builder.build(); // the builder builds the board, and the method returns it
    }

    /**
        The method below receives an index, and returns the tile in the board that match that index.

        @param tileIndex: an index of a tile on the board
        @return: the tile itself with its properties (piece occupying etc)
     */
    public Tile getTile(int tileIndex){
        return gameBoard.get(tileIndex);
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        String tileText;
        for (int i = 0; i < BoardUtils.NUMBER_OF_TILES; i++) {
            tileText = this.gameBoard.get(i).toString(); // get the text that describes the tile
                                                         // (by the piece on it, if exists)
            builder.append(String.format("%3s", tileText)); // adding the text of the current tile to the string builder
            if ((i + 1) % 8 == 0) {
                builder.append("\n"); // skipping to the next line if we finished the current row
            }
        }
        return builder.toString(); // returning a string version of the string builder
    }

    public Collection<Piece> getActiveWhitePieces() {
        return activeWhitePieces;
    }

    public Collection<Piece> getActiveBlackPieces() {
        return activeBlackPieces;
    }

    public WhitePlayer getWhitePlayer() {
        return whitePlayer;
    }

    public BlackPlayer getBlackPlayer() {
        return blackPlayer;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Pawn getEnPassentPawn(){
        return this.enPassentPawn;
    }

    /**
     * @author liavb
     * the Builder class is used to build an instance of the Board class.
     */
    public static class Builder{
        private final Map<Integer, Piece> boardConfig; // a hashMap. the key is an index of a tile and
                                                       // the value is the piece on that tile.
        private Alliance currentMoveMaker; // the side that the right to play belongs to.
        private Pawn enPassentPawn; // the pawn that just moved 2 squares on the previous board (if exists).

        public Builder(){
            this.boardConfig = new HashMap<>();
        }
        public Builder setPiece(Piece piece){
            boardConfig.put(piece.getPiecePosition(), piece); // putting a new piece in the boardConfig
            return this;
        }
        public Builder setMoveMaker(Alliance alliance){
            this.currentMoveMaker = alliance;
            return this;
        }
        public Board build(){
            return new Board(this);
        }

        public void setEnPassentPawn(Pawn pawn) {
            this.enPassentPawn = pawn;
        }
    }
}
