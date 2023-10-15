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
    protected Collection<Move> calculateKingCastles(/*Collection<Move> playerLegalMoves, */ Collection<Move> opponentsLegalMoves) {
        List<Move> kingCastles = new ArrayList<>();
        Tile rookTile;
        if(!this.playerKing.isFirstMove() || this.isInCheck()){
            return kingCastles;
        }
        if(!board.getTile(5).isTileOccupied() &&
                !board.getTile(6).isTileOccupied()){
            rookTile = board.getTile(7);
            if(rookTile.isTileOccupied() &&
                    rookTile.getPiece().getPieceType().isRook() &&
                    rookTile.getPiece().isFirstMove()){
                if(Player.calculateAttacksOnTile(5,opponentsLegalMoves).isEmpty() &&
                        Player.calculateAttacksOnTile(6,opponentsLegalMoves).isEmpty()){
                    kingCastles.add(new KingSideCastleMove(board,playerKing, 6,
                            (Rook)rookTile.getPiece(), 7,5));
                }
            }
        }
        if(!board.getTile(1).isTileOccupied() &&
                !board.getTile(2).isTileOccupied() &&
                !board.getTile(3).isTileOccupied()){
            rookTile = board.getTile(0);
            if(rookTile.isTileOccupied() &&
                    rookTile.getPiece().getPieceType().isRook() &&
                    rookTile.getPiece().isFirstMove()){
                if(Player.calculateAttacksOnTile(1,opponentsLegalMoves).isEmpty() &&
                        Player.calculateAttacksOnTile(2,opponentsLegalMoves).isEmpty() &&
                        Player.calculateAttacksOnTile(3,opponentsLegalMoves).isEmpty()){
                    kingCastles.add(new QueenSideCastleMove(board,playerKing,2,
                            (Rook)rookTile.getPiece(), 0,3));
                }
            }
        }
        return ImmutableList.copyOf(kingCastles);
    }
}
