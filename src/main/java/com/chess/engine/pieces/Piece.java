package com.chess.engine.pieces;

import com.chess.engine.board.Board;
import com.chess.engine.moves.Move;

import java.util.Collection;

/**
 * @author liavb
 * the Piece class is used to represent a piece in our board.
 */
public abstract class Piece {
    protected final int piecePosition; // the current position of a piece
    protected final Alliance pieceAlliance; // the alliance (color) of the piece
    protected final boolean isFirstMove; // in order to know if the piece has moved yet
    protected final PieceType pieceType; // to know the piece's type along with a few more properties
    private final int cachedHashCode; // since the Piece is immutable, we can store the hash code

    public Piece(PieceType pieceType, int piecePosition, Alliance alliance, boolean isFirstMove) {
        this.pieceType = pieceType;
        this.piecePosition = piecePosition;
        this.pieceAlliance = alliance;
        // TODO MORE WORK HERE
        this.isFirstMove = isFirstMove;
        this.cachedHashCode = calculateHashCode();
    }
    /**
     @param board: the board state that the piece is at.
     @return: a collection of all the legal moves the piece has.
     */
    public abstract Collection<Move> calculateLegalMoves(Board board);

    /**
     * @param move: the move that the piece is going to follow.
     * @return: a new piece from the same type as the current one, but the location of the new piece will be the
     *          destination point the move has.
     */
    public abstract Piece movePiece(Move move);
    public abstract int getLocationBonus();
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

    /**
     * @author liavb
     * The PieceTyoe enumaration is used to determine a piece's type and value.
     * Also, it includes a toString method that will help us to display the moved piece on the move log of the game.
     */
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
        KING(10000,"K"){
            @Override
            public boolean isKing() {
                return true;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        };
        private final String pieceLetter; // the letter that represents the piece.
        private final int value; // the value of that piece.
        PieceType(int value, String pieceName){
            this.value = value;
            this.pieceLetter = pieceName;
        }
        public abstract boolean isKing(); // determines if the piece is a king or not.
        public abstract boolean isRook(); // determines if the piece is a rook or not.
        public int getValue(){
            return value;
        }
        @Override
        public String toString(){
            return this.pieceLetter;
        }
    }
}
