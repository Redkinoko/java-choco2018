/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ppc_tp1_choco;

/**
 *
 * @author Red
 */
public class Debug {
    
    public static void mont()
    {
        int n = 8;
        //MONTANTE
        String[][] test = new String[n][n];//débug
        int j = 1;
        while(j < (n+n))
        {
            if(j <= n)//Partie inférieur gauche et milieu
            {
                for(int i=0 ; i < j ; i++)
                {
                    test[i][n-j+i] = "["+(char)('A'-1+j)+"]";//débug
                    System.out.println("(" + i +";" + (n-j+i) + ")");
                }
            }
            else//Partie supérieur droite
            {
                for(int i=0 ; i < (n-j%n) ; i++)
                {
                    test[((n-(n-j%n)+i))][i] = "["+(char)('A'-1+j)+"]";//débug
                    System.out.println("_(" + ((n-(n-j%n)+i)) +";" + i + ")");
                }
            }
            System.out.println();
            j++;
        }
        
        //Affichage pour debug
        for(j=0 ;j<n ; j++)
        {
            for(int i=0 ; i<n ; i++)
            {
                System.out.print(test[i][j]);
            }
            System.out.println();
        }
    }
    
    public static void desc()
    {
        int n = 8;
        //DESC
        String[][] test = new String[n][n];//débug
        int j = 1;
        while(j < (n+n))
        {
            if(j <= n)
            {
                for(int i=0 ; i<j ; i++)
                {
                    System.out.print("(" + (i) + ";" + (j-1-i) + ")");
                    test[i][(j-1-i)] = "["+(char)('A'-1+j)+"]";//débug
                }
                System.out.println();
            }
            else
            {
                for(int i=0 ; i<(n-j%n) ; i++)
                {
                    System.out.print("(" + (j%n+i) + ";" + (n-i-1) + ")");
                    test[(j%n+i)][(n-i-1)] = "["+(char)('A'-1+j)+"]";//débug
                }
                System.out.println();
            }
            j++;
        }
        
        //Affichage pour debug
        for(j=0 ;j<n ; j++)
        {
            for(int i=0 ; i<n ; i++)
            {
                System.out.print(test[i][j]);
            }
            System.out.println();
        }
    }
}
