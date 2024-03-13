package com.chess.engine.board;

import com.chess.engine.pieces.Piece;

public class OccupiedTile extends Tile{
        private Piece pieceOnTile; // the piece that occupies the tile.
        OccupiedTile(int tileIndex, Piece pieceOnTile){
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
