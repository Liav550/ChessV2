package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.AttackMove;
import com.chess.engine.board.Move.MajorMove;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// Check out ChessBoardIndexes.png on resources for indexes!!!
public class King extends Piece{
    private static final int[] CANDIDATE_MOVE_OFFSETS = {-9, -8,-7,-1,1,7,8,9};
       // that array contains the 8 possible offsets the king can go to from its current position (if possible)
       // for example a king can go from tile 26 to tile 34 because 26+8=34.
       // NOTE: there are some exceptions to this rule, which will be handled
    public King(int piecePosition, Alliance alliance) {
        super(PieceType.KING,piecePosition, alliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        int destinationIndex; // the index of the tile that the knight can move to (if valid)
        Tile destinationTile; // the tile of the destination
        Piece pieceOnDestination; // the piece in the destination tile (if exists)
        final List<Move> legalMoves = new ArrayList<>();
        for(int offset: CANDIDATE_MOVE_OFFSETS){ // looping through all offsets
            destinationIndex = this.piecePosition + offset;
            if(!BoardUtils.isValidTileIndex(destinationIndex)){
                continue;
            }
            if(isFirstColumnExclusion(this.piecePosition,offset) ||
               isEighthColumnExclusion(this.piecePosition,offset)){
                                                        //if the current offset is an exclusion because
                                                        //of the knight's position, move on to the next
                                                        //offset
                continue;
            }
            destinationTile = board.getTile(destinationIndex);
            if(!destinationTile.isTileOccupied()){
                legalMoves.add(new MajorMove(board,this,destinationIndex));
            }
            else{
                pieceOnDestination = destinationTile.getPiece();
                if(pieceOnDestination.pieceAlliance != this.pieceAlliance){
                    legalMoves.add(new AttackMove(board,this,destinationIndex,pieceOnDestination));
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }
    @Override
    public King movePiece(Move move) {
        return new King(move.getDestinationIndex(), move.getMovedPiece().getPieceAlliance());
    }
    private boolean isFirstColumnExclusion(int tileIndex, int offset){ // an exclusion to the rule
                                                                       // that occurs when the king is in 1st column
        if(!BoardUtils.isInColumn(tileIndex,0)){
            return false;
        }
        return offset == 7 || offset == -1 || offset == -9; // for example- a king in tile 48
                                                            // can't go to tile 47
    }
    private boolean isEighthColumnExclusion(int tileIndex, int offset){ // an exclusion to the rule
                                                                        // that occurs when the king is in 8th column
        if(!BoardUtils.isInColumn(tileIndex,7)){
            return false;
        }
        return offset == -7 || offset == 1 || offset == 9; // for example- a king in tile 39
                                                           // can't go to tile 48
    }
    @Override
    public String toString() {
        return PieceType.KING.toString();
    }
}
