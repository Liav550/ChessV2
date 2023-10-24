package pgn;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MoveFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PGNUtilities {
    private static final Pattern KING_SIDE_CASTLE = Pattern.compile("O-O#?\\+?");
    private static final Pattern QUEEN_SIDE_CASTLE = Pattern.compile("O-O-O#?\\+?");
    private static final Pattern PLAIN_PAWN_MOVE = Pattern.compile("^([a-h][0-8])(\\+)?(#)?$");
    private static final Pattern PAWN_ATTACK_MOVE = Pattern.compile("(^[a-h])(x)([a-h][0-8])(\\+)?(#)?$");
    private static final Pattern PLAIN_MAJOR_MOVE = Pattern.compile("^(B|N|R|Q|K)([a-h]|[1-8])?([a-h][0-8])(\\+)?(#)?$");
    private static final Pattern MAJOR_ATTACK_MOVE = Pattern.compile("^(B|N|R|Q|K)([a-h]|[1-8])?(x)([a-h][0-8])(\\+)?(#)?$");
    private static final Pattern PLAIN_PAWN_PROMOTION_MOVE = Pattern.compile("(.*?)=(.*?)");
    private static final Pattern ATTACK_PAWN_PROMOTION_MOVE = Pattern.compile("(.*?)x(.*?)=(.*?)");

    private PGNUtilities(){
        throw new RuntimeException("You can't instantiate PGNUtilities");
    }

    public static Move createMove(Board board, String pgnString){
        final Matcher kingSideCastleMatcher = KING_SIDE_CASTLE.matcher(pgnString);
        final Matcher queenSideCastleMatcher = QUEEN_SIDE_CASTLE.matcher(pgnString);
        final Matcher pawnMoveMatcher = PLAIN_PAWN_MOVE.matcher(pgnString);
        final Matcher pawnAttackMatcher = PAWN_ATTACK_MOVE.matcher(pgnString);
        final Matcher pawnPromotionMatcher = PLAIN_PAWN_PROMOTION_MOVE.matcher(pgnString);
        final Matcher pawnAttackPromotionMatcher = ATTACK_PAWN_PROMOTION_MOVE.matcher(pgnString);
        final Matcher plainMajorMatcher = PLAIN_MAJOR_MOVE.matcher(pgnString);
        final Matcher attackMajorMatcher = MAJOR_ATTACK_MOVE.matcher(pgnString);

        int currentIndex;
        int destinationIndex;
        String destinationTileNotation;
        String specifier;
        if(kingSideCastleMatcher.matches()){
            return pullCastlingMove(board, "O-O");
        }
        if(queenSideCastleMatcher.matches()){
            return pullCastlingMove(board, "O-O-O");
        }
        if(pawnMoveMatcher.matches()){
            destinationTileNotation = pawnMoveMatcher.group(1);
            destinationIndex = BoardUtils.getIndexFromNotation(destinationTileNotation);
            currentIndex = deriveCurrentTileIndex(board, "P", destinationTileNotation, "");
            return MoveFactory.createMove(board, currentIndex, destinationIndex);
        }
        if(pawnAttackMatcher.matches()){
            destinationTileNotation = pawnAttackMatcher.group(3);
            destinationIndex = BoardUtils.getIndexFromNotation(destinationTileNotation);
            specifier = pawnAttackMatcher.group(1) != null? pawnAttackMatcher.group(1): "";
            currentIndex = deriveCurrentTileIndex(board, "P", destinationTileNotation, specifier);
            return MoveFactory.createMove(board, currentIndex,destinationIndex);
        }
        if(pawnAttackPromotionMatcher.matches()){
            destinationTileNotation = pawnAttackPromotionMatcher.group(2);
            destinationIndex = BoardUtils.getIndexFromNotation(destinationTileNotation);
            specifier = pawnAttackPromotionMatcher.group(1) != null? pawnAttackMatcher.group(1): "";
            currentIndex = deriveCurrentTileIndex(board, "P", destinationTileNotation, specifier);
            return MoveFactory.createMove(board, currentIndex, destinationIndex);
        }
        if(pawnPromotionMatcher.matches()){ // todo = could result to an error. check on find() instead of matches()
            destinationTileNotation = pawnPromotionMatcher.group(1);
            destinationIndex = BoardUtils.getIndexFromNotation(destinationTileNotation);
            currentIndex = deriveCurrentTileIndex(board, "P", destinationTileNotation, "");
            return MoveFactory.createMove(board, currentIndex, destinationIndex);
        }
        if(plainMajorMatcher.matches()){
            destinationTileNotation = plainMajorMatcher.group(3);
            destinationIndex = BoardUtils.getIndexFromNotation(destinationTileNotation);
            specifier = plainMajorMatcher.group(2) != null? plainMajorMatcher.group(2) : "";
            currentIndex = deriveCurrentTileIndex(board, plainMajorMatcher.group(1), destinationTileNotation, specifier);
            return MoveFactory.createMove(board, currentIndex, destinationIndex);
        }
        if(attackMajorMatcher.matches()){
            destinationTileNotation = attackMajorMatcher.group(4);
            destinationIndex = BoardUtils.getIndexFromNotation(destinationTileNotation);
            specifier = attackMajorMatcher.group(2) != null ? attackMajorMatcher.group(2): "";
            currentIndex = deriveCurrentTileIndex(board, attackMajorMatcher.group(1), destinationTileNotation, specifier);
            return MoveFactory.createMove(board, currentIndex,destinationIndex);
        }

        return Move.NULL_MOVE;
    }

    private static Move pullCastlingMove(Board board, String castlingMove){
        for(Move move: board.getCurrentPlayer().getLegalMoves()){
            if(move.isCastlingMove() && move.toString().equals(castlingMove)){
                return move;
            }
        }
        return Move.NULL_MOVE;
    }

    private static int deriveCurrentTileIndex(Board board, String movedPieceType, String destinationTile, String specifier){
        int destinationIndex = BoardUtils.getIndexFromNotation(destinationTile);
        List<Move> candidateMoves = new ArrayList<>();
        for(Move move: board.getCurrentPlayer().getLegalMoves()){
            if(move.getMovedPiece().toString().equals(movedPieceType) && move.getDestinationIndex() == destinationIndex){
                candidateMoves.add(move);
            }
        }

        if(candidateMoves.size() == 0){
            return -1;
        }
        return candidateMoves.size() == 1?
                candidateMoves.get(0).getCurrentIndex():
                searchBySpecifier(candidateMoves, specifier);
    }

    private static int searchBySpecifier(List<Move> candidateMoves, String specifier) {
        String possibility;
        List<Move> candidatesRefined = new ArrayList<>();
        for(Move move: candidateMoves){
            possibility = BoardUtils.getNotationAtIndex(move.getCurrentIndex());
            if(possibility.contains(specifier)){
                candidatesRefined.add(move);
            }
        }
        if(candidatesRefined.size() == 1){
            return candidatesRefined.get(0).getCurrentIndex();
        }
        return -1;
    }
}
