package com.chess.engine.player.Computer;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

/**
 * @author liavb
 * the MoveStartegy interface will be implemented bt every class which is a way of choosing a move.
 */
public interface MoveStrategy {
    Move execute(Board board, int depth);
}
