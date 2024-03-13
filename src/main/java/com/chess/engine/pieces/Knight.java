package com.chess.engine.pieces;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.moves.Move;
import com.chess.engine.moves.MajorAttackMove;
import com.chess.engine.moves.MajorMove;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// Check out ChessBoardIndexes.png on resources for indexes!!!
/**
 * @author liavb
 * The Knight class represents the knight piece.
 */
public class Knight extends Piece{
    private static final int[] CANDIDATE_MOVE_OFFSETS = {-17,-15,-10,-6,6,10,15,17};
            // that array contains the 8 possible offsets the knight can go to from its current position (if possible)
            // for example a knight can go from tile 26 to tile 32 because 26+6=32.
            // NOTE: there are some exceptions to this rule, which will be handled

    public Knight(int piecePosition, Alliance alliance) {
        super(PieceType.KNIGHT,piecePosition, alliance,true);
    }


    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        int candidateDestinationIndex;
        Tile destinationTile;
        Piece pieceAtDestination; // if exists
        final List<Move> legalMoves = new ArrayList<>();
        for (int offset: CANDIDATE_MOVE_OFFSETS) { // looping through all offsets
            candidateDestinationIndex = this.piecePosition + offset;
            if(!BoardUtils.isValidTileIndex(candidateDestinationIndex)){
                continue; // if an index is invalid, we continue in order to check the next one
            }
            if(isFirstColumnExclusion(this.piecePosition, offset) ||
                    isSecondColumnExclusion(this.piecePosition,offset) ||
                    isSeventhColumnExclusion(this.piecePosition,offset) ||
                    isEighthColumnExclusion(this.piecePosition,offset)){ //if the current offset is an exclusion because
                                                                         //of the knight's position, move on to the next
                                                                         //offset
                continue;
            }
            destinationTile = board.getTile(candidateDestinationIndex);
            if(!destinationTile.isTileOccupied()){
                legalMoves.add(new MajorMove(board,this,candidateDestinationIndex));
                                                            // if the tile is empty, we add this normal move to the list
            }
            else{
                pieceAtDestination = destinationTile.getPiece();
                Alliance destinationPieceAlliance = pieceAtDestination.getPieceAlliance();
                if(this.pieceAlliance != destinationPieceAlliance){
                    legalMoves.add(new MajorAttackMove
                            (board,this,candidateDestinationIndex, pieceAtDestination));
                              // if the piece's color is not like the knight's, we know it's an attacking/capturing move
                }
            }
        }
        return ImmutableList.copyOf(legalMoves); // returns an immutable copy of the list
    }
    @Override
    public Knight movePiece(Move move) {
        return new Knight(move.getDestinationIndex(), move.getMovedPiece().getPieceAlliance());
    }

    private boolean isFirstColumnExclusion(int tileIndex, int offset){ // an exclusion to the rule
                                                                       // that occurs when the knight is in 1st column
        if(!BoardUtils.isInColumn(tileIndex,0)){
            return false;
        }
        return offset == -17 || offset == -10 || offset == 6 || offset == 15; // for instance- a knight in tile 32
                                                                              // can't go to tile 22
    }
    private boolean isSecondColumnExclusion(int tileIndex, int offset){ // an exclusion to the rule
                                                                        // that occurs when the knight is in 2nd column
        if(!BoardUtils.isInColumn(tileIndex,1)){
            return false;
        }
        return offset == -10 || offset == 6; // for instance- a knight in tile 49
                                             // can't go to tile 55
    }

    private boolean isSeventhColumnExclusion(int tileIndex, int offset){ // an exclusion to the rule
                                                                         // that occurs when the knight is in 7th column
        if(!BoardUtils.isInColumn(tileIndex,6)){
            return false;
        }
        return offset == 10 || offset == -6; // for instance- a knight in tile 14
                                             // can't go to tile 24
    }

    private boolean isEighthColumnExclusion(int tileIndex, int offset){ // an exclusion to the rule
                                                                        // that occurs when the knight is in 8th column
        if(!BoardUtils.isInColumn(tileIndex,7)){
            return false;
        }
        return offset == -15 || offset == -6 || offset == 10 || offset == 17; // for instance- a knight in tile 7
                                                                              // can't go to tile 24
    }
    @Override
    public String toString() {
        return PieceType.KNIGHT.toString();
    }

}
