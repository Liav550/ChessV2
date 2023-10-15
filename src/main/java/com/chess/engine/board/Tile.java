package com.chess.engine.board;

import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.ArrayList;
import java.util.List;


/**
 * @author liavb
 * the Tile class is used to represent a single tile in our chess board.
 */
// TODO - YOU DOCUMENTED ME
public abstract class Tile {
    protected final int tileIndex; // the index of the tile in our board (1d array in size of 64)
    private static final List<EmptyTile> EMPTY_TILE_CACHE = getAllPossibleEmptyTiles(); // a cache of all 64
                                                                                                //possible empty tiles

    /**
     * @return: all possible 64 empty tiles in a cache
     */
    private static List<EmptyTile> getAllPossibleEmptyTiles() {
        List<EmptyTile> emptyTiles = new ArrayList<>();
        for (int i = 0; i < BoardUtils.NUMBER_OF_TILES; i++) {
            emptyTiles.add(new EmptyTile(i)); // adding a new empty tile in index i
        }
        return ImmutableList.copyOf(emptyTiles); // returns an immutable copy of the list
    }

    private Tile(int tileIndex) {
        this.tileIndex = tileIndex;
    }

    /**
     * This method is the only way to create a tile of any kind.
     * @param tileIndex: the index of the tile in the board
     * @param piece: the piece that occupies the tile (if at all)
     * @return the tile that has been created
     */
    public static Tile createTile(int tileIndex, Piece piece){
        return piece != null? new OccupiedTile(tileIndex,piece): EMPTY_TILE_CACHE.get(tileIndex);
              // if the piece is null, it means the tile is empty, and therefore we return the corresponding empty tile
              // from out cache, otherwise we instantiate an occupied tile.
    }

    /**
     * @return: true if the tile is occupied, and false otherwise
     * this method's body is changing because an EmptyTile would return false and an OccupiedTile will return true.
     * that is why the method is abstract.
     */
    public abstract boolean isTileOccupied();

    /**
     * @return: the piece that occupies the tile.
     * this method's body is changing because an EmptyTile would return null and an OccupiedTile will return the piece.
     * that is why the method is abstract.
     */
    public abstract Piece getPiece();

    public int getTileIndex(){return this.tileIndex;}


    public static final class EmptyTile extends Tile{
        private EmptyTile(int tileIndex){
            super(tileIndex);
        }

        @Override
        public boolean isTileOccupied() {
            return false;
        }

        @Override
        public Piece getPiece() {
            return null;
        }
        @Override
        public String toString(){
            return "-";
        }
    }


    public static final class OccupiedTile extends Tile{
        private final Piece pieceOnTile; // the piece that occupies the tile.
        private OccupiedTile(int tileIndex, Piece pieceOnTile){
            super(tileIndex);
            this.pieceOnTile=pieceOnTile;
        }

        @Override
        public boolean isTileOccupied() {
            return true;
        }

        @Override
        public Piece getPiece() {
            return this.pieceOnTile;
        }
        @Override
        public String toString(){
            return pieceOnTile.getPieceAlliance().isWhite()? pieceOnTile.toString(): pieceOnTile.toString().toLowerCase();
        }
    }
}
