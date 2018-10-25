/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ppc_tp1_choco;

import java.util.List;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

/**
 *
 * @author Red
 */
public class e5 {
    /**
     * 
     * @param maxHauteur le poids maximal par piles
     * @param maxPoids la hauteur maximal par piles
     * @param hauteurCartons la liste des hauteurs de cartons
     * @param poidsCartons la liste des poids de cartons
     */
    public static void run(int maxHauteur, int maxPoids, int[] hauteurCartons, int[] poidsCartons)
    {
        int n = hauteurCartons.length;
        Model model = new Model(n + " Cartons max(hauteur=" + maxHauteur + " poids=" + maxPoids + ")");
        int[] nbCartons = new int[n];
        IntVar totalHauteur = model.intVar(maxHauteur);
        IntVar totalPoids   = model.intVar(maxPoids);
        
        //Déclarations des ranges
        IntVar[] occurrences = new IntVar[nbCartons.length];
        for (int i = 0; i < nbCartons.length; i++) {
            nbCartons[i] = 1;
            occurrences[i] = model.intVar(i+"="+hauteurCartons[i] + "="+poidsCartons[i], 0, nbCartons[i]);
        }
        
        //Contraintes
        model.knapsack(occurrences, totalHauteur, totalPoids, hauteurCartons, poidsCartons).post();
        
        //Résolution
        Solver solver = model.getSolver();
        solver.showStatistics();
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
