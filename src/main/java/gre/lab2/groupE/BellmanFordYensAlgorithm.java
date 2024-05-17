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

            //Remontage des prédécesseurs
            Queue<Integer> verticesFound = new ArrayDeque<>();
            boolean[] visited = new boolean[graph.getNVertices()];

            int vertexFound = verticesToIterate.remove();
            verticesFound.add(vertexFound);
            visited[vertexFound] = true;

            while(predecessors[vertexFound] != -1)
            {
              vertexFound = predecessors[vertexFound];
              if(visited[vertexFound])
              {
                break;
              }
              verticesFound.add(vertexFound);
              visited[vertexFound] = true;
            }

            //Recherche de la boucle
            ArrayList<Integer> negativeCycle = new ArrayList<>();
            while (vertexFound != verticesFound.peek())
            {
              verticesFound.remove();
            }

            while (!verticesFound.isEmpty())
            {
              negativeCycle.addFirst(verticesFound.remove());
            }

            //Calcul de la longueur de la boucle
            int negativeCycleLength = 0;

            for (int i = 0; i < negativeCycle.size(); i++)
            {
                for (WeightedDigraph.Edge edge : graph.getOutgoingEdges(negativeCycle.get(i)))
                {
                    if(edge.to() == negativeCycle.get((i+1) % negativeCycle.size()))
                    {
                    negativeCycleLength += edge.weight();
                    break;
                    }
                }
            }


            return new BFYResult.NegativeCycle(negativeCycle, negativeCycleLength);
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
