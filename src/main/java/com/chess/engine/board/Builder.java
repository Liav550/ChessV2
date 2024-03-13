package com.chess.engine.board;

import com.chess.engine.pieces.Alliance;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liavb
 * the Builder class is used to build an instance of the Board class.
 */
public class Builder {
    private Map<Integer, Piece> boardConfig; // a hashMap. the key is an index of a tile and
    // the value is the piece on that tile.
    private Alliance currentMoveMaker; // the side that the right to play belongs to.
    private Pawn enPassentPawn; // the pawn that just moved 2 squares on the previous board (if exists).

    public Builder() {
        this.boardConfig = new HashMap<>();
    }

    public Builder setPiece(Piece piece) {
        boardConfig.put(piece.getPiecePosition(), piece); // putting a new piece in the boardConfig
        return this;
    }

    public Builder setMoveMaker(Alliance alliance) {
        this.currentMoveMaker = alliance;
        return this;
    }

    public Board build() {
        return new Board(this);
    }

    public Pawn getEnPassentPawn() {
        return enPassentPawn;
    }

    public Alliance getCurrentMoveMaker() {
        return currentMoveMaker;
    }

    public Map<Integer, Piece> getBoardConfig() {
        return boardConfig;
    }

    public void setEnPassentPawn(Pawn pawn){
        this.enPassentPawn = pawn;
    }
}
