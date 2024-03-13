package com.chess.engine.moves;

import com.chess.engine.board.Board;
import com.chess.engine.board.Builder;
import com.chess.engine.pieces.Piece;

import static com.chess.engine.board.Board.*;
//TODO DOCUMENT THIS CLASS AT THE END
public abstract class Move {
    protected Board board; // the board in which the move happens
    protected Piece movedPiece; // the moved piece
    protected int destinationIndex; // the destination in which the piece will land
    protected boolean isFirstMove;

    public static final Move NULL_MOVE = new NullMove();

    public Move(Board board, Piece movedPiece, int destinationIndex) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationIndex = destinationIndex;
        this.isFirstMove = movedPiece.isFirstMove();
    }
    public Move(Board board, int destinationIndex){
        this.board = board;
        this.destinationIndex = destinationIndex;
        this.movedPiece = null;
        this.isFirstMove = false;
    }

    public Piece getMovedPiece() {
        return movedPiece;
    }
    public int getDestinationIndex() {
        return destinationIndex;
    }
    public int getCurrentIndex(){return this.movedPiece.getPiecePosition();}

    public Piece getAttackedPiece(){return null;}

    public Board execute() {
        Builder builder = new Builder();
        for(Piece piece: this.board.getCurrentPlayer().getActivePieces()){
            if(!this.movedPiece.equals(piece)){
                builder.setPiece(piece);
            }
        }
        for(Piece piece: this.board.getCurrentPlayer().getOpponent().getActivePieces()){
            builder.setPiece(piece);
        }
        builder.setPiece(this.movedPiece.movePiece(this));
        builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
        return builder.build();
    }
    public boolean isAttack(){return false;}
    public boolean isCastlingMove(){return false;}
    public Board getBoard(){
        return this.board;
    }
    @Override
    public boolean equals(Object other) {
        if(this == other){
            return true;
        }
        if(!(other instanceof Move)){
            return false;
        }
        Move otherMove = (Move) other;
        return this.getDestinationIndex() == otherMove.getDestinationIndex() &&
               this.getMovedPiece().equals(((Move) other).getMovedPiece()) &&
                this.getCurrentIndex() == otherMove.getCurrentIndex();
    }
    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + this.destinationIndex;
        result = 31 * result + this.movedPiece.hashCode();
        result = 31 * result + this.movedPiece.getPiecePosition();
        return result;
    }


}
