package com.chess.engine.player.Computer;

import com.chess.engine.board.Board;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;

public class StandardBoardEvaluator implements BoardEvaluator{
    @Override
    public int evaluate(Board board, int depth) {
        return playerScore(board, board.getWhitePlayer(), depth) -
                playerScore(board, board.getBlackPlayer(), depth);
    }

    private int playerScore(Board board, Player player, int depth) {
        int totalScore = 0;
        for(Piece piece: player.getActivePieces()){
            totalScore += piece.getPieceValue();
        }
        return totalScore; // +checkmate/ check/ castle advantages etc.
    }
}
