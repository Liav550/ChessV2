package com.chess.engine.player.Computer;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move.MoveFactory;
import com.chess.engine.player.MoveTransition;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class CheckmateTest {
    @Test
    public void testFoolsMate(){
        final Board board = Board.createStandardBoard();
        final MoveTransition t1 = board.getCurrentPlayer()
                .makeMove(MoveFactory.createMove(board, BoardUtils.getIndexFromNotation("f2"),
                        BoardUtils.getIndexFromNotation("f3")));

        assertTrue(t1.getMoveStatus().isDone());
        final MoveTransition t2 = t1.getTransitionBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t1.getTransitionBoard(), BoardUtils.getIndexFromNotation("e7"),
                        BoardUtils.getIndexFromNotation("e5")));

        assertTrue(t2.getMoveStatus().isDone());

        final MoveTransition t3 = t2.getTransitionBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t2.getTransitionBoard(), BoardUtils.getIndexFromNotation("g2"),
                        BoardUtils.getIndexFromNotation("g4")));

        assertTrue(t3.getMoveStatus().isDone());

        final MoveTransition t4 = t3.getTransitionBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t3.getTransitionBoard(), BoardUtils.getIndexFromNotation("d8"),
                        BoardUtils.getIndexFromNotation("h4")));

        assertTrue(t4.getMoveStatus().isDone());

        assertTrue(t4.getTransitionBoard().getCurrentPlayer().isInCheckmate());
    }
}
