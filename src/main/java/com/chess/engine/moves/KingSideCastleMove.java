package com.chess.engine.moves;

import com.chess.engine.board.Board;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

public final class KingSideCastleMove extends CastleMove {
    public KingSideCastleMove(Board board, Piece movedPiece, int destinationIndex,
                              Rook castleRook, int castleRookStart, int castleRookDestination) {
        super(board, movedPiece, destinationIndex, castleRook, castleRookStart, castleRookDestination);
    }

    @Override
    public boolean equals(Object other) {
        return this == other || other instanceof KingSideCastleMove && super.equals(other);
    }

    @Override
    public String toString() {
        return "O-O";
    }
}
