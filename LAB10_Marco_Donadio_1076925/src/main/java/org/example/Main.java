package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Trie trie  = new Trie();

        trie.insert("casa");
        trie.insert("casa");
        trie.insert("carro");
        trie.insert("carne");
        trie.insert("carnes");
        trie.insert("camino");
        trie.insert("carro");

        System.out.println(trie.autoComplete("carn"));
    }
}