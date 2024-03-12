import java.util.HashMap;

public class MinMaxAI{
    public static int minMax(Katro katro,int depth,boolean isMax){
        HashMap<String, Katro> moves = katro.getAllMovesAvailable(isMax ? 1 : 0);
        
        if(depth == 0 || moves.size() == 0)
            return katro.countPions(1);
        
        if(isMax){
            int v = Integer.MIN_VALUE;

            for(String move : moves.keySet())
                v = Math.max(v,minMax(moves.get(move), depth-1, false));
            
            return v;
        }
        
        else{
            int v = Integer.MAX_VALUE;

            for(String move : moves.keySet())
                v = Math.min(v,minMax(moves.get(move), depth-1, true));
    
            return v;
        }
    }

    /**
     * Basé sur l'algorithme minMax, récupère le meilleur mouvement pour le prochain coup
     * @params katro: l'état de la grille actuelle
     * @params depth: la profondeur pour l'algorithme 
     */
    public static int[] getBestMove(Katro katro, int depth){
        HashMap<String,Katro> moves = katro.getAllMovesAvailable(1);        
        int maxScore = Integer.MIN_VALUE;
        String maxIdx = "";

        for(String move : moves.keySet()){
            int score = Math.max(maxScore,minMax(moves.get(move), depth-1, false));

            if(maxScore < score)
                maxIdx = move;
        }

        return Katro.indexParser(maxIdx);
    }
}
