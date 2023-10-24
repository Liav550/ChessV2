package com.chess.engine.pieces;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.*;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// Check out ChessBoardIndexes.png on resources for indexes!!!
/**
 * @author liavb
 * The Pawn class represents the pawn piece.
 */
public class Pawn extends Piece{
    private static final int[] CANDIDATE_MOVE_OFFSETS = {8,16,7,9};
       /*
            this array contains the 4 possible offsets that a pawn can go to.
            8 is for a normal push of a pawn, 16 is for the double jump pawns can do on their first move,
            and 7 and 9 are for capturing on both sides.
        */
    public Pawn(int piecePosition, Alliance alliance) {
        super(PieceType.PAWN,piecePosition, alliance,true);
    }
    public Pawn(int piecePosition, Alliance alliance, boolean isFirstMove) {
        super(PieceType.PAWN,piecePosition, alliance, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        int candidateDestinationIndex;
        int behindDestinationIndex; // this is for the double jump that pawns can do on their first move.
                                    // we have to make sure this tile is not occupied.
        Piece pieceOnDestination; // if exists
        final List<Move> legalMoves = new ArrayList<>();

        for(int offset: CANDIDATE_MOVE_OFFSETS){ // looping through all offsets
           candidateDestinationIndex = this.piecePosition + (offset*this.pieceAlliance.getDirection());
                                                                      // here we have to consider the direction the pawn
                                                                      // moves towards.
           if(!BoardUtils.isValidTileIndex(candidateDestinationIndex)){
               continue;
           }
           if(offset == 8 && !board.getTile(candidateDestinationIndex).isTileOccupied()){ //if we want to move 1 tile
                                                                                          // and that tile is empty,
                                                                                          // the move is legal
               // here I deal with promotions
               if(this.pieceAlliance.isPawnPromotionTile(candidateDestinationIndex)){
                   legalMoves.add(new PawnPromotion(new PawnMove(board,this,candidateDestinationIndex)));
               }
               else {
                   legalMoves.add(new PawnMove(board, this, candidateDestinationIndex));
               }
           }

           else if (offset == 16 && this.isFirstMove() &&
                   ((BoardUtils.isInRow(this.piecePosition,1) && this.pieceAlliance.isBlack()) ||
                   (BoardUtils.isInRow(this.piecePosition,6) && this.pieceAlliance.isWhite()))) { // checking if
                                                                                                     // double jump
                                                                                                     // is valid
               behindDestinationIndex = this.piecePosition + (8* this.pieceAlliance.getDirection());
               if(!board.getTile(behindDestinationIndex).isTileOccupied() &&
                  !board.getTile(candidateDestinationIndex).isTileOccupied()){ // if the destination tile and the tile
                                                                               // behind it are empty, double jump is
                                                                               // legal
                   legalMoves.add(new PawnJump(board,this,candidateDestinationIndex));
               }
           }

           else if (offset == 7 &&
                   !(BoardUtils.isInColumn(this.piecePosition,7) && this.pieceAlliance.isWhite()
                     || BoardUtils.isInColumn(this.piecePosition,0) && this.pieceAlliance.isBlack())) {
                                                          // making sure that adding an offset of 7 is not an exclusion

                if(board.getTile(candidateDestinationIndex).isTileOccupied()){
                    pieceOnDestination = board.getTile(candidateDestinationIndex).getPiece();
                    if(this.pieceAlliance != pieceOnDestination.pieceAlliance){
                        if(this.pieceAlliance.isPawnPromotionTile(candidateDestinationIndex)){
                            legalMoves.add(new PawnPromotion(new PawnAttackMove
                                    (board,this,candidateDestinationIndex,pieceOnDestination)));
                             // if the tile is occupied by an enemy piece, and we get to the last row,
                             // we can capture and promote.
                        }
                        else {
                            legalMoves.add(new PawnAttackMove
                                    (board, this, candidateDestinationIndex, pieceOnDestination));
                            // if the tile is occupied by an enemy piece, we can capture.
                        }
                    }
                }
                else if (board.getEnPassentPawn() != null) { // if a pawn that just double jumped exists:
                    if(board.getEnPassentPawn().getPiecePosition() == this.piecePosition +
                            (this.pieceAlliance.getOppositeDirection())){ // if the enPassent pawn is next to this pawn.
                           pieceOnDestination = board.getEnPassentPawn();
                           if(pieceOnDestination.getPieceAlliance() != this.pieceAlliance){
                               legalMoves.add(new PawnEnPassentAttackMove
                                       (board,this,candidateDestinationIndex,pieceOnDestination));
                                                            // if the pawn is an enemy pawn, we can capture it enPassent
                           }
                        }
                }

           }

           else if (offset == 9 && !(BoardUtils.isInColumn(this.piecePosition,0) && this.pieceAlliance.isWhite()
                   || BoardUtils.isInColumn(this.piecePosition,7) && this.pieceAlliance.isBlack())) {
                                                          // making sure that adding an offset of 9 is not an exclusion.
               if(board.getTile(candidateDestinationIndex).isTileOccupied()){
                   pieceOnDestination = board.getTile(candidateDestinationIndex).getPiece();
                   if(this.pieceAlliance != pieceOnDestination.pieceAlliance){
                       if(this.pieceAlliance.isPawnPromotionTile(candidateDestinationIndex)){
                           legalMoves.add(new PawnPromotion(new PawnAttackMove
                                   (board,this,candidateDestinationIndex,pieceOnDestination)));
                           // if the tile is occupied by an enemy piece, and we get to the last row,
                           // we can capture and promote.
                       }
                       else {
                           legalMoves.add(new PawnAttackMove
                                   (board, this, candidateDestinationIndex, pieceOnDestination));
                           // if the tile is occupied by an enemy piece, we can capture.
                       }
                   }
               }
               else if (board.getEnPassentPawn() != null) { // if a pawn that just double jumped exists:
                   if(board.getEnPassentPawn().getPiecePosition() == this.piecePosition -
                           (this.pieceAlliance.getOppositeDirection())){ // if the enPassent pawn is next to this pawn.
                       pieceOnDestination = board.getEnPassentPawn();
                       if(pieceOnDestination.getPieceAlliance() != this.pieceAlliance){
                           legalMoves.add(new PawnEnPassentAttackMove
                                   (board,this,candidateDestinationIndex,pieceOnDestination));
                           // if the pawn is an enemy pawn, we can capture it enPassent.
                       }
                   }
               }
           }
        }
        return ImmutableList.copyOf(legalMoves); // returns an immutable copy of the list
    }
    @Override
    public Pawn movePiece(Move move) {
        return new Pawn(move.getDestinationIndex(), move.getMovedPiece().getPieceAlliance(), false);
    }
    @Override
    public int getLocationBonus(){
        return this.pieceAlliance.pawnBonus(this.piecePosition);
    }
    @Override
    public String toString() {
        return PieceType.PAWN.toString();
    }

    public Piece getPromotionPiece() {
        return new Queen(this.piecePosition, this.pieceAlliance, false);
    }
}
