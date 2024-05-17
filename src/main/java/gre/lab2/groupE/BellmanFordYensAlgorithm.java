package gre.lab2.groupE;

import gre.lab2.graph.BFYResult;
import gre.lab2.graph.IBellmanFordYensAlgorithm;
import gre.lab2.graph.WeightedDigraph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

/**
 * Represents the BellmanFordYens algorithm
 *
 * @author Ançay Rémi
 * @author Hutzli Boris
 */
public final class BellmanFordYensAlgorithm implements IBellmanFordYensAlgorithm {

  /** Sentinel vertex value */
  private final int SENTINEL = -2;

  /** Infinite value */
  private final int INFINITE = Integer.MAX_VALUE;

  /** Unreachable vertex value */
  private final int UNREACHABLE = -1;

  @Override
  public BFYResult compute(WeightedDigraph graph, int from) {

    // Array of predecessors
    int[] predecessors = new int[graph.getNVertices()];

    // Array of distances to the source
    int[] distances = new int[graph.getNVertices()];

    // Iteration counter
    int iterationCounter = 0;

    //Queue to store the vertices to iterate
    Queue<Integer> verticesToIterate = new ArrayDeque<>();

    // Determines whether the vertex are currently in the verticesToIterate queue
    boolean[] visitedVerticeToIterate = new boolean[graph.getNVertices()];

    // Initialises each vertex distance and predecessor
    for (int j = 0; j < graph.getNVertices(); j++)
    {
      distances[j] = INFINITE;
      predecessors[j] = UNREACHABLE;
    }
    distances[from] = 0;

    // Adds source the queue
    verticesToIterate.add(from);
    visitedVerticeToIterate[from] = true;

    // Adds the sentinel
    verticesToIterate.add(SENTINEL);

    // Apply the BFY algorithm
    while (!verticesToIterate.isEmpty())
    {
      int vertex = verticesToIterate.remove();

      // If the sentinel is found, increment iteration
      if (vertex == SENTINEL)
      {
        // Continue the algorithm as long as there remains vertices to iterate
        if(!verticesToIterate.isEmpty())
        {
          iterationCounter++;

          // If the amount of iterations done is equal to the amount of vertices, there is a negative cycle
          if(iterationCounter == graph.getNVertices())
          {
            //Queue to store the vertices found
            Queue<Integer> verticesFound = new ArrayDeque<>();

            //Array to store the visited vertices
            boolean[] visited = new boolean[graph.getNVertices()];

            // Vertex found when backtracking the cycle
            int vertexFound;

            //Storing the negative cycle
            ArrayList<Integer> negativeCycle = new ArrayList<>();

            // Initialise the vertex found
            vertexFound = verticesToIterate.remove();
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

            // Remove the vertices that aren't part of the negative cycle
            while (!verticesFound.isEmpty() && vertexFound != verticesFound.peek())
            {
              verticesFound.remove();
            }

            while (!verticesFound.isEmpty())
            {
              negativeCycle.addFirst(verticesFound.remove());
            }

            // Calculating the negative cycle length
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
          // Add back the sentinel for the next iteration
          else
          {
            verticesToIterate.add(SENTINEL);
          }
        }
      }
      // If no sentinel found, continue the iteration
      else
      {
        visitedVerticeToIterate[vertex] = false;

        // Calculates the distances and predecessors and re-iterates the successors if there was a distance upgrade
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
