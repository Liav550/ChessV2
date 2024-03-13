package com.chess.engine.moves;

import com.chess.engine.board.Board;
import com.chess.engine.board.Builder;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

public abstract class CastleMove extends Move {
    protected Rook castleRook;
    protected int castleRookStart;
    protected int castleRookDestination;

    public CastleMove(Board board, Piece movedPiece, int destination,
                          Rook rook, int rookStart, int rookDest) {
        super(board, movedPiece, destination);
        this.castleRook = rook;
        this.castleRookStart = rookStart;
        this.castleRookDestination = rookDest;
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
        Builder builder = new Builder();
        for (Piece piece : board.getCurrentPlayer().getActivePieces()) {
            if (!this.movedPiece.equals(piece) && !castleRook.equals(piece)) {
                builder.setPiece(piece);
            }
        }
        for (Piece piece : board.getCurrentPlayer().getOpponent().getActivePieces()) {
            builder.setPiece(piece);
        }
        builder.setPiece(this.movedPiece.movePiece(this));
        builder.setPiece(new Rook(this.castleRookDestination, this.castleRook.getPieceAlliance(), this.castleRook.getRookSide()));
        builder.setMoveMaker(board.getCurrentPlayer().getOpponent().getAlliance());
        return builder.build();
    }
}
