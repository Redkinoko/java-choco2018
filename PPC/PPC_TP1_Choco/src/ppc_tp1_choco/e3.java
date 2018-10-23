/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ppc_tp1_choco;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.SetVar;

/**
 *
 * @author jh224110
 */
public class e3 {
    
    /***
     * 
     * @param n taille du plateau nxn
     * @param k nombre de reines
     */
    public static void run(int n, int k)
    {
        Model model     = new Model(n + "-queens nxn problem");
        IntVar[][] vars = new IntVar[n][n];
        int[][] debug   = new int[n][n];
        //Déclarations des reines
        for(int j = 0 ; j < n ; j++)
        {
            for(int i = 0 ; i < n ; i++)
            {
                vars[i][j]  = model.intVar("Q_("+i+";"+j+")", 0,1);
                debug[i][j] = 0; 
            }
        }
        //Placement des reines
        for(int i = 0 ; i < k ; i++)
        {
            int j = Tools.getRandomNumber(0, n-1);
            System.out.print("(" + i+";"+j+")");
            model.arithm(vars[i][j], "=", 1).post();
            debug[i][j] = 1;
        }
        System.out.println();
        //------------------------

        int sumLines = 0;
        for(int j = 0 ; j < n ; j++)
        {
            sumLines = 0;
            for(int i = 0 ; i < n ; i++)
            {
               sumLines += vars[j][i].getValue();
               //debug[j][i] = i;
            }
            //System.out.println("l:" + sumLines);
        }

        int sumCols = 0;
        for(int j = 0 ; j < n ; j++)
        {
            sumCols = 0;
            for(int i = 0 ; i < n ; i++)
            {
               sumCols += vars[i][j].getValue();
               //debug[i][j] = i;
            }
            //System.out.println("c:" + sumCols);
        }
        //--------------------------
        //DIAG
        int dd1 = 0;
        int dd2 = 0;
        for(int i = 0 ; i < n ; i++)
        {
            int max = n-i;
            dd1 = 0;
            for(int w = 0 ; w < max ; w++)
            {
                dd1 += debug[i+w][0+w];
            }
            dd2 = 0;
            for(int w = 0 ; w < max ; w++)
            {
                dd2 += debug[0+w][i+w];
            }
            //System.out.println(d1 + "_" + d2);
        }
        //DIAG2
        //0,(n-1)
        int dm1 = 0;
        int dm2 = 0;
        for(int i = 0 ; i < n ; i++)
        {
            int max = n-i;
            dm1 = 0;
            for(int w = 1 ; w < max ; w++)
            {
                debug[i][(n-w)] = 8;
            }
            /*
            dm2 = 0;
            for(int w = 0 ; w < max ; w++)
            {
                dm2 += debug[0+w][i+w];
            }*/
            System.out.println(dm1 + "_" + dm2);
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
        Tools.printAFilter0(debug, n);
    }
}
