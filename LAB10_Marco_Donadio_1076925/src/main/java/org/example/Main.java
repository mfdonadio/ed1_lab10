package org.example;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        //Main generado con IA para satsifacer y corroborar todas las pruebas solicitadas :D
        /*JSJS lo siento Dani, yo habia hecho uno pero me hice bolas, ya me gaste la neuronas en mejorar el trie lol.
            Igual, mi fin es demostrar que el trie esta en orden y que funciona como debe. Una disculpa por los inconvenientes
            -MD :)
         */
        Trie miTrie = new Trie();

        System.out.println("--- 1. Probando Inserción y Frecuencia (Parte 4) ---");
        // Insertamos palabras con diferentes frecuencias
        miTrie.insert("pan");
        miTrie.insert("papa");
        miTrie.insert("pan");    // 'pan' frecuencia 2, tiempo 0
        miTrie.insert("palo");   // 'palo' frecuencia 1, tiempo 2
        miTrie.insert("pato");   // 'pato' frecuencia 1, tiempo 3
        miTrie.insert("pan");    // 'pan' frecuencia 3

        System.out.println("¿Existe 'pan'?: " + miTrie.search("pan"));
        System.out.println("¿Existe 'paz'?: " + miTrie.search("paz"));

        System.out.println("\n--- 2. Probando Top-K con Prefijo 'pa' ---");
        // 'pan' (freq 3), luego 'papa', 'palo' y 'pato' empatan en freq 1.
        // El desempate debe elegir los más antiguos (menor timestamp).
        List<String> top3 = miTrie.autoComplete("pa", 3);
        System.out.println("Top 3 palabras que inician con 'pa': " + top3);

        System.out.println("\n--- 3. Probando Comodines '.' (Parte 5) ---");
        miTrie.insert("pelo");
        miTrie.insert("piso");

        System.out.println("¿Coincide 'p.lo'?: " + miTrie.searchWithWildcards("p.lo")); // true (palo, pelo)
        System.out.println("¿Coincide 'pi.o'?: " + miTrie.searchWithWildcards("pi.o")); // true (piso)
        System.out.println("¿Coincide '..no'?: " + miTrie.searchWithWildcards("..no")); // false

        System.out.println("\n--- 4. Probando Recorridos Ordenados (Parte 5) ---");
        System.out.println("Lista de todas las palabras (Alfabético):");
        System.out.println(miTrie.getWords());

        System.out.println("\nNodos por nivel (BFS):");
        System.out.println(miTrie.nodesByLevel());

        System.out.println("\n--- 5. Probando Prefijos (PreOrder) ---");
        System.out.println("¿Inicia con 'pal'?: " + miTrie.startsWith("pal")); // true
        System.out.println("¿Inicia con 'per'?: " + miTrie.startsWith("per")); // false

        System.out.println("\n--- 6. Probando PostOrder ---");
        System.out.println("PostOrder: " + miTrie.treeTraversal());
    }
}