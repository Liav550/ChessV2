package com.chess.engine.pieces;

public enum PieceType {
    PAWN("P"){
        @Override
        public boolean isKing() {
            return false;
        }

        @Override
        public boolean isRook() {
            return false;
        }
    },
    KNIGHT("N"){
        @Override
        public boolean isKing() {
            return false;
        }

        @Override
        public boolean isRook() {
            return false;
        }
    },
    BISHOP("B"){
        @Override
        public boolean isKing() {
            return false;
        }

        @Override
        public boolean isRook() {
            return false;
        }
    },
    ROOK("R"){
        @Override
        public boolean isKing() {
            return false;
        }

        @Override
        public boolean isRook() {
            return true;
        }
    },
    QUEEN("Q"){
        @Override
        public boolean isKing() {
            return false;
        }

        @Override
        public boolean isRook() {
            return false;
        }
    },
    KING("K"){
        @Override
        public boolean isKing() {
            return true;
        }

        @Override
        public boolean isRook() {
            return false;
        }
    };
    private String pieceLetter; // the letter that represents the piece.
    PieceType(String pieceName){
        this.pieceLetter = pieceName;
    }
    public abstract boolean isKing(); // determines if the piece is a king or not.
    public abstract boolean isRook(); // determines if the piece is a rook or not.
    @Override
    public String toString(){
        return this.pieceLetter;
    }
}
