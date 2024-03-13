package com.chess.engine.player;

import com.chess.FenUtilities;
import com.chess.engine.board.Board;
import com.chess.engine.moves.Move;
import com.chess.openings.PGNUtilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MLModel implements MoveStrategy {
    private static String SERVER_ADDRESS = "localhost";
    private static int PORT = 8888;
    @Override
    public Move execute(Board board) {
        String pgnString = getBestMove(FenUtilities.getFENString(board));
        return PGNUtilities.createMove(board,pgnString);
    }

    private String getBestMove(String fen) {
        try (
                Socket socket = new Socket(SERVER_ADDRESS, PORT);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        )
        {
            out.println(fen);
            String pgnString = in.readLine();
            return pgnString;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
