import java.util.ArrayList;
import java.util.List;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

/**
 *
 * @author Red
 */
public class JH_Exercice5
{

    /**
     * Programme principal
     * @param args aucun argument n'est pris en compte
     */
    public static void main(String[] args)
    {
        //Un carton = même indice sur poids et hauteur
        //Il y a donc 5 cartons.
        int[] carton_hauteur = new int[]{ 1, 2, 2, 1, 3 };
        int[] carton_poids   = new int[]{ 3, 3, 1, 1, 4 };
        run(3, 4, carton_hauteur, carton_poids);
    }
    
    /**
     * Permet de trouver toutes les piles optimales avec Knapsack
     * @param maxHauteur le poids maximal par piles
     * @param maxPoids la hauteur maximal par piles
     * @param hauteurCartons la liste des hauteurs de cartons
     * @param poidsCartons la liste des poids de cartons
     */
    public static void run(int maxHauteur, int maxPoids, int[] hauteurCartons, int[] poidsCartons)
    {
        int n = hauteurCartons.length;
        Model model = new Model(n + " Cartons max(hauteur=" + maxHauteur + " poids=" + maxPoids + ")");

        //correspond au poids d'un objet à maximiser dans knapsack
        //C'est la hauteur de l'entrepot.
        IntVar totalHauteur = model.intVar(maxHauteur);
        
        //correspond à la valeur d'un objet dans knapsack
        //C'est le poids maximal d'une pile.
        IntVar totalPoids   = model.intVar(maxPoids);
        //---------------------------------------------------
        //Déclarations des ranges
        IntVar[] cartons = new IntVar[n];
        for (int i = 0; i < n; i++)
        {
            //On prend le carton = 1
            //On ne prend pas le carton = 0
            cartons[i] = model.intVar(i+"="+hauteurCartons[i] + "="+poidsCartons[i], 0, 1);
        }
        //---------------------------------------------------
        //Contraintes
        model.knapsack(cartons, totalHauteur, totalPoids, hauteurCartons, poidsCartons).post();
        //---------------------------------------------------
        //Résolution
        Solver solver = model.getSolver();
        solver.showStatistics();
        //On cherche toute les solutions optimales
        List<Solution> solutions = solver.findAllSolutions();

        if(solutions.size() > 0)
        {
            afficherPiles(solutions, maxHauteur, maxPoids);
        }
        else
        {
            System.out.println("Pas de solutions");
        }
    }
    
    /**
     * On parse la ligne de solution pour obtenir une meilleur lisibilité
     * @param solutions la liste des solutions possibles
     * @param maxHauteur la hauteur de l'entrepot
     * @param maxPoids  le poids maximal par pile
     */
    public static void afficherPiles(List<Solution> solutions, int maxHauteur, int maxPoids)
    {
        for(int i=0 ; i<solutions.size() ; i++)
		{
			System.out.println("[PILE N°"+i+"]");
			System.out.println("-------------");
			System.out.println("ID=HAUTEUR=POIDS=UTILISE(1=oui,0=non)");
			System.out.println(solutions.get(i).toString());
			System.out.println("- Détails :");
			String[] tuples = solutions.get(i).toString().replace("Solution: ", "").split(", ");
			int currentHauteur = 0;
			int currentPoids = 0;
			for(String tuple:tuples)
			{
				String[] quad = tuple.split("=");
				if(quad.length == 4 && quad[3].equals("1"))
				{
					currentHauteur += Integer.parseInt(quad[1]);
					currentPoids   += Integer.parseInt(quad[2]);
					System.out.println("\tCarton n°" + quad[0] + " hauteur=" + quad[1] + " poids=" + quad[2]);
				}
			}
			System.out.println("\t- Hauteur totale="+currentHauteur+" max="+maxHauteur + "\n\t- Poids totale="+currentPoids+" max="+maxPoids);
			System.out.println();
		}
    }
}
