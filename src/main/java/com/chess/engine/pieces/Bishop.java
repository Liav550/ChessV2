package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MajorAttackMove;
import com.chess.engine.board.Move.MajorMove;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// Check out ChessBoardIndexes.png on resources for indexes!!!
public class Bishop extends Piece{
    private static final int[] CANDIDATE_BISHOP_DIRECTION_OFFSETS = {-9,-7,7,9}; // explanation below
         /*
            the directions that a bishop can move to.
            for example, a bishop can go from tile 35 to tile 62, because 35+9+9+9 = 62.
                         a bishop can go from tile 35 to tile 21 because 35-7-7 = 21
            NOTE: there are some exclusions to this rule, which will be handled
          */

    public Bishop(int piecePosition, Alliance alliance) {
        super(PieceType.BISHOP,piecePosition, alliance,true);
    }
    public Bishop(int piecePosition, Alliance alliance, boolean isFirstMove) {
        super(PieceType.BISHOP,piecePosition, alliance, isFirstMove);
    }
    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        List<Move> legalMoves = new ArrayList<>();
        int candidateDestinationIndex;
        Tile destinationTile;
        Piece pieceOnDestinationTile;
        for(int offset: CANDIDATE_BISHOP_DIRECTION_OFFSETS){
            if(isFirstColumnExclusion(this.piecePosition, offset) ||
               isEighthColumnExclusion(this.piecePosition, offset)){
                continue;
            }
            candidateDestinationIndex = this.piecePosition + offset;
            while(BoardUtils.isValidTileIndex(candidateDestinationIndex)){
                destinationTile = board.getTile(candidateDestinationIndex);
                if(!destinationTile.isTileOccupied()){
                    legalMoves.add(new MajorMove(board, this, candidateDestinationIndex));
                    if(isFirstColumnExclusion(candidateDestinationIndex, offset) ||
                            isEighthColumnExclusion(candidateDestinationIndex, offset)){
                        break;
                    }
                    candidateDestinationIndex += offset;
                }
                else{
                    pieceOnDestinationTile = destinationTile.getPiece();
                    if(this.pieceAlliance != pieceOnDestinationTile.getPieceAlliance()){
                        legalMoves.add(new MajorAttackMove
                                (board,this,candidateDestinationIndex,pieceOnDestinationTile));
                    }
                    break;
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Bishop movePiece(Move move) {
        return new Bishop(move.getDestinationIndex(), move.getMovedPiece().getPieceAlliance());
    }

    private boolean isFirstColumnExclusion(int tileIndex, int offset){ // an exclusion to the rule
                                                                       // that occurs when the bishop is in 1st column
        if(!BoardUtils.isInColumn(tileIndex,0)){
            return false;
        }
        return offset == -9 || offset == 7; // example for this exclusion: a bishop in tile 40 can't go to tile 47
    }
    private boolean isEighthColumnExclusion(int tileIndex, int offset){ // an exclusion to the rule
                                                                        // that occurs when the bishop is in 8th column
        if(!BoardUtils.isInColumn(tileIndex,7)){
            return false;
        }
        return offset == -7 || offset == 9; // example for this exclusion: a bishop in tile 47 can't go to tile 40
    }

    @Override
    public String toString() {
        return PieceType.BISHOP.toString();
    }
}
