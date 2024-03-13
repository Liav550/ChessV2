package com.chess.engine.pieces;

import com.chess.engine.board.Board;
import com.chess.engine.moves.Move;

import java.util.Collection;

/**
 * @author liavb
 * the Piece class is used to represent a piece in our board.
 */
public abstract class Piece {
    protected PieceType pieceType; // to know the piece's type along with a few more properties
    protected int piecePosition; // the current position of a piece
    protected Alliance pieceAlliance; // the alliance (color) of the piece
    protected boolean isFirstMove; // in order to know if the piece has moved yet

    public Piece(PieceType type, int position, Alliance alliance, boolean isFirstMove) {
        this.pieceType = type;
        this.piecePosition = position;
        this.pieceAlliance = alliance;
        this.isFirstMove = isFirstMove;
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
    public PieceType getPieceType(){
        return this.pieceType;
    }
    public int getPiecePosition(){return this.piecePosition;}
    public Alliance getPieceAlliance(){
        return this.pieceAlliance;
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
}
