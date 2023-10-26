package com.chess.engine.player.Computer;

import com.chess.engine.board.Board;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Piece.PieceType;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;

public class StandardBoardEvaluator implements BoardEvaluator{
    private static final int CHECK_BONUS = 50;
    private static final int CHECKMATE_BONUS = 10000;
    private static final int DEPTH_BONUS = 100;
    private static final int CASTLE_BONUS = 80;

    private static final int BISHOP_PAIR_BONUS = 25;

    @Override
    public int evaluate(Board board, int depth) {
        return playerScore(board, board.getWhitePlayer(), depth) - playerScore(board, board.getBlackPlayer(), depth);
    }
    private int playerScore(Board board, Player player, int depth) {
        return piecesValue(player) +
                mobility(player) +
                givesCheck(player) +
                castled(player) +
                givesCheckMate(player,depth);

    }

    private int givesCheckMate(Player player, int depth) {
        return player.getOpponent().isInCheckmate()? CHECKMATE_BONUS* depthBonus(depth): 0;
    }

    private int depthBonus(int depth) {
        return depth == 0? 1: DEPTH_BONUS;
    }

    private int castled(Player player) {
        return player.isCastled()? CASTLE_BONUS: 0;
    }

    private int givesCheck(Player player) {
        return player.getOpponent().isInCheck()? CHECK_BONUS: 0;
    }

    private int mobility(Player player) {
        return player.getLegalMoves().size();
    }

    private static int piecesValue(Player player){
        int totalScore = 0;
        int bishopCounter = 0;
        for(Piece piece: player.getActivePieces()){
            totalScore += piece.getPieceValue() + piece.getLocationBonus();
            if(piece.getPieceType() == PieceType.BISHOP){
                bishopCounter++;
            }
        }
        return totalScore + (bishopCounter==2? BISHOP_PAIR_BONUS: 0);
    }
}
