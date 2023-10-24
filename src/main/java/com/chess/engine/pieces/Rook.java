package com.chess.engine.pieces;

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

/**
 * @author liavb
 * The Rook class represents the rook piece.
 */
public class Rook extends Piece{
    private static final int[] CANDIDATE_ROOK_DIRECTION_OFFSETS = {-8,-1,1,8}; // explanation below
          /*
            the directions that a rook can move to.
            for example, a rook can go from tile 35 to tile 38, because 35+1+1+1 = 38.
                         a rook can go from tile 35 to tile 19 because 35-8-8 = 19
            NOTE: there are some exclusions to this rule, which will be handled
          */
    public Rook(int piecePosition, Alliance alliance) {
        super(PieceType.ROOK,piecePosition, alliance, true);
    }
    public Rook(int piecePosition, Alliance alliance, boolean isFirstMove) {
        super(PieceType.ROOK,piecePosition, alliance, isFirstMove);
    }
    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        int candidateDestinationIndex;
        Tile destinationTile;
        Piece pieceAtDestination; // if exists
        final List<Move> legalMoves = new ArrayList<>();
        for(int offset: CANDIDATE_ROOK_DIRECTION_OFFSETS){ // looping through all directions.
            if(isFirstColumnExclusion(this.piecePosition, offset) ||
                    isEighthColumnExclusion(this.piecePosition,offset)){ //if the current offset is an exclusion because
                                                                         // of the rook's position, move on to the
                                                                         // next offset.
                continue;
            }
            candidateDestinationIndex = this.piecePosition + offset;
            while(BoardUtils.isValidTileIndex(candidateDestinationIndex)){ // while we can go in the direction
                                                                           // without getting out of bounds.
                destinationTile = board.getTile(candidateDestinationIndex);
                if(!destinationTile.isTileOccupied()){
                    legalMoves.add(new MajorMove(board,this,candidateDestinationIndex));
                                                                                // if the tile is empty,
                                                                                // we add a new normal move to the list.

                    if(isFirstColumnExclusion(candidateDestinationIndex, offset) ||
                            isEighthColumnExclusion(candidateDestinationIndex, offset)){
                        break; // if we got to the end of the line, we can break and move on to the next offset.
                    }
                    candidateDestinationIndex += offset; // continue checking in that direction
                }
                else{
                    // now we know there is a piece standing on the destination tile.
                    pieceAtDestination = destinationTile.getPiece();
                    if(this.pieceAlliance != pieceAtDestination.getPieceAlliance()){
                        legalMoves.add(new MajorAttackMove
                                (board,this,candidateDestinationIndex, pieceAtDestination));
                           // if the piece's color is not like the rook's, we know it's an attacking/capturing move
                    }
                    break; // because a piece blocks us, we can't continue checking in that direction
                }
            }
        }
        return ImmutableList.copyOf(legalMoves); // returns an immutable copy of the list
    }
    @Override
    public Rook movePiece(Move move) {
        return new Rook(move.getDestinationIndex(), move.getMovedPiece().getPieceAlliance(), false);
    }
    @Override
    public int getLocationBonus(){
        return this.pieceAlliance.rookBonus(this.piecePosition);
    }

    private boolean isFirstColumnExclusion(int tileIndex, int offset) { // an exclusion to the rule
                                                                        // that occurs when the rook is in 1st column

        return BoardUtils.isInColumn(tileIndex,0) && offset == -1; // for example- a rook in tile 32
                                                                          // can't go to tile 31
    }

    private boolean isEighthColumnExclusion(int tileIndex, int offset) { // an exclusion to the rule
                                                                         // that occurs when the rook is in 8th column

        return BoardUtils.isInColumn(tileIndex,7) && offset == 1; // for example- a rook in tile 15
                                                                         // can't go to tile 16
    }
    @Override
    public String toString() {
        return PieceType.ROOK.toString();
    }
}
