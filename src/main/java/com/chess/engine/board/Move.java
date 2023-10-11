package com.chess.engine.board;

import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

import static com.chess.engine.board.Board.*;

public abstract class Move {
    protected final Board board; // the board in which the move happens
    protected final Piece movedPiece; // the moved piece
    protected final int destinationIndex; // the destination in which the piece will land

    public static final Move NULL_MOVE = new NullMove();

    public Move(Board board, Piece movedPiece, int destinationIndex) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationIndex = destinationIndex;
    }

    public int getDestinationIndex() {
        return destinationIndex;
    }

    public Piece getMovedPiece() {
        return movedPiece;
    }

    public Board execute() {
        Builder builder = new Builder();
        for(Piece piece: this.board.getCurrentPlayer().getActivePieces()){
            if(!this.movedPiece.equals(piece)){ // todo handle hashcode & equals
                builder.setPiece(piece);
            }
        }
        for(Piece piece: this.board.getCurrentPlayer().getOpponent().getActivePieces()){
            builder.setPiece(piece);
        }
        builder.setPiece(this.movedPiece.movePiece(this));
        builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
        return builder.build();
    }
    public boolean isAttack(){return false;}
    public boolean isCastlingMove(){return false;}
    public Piece getAttackedPiece(){return null;}
    public int getCurrentIndex(){return this.movedPiece.getPiecePosition();}
    @Override
    public boolean equals(Object other) {
        if(this == other){
            return true;
        }
        if(!(other instanceof Move)){
            return false;
        }
        Move otherMove = (Move) other;
        return this.getDestinationIndex() == otherMove.getDestinationIndex() &&
               this.getMovedPiece().equals(((Move) other).getMovedPiece());
    }
    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + this.destinationIndex;
        result = 31 * result + this.movedPiece.hashCode();
        return result;
    }

        /*
                This class extends the Move class. It is being equivalent to a normal chess move
                with no special cases (no enpassent, castling, promotion, etc).
        */
    public static final class MajorMove extends Move{
        public MajorMove(Board board, Piece movedPiece, int destinationIndex) {
            super(board, movedPiece, destinationIndex);
        }
    }
    /*
        This class describes an attacking move. This means that there is an enemy piece on the current piece's
        destination, and that our piece is attacking the enemy piece.
     */
    public static class AttackMove extends Move{
        private final Piece attackedPiece; // the piece that is being attacked by the moved piece

        public AttackMove(Board board, Piece movedPiece, int destinationIndex, Piece attackedPiece) {
            super(board, movedPiece, destinationIndex);
            this.attackedPiece = attackedPiece;
        }
        @Override
        public Piece getAttackedPiece() {
            return attackedPiece;
        }
        @Override
        public boolean isAttack(){
            return true;
        }
        @Override
        public Board execute() {
            return null;
        }
        @Override
        public int hashCode(){
            return this.attackedPiece.hashCode() + super.hashCode();
        }

        @Override
        public boolean equals(Object other){
            if(this == other){
                return true;
            }
            if(!(other instanceof AttackMove)){
                return false;
            }
            AttackMove otherAttackMove = (AttackMove) other;
            return super.equals(otherAttackMove) && this.attackedPiece.equals(((AttackMove) other).getAttackedPiece());
        }
    }
    public static final class PawnMove extends Move{
        public PawnMove(Board board, Piece movedPiece, int destinationIndex) {
            super(board, movedPiece, destinationIndex);
        }
    }
    public static class PawnAttackMove extends AttackMove{
        public PawnAttackMove(Board board, Piece movedPiece, int destinationIndex,Piece attackedPiece) {
            super(board, movedPiece, destinationIndex,attackedPiece);
        }
    }
    public static final class PawnEnPassentAttackMove extends PawnAttackMove{
        public PawnEnPassentAttackMove(Board board, Piece movedPiece, int destinationIndex, Piece attackedPiece) {
            super(board, movedPiece, destinationIndex,attackedPiece);
        }
    }

    public static final class PawnJump extends Move{
        public PawnJump(Board board, Piece movedPiece, int destinationIndex) {
            super(board, movedPiece, destinationIndex);
        }

        @Override
        public Board execute() {
            Builder builder = new Builder();
            for(Piece piece: board.getCurrentPlayer().getActivePieces()){
                if(!piece.equals(movedPiece)){
                    builder.setPiece(piece);
                }
            }
            for(Piece piece: board.getCurrentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }
            Pawn movedPawn = (Pawn) this.movedPiece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setEnPassentPawn(movedPawn);
            builder.setMoveMaker(board.getCurrentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }

    public static abstract class CastleMove extends Move{
        protected final Rook castleRook;
        protected final int castleRookStart;
        protected final int castleRookDestination;
        public CastleMove(Board board, Piece movedPiece, int destinationIndex,
                          Rook castleRook, int castleRookStart, int castleRookDestination) {
            super(board, movedPiece, destinationIndex);
            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.castleRookDestination = castleRookDestination;
        }

        public Rook getCastleRook() {
            return castleRook;
        }
        @Override
        public boolean isCastlingMove(){
            return true;
        }

        @Override
        public Board execute(){
            Builder builder = new Builder();
            for(Piece piece: board.getCurrentPlayer().getActivePieces()){
                if(this.movedPiece.equals(piece) && !castleRook.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for(Piece piece: board.getCurrentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setPiece(new Rook(this.castleRookDestination,this.castleRook.getPieceAlliance()));
            builder.setMoveMaker(board.getCurrentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }

    public static final class KingSideCastleMove extends CastleMove{
        public KingSideCastleMove(Board board, Piece movedPiece, int destinationIndex,
                                  Rook castleRook, int castleRookStart, int castleRookDestination) {
            super(board, movedPiece, destinationIndex,castleRook,castleRookStart,castleRookDestination);
        }
        @Override
        public String toString(){
            return "O-O";
        }
    }
    public static final class QueenSideCastleMove extends CastleMove{
        public QueenSideCastleMove(Board board, Piece movedPiece, int destinationIndex,
                                   Rook castleRook, int castleRookStart, int castleRookDestination) {
            super(board, movedPiece, destinationIndex,castleRook,castleRookStart,castleRookDestination);
        }
        @Override
        public String toString(){
            return "O-O-O";
        }
    }

    public static final class NullMove extends Move{

        public NullMove() {
            super(null, null, -1);
        }

        @Override
        public Board execute() {
            throw new RuntimeException("can't execute a null move!!!");
        }
    }

    public static class MoveFactory{
        public MoveFactory() {
            throw new RuntimeException("can't instantiate MoveFactory!!!");
        }
        public static Move createMove(Board board, int currentIndex, int destinationIndex){
            for(Move move: board.getAllLegalMoves()){
                if(move.getCurrentIndex() == currentIndex &&
                   move.getDestinationIndex() == destinationIndex){
                    return move;
                }
            }
                return NULL_MOVE;
        }
    }
}
