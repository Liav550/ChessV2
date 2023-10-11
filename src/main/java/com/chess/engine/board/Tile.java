package com.chess.engine.board;

import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

public abstract class Tile {
    protected final int tileIndex; // the index of the tile in our board (1d array in size of 64)
    private static final Map<Integer, EmptyTile> EMPTY_TILE_CACHE = getAllPossibleEmptyTiles(); // cache of all 64
                                                                                                //possible empty tiles
    private Tile(int tileIndex) {
        this.tileIndex = tileIndex;
    }
    /*
        The following function will be the only way to generate a tile.
        If a piece exists on that tile, the function will return an Occupied Tile.
        Otherwise, an Empty Tile will be returned
     */
    public static Tile createTile(int tileIndex, Piece piece){
        return piece != null? new OccupiedTile(tileIndex,piece): EMPTY_TILE_CACHE.get(tileIndex);
    }

    /*
       the following function fills the cache with all 64 possible empty tiles
     */
    private static Map<Integer, EmptyTile> getAllPossibleEmptyTiles() {
        final Map<Integer,EmptyTile> emptyTileMap = new HashMap<>();
        for (int i = 0; i < BoardUtils.NUMBER_OF_TILES; i++) {
            emptyTileMap.put(i,new EmptyTile(i));
        }
        return ImmutableMap.copyOf(emptyTileMap); // returns an immutable map, which is a copy of the original cache
    }
    public abstract boolean isTileOccupied();
    public abstract Piece getPiece();


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
        private final Piece pieceOnTile;
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
