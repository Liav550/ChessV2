package com.chess.engine.board;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.*;

/**
 * @author liavb
 * The BoardUtils class is used for static methods and variables related to the chess board.
 */
public class BoardUtils {
    private static final List<String> NOTATION = initializeNotations(); // see the initializeNotations() method below

    /**
     * @return a list that is being used in order to convert the indexes our program uses (integer form 0 to 63),
     *         to the official notation that is being used in order to name each tile
     */
    private static List<String> initializeNotations() {
        return ImmutableList.copyOf(Arrays.asList(
                "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8",
                "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
                "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
                "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
                "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
                "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
                "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
                "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"));
    }

    private static final Map<String,Integer> NOTATION_TO_INDEX = initializeNotationToIndexMap(); // explanation below

    /**
     * @return: a map that is being used to convert from the official notation names for each tile, to the indexes
     *          that our program can understand.
     */
    private static Map<String, Integer> initializeNotationToIndexMap() {
        final Map<String, Integer> positionToCoordinate = new HashMap<>();
        for (int i = 0; i < NUMBER_OF_TILES; i++) {
            positionToCoordinate.put(initializeNotations().get(i), i); // putting the notation and the index as a pair
        }
        return ImmutableMap.copyOf(positionToCoordinate);
    }

    private BoardUtils(){ // a constructor that throws an exception if someone tries to instantiate this class.
        throw new RuntimeException("You can't instantiate BoardUtils!!!");
    }
    public static final int NUMBER_OF_TILES = 64; // number of tiles in a standard board

    /**
     *
     * @param tileIndex: the index that we want to make sure that is valid.
     * @return true if the move is valid, and false otherwise
     */
    public static boolean isValidTileIndex(int tileIndex){
        return tileIndex>=0 && tileIndex<NUMBER_OF_TILES;
    }

    /**
     * @param tileIndex: an index of a tile in the board.
     * @param column: the index of the column (between 0-7).
     * @return true if the index is in the given column, and false otherwise.
     */
    public static boolean isInColumn(int tileIndex, int column){
        return tileIndex % 8 == column;
    }

    /**
     * @param tileIndex: an index of a tile in the board.
     * @param row: the index of the row (between 0-7).
     * @return true if the index is in the given row, and false otherwise.
     */
    public static boolean isInRow(int tileIndex, int row){
        return tileIndex >= 8* row && tileIndex< 8*(row+1);
    }

    /**
     * @param index: an index of a tile in the board.
     * @return the matching notation for that specific tile.
     */
    public static String getNotationAtIndex(int index) {
        return NOTATION.get(index);
    }

    /**
     * @param notation: a formal notation of a certain tile.
     * @return the index of that tile in the board.
     */
    public static int getIndexFromNotation(String notation){
        return NOTATION_TO_INDEX.get(notation);
    }

    public static boolean isGameOver(Board board) {
        return board.getCurrentPlayer().isInCheckmate() || board.getCurrentPlayer().isInStalemate();
    }
}
