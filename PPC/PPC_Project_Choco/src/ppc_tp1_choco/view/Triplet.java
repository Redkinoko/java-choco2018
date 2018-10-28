/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ppc_tp1_choco.view;

/**
 *
 * @author Red
 */
public class Triplet {
    public int a;
    public int b;
    public int c;
    
    public Triplet()
    {
        
    }
    
    public Triplet(int a, int b, int c)
    {
        this.a = a;
        this.b = b;
        this.c = c;
    }
    
    @Override
    public String toString()
    {
        return "("+ a +";"+ b +";"+ c +")";
    }
}
