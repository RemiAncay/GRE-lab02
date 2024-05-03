package gre.lab2.groupE;

import gre.lab2.graph.BFYResult;
import gre.lab2.graph.WeightedDigraph;
import gre.lab2.graph.WeightedDigraphReader;

import java.io.IOException;

public final class Main {
  public static void main(String[] args) throws IOException {
    // TODO
    //  - Renommage du package ;
    //  - Écrire le code dans le package de votre groupe et UNIQUEMENT celui-ci ;
    //  - Documentation soignée comprenant :
    //    - la javadoc, avec auteurs et description des implémentations ;
    //    - des commentaires sur les différentes parties de vos algorithmes.

    BellmanFordYensAlgorithm bfy = new BellmanFordYensAlgorithm();
    WeightedDigraph wd = WeightedDigraphReader.fromFile("data/reseau6.txt");

    BFYResult result = bfy.compute(wd, 0);

    System.out.println(result);
  }
}
