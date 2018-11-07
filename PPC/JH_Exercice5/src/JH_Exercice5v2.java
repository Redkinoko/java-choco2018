import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.tools.ArrayUtils;

/**
 *
 * @author Red
 */
public class JH_Exercice5v2
{

    /**
     * Programme principal
     * @param args aucun argument n'est pris en compte
     */
    public static void main(String[] args)
    {
        //Un carton = même indice sur poids et hauteur
        // 11 cartons
        // 3 piles
        int[] carton_hauteur = new int[]{  3, 5,10,11,15,17,20,21,25,26,30};
        int[] carton_poids   = new int[]{ 40,20,30,40,20,10,20,40,30,40,10};
        int hmax = 61;
        int pmax = 100;
        run(hmax, pmax, carton_hauteur, carton_poids);
    }
    
    /**
     * Permet de trouver toutes les piles optimales avec Knapsack
     * @param hMax le poids maximal par piles
     * @param pMax la hauteur maximal par piles
     * @param hauteur la liste des hauteurs de cartons
     * @param poids la liste des poids de cartons
     */
    public static void run(int hMax, int pMax, int[] hauteur, int[] poids)
    {
        //On récupère le nombre de cartons
        int n = hauteur.length;
        //On calcul du nombre de piles possible
        int minH = 0, minP = 0;
        for(int i=0 ; i<n ; i++)
        {
            minH += hauteur[i];
            minP += poids[i];
        }
        minH = minH/hMax;
        minP = minP/pMax;
        int nPile = (minH > minP)?minH:minP;
        //Création du modèle
        Model model = new Model(n + " Cartons max(hauteur="+hMax+" poids="+pMax+" nbPiles="+nPile+")");
        /*DECLARATION
        * Une pile est représenté par une ligne
        * Dans chaque ligne, l'index représente le numéro du carton
        * Si on décide de prendre le carton i, on met la valeur à 1
        * Sinon la valeur à 0
        */
        IntVar[][] c = new IntVar[nPile][n];
        for(int p=0 ; p<nPile ; p++)
        {
            c[p] = new IntVar[n];
            for(int i=0 ; i<n ; i++)
            {
                c[p][i] = model.intVar("c("+(i+1)+")p("+p+")", 0,1);
            }
        }
        
        //CONTRAINTES
        //Un carton ne peut être utilisé qu'une seule et unique fois.
        for(int i=0 ; i<n ; i++)
        {
            model.sum(ArrayUtils.getColumn(c, i), "=", 1).post();
        }
        
        /*On demande de résoudre les 2 équations suivantes :
        * c0*p0 + c1*p1 + .. + c(n-1)*p(n-1) = pMax
        * c0*h0 + c1*h1 + .. + c(n-1)*p(n-1) = hMax
        * par piles
        */
        for(int i=0 ; i<nPile ; i++)
        {
            model.scalar(c[i], poids,   "=", pMax).post();
            model.scalar(c[i], hauteur, "=", hMax).post();
        }
        
        /*RESOLUTION
        * Calcul d'une solution puis affichage sous forme de tableau
        */
        Solver solver = model.getSolver();
        solver.showStatistics();
        Solution solution = solver.findSolution();
        if(solution != null)
        {
            System.out.println(solution);
            afficherPiles(solution, n,  hauteur, poids);
        }
        else
        {
            System.out.println("Aucune solution.");
        }
    }
    
    /**
     * On parse la ligne de solution pour obtenir une meilleur lisibilité
     * @param solution le solution
     * @param n le nombre de cartons
     * @param hauteur le tableau contenant la hauteur des cartons
     * @param poids le tableau contenan le poids des cartons
     */
    public static void afficherPiles(Solution solution, int n, int[] hauteur, int[] poids)
    {
        String[] split = solution.toString().replace("Solution: ", "").split(", ");
        Pattern pattern = Pattern.compile("[0-9]+");
        int count = 0;
        int reset = 0;
        for(int i=0 ; i<n ; i++)
        {
            System.out.print(addSpaces((i+1), 2) + (i+1) + "|");
        }
        System.out.println(":carton n°");
        for(String s:split)
        {
            if(s.startsWith("c"))
            {
                int[] v = new int[3];
                Matcher matcher = pattern.matcher(s);
                for(int i=0 ; i<3 ; i++)
                {
                    matcher.find();
                    int j = Integer.parseInt(matcher.group(0));
                    v[i] = j;
                }
                if(v[2] == 1)
                {
                    System.out.print(addSpaces(v[2], 2)+v[2] + "|");
                }
                else
                {
                    System.out.print(addSpaces(0, 2)+"|");
                }
                count++;
                if(count >= n)
                {
                    System.out.println(":pile n°"+reset);
                    count = 0;
                    reset++;
                }
            }
            
        }
        for(int i=0 ; i<n ; i++)
        {
            System.out.print(addSpaces(poids[i], 2)+poids[i]+"|");
        }
        System.out.println(":poids");
        for(int i=0 ; i<n ; i++)
        {
            System.out.print(addSpaces(hauteur[i], 2)+hauteur[i]+"|");
        }
        System.out.println(":hauteur");
    }
    
    /**
    * Permet d'ajouter des espaces
    * @param j le nombre qui doit être aligné
    * @param n le nombre d'espace désiré en plus
    */
    public static String addSpaces(int j, int n)
    {
        String s = "";
        int deb = (int)(Math.pow(10, n));
        int t = j/10;
        if(t > 0)
        {
            deb = deb - j/t;
        }
        for(int i=deb ; i>0 ; i/=10)
        {
            if(j < i)
            {
                s += " ";
            }
        }
        return s;
    }
}
