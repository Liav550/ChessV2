package com.chess.engine.player.Computer;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.moves.Move;
import com.chess.engine.moves.MoveFactory;
import com.chess.engine.moves.MoveTransition;
import com.chess.gui.Table.MoveLog;
import com.chess.openings.PGNUtilities;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestDbPgnConvertor {
    @Test
    public void test(){
        Board board = Board.createStandardBoard();
        MoveLog moveLog = new MoveLog();

        Move m1 = MoveFactory.createMove(board,
                BoardUtils.getIndexFromNotation("e2"),
                BoardUtils.getIndexFromNotation("e4"));
        MoveTransition t1 = board.getCurrentPlayer().makeMove(m1);
        moveLog.addMove(m1);

        assertTrue(t1.getMoveStatus().isDone());

        Move m2 = MoveFactory.createMove(t1.getTransitionBoard(),
                BoardUtils.getIndexFromNotation("e7"),
                BoardUtils.getIndexFromNotation("e5"));
        MoveTransition t2 = t1.getTransitionBoard().getCurrentPlayer().makeMove(m2);
        moveLog.addMove(m2);

        assertTrue(t2.getMoveStatus().isDone());

        Move m3 = MoveFactory.createMove(t2.getTransitionBoard(),
                BoardUtils.getIndexFromNotation("g1"),
                BoardUtils.getIndexFromNotation("f3"));
        MoveTransition t3 = t2.getTransitionBoard().getCurrentPlayer().makeMove(m3);
        moveLog.addMove(m3);

        assertTrue(t3.getMoveStatus().isDone());

        Move m4 = MoveFactory.createMove(t3.getTransitionBoard(),
                BoardUtils.getIndexFromNotation("b8"),
                BoardUtils.getIndexFromNotation("c6"));
        MoveTransition t4 = t3.getTransitionBoard().getCurrentPlayer().makeMove(m4);
        moveLog.addMove(m4);

        assertTrue(t4.getMoveStatus().isDone());

        Move m5 = MoveFactory.createMove(t4.getTransitionBoard(),
                BoardUtils.getIndexFromNotation("f1"),
                BoardUtils.getIndexFromNotation("b5"));
        MoveTransition t5 = t4.getTransitionBoard().getCurrentPlayer().makeMove(m5);
        moveLog.addMove(m5);

        assertTrue(t5.getMoveStatus().isDone());
        String[] dbMatchingMoves = {"1.", "e4", "e5", "2.", "Nf3", "Nc6", "3.", "Bb5"};
        String[] fixed = PGNUtilities.convertDatabasePGN(dbMatchingMoves);
        for (String s:
             fixed) {
            System.out.println(s + " " + s.length());
        }
        System.out.println("***************************");
        for (Move m:
             moveLog.getMoves()) {
            System.out.println(m.toString()+ " " +m.toString().length());
        }
        assertTrue(PGNUtilities.containsPGNString(moveLog.getMoves(), dbMatchingMoves) != -1);
    }
}
