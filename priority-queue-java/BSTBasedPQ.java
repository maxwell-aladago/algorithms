
/**
 * Implements a priority queue using a binary search tree as the underlying data structure
 * @author aladago
 * @param <E> The element/item. This entity serves as the priority of the entity
 * @param <V> The value of the entity
 */
public class BSTBasedPQ<E extends Comparable<? super E>, V>
        implements PriorityQueue<E, V> {

    private BinaryNode<E, V> root;
    private int size;

    private static class BinaryNode<E, V> {

        E priority;
        V value;
        BinaryNode<E, V> leftChild;
        BinaryNode<E, V> rightChild;

        //constructors
        BinaryNode(E priority, V value, BinaryNode<E, V> lC, BinaryNode<E, V> rC) {
            this.priority = priority;
            this.value = value;
            this.leftChild = lC;
            this.rightChild = rC;
        }

        BinaryNode(E priority, V value) {
            this(priority, value, null, null);
        }

    }

    public BSTBasedPQ() {
        this.root = null;
        this.size = 0;
    }

    /**
     * Checks whether the priority queue is empty or not
     * @return True if the queue is empty. Return false otherwise
     */
    @Override
    public boolean isEmpty() {
        return this.root == null;
    }

    /**
     * Retrieves the enity with the highest priority
     * @return The value of the entity with the highest priority without
     * removing it
     */
    @Override
    public V getMaxEntity() {
        return findMax(this.root).value;
    }

    /**
     * Removes the highest priority item in the queue
     * @return The value of the entity with the highest priority and remove it
     */
    @Override
    public V removeMaxEntity() {
        BinaryNode<E, V> maxNode = this.removeMaxEntity(this.root);
        this.size--;
        return maxNode.value;
    }

    /**
     * Adds a new entity to the priority queue
     * @param p the priority of the new entity
     * @param v the value of the new entity
     */
    @Override
    public void addEntity(E p, V v) {
        this.root = this.insertEntity(p, v, this.root);
        this.size++;
    }

    /**
     * A method to change the priority of an entity already in the queue
     *
     * @param oldPriority the current priority of the entity to update
     * @param value the value of the old entity // not used here because
     * duplicates are not allowed
     * @param newPriority the new priority
     */
    @Override
    public void changePriority(E oldPriority, V value, E newPriority) {
        BinaryNode<E, V> updatedNode = this.removeEntity(oldPriority);
        this.insertEntity(newPriority, updatedNode.value, root);
    }

    /**
     *  The size of the priority queue
     * @return The size of the queue
     */
    @Override
    public int getSize() {
        return this.size;
    }
    
    /**
     * removes all items in the priority queue
     */
    public void empty(){
        this.root = null;
        this.size = 0;
    }

    /**
     *
     * @param node
     * @return
     */
    private BinaryNode<E, V> findMax(BinaryNode<E, V> node) {

        if (node.rightChild == null) {
            return node;
        } else {
            return findMax(node.rightChild);
        }
    }

    /**
     * Inserts a new entity into the priority queue
     * @param p
     * @param v
     * @param node
     * @return
     */
    private BinaryNode<E, V> insertEntity(E p, V v, BinaryNode<E, V> node) {

        if (node == null) {
            return new BinaryNode(p, v, null, null);
        }

        int priorityOrder = p.compareTo(node.priority);

        if (priorityOrder > 0) {
            node.rightChild = insertEntity(p, v, node.rightChild);
        } else if (priorityOrder < 0) {
            node.leftChild = insertEntity(p, v, node.leftChild);
        }

        return node;
    }

    /**
     * The node with the highest priority's right child must be null
     *
     * @param node the current node in recurssion
     * @return the node with the highest priority
     */
    private BinaryNode<E, V> removeMaxEntity(BinaryNode<E, V> node) {
        if (root == null) {
            throw new RuntimeException("Can't remove node from an empty queue");
        }

        BinaryNode<E, V> parent = null;
        BinaryNode<E, V> currentNode = root;
        if (root.rightChild == null) {
            root = root.leftChild;
        } else {
            while (currentNode.rightChild != null) {
                parent = currentNode;
                currentNode = currentNode.rightChild;
            }

            parent.rightChild = currentNode.leftChild;
        }
        return currentNode;
    }

    /**
     *
     *
     * @param node the root of the subtree to remove node from
     * @return The node removed
     */
    private BinaryNode<E, V> removeEntity(E priority) {

        BinaryNode<E, V> currentNode = root;
        BinaryNode<E, V> desiredNode;

        int priorityOrder;
        while (currentNode != null) {
            priorityOrder = priority.compareTo(currentNode.priority);
            if (priorityOrder < 0) {
                currentNode = currentNode.leftChild;
            } else if (priorityOrder > 0) {
                currentNode = currentNode.rightChild;
            } else {
                //the node to change priority has been found
                break;
            }
        }

        if (currentNode == null) {
            throw new RuntimeException("Can't change priority of a non-existent entity");
        }

        //the node to change priority has been found     
        desiredNode = new BinaryNode(currentNode.priority, currentNode.value);

        //if the node has two children, replace it by the maximum node on its left
        if (currentNode.leftChild != null && currentNode.rightChild != null) {
            BinaryNode<E, V> maxNodeOnLeft = this.removeMaxEntity(currentNode.leftChild);
            currentNode.value = maxNodeOnLeft.value;
            currentNode.priority = maxNodeOnLeft.priority;
        } else {
            currentNode = currentNode.leftChild == null ? currentNode.leftChild : currentNode.rightChild;

        }

        return desiredNode;
    }

}
