package com.chess;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;
import com.chess.openings.PGNUtilities;

public class FenUtilities {
    private FenUtilities(){
        throw new RuntimeException("Can't instantiate FenUtilities!");
    }

    public static String getFENString(Board board){
        String fen = getPiecesString(board) + " "+
                getCurrentTurn(board);
        String castles = getCastlesString(board);
        String enPassentSquare = getEnPassentSquareString(board);

        if(!castles.isEmpty()){
            fen += (" " + castles);
        }
        if(!enPassentSquare.isEmpty()){
            fen += (" "+ enPassentSquare);
        }

        fen += " 0 1";
        return fen;
    }

    private static String getEnPassentSquareString(Board board) {
        Pawn enPassentPawn = board.getEnPassentPawn();
        if(enPassentPawn == null){
            return "-";
        }
        int offset = enPassentPawn.getPieceAlliance().getDirection() * 8;
        return BoardUtils.getNotationAtIndex(enPassentPawn.getPiecePosition() - offset);
    }

    private static String getCastlesString(Board board) {
        boolean isWhiteKingSideValid = false;
        boolean isWhiteQueenSideValid = false;
        boolean isBlackKingSideValid = false;
        boolean isBlackQueenSideValid = false;
        Rook rook;
        if(board.getWhitePlayer().getPlayerKing().isFirstMove()){
            for (Piece piece:
                 board.getWhitePlayer().getActivePieces()) {
                if(!piece.getPieceType().isRook()){
                    continue;
                }
                rook = (Rook) piece;
                if(rook.getRookSide().isKing() && rook.isFirstMove()){
                    isWhiteKingSideValid = true;
                }
                else if(!rook.getRookSide().isKing() && rook.isFirstMove()){
                    isWhiteQueenSideValid = true;
                }
            }
        }
        if(board.getBlackPlayer().getPlayerKing().isFirstMove()){
            for (Piece piece:
                    board.getBlackPlayer().getActivePieces()) {
                if(!piece.getPieceType().isRook()){
                    continue;
                }
                rook = (Rook) piece;
                if(rook.getRookSide().isKing() && rook.isFirstMove()){
                    isBlackKingSideValid = true;
                }
                else if(!rook.getRookSide().isKing() && rook.isFirstMove()){
                    isBlackQueenSideValid = true;
                }
            }
        }

        String result = "";
        boolean hasRights = false;

        if(isWhiteKingSideValid){
            hasRights = true;
            result += 'K';
        }
        if(isWhiteQueenSideValid){
            hasRights = true;
            result += 'Q';
        }
        if(isBlackKingSideValid){
            hasRights = true;
            result += 'k';
        }
        if(isBlackQueenSideValid){
            hasRights = true;
            result += 'q';
        }

        return hasRights? result: "-";
    }

    private static char getCurrentTurn(Board board) {
        return Character.toLowerCase(board.getCurrentPlayer().getAlliance().toString().charAt(0));
    }

    private static String getPiecesString(Board board) {
        String boardStr = board.toString().replaceAll(" ", "");
        boardStr = boardStr.
                replaceAll("--------", "8").
                replaceAll("-------","7").
                replaceAll("------","6").
                replaceAll("-----","5").
                replaceAll("----","4").
                replaceAll("---","3").
                replaceAll("--","2").
                replaceAll("-","1");
        boardStr = boardStr.replaceAll("\n", "/");
        boardStr = boardStr.substring(0, boardStr.length() - 1);

        return boardStr;
    }
}
