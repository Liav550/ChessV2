package com.chess.engine.moves;

import com.chess.engine.board.Board;

public final class NullMove extends Move {

    public NullMove() {
        super(null, -1);
    }

    @Override
    public Board execute() {
        throw new RuntimeException("can't execute a null move!!!");
    }
}
