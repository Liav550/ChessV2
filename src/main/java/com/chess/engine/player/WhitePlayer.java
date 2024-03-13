package com.chess.engine.player;

import com.chess.engine.pieces.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.moves.Move;
import com.chess.engine.moves.KingSideCastleMove;
import com.chess.engine.moves.QueenSideCastleMove;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author liavb
 * the WhitePlayer class is used to represent the white player on the game of chess
 */

public class WhitePlayer extends Player{
    public WhitePlayer(Board board, Collection<Move> whiteStandardLegalMoves, Collection<Move> blackStandardLegalMoves) {
        super(board,whiteStandardLegalMoves,blackStandardLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getActiveWhitePieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.WHITE;
    }

    @Override
    public Player getOpponent() {
        return this.board.getBlackPlayer();
    }

    @Override
    public Collection<Move> calculateKingCastles(Collection<Move> opponentsLegalMoves) {
        List<Move> kingCastles = new ArrayList<>();
        Tile rookTile;
        if(!this.playerKing.isFirstMove() || this.isInCheck()){
            return kingCastles; // if the king has already moved or if the king is in check, castling is illegal
        }
        if(!board.getTile(61).isTileOccupied() &&
           !board.getTile(62).isTileOccupied()){ // checking if the 2 tiles in the way of the king side castling
                                                         // are clear

            rookTile = board.getTile(63); // the white rook's original tile on the king's side is at index 63.

            if(rookTile.isTileOccupied() &&
               rookTile.getPiece().getPieceType().isRook() &&
               rookTile.getPiece().isFirstMove()){ // if the white rook is in its original spot:

                if(Player.calculateAttacksOnTile(61,opponentsLegalMoves).isEmpty() &&
                   Player.calculateAttacksOnTile(62,opponentsLegalMoves).isEmpty()){
                                                 // if the 2 tile in the way of the king side castling are not attacked
                                                 // we can add the move to the list
                    kingCastles.add(new KingSideCastleMove(board, this.playerKing, 62,
                                                          (Rook)rookTile.getPiece(), 63,61));
                }
            }
        }
        if(!board.getTile(57).isTileOccupied() &&
           !board.getTile(58).isTileOccupied() &&
           !board.getTile(59).isTileOccupied()){ // checking if the 3 tiles in the way of the queen side
                                                         // castling are clear.
            rookTile = board.getTile(56); // the white rook's original tile on the queen side it at index 56.
            if(rookTile.isTileOccupied() &&
                    rookTile.getPiece().getPieceType().isRook() &&
                    rookTile.getPiece().isFirstMove()){ // if the white rook is in its original spot:

                if(Player.calculateAttacksOnTile(57,opponentsLegalMoves).isEmpty() &&
                   Player.calculateAttacksOnTile(58,opponentsLegalMoves).isEmpty() &&
                   Player.calculateAttacksOnTile(59,opponentsLegalMoves).isEmpty()){
                                                 // if the 3 tile in the way of the queen side castling are not attacked
                                                 // we can add the move to the list.
                    kingCastles.add(new QueenSideCastleMove(board, playerKing, 58,
                            (Rook)rookTile.getPiece(), 56,59));
                }
            }
        }
        return ImmutableList.copyOf(kingCastles); // return an immutable copy of the list
    }
}
