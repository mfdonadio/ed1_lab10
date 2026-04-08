package org.example;

import java.util.*;

public class Trie {

    private Node root;
    private int insertionCounter = 0;

    public Trie(){
        root = new Node();
    }

    public void insert(String word){
        Node current = root;

        for(char c : word.toLowerCase().toCharArray()){
            current.children.putIfAbsent(c, new Node());
            current = current.children.get(c);
        }

        //Si la marcamos por primera vez como 'ultima', aumentamos el coontador de insercion y lo igualamos al timestamp
        if(!current.isLast){
            current.isLast = true;
            current.timestamp = insertionCounter++;
        }
        current.frequency++; //Auemantamos la frecuencia...SIEMPRE
    }

    public boolean search(String word){
        Node current = root;

        for(char c : word.toLowerCase().toCharArray()){
            current = current.children.get(c);
            if(current == null) return false;
        }
        return current.isLast;
    }

    public boolean startsWith(String prefix){
        Node current = root;

        for(char c : prefix.toLowerCase().toCharArray()){
            current = current.children.get(c);
            if(current == null) return false;
        }
        return true;
    }

    public List<String> autoComplete(String prefix){
        Node current = root;

        for(char c : prefix.toLowerCase().toCharArray()){
            current = current.children.get(c);
            if(current == null) return List.of();
        }

        List<String> result = new ArrayList<>();
        autoComplete(current, result, new StringBuilder(prefix));

        return result;
    }

    //Adaptado de StackOverflow
    public List<String> autoComplete(String prefix, int k) {
        Node current = root;

        //Vamos al nodo del prefijo
        for (char c : prefix.toLowerCase().toCharArray()) {
            current = current.children.get(c);
            if(current == null) return List.of();
        }

        //Recolectamos todas las palabras posibles desde este nodo
        List<WordStats> candidates = new ArrayList<>();
        collectStats(current, new StringBuilder(prefix), candidates);
        //Ordenamos segun lo solicitado
        candidates.sort((a, b) -> {
            if (b.frequency != a.frequency) {
                return b.frequency - a.frequency; //El de mayor frecuencia primero
            }
            return a.time - b.time; //Si hay empate, el de menor timestamp (osea el mas viejo) de primero
        });

        List<String> result = new ArrayList<>();
        for (int i = 0; i < Math.min(k, candidates.size()); i++) {
            result.add(candidates.get(i).word);
        }
        return result;
    }

    public List<String> getWords(){
        List<String> result = new ArrayList<>();
        preOrder(this.root, "", result);
        return result;
    }

    public List<String> treeTraversal(){
        List<String> result = new ArrayList<>();
        postOrder(this.root, "", result);
        return result;
    }

    public List<String> nodesByLevel(){
        List<String> result = new ArrayList<>();
        if(this.root == null) return result;

        //Hacemos una cola para almacenar los nodos que debemos visitar
        Queue<NodeWrapper> queue = new LinkedList<>();

        //Primero la raiz
        queue.add(new NodeWrapper(this.root, ""));

        while(!queue.isEmpty()){
            //Sacamos el primero nodo
            NodeWrapper current = queue.poll();

            //Agregamos el nodo a 'result'
            result.add(current.prefix);

            //Obtenemos las llaves y ordenamos
            List<Character> letters = new ArrayList<>(current.node.children.keySet());
            Collections.sort(letters);

            //Agregamos a los hijos a la cola para procesarlos despues
            for(char letter : letters){
                queue.add(new NodeWrapper(current.node.children.get(letter), current.prefix + letter));
            }
        }
        return result;
    }

    //Adaptado de un video
    public boolean searchWithWildcards(String word){
        return searchRecursive(root, word.toLowerCase(), 0); //Indice empieza en 0
    }

    private void preOrder(Node root, String prefix,List<String> result){
        if(root == null) return;

        //Si el nodo 'raiz' es el fin de una palabra, lo guardamos
        if(root.isLast){
            result.add(prefix);
        }

        //Creamos la lista de caracteres --- obtenemos las llaves
        List<Character> letters = new ArrayList<>(root.children.keySet());
        Collections.sort(letters);

        //Exploramos los hijos
        for(char letter : letters){
            preOrder(root.children.get(letter), prefix + letter, result);
        }
    }

    private void postOrder(Node root, String prefix, List<String> result){
        if(root == null) return;

        //Primero visitamos los hijos
        //Creamos la lista de caracteres --- obtenemos las llaves
        List<Character> letters = new ArrayList<>(root.children.keySet());
        Collections.sort(letters);

        //Exploramos los hijos
        for(char letter : letters){
            postOrder(root.children.get(letter), prefix + letter, result);
        }

        //De ultimo procesamos la raiz
        result.add(prefix);
    }

    private void autoComplete(Node root, List<String> result, StringBuilder sb){
        if(root.isLast){
            result.add(sb.toString());
        }

        //Creamos la lista de caracteres --- obtenemos las llaves
        List<Character> letters = new ArrayList<>(root.children.keySet());
        Collections.sort(letters);

        //Exploramos los hijos
        for(char letter : letters){
            sb.append(letter);
            autoComplete(root.children.get(letter), result, sb);
            sb.setLength(sb.length() - 1);
        }
    }

    //Para recolectar las estadisticas de los nodos ---> igual adaptado de StackOverflow
    private void collectStats(Node node, StringBuilder sb, List<WordStats> candidates){
        if(node.isLast){
            candidates.add(new WordStats(sb.toString(), node.frequency,  node.timestamp));
        }

        //Creamos la lista de caracteres --- obtenemos las llaves
        List<Character> letters = new ArrayList<>(node.children.keySet());
        Collections.sort(letters); //Ordena el map

        for (char letter : letters) {
            sb.append(letter);
            collectStats(node.children.get(letter), sb, candidates);
            sb.setLength(sb.length() - 1);
        }
    }

    //Apatado de un video
    private boolean searchRecursive(Node current, String word, int index){
        //Si llegamos al final de la palapra, verificamos si es valida
        if(index == word.length()) return current.isLast;

        char c = word.charAt(index);

        if(c == '.'){
            //Si es un comodin, intentamos con todos los hijos posibles --> 26 letras (a-z)
            for(Node child : current.children.values()){
                if(searchRecursive(child, word, index+1))
                    return true; //Si alguna rama encuentra a la palabra, retornamos true
            }
            return false; //Si es que ninguna rama coincide
        } else{
            Node nextNode = current.children.get(c); //buscamos el caracter especifico en el HashMap
            if(nextNode == null){
                return false; //El carácter  no existe en este nivel
            }
            return searchRecursive(nextNode, word, index+1);
        }
    }


    private static class Node{
        //Para mejorar la eficiencia, mejor que un arreglo, es un HashMap
        public Map<Character, Node> children;
        public boolean isLast;
        public int frequency;
        public int timestamp; //El cirterio de desmpate en caso de que la frecuencia sea la misma

        public Node(){
            children = new HashMap<>();
            isLast = false;
            frequency = 0;
            timestamp = Integer.MAX_VALUE;
        }
    }

    //Este 'wrapper' nos ayuda a manetener el nodo junto con su palabra acumulada ---> lo vi en un video jeje
    private static class NodeWrapper {
        Node node;
        String prefix;

        //Constructor
        NodeWrapper(Node node, String prefix) {
            this.node = node;
            this.prefix = prefix;
        }
    }

    //Creamos un record para guardar las frequencuias de las palabras
    private static class WordStats{
        String word;
        int frequency;
        int time;

        WordStats(String word, int frequency, int time){
            this.word = word;
            this.frequency = frequency;
            this.time = time;
        }
    }
}
