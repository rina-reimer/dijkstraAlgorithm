package graphdtypes;

import java.util.Collection;

public interface Graph<V, E extends BaseEdge<V, E>> {
    Collection<E> outgoingEdgesFrom(V vertex);
}