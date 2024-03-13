package com.chess.engine.board;

import com.chess.engine.pieces.Piece;

public class EmptyTile extends Tile{
    EmptyTile(int tileIndex){
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
