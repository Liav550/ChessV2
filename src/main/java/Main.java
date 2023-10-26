import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.gui.Table;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

import java.util.Comparator;

public class Main {
    public static void main(String[] args) {
        Board board = Board.createStandardBoard();
        Table table = Table.getInstance();


        // todo - solve the issue that move highlighter highlights illegal moves that exposes player to check

        // todo - sit down with a pen and a paper and make sure you understand alpha beta

        // todo - look at piece's location bonus, and bishop pair advantages

    }
}
