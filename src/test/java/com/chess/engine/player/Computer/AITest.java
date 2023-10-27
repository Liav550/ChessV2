package com.chess.engine.player.Computer;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.moves.Move;
import com.chess.engine.moves.MoveFactory;
import com.chess.engine.player.MoveTransition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AITest {

    @Test
    void testFoolsMate() {
        Board board = Board.createStandardBoard();

        MoveTransition t1 = board.getCurrentPlayer().makeMove(MoveFactory.createMove(board,
                BoardUtils.getIndexFromNotation("f2"), BoardUtils.getIndexFromNotation("f3")));
        assertTrue(t1.getMoveStatus().isDone());

        MoveTransition t2 = t1.getTransitionBoard().getCurrentPlayer().makeMove(MoveFactory.createMove(t1.getTransitionBoard(),
                BoardUtils.getIndexFromNotation("e7"), BoardUtils.getIndexFromNotation("e5")));
        assertTrue(t2.getMoveStatus().isDone());

        MoveTransition t3 = t2.getTransitionBoard().getCurrentPlayer().makeMove(MoveFactory.createMove(t2.getTransitionBoard(),
                BoardUtils.getIndexFromNotation("g2"), BoardUtils.getIndexFromNotation("g4")));
        assertTrue(t3.getMoveStatus().isDone());

        MoveStrategy moveStrategy = new MiniMax(4);
        Move aiMove = moveStrategy.execute(t3.getTransitionBoard());
        Move bestMove = MoveFactory.createMove(t3.getTransitionBoard(),
                BoardUtils.getIndexFromNotation("d8"), BoardUtils.getIndexFromNotation("h4"));

        System.out.println(aiMove.toString()); // OMG IT WORKS

        assertEquals(aiMove,bestMove);
    }

    @Test
    public void testScholarsMate(){
        Board board = Board.createStandardBoard();
        MoveTransition t1 = board.getCurrentPlayer().makeMove(MoveFactory.createMove(board,
                BoardUtils.getIndexFromNotation("e2"), BoardUtils.getIndexFromNotation("e4")));
        assertTrue(t1.getMoveStatus().isDone());

        MoveTransition t2 = t1.getTransitionBoard().getCurrentPlayer().makeMove(MoveFactory.createMove(t1.getTransitionBoard(),
                BoardUtils.getIndexFromNotation("e7"), BoardUtils.getIndexFromNotation("e5")));
        assertTrue(t2.getMoveStatus().isDone());

        MoveTransition t3 = t2.getTransitionBoard().getCurrentPlayer().makeMove(MoveFactory.createMove(t2.getTransitionBoard(),
                BoardUtils.getIndexFromNotation("f1"), BoardUtils.getIndexFromNotation("c4")));
        assertTrue(t3.getMoveStatus().isDone());

        MoveTransition t4 = t3.getTransitionBoard().getCurrentPlayer().makeMove(MoveFactory.createMove(t3.getTransitionBoard(),
                BoardUtils.getIndexFromNotation("b8"), BoardUtils.getIndexFromNotation("c6")));
        assertTrue(t4.getMoveStatus().isDone());

        MoveTransition t5 = t4.getTransitionBoard().getCurrentPlayer().makeMove(MoveFactory.createMove(t4.getTransitionBoard(),
                BoardUtils.getIndexFromNotation("d1"), BoardUtils.getIndexFromNotation("f3")));
        assertTrue(t5.getMoveStatus().isDone());

        MoveTransition t6 = t5.getTransitionBoard().getCurrentPlayer().makeMove(MoveFactory.createMove(t5.getTransitionBoard(),
                BoardUtils.getIndexFromNotation("f8"), BoardUtils.getIndexFromNotation("c5")));
        assertTrue(t6.getMoveStatus().isDone());

        MoveStrategy strategy = new MiniMax(4);
        Move aiMove = strategy.execute(t6.getTransitionBoard());
        Move bestMove = MoveFactory.createMove(t6.getTransitionBoard(), BoardUtils.getIndexFromNotation("f3"),
                        BoardUtils.getIndexFromNotation("f7"));

        System.out.println(aiMove.toString());
        assertEquals(aiMove,bestMove);
    }
}