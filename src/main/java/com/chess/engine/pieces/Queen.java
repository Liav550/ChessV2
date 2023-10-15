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

/**
 * @author liavb
 * The Queen class represents the queen piece.
 */
// TODO - YOU DOCUMENTED ME
public class Queen extends Piece{
    private static final int[] CANDIDATE_QUEEN_DIRECTION_OFFSETS = {-9,-8,-7,-1,1,7,8,9};
          /*
            the directions that a queen can move to.
            for example, a queen can go from tile 45 to tile 9, because 45-9-9-9-9= 9.
                         a queen can go from tile 28 to tile 44 because 28+8+8 = 44
            NOTE: there are some exclusions to this rule, which will be handled
          */
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
                                                                            // if the tile is empty,
                                                                            // we add a new normal move to the list.

                    if(isFirstColumnExclusion(candidateDestinationIndex, offset) ||
                            isEighthColumnExclusion(candidateDestinationIndex, offset)){
                        break; // if we got to the end of the diagonal or the line, we can break and move on to the next
                               // offset.
                    }

                    candidateDestinationIndex += offset; // continue checking in that direction.
                }
                else{
                    // now we know there is a piece standing on the destination tile.
                    pieceAtDestination = destinationTile.getPiece();
                    Alliance destinationPieceAlliance = pieceAtDestination.getPieceAlliance();
                    if(this.pieceAlliance != destinationPieceAlliance){
                        legalMoves.add(new MajorAttackMove
                                (board,this,candidateDestinationIndex, pieceAtDestination));
                          // if the piece's color is not like the queen's, we know it's an attacking/capturing move
                    }
                    break; // because a piece blocks us, we can't continue checking in that direction.
                }
            }
        }
        return ImmutableList.copyOf(legalMoves); // return an immutable copy of the list
    }
    private boolean isFirstColumnExclusion(int tileIndex, int offset) { // an exclusion that occurs when the queen is
                                                                        // on the first column
        if(!BoardUtils.isInColumn(tileIndex,0)){
            return false;
        }
        return offset == -1 || offset == -9 || offset == 7;
            // for example, a queen on tile 32 can't move to tile 31 nor tile 23
    }

    private boolean isEighthColumnExclusion(int tileIndex, int offset) {
        if(!BoardUtils.isInColumn(tileIndex,7)){
            return false;
        }
        return offset == 9 ||  offset == 1 || offset == -7;
           // for example, a queen on tile 47 can't move to tile 48 nor tile 56
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
