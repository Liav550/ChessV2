package com.chess.engine.player;

import com.chess.engine.board.Board;
import com.chess.engine.moves.Move;

/**
 * @author liavb
 * the MoveStartegy interface will be implemented bt every class which is a way of choosing a move.
 */
public interface MoveStrategy {
    Move execute(Board board);
}
