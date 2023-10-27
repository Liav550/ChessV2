package com.chess.engine.moves;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;

public final class PawnJump extends Move {
    public PawnJump(Board board, Piece movedPiece, int destinationIndex) {
        super(board, movedPiece, destinationIndex);
    }

    @Override
    public Board execute() {
        Board.Builder builder = new Board.Builder();
        for (Piece piece : board.getCurrentPlayer().getActivePieces()) {
            if (!piece.equals(movedPiece)) {
                builder.setPiece(piece);
            }
        }
        for (Piece piece : board.getCurrentPlayer().getOpponent().getActivePieces()) {
            builder.setPiece(piece);
        }
        Pawn movedPawn = (Pawn) this.movedPiece.movePiece(this);
        builder.setPiece(movedPawn);
        builder.setEnPassentPawn(movedPawn);
        builder.setMoveMaker(board.getCurrentPlayer().getOpponent().getAlliance());
        return builder.build();
    }

    @Override
    public String toString() {
        return BoardUtils.getNotationAtIndex(this.destinationIndex);
    }
}
