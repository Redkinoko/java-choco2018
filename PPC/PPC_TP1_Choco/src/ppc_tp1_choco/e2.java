/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ppc_tp1_choco;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.IntVar;

/**
 *
 * @author jh224110
 */
public class e2 {
    
    /***
     * 
     * @param n taille du plateau nxn
     * @param k nombre de reines
     */
    public static void run(int n, int k)
    {
        Model model = new Model(n + "-queens nxn problem");
        IntVar[] vars = new IntVar[n];
        //Déclarations des reines
        for(int q = 0; q < n; q++)
        {
            vars[q] = model.intVar("Q_"+q, 1, n);
        }
        //On place aleatoirement les k premières reines
        int[][] debug = new int[n][n];
        for(int y=0 ; y<n ; y++)
            for(int x=0 ; x<n ; x++)
            {
                debug[x][y] = 0;
            }
        //----------------------
        for(int i = 0; i < k; i++)
        {
            int r = Tools.getRandomNumber(1, n);
            model.arithm(vars[i], "=", r).post();
            //---------
            debug[i][r-1] = 1;
            System.out.println("Q_"+i + " = " + r);
        }
        for(int y=0 ; y<n ; y++)
        {
            for(int x=0 ; x<n ; x++)
            {
                System.out.print(debug[x][y] + "|");
            }
            System.out.println();
        }
        //------------------------
        for(int i = 0; i < n-1; i++)
        {
            for(int j = i + 1; j < n; j++)
            {
                model.arithm(vars[i], "!=", vars[j]).post();
                model.arithm(vars[i], "!=", vars[j], "-", j - i).post();
                model.arithm(vars[i], "!=", vars[j], "+", j - i).post();
            }
        }
        model.getSolver().showStatistics();
        Solution solution = model.getSolver().findSolution();

        if(solution != null)
        {
            System.out.println(solution.toString());
        }
        else
        {
            System.out.println("Pas de solutions");
        }
    }
}
