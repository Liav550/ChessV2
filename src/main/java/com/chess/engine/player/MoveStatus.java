package com.chess.engine.player;

/**
 * @author liavb
 * the MoveStatus enumaration is used in order to keep track on move executions.
 */
public enum MoveStatus {
    DONE{
        @Override
        public boolean isDone() {
            return true;
        }
    },
    ILLEGAL_MOVE{
        @Override
        public boolean isDone() {
            return false;
        }
    },
    LEAVES_PLAYER_IN_CHECK{
        @Override
        public boolean isDone() {
            return false;
        }
    };
    public abstract boolean isDone();
}
