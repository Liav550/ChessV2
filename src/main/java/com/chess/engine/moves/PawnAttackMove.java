package com.chess.engine.moves;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.pieces.Piece;

public class PawnAttackMove extends AttackMove {
    public PawnAttackMove(Board board, Piece movedPiece, int destinationIndex, Piece attackedPiece) {
        super(board, movedPiece, destinationIndex, attackedPiece);
    }

    @Override
    public boolean equals(Object other) {
        return this == other || other instanceof com.chess.engine.moves.PawnAttackMove && super.equals(other);
    }

    @Override
    public String toString() {
        return BoardUtils.getNotationAtIndex(this.movedPiece.getPiecePosition()).charAt(0) +
                "x" +
                BoardUtils.getNotationAtIndex(this.destinationIndex);
    }
}
