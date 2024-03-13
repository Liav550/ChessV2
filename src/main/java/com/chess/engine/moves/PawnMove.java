package com.chess.engine.moves;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.pieces.Piece;

public class PawnMove extends Move {
    public PawnMove(Board board, Piece movedPiece, int destinationIndex) {
        super(board, movedPiece, destinationIndex);
    }

    @Override
    public boolean equals(Object other) {
        return this == other || other instanceof com.chess.engine.moves.PawnMove && super.equals(other);
    }

    @Override
    public String toString() {
        return BoardUtils.getNotationAtIndex(this.destinationIndex);
    }
}
