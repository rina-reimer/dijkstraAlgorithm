import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

public class ArrayHeapMinPQ<V> {
    static final int START_INDEX = 0;
    List<PriorityNode<V>> items; // heap of priority nodes
    int capacity;

    HashMap<V, Integer> dict;

    public ArrayHeapMinPQ() {
        items = new ArrayList<>();
        capacity = 10;
        dict = new HashMap<>();
    }

    public void add(V item, double priority) {
        PriorityNode<V> newItem = new PriorityNode<>(item, priority);
        if (items.size() == capacity) { // check if the ArrayList is full
            ArrayList<PriorityNode<V>> newItems = new ArrayList<>(capacity * 2);
            newItems.addAll(items);
            items = newItems; // update the reference to the items ArrayList to the new one
            capacity *= 2;
        }
        if (this.contains(item)) {
            throw new IllegalArgumentException("The item already exists.");
        }
        items.add(newItem);
        dict.put(item, items.size() - 1);
        percolateUp(items.size() - 1);
    }

    public boolean contains(V item) {
        return dict.containsKey(item);
    }

    private void swap(int a, int b) {
        PriorityNode<V> temp = items.get(a);
        items.set(a, items.get(b));
        items.set(b, temp);

        dict.put(items.get(a).getItem(), a);
        dict.put(items.get(b).getItem(), b);
    }

    public boolean isEmpty() {
        return (items.size() == 0);
    }

    public V removeMin() {
        if (items.size() == 0) {
            throw new NoSuchElementException("The heap is empty.");
        }
        V minItem = items.get(0).getItem();
        swap(0, items.size() - 1);
        items.remove(items.size() - 1);
        dict.remove(minItem);
        percolateDown(0);
        return minItem;
    }

    private void percolateUp(int index) {
        int current = index;
        int parent = (current - 1) / 2;
        while (current > 0 && items.get(current).getPriority() < items.get(parent).getPriority()) {
            swap(current, parent);
            current = parent;
            parent = (current - 1) / 2;
        }
    }

    private void percolateDown(int index) {
        int current = index;
        int child1 = 2 * current + 1;
        int child2 = 2 * current + 2;
        int smallestChild;
        while (child1 < items.size()) {
            if (child2 >= items.size() || items.get(child1).getPriority() < items.get(child2).getPriority()) {
                smallestChild = child1;
            } else {
                smallestChild = child2;
            }
            if (items.get(current).getPriority() > items.get(smallestChild).getPriority()) {
                swap(current, smallestChild);
                current = smallestChild;
                child1 = 2 * current + 1;
                child2 = 2 * current + 2;
            } else {
                break;
            }
        }
    }

    public void changePriority(V item, double priority) {
        PriorityNode<V> newPriority = new PriorityNode<V>(item, priority);
        boolean found = dict.containsKey(item); // when the item is in the dictionary, it's in the heap
        if (found) {
            int index = dict.get(item);
            items.set(index, newPriority); //change priority of the index
            percolateUp(index);
            percolateDown(index);
        } else {
            throw new NoSuchElementException("The item does not exist in the priority queue.");
        }
    }

    public double get(V item){
        return items.get(dict.get(item)).getPriority();
    }

    public int size() {
        return dict.size();
    }

    private class PriorityNode<V> {
        private final V item;
        private double priority;

        PriorityNode(V e, double p) {
            this.item = e;
            this.priority = p;
        }

        V getItem() {
            return this.item;
        }

        double getPriority() {
            return this.priority;
        }

        @Override
        public String toString() {
            return "PriorityNode{" +
                "item=" + item +
                ", priority=" + priority +
                '}';
        }
    }   
}
