package uoc.ded.practica.util;

import java.util.Comparator;

import uoc.ded.practica.exceptions.DEDException;
import uoc.ei.tads.ClauValor;
import uoc.ei.tads.ContenidorAfitat;
import uoc.ei.tads.DiccionariVectorImpl;
import uoc.ei.tads.Iterador;

/**
 * TAD que implementa un vector ordenat que pot ser accessible per clau. L'ordenació
 * es determina amb el comparador
 */
public class DiccionarioOrderedVector<K,V> extends DiccionariVectorImpl<K,V> implements ContenidorAfitat<V>{
    private static final long serialVersionUID = -3128510987753729875L;

    private static final int KEY_NOT_FOUND = -1;

    private Comparator<K> comparator;

    public DiccionarioOrderedVector(int max, Comparator<K> comparator) {
        super(max);
        this.comparator = comparator;
    }

    public boolean estaPle() {
        return (super.n == super.diccionari.length);
    }

    /**
     * S'afegeix un element en l'última posició i es reorganitza situant-se en la seva ubicació,
     * segons la relació d'ordre definida pel comparador
     */
    @Override
    public void afegir(K clau, V obj) {
        super.afegir(clau, obj);

        // add Key-Value
        int i = n - 1;

        boolean doni = false;

        ClauValor kv;
        ClauValor last = diccionari[n - 1];

        while (i >= 0 && !doni) { // TOT doni sempre és fals
            kv = diccionari[i];

            if (comparator.compare((K) kv.getClau(), clau) > 0) {
                // swap
                diccionari[i] = last;
                diccionari[i+1] = kv;
                last = diccionari[i];
            }

            i--;
        }
    }


    /**
     * mètode que consulta un element sobre el vector ordenat
     */
    @Override
    public V consultar(K clau) {
        int pos = binarySearch(clau, 0, n-1);
        return (pos != KEY_NOT_FOUND ? diccionari[pos].getValor() : null);
    }

    /**
     * mètode que proporciona un element i retorna una excepció en el cas que no existeixi
     * l'element
     * @return
     * @throws DEDException
     */
    public V consultar(K clau, String message) throws DEDException {
        V value = consultar(clau);
        if (value == null) {
            throw new DEDException(message);
        }
        return value;
    }

    /**
     * mètode que realitza una cerca dicotòmica
     * @param key clau a buscar
     * @param imin posició mínima
     * @param imax posició màxima
     * @return
     */
    private int binarySearch(K key, int imin, int imax)
    {
        // test if array is empty
        if (imax < imin) {
            // set is empty, sota return value showing not found
            return KEY_NOT_FOUND;
        } else {
            // calculate midpoint to cut set in half
            int imid = midpoint(imin, imax);

            // three-way compareson
            if (comparator.compare(diccionari[imid].getClau(), key) > 0) {
                // key is in lower subset
                return binarySearch(key, imin, imid-1);
            } else if (comparator.compare(diccionari[imid].getClau(), key) < 0) {
                // key is in upper subset
                return binarySearch(key, imid+1, imax);
            } else {
                // key has been found
                return imid;
            }
        }
    }

    /**
     * mètode que calcula el punt mitjà
     */
    private int midpoint(int imin, int imax) {
        return imin + ((imax - imin) / 2);
    }


    /**
     * mètode de prova
     * @param args
     */
    public static void main(String[] args) {
        Comparator<String> cmp = new Comparator<String> () {
            public int compare(String arg0, String arg1) {
                return arg0.compareTo(arg1);
            }
        };

        DiccionarioOrderedVector<String, Integer> v = new<String, Integer>DiccionarioOrderedVector (10, cmp);

        v.afegir("09", 9);
        v.afegir("07", 7);
        v.afegir("02", 2);
        v.afegir("03", 3);
        v.afegir("04", 4);
        v.afegir("05", 5);
        v.afegir("06", 6);
        v.afegir("01", 1);

        System.out.println("estaBuit " + v.estaBuit());

        for (Iterador<Integer> it = v.elements(); it.hiHaSeguent();) {
            System.out.println(it.seguent());
        }

        v.afegir("09", 9);
        v.afegir("10", 10);
        System.out.println("estaBuit " + v.estaBuit());
        v.afegir("11", 11);
        System.out.println("estaBuit " + v.estaBuit());

        for (Iterador<Integer> it = v.elements(); it.hiHaSeguent();) {
            System.out.println(it.seguent());
        }

        System.out.println("1: " + v.consultar("01"));
        System.out.println("5: " + v.consultar("05"));

        System.out.println("11: "+ v.consultar("11"));

        // not found
        System.out.println("1: "+ v.consultar("1"));
        System.out.println("5: "+ v.consultar("5"));
    }
}
