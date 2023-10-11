package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Player {
    protected final Board board;
    protected final King playerKing;
    protected final Collection<Move> legalMoves;
    private final boolean isInCheck;

    public Player(Board board, Collection<Move> legalMoves, Collection<Move> opponentLegalMoves){
        this.board = board;
        this.playerKing = detectKing();
        this.legalMoves = ImmutableList.copyOf
                (Iterables.concat(legalMoves,calculateKingCastles(legalMoves,opponentLegalMoves)));
        this.isInCheck = !calculateAttacksOnTile(this.playerKing.getPiecePosition(), opponentLegalMoves).isEmpty();
    }

    protected static Collection<Move> calculateAttacksOnTile(int piecePosition, Collection<Move> opponentLegalMoves) {
        List<Move> attackingMoves = new ArrayList<>();
        for(Move move: opponentLegalMoves){
            if(move.getDestinationIndex() == piecePosition){
                attackingMoves.add(move);
            }
        }
        return ImmutableList.copyOf(attackingMoves);
    }

    public abstract Collection<Piece> getActivePieces();
    public abstract Alliance getAlliance();
    public abstract Player getOpponent();
    protected abstract Collection<Move> calculateKingCastles(Collection<Move> playerLegalMoves,
                                                             Collection<Move> opponentsLegalMoves);
    public Collection<Move> getLegalMoves() {
        return legalMoves;
    }

    public King getPlayerKing() {
        return playerKing;
    }

    public boolean isMoveLegal(Move move){
        return this.legalMoves.contains(move);
    }
    public boolean isInCheck(){
        return this.isInCheck;
    }
    public boolean isInCheckmate(){
        return this.isInCheck && !hasEscapeMoves();
    }

    public boolean isInStalemate(){
        return !this.isInCheck && !hasEscapeMoves();
    }
    public boolean isCastled(){
        return false;
    }

    protected boolean hasEscapeMoves() {
        MoveTransition transition;
        for(Move move: this.legalMoves){
            transition= makeMove(move);
            if(transition.getMoveStatus().isDone()){
                return true;
            }
        }
        return false;
    }
    public MoveTransition makeMove(Move move){
        if(!isMoveLegal(move)){
            return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
        }
        Board transitionBoard = move.execute();
        Collection<Move> kingAttacks = Player.calculateAttacksOnTile(transitionBoard.getCurrentPlayer().getOpponent().getPlayerKing().getPiecePosition(),
                transitionBoard.getCurrentPlayer().getLegalMoves());
        if(!kingAttacks.isEmpty()){
            return new MoveTransition(this.board,move,MoveStatus.LEAVES_PLAYER_IN_CHECK);
        }
        return new MoveTransition(this.board,move,MoveStatus.DONE);
    }

    private King detectKing() {
        for(Piece piece : getActivePieces()){
            if(piece.getPieceType().isKing()){
                return (King) piece;
            }
        }
        throw new RuntimeException("Invalid Board!!!");
    }

}
