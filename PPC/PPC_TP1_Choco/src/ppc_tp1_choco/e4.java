/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ppc_tp1_choco;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.tools.ArrayUtils;

/**
 *
 * @author jh224110
 */
public class e4 {
    /*
    On appelle tomographie discrète l’art de reconstituer une image à partir de projection. 
    Dans le cas qui nous intéresse, l’image est constituée de cases noires et de cases blanche sur une grille de N par N cases. 
    Mais on ne connait pas cette image, on connait juste le nombre de cases noires sur chaque ligne, 
    chaque colonne et chaque diagonale montante et descendante. 
    Ces données sont appelées la signature de l’image. Le but de la mission est de modéliser ce problème de manière à retrouver une
    image compatible avec une signature donnée, ou de déterminer, le cas échéant, qu’une telle image
    n’existe pas. Vous devrez tester votre solution avec des signatures construite à la main ou produites
    à partir d’images aléatoires.
    */
    //INSTANCE DE TEST TOMO
    /*
    [M]00024 12423 12100
    [D]00121 23421 42000
    [-]22264 222
    [|]11633 611
    *
    __0,1,2,3,4,5,6,7
    __:_:_:_:_:_:_:_:
    0:7,8,9,0,1,2,3,4
    1:6,7,8,9,0,1,2,3
    2:5,6,7,8,9,0,1,2
    3:4,5,6,7,8,9,0,1
    4:3,4,5,6,7,8,9,0
    5:2,3,4,5,6,7,8,9
    6:1,2,3,4,5,6,7,8
    7:0,1,2,3,4,5,6,7
    */
    
    public static int[] ligne()         { return new int[]{ 2,2,2,6,4, 2,2,2 }; }
    public static int[] colonne()       { return new int[]{ 1,1,6,3,3, 6,1,1 }; }
    public static int[] montante()      { return new int[]{ 0,0,0,2,4, 1,2,4,2,3, 1,2,1,0,0 }; }
    public static int[] descendante()   { return new int[]{ 0,0,1,2,1, 2,3,4,2,1, 4,2,0,0,0 }; }
    //Gauche vers droite
    
    public static void test(int i)
    {
        switch(i)
        {
            case 1 :
                /*
                1,0,1
                0,0,1
                1,0,0
                */
                run(new int[]{2,0,2}, new int[]{2,1,1}, new int[]{1,0,1,1,1}, new int[]{1,0,2,1,0});
                break;
            default :
                /*
                1,1,0,1
                0,1,0,1
                0,1,0,0
                1,0,0,1
                */
                run(new int[]{2,3,0,3}, new int[]{3,2,1,2}, new int[]{1,0,1,3,1,1,1}, new int[]{1,1,1,3,1,0,1});
        }
    }
    
    public static void run(int[] ligne, int[] colonne, int[] montante, int[] descendante)
    {
        int n = ligne.length;
        Model model         = new Model("Tomologie n = " + n + "");
        IntVar[][] vars     = new IntVar[n][n];
        //Déclarations des ranges
        for(int j = 0 ; j < n ; j++)
        {
            for(int i = 0 ; i < n ; i++)
            {
                vars[i][j]  = model.intVar(""+i+"="+j+"", 0,1);
            }
        }
        //Contraintes
        for(int i=0; i<n ; i++)
        {
            model.sum(vars[i], "=", ligne[i]).post();
        }
        for(int i=0; i<n ; i++)
        {
            model.sum(ArrayUtils.getColumn(vars, i), "=", colonne[i]).post();
        }

        IntVar[] tmp;
        
        //MONTANTE
        int j = 1;
        while(j < (n+n))
        {
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
        
        //DESCENDENTE
        j = 1;
        while(j < (n+n))
        {
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
                if(view[x][y] == 0) { System.out.print("_|"); }
                else { System.out.print(view[x][y] + "|"); }
            }
            System.out.println();
        }
    }
}
