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
import org.chocosolver.solver.variables.IntVar;

/**
 *
 * @author Red
 */
public class e6 {

    public static void run(int n, int s)
    {
        Model model = new Model("nombres de Schur");
        /*
        * S(2)
        * (1,1,2)
        * (1,1,2)(1,2,3)[(2,1,3)]
        * (1,3,4)
        * (2,2,4)
        * [(3,1,4)]
        */
        //L'index correspond au nombre la couleur à la valeur dans la case
        IntVar[] colors = new IntVar[s*s];
        for(int i=0 ; i<s ; i++)
        {
            //0 = couleur non attribuée
            //1 = couleur 1
            //2 = couleur 2
            colors[i] = model.intVar("c"+(i+1), 0,n);
        }
        //Contraintes
        //indice(0) = chiffre 1, indice(1) = chiffre 2
        for(int i=0 ; i<s ; i++)
        {
            model.arithm(colors[i], "!=", 0).post();
        }

        for(int j=1 ; j<s ; j++)
        {
            for(int i=1 ; i<=j ; i++)
            {
                int a = i;
                int b = j+1-i;
                int c = a + b;
                System.out.println("("+ a + ";" + b + "=" +  c +")");
                model.ifThen(model.arithm(colors[a-1], "=", colors[b-1]), model.arithm(colors[c-1], "!=", colors[a-1]));
            }
        }
        //-------------
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
    
    public static void calc(int n, int size, List<Integer> varA, List<Integer> varB, List<Integer> varC, List<Integer> color)
    {
        int t = (int)Math.floor((size+0.0f)/2);
        for(int i=0 ; i<=t ; i++)
        {
            varA.add(i+1);
            varB.add(size-i);
            varC.add(i+1 + size-i);
            color.add(i);
            System.out.println("(" + varA.get(i) + ";" + varB.get(i) + ";" + varC.get(i) + ")");
        }
    }
}
