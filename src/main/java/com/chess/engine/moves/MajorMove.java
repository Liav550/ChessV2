package com.chess.engine.moves;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.pieces.Piece;

public final class MajorMove extends Move {
    public MajorMove(Board board, Piece movedPiece, int destinationIndex) {
        super(board, movedPiece, destinationIndex);
    }

    @Override
    public boolean equals(Object other) {
        return this == other || other instanceof MajorMove && super.equals(other);
    }

    @Override
    public String toString() {
        return movedPiece.getPieceType().toString() + BoardUtils.getNotationAtIndex(this.destinationIndex);
    }
}
