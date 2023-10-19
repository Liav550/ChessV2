package com.chess.engine.player.Computer;


import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.player.MoveTransition;
import com.chess.engine.player.Player;
import com.chess.engine.player.PlayerType;

import java.awt.*;

public class AlphaBeta implements MoveStrategy{
    private final BoardEvaluator evaluator;
    private int depth;

    public AlphaBeta(int depth) {
        this.depth=depth;
        this.evaluator = new StandardBoardEvaluator();
    }

    @Override
    public Move execute(Board board) {
        Long startingTime = System.currentTimeMillis();
        Player currentPlayer = board.getCurrentPlayer();
        Move bestMove = Move.NullMove.NULL_MOVE;
        int highestSeenValue = Integer.MIN_VALUE;
        int lowestSeenValue = Integer.MAX_VALUE;
        int currentValue;
        System.out.println(board.getCurrentPlayer().getAlliance().toString() + " THINKING with depth = " + this.depth);
        int moveCounter = 1;
        int numMoves = board.getCurrentPlayer().getLegalMoves().size();
        for (final Move move : ((board.getCurrentPlayer().getLegalMoves()))) {
            final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            final String s;
            if (moveTransition.getMoveStatus().isDone()) {
                final long candidateMoveStartTime = System.nanoTime();
                currentValue = currentPlayer.getAlliance().isWhite() ?
                        minimum(moveTransition.getTransitionBoard(), this.depth - 1, highestSeenValue, lowestSeenValue) :
                        maximum(moveTransition.getTransitionBoard(), this.depth - 1, highestSeenValue, lowestSeenValue);
                if (currentPlayer.getAlliance().isWhite() && currentValue > highestSeenValue) {
                    highestSeenValue = currentValue;
                    bestMove = move;
                    if (moveTransition.getTransitionBoard().getBlackPlayer().isInCheckmate()) {
                        break;
                    }
                } else if (currentPlayer.getAlliance().isBlack() && currentValue < lowestSeenValue) {
                    lowestSeenValue = currentValue;
                    bestMove = move;
                    if (moveTransition.getTransitionBoard().getWhitePlayer().isInCheckmate()) {
                        break;
                    }
                }
            }
        }
        long totalTime = System.currentTimeMillis() - startingTime;
        System.out.println("calculation time: "+totalTime);
        return bestMove;
    }

    private int maximum(Board board, int depth, int highest, int lowest){
        if(depth == 0 || BoardUtils.isGameOver(board)){
            return evaluator.evaluate(board,depth);
        }
        int currentHighest = highest;
        MoveTransition transition;
        Board transitionBoard;
        for(Move move: board.getCurrentPlayer().getLegalMoves()){
            transition = board.getCurrentPlayer().makeMove(move);
            if(transition.getMoveStatus().isDone()){
                transitionBoard = transition.getTransitionBoard();
                currentHighest = Math.max(currentHighest, minimum(transitionBoard, depth-1, currentHighest,lowest));
                if(currentHighest>= lowest){
                    return lowest;
                }
            }
        }
        return currentHighest;
    }

    private int minimum(Board board, int depth, int highest, int lowest){
        if(depth == 0 || BoardUtils.isGameOver(board)){
            return evaluator.evaluate(board,depth);
        }
        int currentLowest = lowest;
        MoveTransition transition;
        Board transitionBoard;
        for(Move move: board.getCurrentPlayer().getLegalMoves()){
            transition = board.getCurrentPlayer().makeMove(move);
            if(transition.getMoveStatus().isDone()){
                transitionBoard = transition.getTransitionBoard();
                currentLowest = Math.min(currentLowest, maximum(transitionBoard, depth-1, highest,currentLowest));
                if(currentLowest<= highest){
                    return highest;
                }
            }
        }
        return currentLowest;
    }
}
