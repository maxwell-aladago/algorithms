
/**
 *
 * @author aladago
 */
public class PQImplementationsTests {

    public static void main(String[] args) {
        //instantiating all three kinds of implementations
        
        ArrayBasedPQ<Integer, Integer> arrayPq = new ArrayBasedPQ();
        LinkedListBasedPQ<Integer, Integer> linkedListPq = new LinkedListBasedPQ();
        BSTBasedPQ<Integer, Integer> bstPq = new BSTBasedPQ();
        
        System.out.println("*****Testing Array-based Implementation******\n");
        PQImplementationsTests.testPriorityQueue(arrayPq);
        System.out.println("\n****Testing LinkedListed-based Implementation******\n");
        PQImplementationsTests.testPriorityQueue(linkedListPq);
        System.out.println("\n****Testing Binary Search Tree -based Implementation******\n");
        PQImplementationsTests.testPriorityQueue(bstPq);      
        
        
        
       
    }
    
    /**
     * Since all methods implement the priority queue interface, a single method
     * is written to test all the different implementations
     * 
     * @param pQ an object which implements the Priority Queue interface
     */
    public static void testPriorityQueue(PriorityQueue pQ){
        
        System.out.println("The size of an empty queue is " + pQ.getSize());
        System.out.println("isEmpty() should return true: " + pQ.isEmpty());
        
        System.out.println("adding items 5 to 19 to queue with varing priorities.....");

        //insert items such that odd numbers are given some slight priorities than even numbers
        for (int i = 5; i < 20; i++) {
            if (i % 2 == 0) {
                pQ.addEntity(i, i);
            } else {
                pQ.addEntity(i * i, i);
            }
        }
        
        

        // size of array should be 15
        //item with highest priority should be 19
        System.out.println("The size of the queue is " + pQ.getSize());
        System.out.println("The maximum entity is " + pQ.getMaxEntity());

        //remove max entity should return 19
        System.out.println("The maximum entity removed is " + pQ.removeMaxEntity());

        //size should be 14
        //item with highest priority should now be 17. odd numbers are given the sqaures of their priorities
        System.out.println("\nThe new size of the queue is " + pQ.getSize());
        System.out.println("The new maximum entity after 19 is " + pQ.getMaxEntity());
        
        //changing the priority of 18
        pQ.changePriority(18, 18, 20*20);
        System.out.println("\nThe new maximum entity after changing priority "
                + "of 18 is (18 should be maximum now)" + pQ.getMaxEntity());
        
        //this will change the priority of the item with the least priority to the one with highest priority
        System.out.println("Changing the priority of the 6, the item with the lowest priorty to "
                + "give it the highest priority");
        pQ.changePriority(6, 6, 20*21);
        System.out.println("\nThe new maximum entity is: " + pQ.getMaxEntity());
    }

}
