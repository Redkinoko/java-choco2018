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
public class e1 {
    public static void run()
    {
        int n = 8;
        Model model = new Model(n + "-queens problem");
        IntVar[] vars = new IntVar[n];
        for(int q = 0; q < n; q++)
        {
            vars[q] = model.intVar("Q_"+q, 1, n);
        }
        for(int i = 0; i < n-1; i++)
        {
            for(int j = i + 1; j < n; j++)
            {
                model.arithm(vars[i], "!=",vars[j]).post();
                model.arithm(vars[i], "!=", vars[j], "-", j - i).post();
                model.arithm(vars[i], "!=", vars[j], "+", j - i).post();
            }
        }
        Solution solution = model.getSolver().findSolution();
        if(solution != null)
        {
            System.out.println(solution.toString());
        }
    }
}
