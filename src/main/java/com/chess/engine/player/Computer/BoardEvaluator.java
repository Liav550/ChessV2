package com.chess.engine.player.Computer;

import com.chess.engine.board.Board;

/**
 * @author liavb
 * the BoardEvaluator inteface will be implemented by every class that represents a way to evaluate a board.
 */
public interface BoardEvaluator {
    int evaluate(Board board, int depth);
}
