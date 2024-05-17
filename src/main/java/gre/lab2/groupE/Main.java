package gre.lab2.groupE;

import gre.lab2.graph.BFYResult;
import gre.lab2.graph.WeightedDigraph;
import gre.lab2.graph.WeightedDigraphReader;

import java.io.IOException;

public final class Main {
  public static void main(String[] args) throws IOException {
    // TODO
    //  - Documentation soignée comprenant :
    //    - la javadoc, avec auteurs et description des implémentations ;
    //    - des commentaires sur les différentes parties de vos algorithmes.

    BellmanFordYensAlgorithm bfy = new BellmanFordYensAlgorithm();
    WeightedDigraph wd = WeightedDigraphReader.fromFile("data/reseau4.txt");

    BFYResult result = bfy.compute(wd, 0);

    // Print the result if there are less than 25 vertices
    if (result.isNegativeCycle() || result.getShortestPathTree().distances().length <= 25) {
      System.out.println(result);
    }
    else {
      System.out.println("The graph is too large to print the result. (more than 25 vertices)");
    }
  }
}
