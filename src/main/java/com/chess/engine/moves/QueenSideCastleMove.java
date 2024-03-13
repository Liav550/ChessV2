package com.chess.engine.moves;

import com.chess.engine.board.Board;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

public class QueenSideCastleMove extends CastleMove {
    public QueenSideCastleMove(Board board, Piece movedPiece, int destinationIndex,
                               Rook rook, int rookStart, int rookDest) {
        super(board, movedPiece, destinationIndex, rook, rookStart, rookDest);
    }

    @Override
    public boolean equals(Object other) {
        return this == other || other instanceof QueenSideCastleMove && super.equals(other);
    }

    @Override
    public String toString() {
        return "O-O-O";
    }
}
