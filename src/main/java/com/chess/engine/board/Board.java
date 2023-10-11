package com.chess.engine.board;

import com.chess.engine.Alliance;
import com.chess.engine.pieces.*;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.*;

public class Board {
    private final List<Tile> gameBoard;
    private final Collection<Piece> whitePieces;
    private final Collection<Piece> blackPieces;
    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;
    private final Player currentPlayer;

    private Board(Builder builder){
        this.gameBoard = createGameBoard(builder);
        this.whitePieces = calculateActivePieces(this.gameBoard,Alliance.WHITE);
        this.blackPieces = calculateActivePieces(this.gameBoard,Alliance.BLACK);

        Collection<Move> whiteStandardLegalMoves = calculateLegalMoves(this.whitePieces);
        Collection<Move> blackStandardLegalMoves = calculateLegalMoves(this.blackPieces);

        this.whitePlayer = new WhitePlayer(this,whiteStandardLegalMoves,blackStandardLegalMoves);
        this.blackPlayer = new BlackPlayer(this,whiteStandardLegalMoves,blackStandardLegalMoves);

        this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.whitePlayer,this.blackPlayer);
    }

    private static Collection<Piece> calculateActivePieces(List<Tile> gameBoard, Alliance alliance) {
        List<Piece> activePieces = new ArrayList<>();
        for(Tile tile: gameBoard){
            if(!tile.isTileOccupied()){
                continue;
            }
            if(tile.getPiece().getPieceAlliance() == alliance){
                activePieces.add(tile.getPiece());
            }
        }
        return ImmutableList.copyOf(activePieces);
    }
    private Collection<Move> calculateLegalMoves(Collection<Piece> pieces){
        List<Move> legalMoves = new ArrayList<>();
        for(Piece piece: pieces){
            legalMoves.addAll(piece.calculateLegalMoves(this));
        }
        return ImmutableList.copyOf(legalMoves);
    }

    public static List<Tile> createGameBoard(Builder builder){
        final Tile[] tiles = new Tile[BoardUtils.NUMBER_OF_TILES];
        for(int i =0;i<BoardUtils.NUMBER_OF_TILES;i++){
            tiles[i] = Tile.createTile(i, builder.boardConfig.get(i));
        }
        return ImmutableList.copyOf(tiles);
    }

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
            builder.setPiece(new Pawn(i,Alliance.BLACK));
        }

        for(i= 48;i<56;i++){
            builder.setPiece(new Pawn(i,Alliance.WHITE));
        }

        builder.setPiece(new Rook(56,Alliance.WHITE));
        builder.setPiece(new Knight(57,Alliance.WHITE));
        builder.setPiece(new Bishop(58,Alliance.WHITE));
        builder.setPiece(new Queen(59,Alliance.WHITE));
        builder.setPiece(new King(60,Alliance.WHITE));
        builder.setPiece(new Bishop(61,Alliance.WHITE));
        builder.setPiece(new Knight(62,Alliance.WHITE));
        builder.setPiece(new Rook(63,Alliance.WHITE));

        builder.setMoveMaker(Alliance.WHITE);

        return builder.build();
    }
    public Tile getTile(int tileIndex){
        return gameBoard.get(tileIndex);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        String tileText;
        for (int i = 0; i < BoardUtils.NUMBER_OF_TILES; i++) {
            tileText = this.gameBoard.get(i).toString();
            builder.append(String.format("%3s", tileText));
            if ((i + 1) % 8 == 0) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    public Collection<Piece> getWhitePieces() {
        return whitePieces;
    }

    public Collection<Piece> getBlackPieces() {
        return blackPieces;
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

    public Iterable<Move> getAllLegalMoves() {
        return Iterables.unmodifiableIterable
                (Iterables.concat(whitePlayer.getLegalMoves(), blackPlayer.getLegalMoves()));
    }

    public static class Builder{
        private Map<Integer, Piece> boardConfig;
        private Alliance nextMoveMaker;
        private Pawn enPassentPawn;

        public Builder(){
            this.boardConfig = new HashMap<>();
        }
        public Builder setPiece(Piece piece){
            boardConfig.put(piece.getPiecePosition(), piece);
            return this;
        }
        public Builder setMoveMaker(Alliance alliance){
            this.nextMoveMaker = alliance;
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
