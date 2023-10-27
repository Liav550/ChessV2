package com.chess.engine.moves;

import com.chess.engine.board.Board;
import com.chess.engine.pieces.Piece;

public final class PawnEnPassentAttackMove extends PawnAttackMove {
    public PawnEnPassentAttackMove(Board board, Piece movedPiece, int destinationIndex, Piece attackedPiece) {
        super(board, movedPiece, destinationIndex, attackedPiece);
    }

    @Override
    public boolean equals(Object other) {
        return this == other || other instanceof com.chess.engine.moves.PawnEnPassentAttackMove && super.equals(other);
    }

    @Override
    public Board execute() {
        Board.Builder builder = new Board.Builder();
        for (Piece piece : board.getCurrentPlayer().getActivePieces()) {
            if (!this.movedPiece.equals(piece)) {
                builder.setPiece(piece);
            }
        }
        for (Piece piece : board.getCurrentPlayer().getOpponent().getActivePieces()) {
            if (!this.getAttackedPiece().equals(piece)) {
                builder.setPiece(piece);
            }
        }
        builder.setPiece(this.movedPiece.movePiece(this));
        builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
        return builder.build();
    }
}
