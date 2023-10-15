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

/**
 * @author liavb
 * the Player class represents a player in the game of chess.
 */
// TODO - YOU DOCUMENTED ME
public abstract class Player {

    protected final Board board; // the board that the player plays on.
    protected final King playerKing; // the king of the player.
    protected final Collection<Move> legalMoves; // all the legal moves the player has.
    private final boolean isInCheck; // stores true if the player's king is in check and false otherwise.

    public Player(Board board, Collection<Move> legalMoves, Collection<Move> opponentLegalMoves){
        this.board = board;
        this.playerKing = detectKing(); // detecting the king of the player
        this.isInCheck = !calculateAttacksOnTile(this.playerKing.getPiecePosition(), opponentLegalMoves).isEmpty();
                                                           // calculating if there are any attacks on the player's king.
        this.legalMoves = ImmutableList.copyOf
                (Iterables.concat(legalMoves,calculateKingCastles(opponentLegalMoves)));
                 // initializing the list of the legal moves, plus the castling possibilities of the player (if exists).
    }

    public abstract Collection<Piece> getActivePieces();
    public abstract Alliance getAlliance();
    public abstract Player getOpponent();

    /**
     * @param opponentsLegalMoves: to know if the opponent attacks the critical tiles that determine if a castle is
     *                             legal or not.
     * @return: a collection of all possible castles the player has.
     */
    protected abstract Collection<Move> calculateKingCastles(Collection<Move> opponentsLegalMoves);
    /**
     * @param piecePosition: the position of the tile that is being attacked (if at all).
     * @param opponentLegalMoves: the legal moves of the opponent, because we want to know if the opponent can attack
     *                            our tile.
     * @return: a collection consists of every single move of the opponent, that attacks the given tile index.
     */
    protected static Collection<Move> calculateAttacksOnTile(int piecePosition, Collection<Move> opponentLegalMoves) {
        List<Move> attackingMoves = new ArrayList<>();
        for(Move move: opponentLegalMoves){
            if(move.getDestinationIndex() == piecePosition){
                attackingMoves.add(move); // if the destination of the opponent's move is the given tile, we know it
                // attacks it, so we add to our list
            }
        }
        return ImmutableList.copyOf(attackingMoves); // returns an immutable copy of the list
    }

    /**
     * @param move: the move that will be executed on the board.
     * @return: a move transition that has a move status, and determines if the move execution was successful or not.
     */
    public MoveTransition makeMove(Move move){
        if(!isMoveLegal(move)){
            return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
        }
        Board transitionBoard = move.execute(); // executing the move, then checking if it leaves the player in check.

        return transitionBoard.getCurrentPlayer().getOpponent().isInCheck()? // getting the opponent because execute()
                                                                             // changed the current player.
                new MoveTransition(transitionBoard, move, MoveStatus.LEAVES_PLAYER_IN_CHECK):
                new MoveTransition(transitionBoard, move, MoveStatus.DONE);
    }

    /**
     * @return true if the player has any legal escape moves, and false otherwise.
     */
    protected boolean hasLegalEscapeMoves() {
        MoveTransition transition;
        for(Move move: this.legalMoves){
            transition= makeMove(move);
            if(transition.getMoveStatus().isDone()){
                return true;
            }
        }
        // playing the possible moves one by one. if any of them are successful, return true. otherwise:
        return false;
    }

    /**
     * @return the player's king
     */
    private King detectKing() {
        for(Piece piece : getActivePieces()){ // looping through the player's active pieces

            if(piece.getPieceType().isKing()){
                return (King) piece;
            }
        }
        throw new RuntimeException("Invalid Board!!!"); // there is no board without a king
    }
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
        return this.isInCheck && !hasLegalEscapeMoves();
    }

    public boolean isInStalemate(){
        return !this.isInCheck && !hasLegalEscapeMoves();
    }
    public boolean isCastled(){
        return false;
    }

}
