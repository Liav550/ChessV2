package com.chess.engine.player.Computer;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.NullMove;
import com.chess.engine.player.MoveTransition;

public class MiniMax implements MoveStrategy{
    private final BoardEvaluator boardEvaluator;
    public MiniMax(){
        this.boardEvaluator = new StandardBoardEvaluator(); // todo- be specific about which BoardEvaluator
    }
    @Override
    public Move execute(Board board, int depth) {
        long startingTime = System.currentTimeMillis();
        Move bestMove = new NullMove();

        int highestValueSeen = Integer.MIN_VALUE;
        int lowestValueSeen = Integer.MAX_VALUE;
        int currentValue;
        int numberOfMoves = board.getCurrentPlayer().getLegalMoves().size();

        MoveTransition transition;

        System.out.println(board.getCurrentPlayer().getAlliance() + "thinking with depth = "+ depth);
        for(Move move: board.getCurrentPlayer().getLegalMoves()){
            transition = board.getCurrentPlayer().makeMove(move);
            if(!transition.getMoveStatus().isDone()){
                continue;
            }

            currentValue = board.getCurrentPlayer().getAlliance().isWhite()?
                           minimum(transition.getTransitionBoard(), depth - 1):
                           maximum(transition.getTransitionBoard(), depth - 1);
            if(board.getCurrentPlayer().getAlliance().isWhite() && currentValue >= highestValueSeen){
                highestValueSeen = currentValue;
                bestMove = move;
            }
            else if(board.getCurrentPlayer().getAlliance().isBlack() && currentValue <= lowestValueSeen){
                lowestValueSeen = currentValue;
                bestMove = move;
            }
        }

        long executionTime = System.currentTimeMillis() - startingTime;
        System.out.println("executed in "+ executionTime + " millis");
        return bestMove;
    }

    public int minimum(Board board, int depth){
        if(depth == 0 /* || gameOver*/){
            return boardEvaluator.evaluate(board,depth);
        }

        int lowestValueSeen = Integer.MAX_VALUE;
        int currentValue;
        MoveTransition transition;
        for(Move move: board.getCurrentPlayer().getLegalMoves()){
            transition = board.getCurrentPlayer().makeMove(move);
            if(!transition.getMoveStatus().isDone()){
                continue;
            }
            currentValue = maximum(transition.getTransitionBoard(), depth-1);
            if(currentValue<=lowestValueSeen){
                lowestValueSeen = currentValue;
            }
        }
        return lowestValueSeen;
    }
    public int maximum(Board board, int depth){
        if(depth == 0 /* || gameOver*/){
            return boardEvaluator.evaluate(board,depth);
        }

        int highestValueSeen = Integer.MIN_VALUE;
        int currentValue;
        MoveTransition transition;
        for(Move move: board.getCurrentPlayer().getLegalMoves()){
            transition = board.getCurrentPlayer().makeMove(move);
            if(!transition.getMoveStatus().isDone()){
                continue;
            }
            currentValue = minimum(transition.getTransitionBoard(), depth-1);
            if(currentValue>=highestValueSeen){
                highestValueSeen = currentValue;
            }
        }
        return highestValueSeen;
    }
    @Override
    public String toString() {
        return "MiniMax";
    }
}