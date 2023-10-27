package com.chess.engine.moves;

import com.chess.engine.board.Board;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

public abstract class CastleMove extends Move {
    protected final Rook castleRook;
    protected final int castleRookStart;
    protected final int castleRookDestination;

    public CastleMove(Board board, Piece movedPiece, int destinationIndex,
                      Rook castleRook, int castleRookStart, int castleRookDestination) {
        super(board, movedPiece, destinationIndex);
        this.castleRook = castleRook;
        this.castleRookStart = castleRookStart;
        this.castleRookDestination = castleRookDestination;
    }

    public Rook getCastleRook() {
        return castleRook;
    }

    @Override
    public boolean isCastlingMove() {
        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + this.castleRook.hashCode();
        result = 31 * result + this.castleRookDestination;
        return result;
    }

    @Override
    public boolean equals(Object other) {
        return this == other || other instanceof CastleMove && super.equals(other) &&
                this.castleRook.equals(((CastleMove) other).getCastleRook());
    }

    @Override
    public Board execute() {
        Board.Builder builder = new Board.Builder();
        for (Piece piece : board.getCurrentPlayer().getActivePieces()) {
            if (!this.movedPiece.equals(piece) && !castleRook.equals(piece)) {
                builder.setPiece(piece);
            }
        }
        for (Piece piece : board.getCurrentPlayer().getOpponent().getActivePieces()) {
            builder.setPiece(piece);
        }
        builder.setPiece(this.movedPiece.movePiece(this));
        builder.setPiece(new Rook(this.castleRookDestination, this.castleRook.getPieceAlliance()));
        builder.setMoveMaker(board.getCurrentPlayer().getOpponent().getAlliance());
        return builder.build();
    }
}
