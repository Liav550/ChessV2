package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.KingSideCastleMove;
import com.chess.engine.board.Move.QueenSideCastleMove;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    protected Collection<Move> calculateKingCastles(Collection<Move> opponentsLegalMoves) {
        List<Move> kingCastles = new ArrayList<>();
        Tile rookTile;
        if(!this.playerKing.isFirstMove() || this.isInCheck()){
            return kingCastles;
        }
        if(!board.getTile(61).isTileOccupied() &&
           !board.getTile(62).isTileOccupied()){
            rookTile = board.getTile(63);
            if(rookTile.isTileOccupied() &&
               rookTile.getPiece().getPieceType().isRook() &&
               rookTile.getPiece().isFirstMove()){
                if(Player.calculateAttacksOnTile(61,opponentsLegalMoves).isEmpty() &&
                   Player.calculateAttacksOnTile(62,opponentsLegalMoves).isEmpty()){
                    kingCastles.add(new KingSideCastleMove(board, this.playerKing, 62,
                                                          (Rook)rookTile.getPiece(), 63,61));
                }
            }
        }
        if(!board.getTile(57).isTileOccupied() &&
           !board.getTile(58).isTileOccupied() &&
           !board.getTile(59).isTileOccupied()){
            rookTile = board.getTile(56);
            if(rookTile.isTileOccupied() &&
                    rookTile.getPiece().getPieceType().isRook() &&
                    rookTile.getPiece().isFirstMove()){
                if(Player.calculateAttacksOnTile(57,opponentsLegalMoves).isEmpty() &&
                   Player.calculateAttacksOnTile(58,opponentsLegalMoves).isEmpty() &&
                   Player.calculateAttacksOnTile(59,opponentsLegalMoves).isEmpty()){
                    kingCastles.add(new QueenSideCastleMove(board, playerKing, 58,
                            (Rook)rookTile.getPiece(), 56,59));
                }
            }
        }
        return ImmutableList.copyOf(kingCastles);
    }
}
