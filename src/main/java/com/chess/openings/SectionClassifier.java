package com.chess.openings;

import com.chess.engine.moves.Move;

import java.util.Random;

public class SectionClassifier {
    private SectionClassifier() {
        throw new RuntimeException("can't instantiate SectionClassifier");
    }

    public static char classify(Move whiteMove, Move blackMove){
        if(whiteMove.toString().equals("e4")){
            if(blackMove.toString().equals("e5") || blackMove.toString().equals("e6")){
                return 'C';
            }
            else{
                return 'B';
            }
        }
        if(whiteMove.toString().equals("d4")){
            if(blackMove.toString().equals("Nf6")){
                return 'E';
            }
            else{
                return 'D';
            }
        }
        return 'A';
    }

    public static char classify(Move firstMove){
        Random random = new Random();
        if(firstMove.toString().equals("e4")){
            return random.nextInt(2) == 0? 'B': 'C';
        }
        if(firstMove.toString().equals("d4")){
            return random.nextInt(2) == 0? 'D': 'E';
        }
        return 'A';
    }
}
