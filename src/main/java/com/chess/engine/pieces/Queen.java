package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// Check out ChessBoardIndexes.png on resources for indexes!!!
public class Queen extends Piece{
    public Queen(int piecePosition, Alliance alliance) {
        super(PieceType.QUEEN,piecePosition, alliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {

        /* explanation: a Queen is a combination of a Rook and a Bishop, so we can pretend the queen
           is a rook and get its legal moves in one list. then we can pretend that the queen is actually a rook and get
           its legal moves in a different list. then all we have to do is combine the 2 lists.
         */
        final Collection<Move> equivalentRookMoves =  new Rook(this.piecePosition, this.pieceAlliance).calculateLegalMoves(board);
        final Collection<Move> equivalentBishopMoves = new Bishop(this.piecePosition, this.pieceAlliance).calculateLegalMoves(board);
        final List<Move> legalMoves = new ArrayList<>(equivalentRookMoves);
        legalMoves.addAll(equivalentBishopMoves);

        return ImmutableList.copyOf(legalMoves);
    }
    @Override
    public Queen movePiece(Move move) {
        return new Queen(move.getDestinationIndex(), move.getMovedPiece().getPieceAlliance());
    }
    @Override
    public String toString() {
        return PieceType.QUEEN.toString();
    }
}
