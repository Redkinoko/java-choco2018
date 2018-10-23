/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ppc_tp1_choco;

/**
 *
 * @author jh224110
 */
public class Tools {
    
    /**
     * 
     * @param min inclusif
     * @param max inclusif
     * @return nombre aléatoire entre min et max
     */
    public static int getRandomNumber(int min, int max)
    {
        return (int)(Math.random()*(max - min + 1) + min);
    }
    
    /**
     * 
     * @param array tableau à afficher
     * @param n taille nxn du tableau
     */
    public static void printA(int[][] array, int n)
    {
        for(int y=0 ; y<n ; y++)
        {
            for(int x=0 ; x<n ; x++)
            {
                System.out.print(array[x][y] + "|");
            }
            System.out.println();
        }
    }
    
    /**
     * 
     * @param array tableau à afficher
     * @param n taille nxn du tableau
     */
    public static void printAFilter0(int[][] array, int n)
    {
        for(int y=0 ; y<n ; y++)
        {
            for(int x=0 ; x<n ; x++)
            {
                if(array[x][y] == 0)
                {
                    System.out.print("_|");
                }
                else
                {
                    System.out.print(array[x][y] + "|");
                }
                
            }
            System.out.println();
        }
    }
}
