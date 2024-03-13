package com.chess.engine.moves;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.pieces.Piece;

public class MajorAttackMove extends AttackMove {

    public MajorAttackMove(Board board, Piece movedPiece, int destinationIndex, Piece attackedPiece) {
        super(board, movedPiece, destinationIndex, attackedPiece);
    }

    @Override
    public boolean equals(Object other) {
        return this == other || other instanceof MajorAttackMove && super.equals(other);
    }

    @Override
    public String toString() {
        return this.movedPiece.getPieceType().toString() + "x" + BoardUtils.getNotationAtIndex(this.destinationIndex);
    }
}
