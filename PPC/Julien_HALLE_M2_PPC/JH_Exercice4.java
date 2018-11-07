import java.util.ArrayList;
import java.util.List;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.tools.ArrayUtils;

/**
 *
 * @author Red
 */
public class JH_Exercice4
{

    /**
     * Permet de lancer le test 1
     * Qui est une simple image composé d'un coin
     */
    private static final int TEST_CUSTOM1 = 1;
    /**
     * Permet de lancer le test 2
     * Qui est la représentation de la lettre R
     */
    private static final int TEST_CUSTOM2 = 3;
    /**
     * Permet de lancer le test 3
     * Qui est la représentation d'un personnage
     */
    private static final int TEST_CUSTOM3   = 2;
    
    /**
     * Programme principal
     * @param args aucun argument n'est pris en compte
     */
    public static void main(String[] args)
    {
        test(TEST_CUSTOM3);
    }
    
    //INSTANCE DE TEST TOMO
    /*
    [D]00024 12432 12100
    [M]00121 23421 42000
    [c]22264 222
    [l]11633 611
    */
    public static int[] ligne()       { return new int[]{ 1,1,6,3,3, 6,1,1 }; }
    public static int[] colonne()     { return new int[]{ 2,2,2,6,4, 2,2,2 }; }
    public static int[] montante()    { return new int[]{ 0,0,1,2,1, 2,3,4,2,1, 4,2,0,0,0 }; }
    public static int[] descendante() { return new int[]{ 0,0,0,2,4, 1,2,4,3,2, 1,2,1,0,0 }; }
    //Gauche vers droite
    
    /**
     * Jeu de tests
     * @param i l'image a tester
     */
    public static void test(int i)
    {
        switch(i)
        {
            case 1 :
                /*
                1,1,1
                0,0,1
                1,0,1
                */
                run(new int[]{2,1,3}, new int[]{3,1,2}, new int[]{1,0,2,2,1}, new int[]{1,1,2,1,1});
                break;
            case 2:
                run(ligne(), colonne(), montante(), descendante());
                break;
            default :
                /*
                1,1,1,0
                1,0,0,1
                1,1,1,0
                1,0,0,1
                */
                run(new int[]{4,2,2,2}, new int[]{3,2,3,2}, new int[]{1,1,2,3,1,2,0}, new int[]{1,2,2,2,2,0,1});
        }
    }
    
    /**
     * Permet de lancer une recherche tomographique
     * @param ligne le nombre de pixel noir sur la ligne
     * @param colonne le nombre de pixel noir sur la colonne
     * @param montante le nombre de pixel noir sur la diagonale montante de gauche à droite
     * @param descendante le nombre de pixel noir sur la diagonale descendante de gauche à droite
     */
    public static void run(int[] ligne, int[] colonne, int[] montante, int[] descendante)
    {
        //Extraction de la taille de l'image ligne.length ou colonne.length
        int n = ligne.length;
        Model model         = new Model("Tomologie n = " + n + "");
        //Matrice représentation physique d'une image 2d.
        IntVar[][] vars     = new IntVar[n][n];
        //------------------------------------------------------
        //RANGES
        for(int j = 0 ; j < n ; j++)
        {
            for(int i = 0 ; i < n ; i++)
            {
                //Chaque case peut contenir un pixel blanc ou noir
                vars[i][j]  = model.intVar(""+i+"="+j+"", 0,1);
            }
        }
        //------------------------------------------------------
        //CONTRAINTES
        //_Ligne
        for(int i=0; i<n ; i++)
        {
            //Chaque ligne ne doit pas dépasser la somme donnée
            model.sum(vars[i], "=", ligne[i]).post();
        }
        //_Colonne
        for(int i=0; i<n ; i++)
        {
            //Chaque colonne ne doit pas dépasser la somme donnée
            model.sum(ArrayUtils.getColumn(vars, i), "=", colonne[i]).post();
        }
        //_Diagonales
        //On remplit un tableau temporaire contenant les valeurs de la diagonale
        //On poste la contrainte à partir des valeurs contenus dans ce tableau
        IntVar[] tmp;
        //MONTANTE
        int j = 1;
        while(j < (n+n))
        {
            //Chaque diagonal montante ne doit pas dépasser la somme donnée
            if(j <= n)//Partie inférieur gauche et milieu
            {
                tmp = new IntVar[j];
                for(int i=0 ; i < j ; i++)
                {
                    tmp[i] = vars[i][(n-j+i)];
                }
                model.sum(tmp, "=", montante[j-1]).post();
            }
            else//Partie supérieur droite
            {
                tmp = new IntVar[(n-j%n)];
                for(int i=0 ; i < (n-j%n) ; i++)
                {
                    tmp[i] = vars[(n-(n-j%n)+i)][i];
                }
                model.sum(tmp, "=", montante[j-1]).post();
            }
            j++;
        }
        //DESCENDANTE
        j = 1;
        while(j < (n+n))
        {
            //Chaque diagonal descendante ne doit pas dépasser la somme donnée
            if(j <= n)
            {
                tmp = new IntVar[j];
                for(int i=0 ; i<j ; i++)
                {
                    tmp[i] = vars[(i)][(j-1-i)];
                }
                model.sum(tmp, "=", descendante[j-1]).post();
            }
            else
            {
                tmp = new IntVar[(n-j%n)];
                for(int i=0 ; i<(n-j%n) ; i++)
                {
                    tmp[i] = vars[(j%n+i)][(n-i-1)];
                }
                model.sum(tmp, "=", descendante[j-1]).post();
            }
            j++;
        }
        //------------------------------------------------------
        //RESOLUTION
        //Calcul de la solution et affichage
        model.getSolver().showStatistics();
        Solution solution = model.getSolver().findSolution();

        if(solution != null)
        {
            System.out.println(solution.toString());
            afficherMatrice(n, solution.toString());
        }
        else
        {
            System.out.println("Pas de solutions");
        }
    }
    
    /**
     * Permet de décompiler la string de solution.
     * @param n la taille de l'image nxn
     * @param resultat la chaine à décompiler
     */
    public static void afficherMatrice(int n, String resultat)
    {
        int[][] view = new int[n][n];
        for(int y=0 ; y<n ; y++) 
            for(int x=0 ; x<n ; x++) 
                view[x][y] = 0;
        String[] split1 = resultat.replaceFirst("Solution: ", "").split(", ");
        for (String s : split1) {
            String[] s2 = s.split("=");
            if(s2.length == 3)
            {
                view[Integer.parseInt(s2[0])][Integer.parseInt(s2[1])] = Integer.parseInt(s2[2]);
            }
        }
        for(int y=0 ; y<n ; y++)
        {
            for(int x=0 ; x<n ; x++)
            {
                //On remplace un pixel noir par un carré
                if(view[x][y] == 1) { System.out.print("◘|"); }
                //On remplace un pixel blanc par un vide
                else { System.out.print("░|"); }
            }
            System.out.println();
        }
    }
    
}
