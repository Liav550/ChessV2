package com.chess.engine.board;

public class BoardUtils {
    private BoardUtils(){ // a constructor that throws an exception if someone tries to instantiate this class.
        throw new RuntimeException("You can't instantiate BoardUtils!!!");
    }
    public static final int NUMBER_OF_TILES = 64;

    /*
        the following function checks if a given tile index is legal or not,
        and returns true/false accordingly.
     */
    public static boolean isValidTileIndex(int tileIndex){
        return tileIndex>=0 && tileIndex<NUMBER_OF_TILES;
    }

    /*
        the following function checks if a certain tile index (a number between 0 and 63) is in a specific
        column, and returns true/false accordingly.
     */
    public static boolean isInColumn(int tileIndex, int column){
        return tileIndex%8 == column;
    }

    /*
        the following function checks if a certain tile index (a number between 0 and 63) is in a specific
        row, and returns true/false accordingly.
     */
    public static boolean isInRow(int tileIndex, int row){
        return tileIndex >= 8* row && tileIndex< 8*(row+1);
    }
}
