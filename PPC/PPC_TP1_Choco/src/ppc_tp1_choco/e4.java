/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ppc_tp1_choco;

import org.chocosolver.solver.Model;
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
    [M]000241242312100
    [D]001212342142000
    [-]22264222
    [|]11633611
    */
    
    public static int[] inst_ligne()         { return new int[]{ 2,2,2,6,4,2,2,2 }; }
    public static int[] inst_colonne()       { return new int[]{ 1,1,6,3,3,6,1,1 }; }
    public static int[] inst_montante()      { return new int[]{ 0,0,0,2,4,1,2,4,3,2,1,2,1,0,0 }; }
    public static int[] inst_descendante()   { return new int[]{ 0,0,1,2,1,2,3,4,2,1,4,2,0,0,0 }; }
    //Gauche vers droite
    /*
    0,0,0,0,0,0,0,0
    0,0,0,0,0,0,0,0
    0,0,0,0,0,0,0,0
    0,0,0,0,0,0,0,0
    0,0,0,0,0,0,0,0
    0,0,0,0,0,0,0,0
    0,0,0,0,0,0,0,0
    0,0,0,0,0,0,0,0
    */
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
                vars[i][j]  = model.intVar("P_("+i+";"+j+")", 0,1);
            }
        }
        //Contraintes
        for(int i=0; i<n ; i++)
        {
            model.sum(vars[i], "=", ligne[i]);
        }
        for(int i=0; i<n ; i++)
        {
            model.sum(ArrayUtils.getColumn(vars, i), "=", colonne[i]);
        }

        IntVar[] descendantes = new IntVar[n];
        //DESCENDANTES
        for(int i = 0 ; i < n ; i++)
        {
            //reset
            reset(descendantes);
            int max = n-i;
            //descendante haut droite
            for(int w = 0 ; w < max ; w++)
            {
                //System.out.println((i+w) + ";" + (0+w));
                descendantes[w] = vars[i+w][0+w];
            }
            model.sum(descendantes, "=", sum);
            reset(descendantes);
            //descendante haut gauche
            if(i > 0)
            {
                for(int w = 0 ; w < max ; w++)
                {
                    //System.out.println((0+w) + ";" + (i+w));
                    descendantes[w] = vars[0+w][i+w];
                }
            }
            //System.out.println();
        }
        
        /*
        for(int j=0 ;j<n ; j++)
        {
            for(int i=0 ; i<n ; i++)
            {
                System.out.print(d[i][j]);
            }
            System.out.println();
        }
        */
    }
    
    public static void reset(IntVar[] a)
    {
        for(int i=0 ; i<a.length ; i++)
        {
            a[i] = null;
        }
    }
}
