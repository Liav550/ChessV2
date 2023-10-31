package com.chess.engine.player.Computer;

import com.chess.engine.pieces.Piece;
import com.chess.engine.player.Player;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PawnStructureAnalyzer {
    private static final int DOUBLED_PAWN_PENALTY = -10;
    private static final int ISOLATED_PAWN_PENALTY = -10;
    private PawnStructureAnalyzer(){
        throw new RuntimeException("you can't instantiate PawnStructureAnalyzer!!!");
    }

    public static int pawnStructureScore(Player player){
        int[] pawnsForEachColumnCounter = countPawnForEachColumn(getPlayerPawns(player));
        return getDoubledPawnsTotalPenalty(pawnsForEachColumnCounter) +
                getIsolatedPawnsTotalPenalty(pawnsForEachColumnCounter);
    }
    private static int isolatedPawnPenalty(final Player player) {
        return getIsolatedPawnsTotalPenalty(countPawnForEachColumn(getPlayerPawns(player)));
    }

    private static int doubledPawnPenalty(final Player player) {
        return getDoubledPawnsTotalPenalty(countPawnForEachColumn(getPlayerPawns(player)));
    }

    private static Collection<Piece> getPlayerPawns(Player player){
        List<Piece> pawns = new ArrayList<>();
        for(Piece piece: player.getActivePieces()){
            if(piece.getPieceType() == Piece.PieceType.PAWN){
                pawns.add(piece);
            }
        }
        return ImmutableList.copyOf(pawns);
    }

    private static int[] countPawnForEachColumn(Collection<Piece> pawns){
        int[] counter = new int[8];
        for(Piece pawn: pawns){
            counter[pawn.getPiecePosition()%8]++;
        }
        return counter;
    }

    private static int getDoubledPawnsTotalPenalty(int[] pawnCounter){
        int totalPawnsStacked = 0;
        for(int pawnCountAtColumn: pawnCounter){
            if(pawnCountAtColumn > 1){
                totalPawnsStacked += pawnCountAtColumn;
            }
        }
        return totalPawnsStacked * DOUBLED_PAWN_PENALTY;
    }
    private static int getIsolatedPawnsTotalPenalty(int[] pawnCounter) {
        int totalIsolatedPawns = 0;
        if(pawnCounter[0] > 0 && pawnCounter[1] == 0) {
            totalIsolatedPawns += pawnCounter[0];
        }
        if(pawnCounter[7] > 0 && pawnCounter[6] == 0) {
            totalIsolatedPawns += pawnCounter[7];
        }
        for(int i = 1; i < pawnCounter.length - 1; i++) {
            if((pawnCounter[i-1] == 0 && pawnCounter[i+1] == 0)) {
                totalIsolatedPawns += pawnCounter[i];
            }
        }
        return totalIsolatedPawns * ISOLATED_PAWN_PENALTY;
    }
}
