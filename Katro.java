import java.util.HashMap;
import java.util.Scanner;

public class Katro{
    private int[][][]  grille;
    Scanner input;

    public Katro(int n){
        grille = new int[2][2][n];
        input = new Scanner(System.in);

        for(int i=0; i<2; i++){
            for(int j=0; j<n; j++){
                grille[0][i][j] = 2;
                grille[1][i][j] = 2;
            }
        }
    }

    public Katro(int[][][] grid){
        grille = new int[2][2][grid[0][0].length];

        for(int i=0; i<2; i++){
            for(int j=0; j<2; j++){
                for(int k=0; k<grid[i][j].length; k++)
                    grille[i][j][k] = grid[i][j][k];
            }
        }
    }

    /*
     * Affichage de l'état actuel de la grille de jeu
     */
    public void afficher(){
        for(int i=0; i<2; i++){
            for(int j=0; j<2; j++){
                for(int k=0; k<grille[i][j].length; k++)
                    System.out.printf("%d ",grille[i][j][k]);
                System.out.println();
            }

            if(i == 0){
                for(int l=0 ; l < 2*grille[0][0].length-1; l++)
                    System.out.print("-");
                System.out.println();
            }
        }
    }

    public void move(int joueur, int x , int y){
        int pions = grille[joueur][x][y];

        grille[joueur][x][y] = 0;
        
        while(pions != 0){
            if(x == 0){
                if(y != 0)
                   grille[joueur][x][--y]++;
                else
                    grille[joueur][++x][y]++;
            }
            else{
                if(y != grille[joueur][x].length-1)
                   grille[joueur][x][++y]++;
                else
                    grille[joueur][--x][y]++;
            }

            pions--;

            if(pions == 0 && grille[joueur][x][y] != 1 && !isGameOver((joueur+1)%2)){
                if(x == (joueur + 1)%2){
                    if(grille[(joueur+1)%2][(x+1)%2][y] != 0){
                        pions += grille[(joueur+1)%2][(x+1)%2][y];
                        grille[(joueur+1)%2][(x+1)%2][y] = 0; 
                    }
                    
                    if(firstRowAllEmpty((joueur+1)%2) && grille[(joueur+1)%2][x][y] != 0){
                        pions += grille[(joueur+1)%2][x][y];
                        grille[(joueur+1)%2][x][y] = 0;
                    } 
                }

                pions += grille[joueur][x][y];
                grille[joueur][x][y] = 0;
            }
        }
    }

    public boolean firstRowAllEmpty(int joueur){ 
        for(int elt : grille[joueur][(joueur+1)%2]){
            if(elt != 0)
                return false;
        }
        
        return true;
    } 

    public boolean isGameOver(int joueur){
        for(int[] row : grille[joueur]){
            for(int elt : row){
                if(elt != 0)
                    return false;
            }
        }

        return true;
    }

    public int countPions(int joueur){
        int count = 0;

        for(int i=0; i<2 ; i++){
            for(int j=0; j<grille[joueur][i].length; j++)
                count += grille[joueur][i][j]; 
        }

        return count;
    }

    public void play(){
        int x=-1,y=-1,turn=0;

        while(!isGameOver(0) && !isGameOver(1)){
            System.out.printf("Tour du joueur %d\n",turn);
            
            if(turn == 0){
                do{
                    System.out.print("Entrez la coordonnée x de la case où vous souhaitez commencer le déplacement: ");
                    x = input.nextInt();
                }while(x > 1 || x < 0);
    
                do{
                    System.out.print("Entrez la coordonnée y de la case où vous souhaitez commencer le déplacement: ");
                    y = input.nextInt();
                } while(y >= grille[turn][x].length || y < 0);
            }
            else{
                long startTime = System.currentTimeMillis();
                int[] bestAiMove = MinMaxAI.getBestMove(this, 8);
                long endTime = System.currentTimeMillis();

                x = bestAiMove[0];
                y = bestAiMove[1];

                System.out.printf("Temps de réflexion de l'IA: %dms\n",endTime - startTime);
            }

            System.out.printf("Mouvement partant de la position (%d,%d) par le joueur %d\n",x,y,turn);
            move(turn, x, y);
            
            afficher();

            turn = (turn+1)%2;

            if(isGameOver(turn))
                System.out.printf("Victoire du joueur %d",(turn+1)%2);
        }
    }

    /*
     * Récupere tous les mouvements possibles d'un joueur à partir de l'état actuel de la grille0
     * Nous ne considererons pas les cases ne contenant aucun pion qui ne sont pas déplaçable
     */
    public HashMap<String,Katro> getAllMovesAvailable(int joueur){
        HashMap<String,Katro> moves = new HashMap<>();

        for(int i=0; i<2; i++){
            for(int j=0; j<grille[joueur][i].length; j++){
                if(grille[joueur][i][j] != 0){
                    moves.put(String.format("(%d,%d)",i,j), new Katro(grille));
                    moves.get(String.format("(%d,%d)",i,j))
                         .move(joueur,i,j);
                }
            }
        }

        return moves;
    }

    /**
     * @params idx : Une position au format "(x,y)"
     * @return int[] {x,y}
     */
    public static int[] indexParser(String idx){
        String[] splitted = idx.split(",");

        return new int[]{Integer.valueOf(splitted[0].substring(1)), Integer.valueOf(splitted[1].substring(0,splitted[1].length()-1))};
    }
}