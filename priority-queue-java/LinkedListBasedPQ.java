
/**
 * This an implementation of a priority queue using a singly linked list
 * @author aladago
 * @param <E> the priority of an entity
 * @param <V> the data of an entity
 */
public class LinkedListBasedPQ<E extends Comparable<? super E>, V>  implements PriorityQueue<E, V> {

    Node<E, V> head;
    int size;

    private static class Node<E, V> {
        Node<E, V> next;
        V data;
        E priority;

          //constructors of inner class
        Node(E p, V d, Node<E, V> next) {
            this.data = d;
            this.priority = p;
            this.next = next;
        }

        Node(E p, V d) {
            this(p, d, null);
        }
    }

    public LinkedListBasedPQ() {
        head = null;
        size = 0;
    }

    public LinkedListBasedPQ(E p, V d) {
        head = new Node(p, d);
        this.size = 1;
    }

    @Override
    public void addEntity(E p, V d) {
        
        Node<E, V> newNode = new Node(p, d);
        if (head == null) {
            head = newNode;
        } else if (head.priority.compareTo(p) < 0) {
            //if the new item has the highest priority
            newNode.next = head;
            head = newNode;
        } else {
            Node<E, V> prevNode = head;
            Node<E, V> nextNode = head.next;

            while (nextNode != null) {
                if (prevNode.priority.compareTo(p) >= 0 && nextNode.priority.compareTo(p) < 0) {
                    prevNode.next = newNode;
                    newNode.next = nextNode;
                    break;
                }
                //update nodes
                prevNode = nextNode;
                nextNode = nextNode.next;
            }

            if (nextNode == null) {
                prevNode.next = newNode;
            }
        }        
        size++;
    }

    @Override
    public V removeMaxEntity() {
        if(head == null){
            throw new RuntimeException("Can't remove max entity from an empty queue");
        }
        
        V dataOfHighest = head.data;
        head = head.next;
        size--;
        return dataOfHighest;
    }

    @Override
    public V getMaxEntity() {
        if(head == null){
            throw new RuntimeException("Can't get max entity from an empty queue");
        }
        return head.data;
    }

    @Override
    public boolean isEmpty() {
        return head == null;
    }
    
    @Override
    public int getSize(){
      return this.size;  
    }
    
    @Override
    public void changePriority(E p, V data, E newPriority){
        
      if(head == null){
          throw new RuntimeException("Can't change priority of non-existent item");
      }
        Node<E, V> currentNode = head;
        //if the node to change priority is the head, then point the head to the next node and 
        //insert a new node
        if(currentNode.priority.equals(p) && currentNode.data.equals(data)){
            head = currentNode.next;
            this.addEntity(newPriority, data);
        }else{
            //iterate through the list
            //if the node exist, points its previous to its next
            // add a new node with the new priority
            while(currentNode.next != null){
               if(currentNode.next.priority.equals(p) && currentNode.next.data.equals(data)){
                   currentNode.next = currentNode.next.next;
                   this.addEntity(newPriority, data);
                   break;
               }
               currentNode = currentNode.next;
            }
            
            //the node to change had the last priority
            if(currentNode.next == null && currentNode.priority.equals(p) 
                    && currentNode.data.equals(data)){
                currentNode = null;
                this.addEntity(newPriority, data);
            }
        }      
    }
    
    
    @Override
    public void empty(){
        this.head = null;
        this.size = 0;
    }
    
    public void printQueue(){
        Node<E, V> currentNode = head;
        while (currentNode != null){
           System.out.print(currentNode.data + "  ");
           currentNode = currentNode.next;
        }
        System.out.println();
    }
}
