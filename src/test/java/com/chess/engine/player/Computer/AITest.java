package com.chess.engine.player.Computer;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.player.MoveTransition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AITest {

    @Test
    void testFoolsMate() {
        Board board = Board.createStandardBoard();

        MoveTransition t1 = board.getCurrentPlayer().makeMove(Move.MoveFactory.createMove(board,
                BoardUtils.getIndexFromNotation("f2"), BoardUtils.getIndexFromNotation("f3")));
        assertTrue(t1.getMoveStatus().isDone());

        MoveTransition t2 = t1.getTransitionBoard().getCurrentPlayer().makeMove(Move.MoveFactory.createMove(t1.getTransitionBoard(),
                BoardUtils.getIndexFromNotation("e7"), BoardUtils.getIndexFromNotation("e5")));
        assertTrue(t2.getMoveStatus().isDone());

        MoveTransition t3 = t2.getTransitionBoard().getCurrentPlayer().makeMove(Move.MoveFactory.createMove(t2.getTransitionBoard(),
                BoardUtils.getIndexFromNotation("g2"), BoardUtils.getIndexFromNotation("g4")));
        assertTrue(t3.getMoveStatus().isDone());

        MoveStrategy moveStrategy = new MiniMax(4);
        Move aiMove = moveStrategy.execute(t3.getTransitionBoard());
        Move bestMove = Move.MoveFactory.createMove(t3.getTransitionBoard(),
                BoardUtils.getIndexFromNotation("d8"), BoardUtils.getIndexFromNotation("h4"));

        System.out.println(aiMove.toString()); // OMG IT WORKS

        assertEquals(aiMove,bestMove);
    }
}