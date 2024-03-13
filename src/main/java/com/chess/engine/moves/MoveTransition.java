package com.chess.engine.moves;

import com.chess.engine.board.Board;

/**
 * @author liavb
 * the MoveTransition class is used for tracking transitions between board after each move
 * (since each board is immutable).
 */
public class MoveTransition {
    private Board transitionBoard; // the board we transition into
    private Move move;
    private MoveStatus moveStatus; // the status on the transition

    public MoveTransition(Board transitionBoard, Move move, MoveStatus moveStatus) {
        this.transitionBoard = transitionBoard;
        this.move = move;
        this.moveStatus = moveStatus;
    }

    public MoveStatus getMoveStatus() {
        return this.moveStatus;
    }

    public Board getTransitionBoard() {
        return this.transitionBoard;
    }
}
