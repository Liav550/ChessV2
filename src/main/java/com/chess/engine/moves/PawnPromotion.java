package com.chess.engine.moves;

import com.chess.engine.board.Board;
import com.chess.engine.board.Builder;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;

public final class PawnPromotion extends Move {
    private Move mainMove;
    private Pawn promotedPawn;

    public PawnPromotion(Move mainMove) {
        super(mainMove.getBoard(), mainMove.getMovedPiece(), mainMove.getDestinationIndex());
        this.mainMove = mainMove;
        this.promotedPawn = (Pawn) mainMove.getMovedPiece();
    }

    @Override
    public Board execute() {
        Board afterMainMove = mainMove.execute();
        Builder builder = new Builder();
        for (Piece piece : afterMainMove.getCurrentPlayer().getActivePieces()) {
            if (!this.promotedPawn.equals(piece)) {
                builder.setPiece(piece);
            }
        }
        for (Piece piece : afterMainMove.getCurrentPlayer().getOpponent().getActivePieces()) {
            builder.setPiece(piece);
        }
        builder.setPiece(this.promotedPawn.getPromotionPiece().movePiece(this));
        builder.setMoveMaker(afterMainMove.getCurrentPlayer().getAlliance());
        return builder.build();
    }

    @Override
    public boolean isAttack() {
        return mainMove.isAttack();
    }

    @Override
    public Piece getAttackedPiece() {
        return mainMove.getAttackedPiece();
    }

    @Override
    public boolean equals(Object other) {
        return this == other || other instanceof com.chess.engine.moves.PawnPromotion && this.mainMove.equals(other);
    }

    @Override
    public int hashCode() {
        return mainMove.hashCode() + 31 * promotedPawn.hashCode();
    }

    @Override
    public String toString() {
        return mainMove.toString() + "=" + promotedPawn.getPromotionPiece().toString();
    }
}
