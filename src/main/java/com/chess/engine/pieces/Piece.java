package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import java.util.Collection;

public abstract class Piece {
    protected final int piecePosition;
    protected final Alliance pieceAlliance;
    protected final boolean isFirstMove;
    protected final PieceType pieceType;
    private final int cachedHashCode;

    public Piece(PieceType pieceType, int piecePosition, Alliance alliance, boolean isFirstMove) {
        this.pieceType = pieceType;
        this.piecePosition = piecePosition;
        this.pieceAlliance = alliance;
        // TODO MORE WORK HERE
        this.isFirstMove = isFirstMove;
        this.cachedHashCode = calculateHashCode();
    }
    /*
        the following function is shared with all Piece subclass.
        Its purpose is to generate all the legal moves of a certain piece in a given board.
     */
    public abstract Collection<Move> calculateLegalMoves(Board board);
    public abstract Piece movePiece(Move move);
    public Alliance getPieceAlliance(){
        return this.pieceAlliance;
    }
    public int getPiecePosition(){return this.piecePosition;}
    public PieceType getPieceType(){
        return this.pieceType;
    }
    public int getPieceValue(){
        return this.pieceType.getValue();
    }

    public boolean isFirstMove() {
        return isFirstMove;
    }

    @Override
    public boolean equals(Object other) {
        if(this == other){
            return true;
        }
        if(!(other instanceof Piece)){
            return false;
        }
        Piece otherPiece = (Piece) other;
        return pieceType == otherPiece.pieceType && pieceAlliance == otherPiece.pieceAlliance &&
                isFirstMove == otherPiece.isFirstMove && piecePosition == otherPiece.piecePosition;
    }

    @Override
    public int hashCode(){
        return this.cachedHashCode;
    }

    public int calculateHashCode() {
        int result = pieceType.hashCode();
        result = 31 * result + pieceAlliance.hashCode();
        result = 31 * result + (isFirstMove? 1: 0);
        result = 31 * result + piecePosition;
        return result;
    }

    public enum PieceType{
        PAWN(100,"P"){
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KNIGHT(300,"N"){
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        BISHOP(300,"B"){
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        ROOK(500,"R"){
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return true;
            }
        },
        QUEEN(900,"Q"){
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KING(1000,"K"){
            @Override
            public boolean isKing() {
                return true;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        };
        private final  String pieceName;
        private final int value;
        PieceType(int value, String pieceName){
            this.value = value;
            this.pieceName = pieceName;
        }
        public abstract boolean isKing();
        public abstract boolean isRook();
        public int getValue(){
            return value;
        }
        @Override
        public String toString(){
            return this.pieceName;
        }
    }
}
