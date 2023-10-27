package com.chess.engine.moves;

import com.chess.engine.board.Board;

public class MoveFactory {
    public MoveFactory() {
        throw new RuntimeException("can't instantiate MoveFactory!!!");
    }

    public static Move createMove(Board board, int currentIndex, int destinationIndex) {
        for (Move move : board.getAllLegalMoves()) {
            if (move.getCurrentIndex() == currentIndex &&
                    move.getDestinationIndex() == destinationIndex) {
                return move;
            }
        }
        return Move.NULL_MOVE;
    }
}
