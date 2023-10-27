package com.chess.engine.pieces;

import com.chess.engine.board.BoardUtils;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;

/**
 * @author liavb
 * the Alliance enumaration represents a color of the 2 sides/pieces.
 */
public enum Alliance {
    WHITE{
        @Override
        public int getDirection(){
            return -1;
        }

        @Override
        public int getOppositeDirection() {
            return 1;
        }

        @Override
        public boolean isBlack() {
            return false;
        }

        @Override
        public boolean isWhite() {
            return true;
        }

        @Override
        public boolean isPawnPromotionTile(int tileIndex) {
            return BoardUtils.isInRow(tileIndex, 0);
        }

        @Override
        public Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer) {
            return whitePlayer;
        }

        @Override
        public String toString() {
            return "White";
        }
    },
    BLACK{
        @Override
        public int getDirection(){
            return 1;
        }

        @Override
        public int getOppositeDirection() {
            return -1;
        }

        @Override
        public boolean isBlack() {
            return true;
        }

        @Override
        public boolean isWhite() {
            return false;
        }
        @Override
        public boolean isPawnPromotionTile(int tileIndex) {
            return BoardUtils.isInRow(tileIndex, 7);
        }

        @Override
        public Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer) {
            return blackPlayer;
        }


        @Override
        public String toString() {
            return "Black";
        }
    };
    public abstract int getDirection(); // the direction that pawns in this alliance type advance towards
    public abstract int getOppositeDirection(); // the opposite direction to the direction that pawns in this alliance
                                                // type advance towards
    public abstract boolean isBlack();
    public abstract boolean isWhite();
    public abstract boolean isPawnPromotionTile(int tileIndex);

    public abstract Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer);
}
