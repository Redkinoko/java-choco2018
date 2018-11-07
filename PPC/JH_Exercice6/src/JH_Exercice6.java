/*
* Recherche du nombre de Schur
* Le solveur doit trouver lui même le nombre de Schur, noté S(n)
* On connait les bornes de S(n) minimale et maximale
* min(S(n)) = (3^n-1)/2
* max(S(n)) = (3*n!)-1
* On peut dont le représenté par un IntVar, il sera appelé "borne"
* ENTIERS/COULEURS
* - On représentera la valeur des nombres et couleurs par un tableau d'entiers.
* Il sera appelé "colors" et stockera des IntVar allant de 0, jusqu'à n
* L'index sera le nombre et la valeur sera la couleur
* On sait que les nombres vont de 1 jusqu'à max(S(n)) de ce fait
* la valeur du tableau à l'index 0 ne sera jamais utilisé.
* - On utilisera une variable IntVar appelé "used" qui contiendra la valeur
* maximum contenu dans le tableau "colors" soit le nombre de couleurs utilisé
* pendant la résolution.
* TRIPLETS
* - On génère les triplets (a,b,c) où c varie de 2 jusqu'à max(S(n))
* Il ne faut pas générer les triplets mirroir pour éviter les contraintes doublons
* CONTRAINTES
* - On fixera le chiffre 1 à la couleur 1, pour éviter les n-1 couleurs.
* - On fixera le chiffre 1 à la couleur 0, qui signifie non utilisé.
* (requis pour la contrainte max utilisé par la valeur 'used')
* - On utilisera la contrainte max sur la variable used avec le tableau colors
* - On imposera la contrainte <<si a=b alors colors[c]!=colors[a]>> à tous les triplets
* Cependant, il faudra ajouter la contrainte suivante :
* << Si c est supérieur ou égale à la borne courante testé >>
* Car tous les triplets supérieurs à la borne courante n'ont pas besoin d'être évalué
* RESOLUTION
* - On demande au solveur de maximiser la valeur de la borne
*/
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.IntVar;

/**
 * Une classe triplet contient 3 entiers (a,b,c)
 * @author Red
 */
class Triplet
{
    public int a;
    public int b;
    public int c;

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
/**
 *
 * @author Red
 */
public class JH_Exercice6
{
    /**
     * Permet l'exécution du programme et Mesure du temps d'execution
     * @param args 
     */
    public static void main(String[] args) 
    {
        /*
        * Appel de la résolution pour S(n) où n est le paramètre
        * Triplets (a,b,c)
        * S(n) = max(c)
        * S(1) = 1,    temps = hour:00 - min:00 - sec:00 - millis:0262
        * S(2) = 4,    temps = hour:00 - min:00 - sec:00 - millis:0263
        * S(3) = 13,   temps = hour:00 - min:00 - sec:00 - millis:0424
        * S(4) = 44,   temps = hour:00 - min:00 - sec:44 - millis:0577
        * S(5) = 160?, temps = N/C
        */
        int n = 4;
        
        //Lancement du timer
        long startTime = System.currentTimeMillis();
        
        //Résolution
        run(n);
        
        //Calcul du temps de résolution
        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime);
        System.out.println();
        System.out.println("Temps écoulé : "+millisToShortDHMS( duration ) );
    }
    
    /**
     * Permet de calculer le nombre de Schur
     * S(1) = 1
     * S(2) = 4
     * S(3) = 13
     * S(4) = 44
     * S(5) = 160?
     * S(6) = N/C
     * @param n le nombre de couleur
     */
    public static void run(int n)
    {
        /*
        * DECLARATIONS
        * Création de la borne minimale et maximale
        */
        int min = (int)((Math.pow(3, n)-1)/2);
        int max = (int)(3*fact(n)-1);
        
        //Création du model
        Model model   = new Model("Borne inférieur de Schur S("+n+"){"+min+";"+max+"}");
        Solver solver = model.getSolver();
        
        /*
        * 'borne' est la variable contenant le nombre Schur compris dans {min,max}
        */
        IntVar borne = model.intVar("borne", min,max);
        
        /*
        * 'used' est le nombre de couleurs utilisé
        */
        IntVar used  = model.intVar("used", 1, n);
        
        /*
        * 'colors' contient les nombres compris dans {0,max}
        * Les index représente la valeur du chiffre
        * La valeur représente la couleur
        * Valeur = 0 -> la variable n'est pas utilisé pour la valeur courante de borne
        * Valeur = 1..n -> la couleur n°1..n
        */
        IntVar[] colors = new IntVar[max+1];
        for(int i=0 ; i<colors.length ; i++)
        {
            //=0 -> variable inutilisé
            //>0 -> couleur
            colors[i] = model.intVar("c="+i, 0,n);
        }
        
        /*
        * CREATIONS DES TRIPLETS
        * On génère les triplets de la forme (a,b,c) où c=a+b
        * On ne gènère pas les triplets et leur miroir ex:(1,3,4) et (3,1,4)
        * Il existe 1 forme pour les nombres impairs : (a,b,c)
        * Il existe 2 formes pour les nombres pairs  : (a,a,c) et (a,b,c)
        */
        List<Triplet> triplets   = new ArrayList();
        for(int i=2 ; i<=max ; i++)
        {
            addTriplets(triplets, i);
        }
        
        /*
        * CONTRAINTES
        * pour tout triplets (a,b,c)
        * Si la variable c est active dans la borne courante et que a=b
        * alors la couleur de c est différente de celle de a
        */
        for(Triplet t:triplets)
        {
            Constraint gte   = model.arithm(borne, ">=", t.c);
            Constraint eq    = model.arithm(colors[t.a], "=", colors[t.b]);
            Constraint and   = model.and(gte, eq);
            Constraint cDifa = model.arithm(colors[t.c], "!=", colors[t.a]);
            model.ifThen(and, cDifa);
        }
        
        //Le chiffre 0 n'est pas utilisable
        model.arithm(colors[0], "=", 0).post();
        
        //Le chiffre 1 a obligatoirement la couleur 1
        model.arithm(colors[1], "=", 1).post();
        
        /*
        * CHOIX DES VARIABLES UTILES
        * Toutes les variables plus grandes que la borne courante
        * ne sont pas utilisable (couleur = 0)
        */
        for(int i=1 ; i<=max ; i++)
        {
            Constraint gte = model.arithm(borne, ">=", i);
            Constraint gt0 = model.arithm(colors[i], ">", 0);
            Constraint eq0 = model.arithm(colors[i], "=", 0);
            model.ifThenElse(gte, gt0, eq0);
        }
        
        /*
        * COULEUR MAXIMALE
        * On est sûr et certain qu'il est possible de colorier avec leur n couleur
        * associé tous les nombres qui sont inférieur à leur minimum de Schur
        */
        for(int j=(n-1); j>=1 ; j--)
        {
            int prev = (int)((Math.pow(3, j)-1)/2);
            for(int i=1 ; i<prev ; i++)
            {
                model.arithm(colors[i], "<=", j).post();
            }
        }
        
        //Permet de récupérer le nombre de couleurs utilisés
        model.max(used, colors).post();
        
        /*
        * RESOLUTION
        * On cherche une solution qui maximise la valeur 'borne' -> nombre de Schur
        */
        Solution solution = solver.findOptimalSolution(borne, true);
        solver.printStatistics();
        if(solution != null)
        {
            System.out.println(solution.toString());
            printColors(getColors(n, solution));
        }
    }
    
    /**
     * Permet de récupérer un tableau contenant les couleurs associées à la solution
     * @param n le nombre maximal de couleurs
     * @param solution le résulat de la résolution
     * @return 
     */
    public static List<Integer>[] getColors(int n, Solution solution)
    {
        List<Integer>[] values = new ArrayList[n+2];
        String[] cols = solution.toString().replace("Solution: ", "").split(", ");
        for(int j=0; j<values.length ; j++)
        {
            values[j] = new ArrayList();
        }
        for(String col:cols)
        {
            if(col.startsWith("c"))
            {
                String[] nb = col.split("=");
                int v  = Integer.parseInt(nb[2]);
                int no = Integer.parseInt(nb[1]);
                values[v].add(no);
            }
        }
        return values;
    }
    
    /**
     * Permet d'afficher les variables groupé par leurs couleurs 
     * @param values Le tableau généré par getColors
     */
    public static void printColors(List<Integer>[] values)
    {
        for(int j=0 ; j<values.length; j++)
        {
            if(values[j].size() > 0)
            {
                if(j==0)
                {
                    System.out.print("Non_utilisé: ");
                }
                else
                {
                    System.out.print("Couleur_n°"+j+": ");
                }
                for(int w=0 ; w<values[j].size() ; w++)
                {
                    int i = values[j].get(w);
                    if(i < 100)
                    {
                        System.out.print(" ");
                    }
                    if(i < 10)
                    {
                        System.out.print(" ");
                    }
                    System.out.print(i + " ");
                }
                System.out.println();
            }
        }
    }
    
    /**
     * Permet de récupérer les triplets (a,b,c) tel que a+b=c
     * @param triplets le tableau stockant les triplets
     * @param c la somme a atteindre
     */
    public static void addTriplets(List<Triplet> triplets, int c)
    {
        int t = (int)Math.round((c-1 + 0.0f)/2);
        for(int i=0 ; i<t ; i++)
        {
            int a = (i+1);
            int b = (c-1-i);
            triplets.add(new Triplet(a, b, c));
        }
    }
    
    /**
     * Permet d'afficher les triplets
     * @param ts Le tableau stockant les triplets
     */
    public static void afficher(List<Triplet> ts)
    {
        for(Triplet t:ts)
        {
            System.out.println(t.toString());
        }
    }
    
    /**
     * Permet de calculer un nombre factoriel
     * Utiliser dans la génération de la borne maximal du nombre de Schur
     * @param n le nombre n! a atteindre
     * @return la fatoriel d'un nombre donné
     */
    public static long fact(int n) {
        long result = 1;

        for (int factor = 2; factor <= n; factor++) {
            result *= factor;
        }

        return result;
    }
    
    /**
     * Permet de convertir le résultat donné par System.currentTimeMillis();
     * Dans un language humain compréhensible
     * @param duration la valeur a convertir
     * @return (d:h:m:s:ms) ou si d=0 (h:m:s:ms)
     */
    public static String millisToShortDHMS(long duration)
    {
        String res = "";    // java.util.concurrent.TimeUnit;
        long days       = TimeUnit.MILLISECONDS.toDays(duration);
        long hours      = TimeUnit.MILLISECONDS.toHours(duration) -
                          TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(duration));
        long minutes    = TimeUnit.MILLISECONDS.toMinutes(duration) -
                          TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration));
        long seconds    = TimeUnit.MILLISECONDS.toSeconds(duration) -
                          TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration));
        long millis     = TimeUnit.MILLISECONDS.toMillis(duration) - 
                          TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(duration));

        if (days == 0)      res = String.format("hour:%02d - min:%02d - sec:%02d - msec:%04d", hours, minutes, seconds, millis);
        else                res = String.format("day:%dd - hour:%02d - min:%02d - sec:%02d - msec:%04d", days, hours, minutes, seconds, millis);
        return res;
    }
}