import graphdtypes.BaseEdge;
import graphdtypes.Graph;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;


public class DijkstraShortestPathFinder<G extends Graph<V, E>, V, E extends BaseEdge<V, E>> {

    protected <V> ArrayHeapMinPQ<V> createMinPQ() {
        return new ArrayHeapMinPQ<V>();
    }

    protected Map<V, E> constructShortestPathsTree(Graph<V, E> graph, V start, V end) {
        Map<V, E> edgeTo = new HashMap<>();
        // edge that is the most efficient from any specific node. A: NULL, B: A -> B
        Map<V, Double> distTo = new HashMap<>(); // shortest distance from the start node. A: 0
        distTo.put(start, 0.0);
        ArrayHeapMinPQ<V> pq = createMinPQ(); // will essentially store the unprocessed nodes
        pq.add(start, 0);
        while (!pq.isEmpty()) {
            V from = pq.removeMin();
            if (from.equals(end)) {
                edgeTo.remove(start); //hmm
                return edgeTo;
            }
            for (E edge : graph.outgoingEdgesFrom(from)) {
                V to = edge.to();
                double oldDist = distTo.getOrDefault(to, Double.POSITIVE_INFINITY);
                double newDist = distTo.get(from) + edge.weight();
                if (newDist < oldDist) {
                    distTo.put(to, newDist);
                    edgeTo.put(to, edge);
                    if (pq.contains(to)) {
                        pq.changePriority(to, newDist);
                    } else {
                        pq.add(to, newDist);
                    }
                }
            }
        }
        return edgeTo;
    }

    protected ShortestPath<V, E> extractShortestPath(Map<V, E> spt, V start, V end) {
        if (start.equals(end)) {
            return new ShortestPath.SingleVertex<>(start);
        }
        LinkedList<V> path = new LinkedList<>();
        LinkedList<E> edges = new LinkedList<>();
        path.add(end);
        while (true) {
            //System.out.println("check 3");
            V next = path.remove();
            if (next.equals(start)) {
                return new ShortestPath.Success<>(new ArrayList<>(edges));
            } else {
                E edge = spt.get(next);
                if (edge == null) {
                    return new ShortestPath.Failure<>();
                    //
                } else {
                    edges.addFirst(edge);
                    path.add(edge.from());
                }
            }

        }

    }



}