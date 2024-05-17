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
    final int SENTINEL = -2;
    final int INFINITE = Integer.MAX_VALUE;
    final int UNREACHABLE = -1;

    //Bellman Ford Yens Algorythme
    int[] predecessors = new int[graph.getNVertices()];
    int[] distances = new int[graph.getNVertices()];
    int interCounter = 0;

    for (int j = 0; j < graph.getNVertices(); j++)
    {
      distances[j] = INFINITE;
      predecessors[j] = UNREACHABLE;
    }
    distances[from] = 0;

    Queue<Integer> verticesToIterate = new ArrayDeque<>(); //Queue to store the vertices to iterate
    boolean[] visitedVerticeToIterate = new boolean[graph.getNVertices()];


    verticesToIterate.add(from);
    visitedVerticeToIterate[from] = true;
    verticesToIterate.add(SENTINEL);

    while (!verticesToIterate.isEmpty())
    {
      int vertex = verticesToIterate.remove();
      if (vertex == SENTINEL)
      {
        if(!verticesToIterate.isEmpty())
        {
          interCounter++;
          if(interCounter == graph.getNVertices()) //If true : There is a negative cycle
          {
            //Backtracking the predecessors to find the negative cycle
            Queue<Integer> verticesFound = new ArrayDeque<>(); //Queue to store the vertices found
            boolean[] visited = new boolean[graph.getNVertices()]; //Array to store the visited vertices

            int vertexFound = verticesToIterate.remove();
            verticesFound.add(vertexFound);
            visited[vertexFound] = true;

            //Backtracking the predecessors
            while(predecessors[vertexFound] != UNREACHABLE)
            {
              vertexFound = predecessors[vertexFound];
              if(visited[vertexFound])
              {
                break;
              }
              verticesFound.add(vertexFound);
              visited[vertexFound] = true;
            }

            //Storing the negative cycle
            ArrayList<Integer> negativeCycle = new ArrayList<>();
            while (vertexFound != verticesFound.peek())
            {
              verticesFound.remove();
            }

            while (!verticesFound.isEmpty())
            {
              negativeCycle.addFirst(verticesFound.remove());
            }

            //Calculating the negative cycle length
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
        visitedVerticeToIterate[vertex] = false;

        for (WeightedDigraph.Edge successor : graph.getOutgoingEdges(vertex))
        {
          if(distances[successor.to()] > distances[vertex] + successor.weight())
          {
            distances[successor.to()] = distances[vertex] + successor.weight();
            predecessors[successor.to()] = vertex;

            if(!visitedVerticeToIterate[successor.to()])
            {
              verticesToIterate.add(successor.to());
              visitedVerticeToIterate[successor.to()] = true;
            }
          }
        }
      }
    }

    return new BFYResult.ShortestPathTree(distances, predecessors);
  }
}
