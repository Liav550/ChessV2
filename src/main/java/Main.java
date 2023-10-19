import com.chess.engine.board.Board;
import com.chess.gui.Table;

public class Main {
    public static void main(String[] args) {
        Board board = Board.createStandardBoard();
        Table table = Table.getInstance();

        // todo - solve the issue that move highlighter highlights illegal moves that exposes player to check

        // todo - sit down with a pen and a paper and make sure you understand alpha beta
    }
}
