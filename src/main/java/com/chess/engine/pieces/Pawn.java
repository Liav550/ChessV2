package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.*;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// Check out ChessBoardIndexes.png on resources for indexes!!!
public class Pawn extends Piece{
    private static final int[] CANDIDATE_MOVE_OFFSETS = {8,16,7,9};
    public Pawn(int piecePosition, Alliance alliance) {
        super(PieceType.PAWN,piecePosition, alliance,true);
    }
    public Pawn(int piecePosition, Alliance alliance, boolean isFirstMove) {
        super(PieceType.PAWN,piecePosition, alliance, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        int candidateDestinationIndex; // the index of the tile that the pawn can move to (if valid)
        int behindDestinationIndex; // this is for the double jump that pawns can do on their first move.
                                    // we have to make sure this tile is not occupied.
        Piece pieceOnDestination; // the piece in the destination tile (if exists)
        final List<Move> legalMoves = new ArrayList<>();

        for(int offset: CANDIDATE_MOVE_OFFSETS){ // looping through all offsets
           candidateDestinationIndex = this.piecePosition + (offset*this.pieceAlliance.getDirection());// here we have to consider
                                                                                              // the direction the pawn
                                                                                              // moves towards.
           if(!BoardUtils.isValidTileIndex(candidateDestinationIndex)){
               continue;
           }
           if(offset == 8 && !board.getTile(candidateDestinationIndex).isTileOccupied()){ //if we want to move 1 tile and
                                                                                 // that tile is empty,
                                                                                 // the move is legal
               // TODO: MORE WORK TO DO HERE - deal with promotions
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
                  !board.getTile(candidateDestinationIndex).isTileOccupied()){ // if the destination tile and the tile behind it
                                                                      // are empty, double jump is legal
                   legalMoves.add(new PawnJump(board,this,candidateDestinationIndex));
               }
           }
           else if (offset == 7 && !(BoardUtils.isInColumn(this.piecePosition,7) && this.pieceAlliance.isWhite()
                                  || BoardUtils.isInColumn(this.piecePosition,0) && this.pieceAlliance.isBlack())) {
                      // making sure to deal with special cases

                if(board.getTile(candidateDestinationIndex).isTileOccupied()){
                    pieceOnDestination = board.getTile(candidateDestinationIndex).getPiece();
                    if(this.pieceAlliance != pieceOnDestination.pieceAlliance){
                        //TODO MORE TO BE DONE HERE
                        if(this.pieceAlliance.isPawnPromotionTile(candidateDestinationIndex)){
                            legalMoves.add(new PawnPromotion(new PawnAttackMove
                                    (board,this,candidateDestinationIndex,pieceOnDestination)));
                        }
                        else {
                            legalMoves.add(new PawnAttackMove(board, this, candidateDestinationIndex, pieceOnDestination));
                        }
                    }
                }
                else if (board.getEnPassentPawn() != null) {
                    if(board.getEnPassentPawn().getPiecePosition() == this.piecePosition +
                            (this.pieceAlliance.getOppositeDirection())){
                           pieceOnDestination = board.getEnPassentPawn();
                           if(pieceOnDestination.getPieceAlliance() != this.pieceAlliance){
                               legalMoves.add(new PawnEnPassentAttackMove
                                       (board,this,candidateDestinationIndex,pieceOnDestination));
                           }
                        }
                }

           }
           else if (offset == 9 && !(BoardUtils.isInColumn(this.piecePosition,0) && this.pieceAlliance.isWhite()
                   || BoardUtils.isInColumn(this.piecePosition,7) && this.pieceAlliance.isBlack())) {
                      // making sure to deal with special cases
               if(board.getTile(candidateDestinationIndex).isTileOccupied()){
                   pieceOnDestination = board.getTile(candidateDestinationIndex).getPiece();
                   if(this.pieceAlliance != pieceOnDestination.pieceAlliance){
                       if(this.pieceAlliance.isPawnPromotionTile(candidateDestinationIndex)){
                           legalMoves.add(new PawnPromotion(new PawnAttackMove
                                   (board,this,candidateDestinationIndex,pieceOnDestination)));
                       }
                       else {
                           legalMoves.add(new PawnAttackMove(board, this, candidateDestinationIndex, pieceOnDestination));
                       }
                   }
               }
               else if (board.getEnPassentPawn() != null) {
                   if(board.getEnPassentPawn().getPiecePosition() == this.piecePosition -
                           (this.pieceAlliance.getOppositeDirection())){
                       pieceOnDestination = board.getEnPassentPawn();
                       if(pieceOnDestination.getPieceAlliance() != this.pieceAlliance){
                           legalMoves.add(new PawnEnPassentAttackMove
                                   (board,this,candidateDestinationIndex,pieceOnDestination));
                       }
                   }
               }
           }
        }
        return ImmutableList.copyOf(legalMoves);
    }
    @Override
    public Pawn movePiece(Move move) {
        return new Pawn(move.getDestinationIndex(), move.getMovedPiece().getPieceAlliance());
    }
    @Override
    public String toString() {
        return PieceType.PAWN.toString();
    }

    public Piece getPromotionPiece() {
        return new Queen(this.piecePosition, this.pieceAlliance, false);
    }
}
