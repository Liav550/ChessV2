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
 * the BlackPlayer class is used to represent the black player on the game of chess
 */
public class BlackPlayer extends Player{

    public BlackPlayer(Board board, Collection<Move> whiteStandardLegalMoves, Collection<Move> blackStandardLegalMoves) {
        super(board,blackStandardLegalMoves,whiteStandardLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getActiveBlackPieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.BLACK;
    }

    @Override
    public Player getOpponent() {
        return this.board.getWhitePlayer();
    }

    @Override
    protected Collection<Move> calculateKingCastles(Collection<Move> opponentsLegalMoves) {
        List<Move> kingCastles = new ArrayList<>();
        Tile rookTile;
        if(!this.playerKing.isFirstMove() || this.isInCheck()){
            return kingCastles; // if the king has already moved or if the king is in check, castling is illegal
        }
        if(!board.getTile(5).isTileOccupied() &&
                !board.getTile(6).isTileOccupied()){ // checking if the 2 tiles in the way of the king side
                                                            // castling are clear

            rookTile = board.getTile(7); // the black rook's original tile on the king's side is at index 7.
            if(rookTile.isTileOccupied() &&
                    rookTile.getPiece().getPieceType().isRook() &&
                    rookTile.getPiece().isFirstMove()){ // if the black rook is in its original spot:

                if(Player.calculateAttacksOnTile(5,opponentsLegalMoves).isEmpty() &&
                        Player.calculateAttacksOnTile(6,opponentsLegalMoves).isEmpty()){
                                                  // if the 2 tile in the way of the king side castling are not attacked
                                                  // we can add the move to the list
                    kingCastles.add(new KingSideCastleMove(board,playerKing, 6,
                            (Rook)rookTile.getPiece(), 7,5));
                }
            }
        }
        if(!board.getTile(1).isTileOccupied() &&
                !board.getTile(2).isTileOccupied() &&
                !board.getTile(3).isTileOccupied()){// checking if the 3 tiles in the way of the queen side
                                                            // castling are clear

            rookTile = board.getTile(0); // the black rook's original tile on the queen's side is at index 0.
            if(rookTile.isTileOccupied() &&
                    rookTile.getPiece().getPieceType().isRook() &&
                    rookTile.getPiece().isFirstMove()){ // if the black rook is in its original spot:
                if(Player.calculateAttacksOnTile(1,opponentsLegalMoves).isEmpty() &&
                        Player.calculateAttacksOnTile(2,opponentsLegalMoves).isEmpty() &&
                        Player.calculateAttacksOnTile(3,opponentsLegalMoves).isEmpty()){
                                                  // if the 3 tile in the way of the king side castling are not attacked
                                                  // we can add the move to the list
                    kingCastles.add(new QueenSideCastleMove(board,playerKing,2,
                            (Rook)rookTile.getPiece(), 0,3));
                }
            }
        }
        return ImmutableList.copyOf(kingCastles);
    }
}
