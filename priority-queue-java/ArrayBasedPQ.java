
/**
 *
 * @author aladago
 * @param <E> The priority of the item
 * @param <V> the value of the item
 */
public class ArrayBasedPQ<E extends Comparable<? super E>, V>
        implements PriorityQueue<E, V> {

    private Entity<E, V>[] entities;
    private static final int INITIAL_SIZE = 10;
    public static final int INDEX_OF_MAX_ENTITY = 0;
    private int numEntities;

    private static class Entity<E, V> {

        E priority;
        V value;

        Entity(E p, V data) {
            this.priority = p;
            this.value = data;
        }

        boolean equals(Entity<E, V> o) {
            return this.value.equals(o.value) && this.priority == o.priority;
        }
    }

    public ArrayBasedPQ() {
        this.numEntities = 0;
        this.entities = new Entity[ArrayBasedPQ.INITIAL_SIZE];
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isEmpty() {
        return this.numEntities < 1;
    }

    /**
     *
     * @return the item with the highest priority
     */
    @Override
    public V getMaxEntity() {
        if (this.isEmpty()) {
            throw new RuntimeException("Can't get maximum entity from an empty queue");
        }
        return this.entities[INDEX_OF_MAX_ENTITY].value;
    }

    /**
     *
     * @return
     */
    @Override
    public V removeMaxEntity() {
        if (this.isEmpty()) {
            throw new RuntimeException("Can't get maximum entity from an empty queue");
        }

        Entity<E, V> maxEntity = this.entities[INDEX_OF_MAX_ENTITY];
        //you only adjust entities after removing the highest value if there are more than one item
        if (this.numEntities == 1) {
            this.entities[INDEX_OF_MAX_ENTITY] = null;
        } else {
            E curMaxEntity = this.entities[1].priority;
            int indexOfMaxEntity = 1;

            for (int i = 2; i < numEntities; i++) {
                if (curMaxEntity.compareTo(this.entities[i].priority) == -1) {
                    curMaxEntity = this.entities[i].priority;
                    indexOfMaxEntity = i;
                }
            }
            //once the maximum has been found, put the maximum at index 0
            //and replace the maximum by the last item in the array
            //reset the last item in the array to null
            this.entities[INDEX_OF_MAX_ENTITY] = this.entities[indexOfMaxEntity];
            this.entities[indexOfMaxEntity] = this.entities[this.numEntities - 1];
            this.entities[this.numEntities - 1] = null;

        }

        this.numEntities--;
        return maxEntity.value;
    }

    /**
     * This method adds a new entity to the heap. post-condition: number of
     * entities increases by 1 and size of the array may increase
     *
     * @param p
     * @param data
     */
    @Override
    public void addEntity(E p, V data) {

        if (this.numEntities == this.entities.length) {
            //expand the array
            this.expandHeap(2 * this.numEntities);
        }

        //the entity with the highest priority is at always at index 0
        //the first item by default has the highest priority
        if (this.numEntities == 0) {
            this.entities[INDEX_OF_MAX_ENTITY] = new Entity(p, data);
        } else {
            this.entities[this.numEntities] = new Entity(p, data);

            if (p.compareTo(this.entities[INDEX_OF_MAX_ENTITY].priority) == 1) {
                this.swapEntities(INDEX_OF_MAX_ENTITY, numEntities);
            }
        }

        //adding a new item increases size
        this.numEntities++;
    }

    /**
     *
     * @return the number of entities in the heap
     */
    @Override
    public int getSize() {
        return this.numEntities;
    }

    /**
     * Reinitialize the queue
     */
    @Override
    public void empty() {
        this.entities = new Entity[INITIAL_SIZE];
        this.numEntities = 0;
    }

    /**
     * A method to change the priority of an entity in the heap. Since there can
     * be two or more similar entities in the array, I have decided to change
     * the prirorities of all of them.
     *
     * @param p
     * @param data
     * @param newPriority the new priority of that entity
     */
    @Override
    public void changePriority(E p, V data, E newPriority) {
        Entity<E, V> entity = new Entity(p, data);
        for (int i = 0; i < this.numEntities; i++) {
            if (this.entities[i].equals(entity)) {
                this.entities[i].priority = newPriority;

                //if the new priority is greater than the current highest priority, swap them
                if (newPriority.compareTo(this.entities[INDEX_OF_MAX_ENTITY].priority) == 1) {
                    this.swapEntities(INDEX_OF_MAX_ENTITY, i);
                }
            }
        }
    }

    /**
     *
     * @param posOfFirstEntity
     * @param posOfSecondEntity
     */
    private void swapEntities(int posOfFirstEntity, int posOfSecondEntity) {

        Entity<E, V> tempt = this.entities[posOfFirstEntity];
        this.entities[posOfFirstEntity] = this.entities[posOfSecondEntity];
        this.entities[posOfSecondEntity] = tempt;
    }

    /**
     * Handle over-flows of the heap by expanding its size. * @param newSize the
     * new size of the array
     */
    private void expandHeap(int newSize) {

        Entity<E, V>[] updatedHeap = new Entity[newSize];
        System.arraycopy(this.entities, 0, updatedHeap, 0, this.numEntities);

        //functional entities are now the updated heap
        this.entities = updatedHeap;
    }

}
