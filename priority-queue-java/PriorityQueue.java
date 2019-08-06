
/**
 *
 * @author aladago
 * @param <E>
 * @param <V>
 */
public interface PriorityQueue<E, V> {

    public V getMaxEntity();

    public V removeMaxEntity();

    public boolean isEmpty();

    public void addEntity(E p, V data);

    public int getSize();

    public void changePriority(E p, V data, E newPriority);
    public void empty();

}
