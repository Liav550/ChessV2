package com.chess.engine.moves;

import com.chess.engine.board.Board;
import com.chess.engine.pieces.Piece;

public class AttackMove extends Move {
    private final Piece attackedPiece; // the piece that is being attacked by the moved piece

    public AttackMove(Board board, Piece movedPiece, int destinationIndex, Piece attackedPiece) {
        super(board, movedPiece, destinationIndex);
        this.attackedPiece = attackedPiece;
    }

    @Override
    public Piece getAttackedPiece() {
        return attackedPiece;
    }

    @Override
    public boolean isAttack() {
        return true;
    }

    @Override
    public int hashCode() {
        return this.attackedPiece.hashCode() + super.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof com.chess.engine.moves.AttackMove)) {
            return false;
        }
        com.chess.engine.moves.AttackMove otherAttackMove = (com.chess.engine.moves.AttackMove) other;
        return super.equals(otherAttackMove) && this.attackedPiece.equals(((com.chess.engine.moves.AttackMove) other).getAttackedPiece());
    }
}
