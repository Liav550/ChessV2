package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MajorAttackMove;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// Check out ChessBoardIndexes.png on resources for indexes!!!
public class Queen extends Piece{
    private static final int[] CANDIDATE_QUEEN_DIRECTION_OFFSETS = {-9,-8,-7,-1,1,7,8,9};
    public Queen(int piecePosition, Alliance alliance) {
        super(PieceType.QUEEN,piecePosition, alliance,true);
    }
    public Queen(int piecePosition, Alliance alliance, boolean isFirstMove) {
        super(PieceType.QUEEN,piecePosition, alliance, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        List<Move> legalMoves = new ArrayList<>();
        int candidateDestinationIndex;
        Tile destinationTile;
        Piece pieceAtDestination;
        for(int offset: CANDIDATE_QUEEN_DIRECTION_OFFSETS){ // looping through all directions
            if(isFirstColumnExclusion(this.piecePosition, offset) ||
                    isEighthColumnExclusion(this.piecePosition,offset)){ //if the current offset is an exclusion because
                //of the queen's position, move on to the next
                //offset
                continue;
            }
            candidateDestinationIndex = this.piecePosition + offset;
            while(BoardUtils.isValidTileIndex(candidateDestinationIndex)){ // while we can go in the direction
                // without getting out of bounds

                destinationTile = board.getTile(candidateDestinationIndex);
                if(!destinationTile.isTileOccupied()){
                    legalMoves.add(new Move.MajorMove(board,this,candidateDestinationIndex));
                    if(isFirstColumnExclusion(candidateDestinationIndex, offset) ||
                            isEighthColumnExclusion(candidateDestinationIndex, offset)){
                        break;
                    }
                    candidateDestinationIndex += offset; // continue checking in that direction
                }
                else{
                    pieceAtDestination = destinationTile.getPiece();
                    Alliance destinationPieceAlliance = pieceAtDestination.getPieceAlliance();
                    if(this.pieceAlliance != destinationPieceAlliance){
                        legalMoves.add(new MajorAttackMove(board,this,candidateDestinationIndex, pieceAtDestination));
                    }
                    break; // because a piece blocks us, we can't continue checking in that direction
                }
            }
        }



        return ImmutableList.copyOf(legalMoves);
    }

    private boolean isEighthColumnExclusion(int tileIndex, int offset) {
        if(!BoardUtils.isInColumn(tileIndex,7)){
            return false;
        }
        return offset == 9 ||  offset == 1 || offset == -7;
    }

    private boolean isFirstColumnExclusion(int tileIndex, int offset) {
        if(!BoardUtils.isInColumn(tileIndex,0)){
            return false;
        }
        return offset == -1 || offset == -9 || offset == 7;
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
