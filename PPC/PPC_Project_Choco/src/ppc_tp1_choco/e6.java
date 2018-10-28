/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ppc_tp1_choco;

import java.util.ArrayList;
import java.util.List;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;
import ppc_tp1_choco.view.Triplet;

/**
 *
 * @author Red
 */
public class e6 {

    /**
     * S(1) = 1
     * S(2) = 4
     * S(3) = 13
     * S(4) = 44
     * S(5) = 
     * S(6) = 
     * @param n 
     */
    public static void run(int n)
    {
        Model lastModel        = null;
        Solution lastSolution  = null;
        Solution solution      = null;
        int lastTripletsSize   = 0;
        List<Triplet> triplets = new ArrayList();
        
        int s = 1;
        do
        {
            solution = null;
            Model model = new Model("nombre de Schur pour " + n + " couleurs");
            //L'index correspond au nombre la couleur à la valeur dans la case
            IntVar[] colors = new IntVar[s+1];
            for(int i=0 ; i<colors.length ; i++)
            {
                //0 = couleur non attribuée
                //1 = couleur 1
                //2 = couleur 2
                colors[i] = model.intVar("c="+(i+1), 0,n);
            }
            //Contraintes
            //indice(0) = chiffre 1
            //indice(1) = chiffre 2
            model.arithm(colors[0], "=", 1).post();
            for(int i=0 ; i<colors.length ; i++)
            {
                model.arithm(colors[i], "!=", 0).post();
            }
            calcTriplets(n,s,triplets);
            for(Triplet t:triplets)
            {
                int a = t.a-1;
                int b = t.b-1;
                int c = t.c-1;
                model.ifThen(model.arithm(colors[a], "=", colors[b]), model.arithm(colors[c], "!=", colors[a]));
            }
            //-------------
            solution = model.getSolver().findSolution();
            if(solution != null)
            {
                System.out.println("n="+n+" s="+s+" "+ solution.toString());
                lastSolution = solution;
                lastModel = model;
                lastTripletsSize = triplets.size();
                s++;
            }
        }
        while(solution != null);
        
        System.out.println("S("+ n +")=" + s);
        afficher(triplets, lastTripletsSize);
        Solver solver = lastModel.getSolver();
        solver.showStatistics();
        solver.findSolution();
        afficherCouleurs(n, lastSolution);
        System.out.println("S("+ n +")=" + s);
    }
    
    public static List<String>[] afficherCouleurs(int n, Solution solution)
    {
        String[] cols = solution.toString().replace("Solution: ", "").split(", ");
        List<String>[] values = new ArrayList[n];
        for(int j=0; j<n ; j++)
        {
            values[j] = new ArrayList();
        }
        for(String col:cols)
        {
            if(col.startsWith("c"))
            {
                String[] nb = col.split("=");
                values[Integer.parseInt(nb[2])-1].add(nb[1]);
            }
        }
        for(int j=0 ; j<n; j++)
        {
            if(values[j].size() > 0)
            {
                System.out.print("C"+(j+1) + ": ");
                for(int w=0 ; w<values[j].size() ; w++)
                {
                    System.out.print(values[j].get(w) + " ");
                }
                System.out.println();
            }
        }
        return values;
    }
    
    public static void calcTriplets(int n, int size, List<Triplet> ts)
    {
        int t = (int)Math.round((size + 0.0f)/2);
        for(int i=0 ; i<t ; i++)
        {
            //System.out.println("(" + (i+1) + ";" + (size-i) + ";" + (i+1 + size-i) + ")");
            ts.add(new Triplet((i+1), (size-i), (i+1 + size-i)));
        }
    }
    
    public static void afficher(List<Triplet> ts, int size)
    {
        for(int i=0 ; i<size ; i++)
        {
            System.out.println(ts.get(i).toString());
        }
    }
}
