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
 * The King class represents the king piece.
 */
public class King extends Piece{
    private boolean isCastled;
    private static final int[] CANDIDATE_MOVE_OFFSETS = {-9, -8,-7,-1,1,7,8,9};
       // that array contains the 8 possible offsets the king can go to from its current position (if possible)
       // for example a king can go from tile 26 to tile 34 because 26+8=34.
       // NOTE: there are some exceptions to this rule, which will be handled
    public King(int piecePosition, Alliance alliance) {
        super(PieceType.KING,piecePosition, alliance,true);
        this.isCastled = false;
    }
    public King(int piecePosition, Alliance alliance, boolean isFirstMove, boolean isCastled) {
        super(PieceType.KING,piecePosition, alliance, isFirstMove);
        this.isCastled = isCastled;
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        int candidateDestinationIndex; // the index of the tile that the knight can move to (if valid)
        Tile destinationTile; // the tile of the destination
        Piece pieceOnDestination; // the piece in the destination tile (if exists)
        final List<Move> legalMoves = new ArrayList<>();
        for(int offset: CANDIDATE_MOVE_OFFSETS){ // looping through all offsets
            candidateDestinationIndex = this.piecePosition + offset;
            if(!BoardUtils.isValidTileIndex(candidateDestinationIndex)){
                continue;
            }
            if(isFirstColumnExclusion(this.piecePosition,offset) ||
               isEighthColumnExclusion(this.piecePosition,offset)){
                                                        //if the current offset is an exclusion because
                                                        //of the king's position, move on to the next
                                                        //offset
                continue;
            }
            destinationTile = board.getTile(candidateDestinationIndex);
            if(!destinationTile.isTileOccupied()){ // if the tile is empty, we add a normal move to the list.
                legalMoves.add(new MajorMove(board,this,candidateDestinationIndex));
            }
            else{
                // now we know there is a piece on the destination tile
                pieceOnDestination = destinationTile.getPiece();
                if(pieceOnDestination.pieceAlliance != this.pieceAlliance){ // if the piece's color
                                                                            // is not like the king's
                    legalMoves.add(new MajorAttackMove(board,this,candidateDestinationIndex,pieceOnDestination));
                                  // we know it is an attack/capturing move
                }
            }
        }
        return ImmutableList.copyOf(legalMoves); // returns an immutable copy of the list
    }
    @Override
    public King movePiece(Move move) {
        return new King(move.getDestinationIndex(),
                move.getMovedPiece().getPieceAlliance(),
                false,
                move.isCastlingMove());
    }
    @Override
    public int getLocationBonus(){
        return this.pieceAlliance.kingBonus(this.piecePosition);
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
    public boolean isCastled(){
        return this.isCastled;
    }
}
