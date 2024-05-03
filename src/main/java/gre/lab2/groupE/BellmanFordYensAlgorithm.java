package gre.lab2.groupE;

import gre.lab2.graph.BFYResult;
import gre.lab2.graph.IBellmanFordYensAlgorithm;
import gre.lab2.graph.WeightedDigraph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Queue;

public final class BellmanFordYensAlgorithm implements IBellmanFordYensAlgorithm {
  @Override
  public BFYResult compute(WeightedDigraph graph, int from) {
    //Bellman Ford Yens Algorythme

    int[] predecessors = new int[graph.getNVertices()];
    int[] distances = new int[graph.getNVertices()];
    int interCounter = 0;
    final int SENTINEL = -2;

    for (int j = 0; j < graph.getNVertices(); j++)
    {
      distances[j] = Integer.MAX_VALUE;
      predecessors[j] = -1; //Represent null value
    }
    distances[from] = 0;

    Queue<Integer> verticesToIterate = new ArrayDeque<>();

    verticesToIterate.add(from);
    verticesToIterate.add(SENTINEL);

    while (!verticesToIterate.isEmpty())
    {
      int vertex= verticesToIterate.remove();
      if (vertex == SENTINEL)
      {
        if(!verticesToIterate.isEmpty())
        {
          interCounter++;
          if(interCounter == graph.getNVertices())
          {
            //Negative Cycle
            //TODO
            //regarder le premier sommet dans verticesToIterate
            //remonter l'arboraissance des prédessesseur et trouver la boucle

            return new BFYResult.NegativeCycle(new ArrayList<>(1), 1);
          }
          else
          {
            verticesToIterate.add(SENTINEL);
          }
        }
      }
      else
      {
        for (WeightedDigraph.Edge successor : graph.getOutgoingEdges(vertex))
        {
          if(distances[successor.to()] > distances[vertex] + successor.weight())
          {
            distances[successor.to()] = distances[vertex] + successor.weight();
            predecessors[successor.to()] = vertex;
            //TO CHANGE
            if(!verticesToIterate.contains(successor.to()))
            {
              verticesToIterate.add(successor.to());
            }
          }
        }
      }
    }

    return new BFYResult.ShortestPathTree(distances, predecessors);
  }
}
