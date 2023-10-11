package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MajorMove;
import com.chess.engine.board.Move.AttackMove;
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
        super(PieceType.BISHOP,piecePosition, alliance);
    }
    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        int destinationIndex; // the index of the tile that the bishop can move to (if valid)
        Tile destinationTile; // the tile of the destination
        Piece pieceAtDestination; // the piece in the destination tile (if exists)
        final List<Move> legalMoves = new ArrayList<>();
        for(int offset: CANDIDATE_BISHOP_DIRECTION_OFFSETS){ // looping through all directions
            if(isFirstColumnExclusion(this.piecePosition, offset) ||
               isEighthColumnExclusion(this.piecePosition,offset)){ //if the current offset is an exclusion because
                                                                    //of the bishop's position, move on to the next
                                                                    //offset
                continue;
            }
            destinationIndex = this.piecePosition + offset;
            while(BoardUtils.isValidTileIndex(destinationIndex)){ // while we can go in the direction
                                                                  // without getting out of bounds

                destinationTile = board.getTile(destinationIndex);
                if(!destinationTile.isTileOccupied()){
                    legalMoves.add(new MajorMove(board,this,destinationIndex));
                    destinationIndex += offset; // continue checking in that direction
                }
                else{
                    pieceAtDestination = destinationTile.getPiece();
                    Alliance destinationPieceAlliance = pieceAtDestination.getPieceAlliance();
                    if(this.pieceAlliance != destinationPieceAlliance){
                        legalMoves.add(new AttackMove(board,this,destinationIndex, pieceAtDestination));
                    }
                    break; // because a piece blocks us, we can't continue checking in that direction
                }
            }
        }
        return ImmutableList.copyOf(legalMoves); // returns an immutable copy of the list
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
