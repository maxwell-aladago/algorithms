/**
 * 
 * @author maxwellaladago
 *
 */
public class QuickSelect {
	/**
	 * Partitions a list of real numbers using Lumuto's partitioning algorithm. 
	 * Note: the entire list is not considered for partitioning. Only a range of it is considered
	 * The pivot is the first number in the partitioning range. 
	 * @param values the real numbers to partition. 
	 * @param lowerIndex the index from which to start partitioning
	 * @param upperIndex the first index to the right of the startIndex which is not considered for 
	 * partitioning
	 * @return An integer i indicating the partition index. Must be greater than or lowerIndex and less than 
	 * upper Index
	 */
	private int lumotoPartition(double [] values, int lowerIndex, int upperIndex) {
		int i = lowerIndex;
		for (int j = lowerIndex + 1; j < upperIndex; j++) {
			if (values[j] < values[lowerIndex])
				this.swap(values, ++i, j);
		}
		this.swap(values, lowerIndex, i);
		return i;
	}
	/**
	 * change the positions of two numbers in a list. 
	 * @param values the numbers
	 * @param i the index of one of the two numbers
	 * @param j the index of the other number
	 */
	private void swap(double [] values, int i, int j) {
		double temp = values[i];
		values[i] = values[j];
		values[j] = temp;
	}
	/**
	 * Recursively selects the k-th order statistic from a list of real numbers. 
	 * This implementation is directly based on on the quick select algorithm
	 * @param values the real numbers from which to select the order-statistic  
	 * @param lowerIndex the starting index to partition the list
	 * @param upperIndex the index marking the end of the list considered for partitioning
	 * @param k the oder statistic needed. eg. 4-th order statistic 
	 * @return the k-th order statistic 
	 */
	private double quickSelect(double [] values, int lowerIndex, int upperIndex, int k) {
		int p = this.lumotoPartition(values, lowerIndex, upperIndex);
		
		if (p == k - 1) {
			return values[p];
		}else if(p > k - 1) {
			return this.quickSelect(values, lowerIndex, p, k);
		}else {
			return this.quickSelect(values, p + 1, upperIndex, k);
		}	
	}
	
	/**
	 * Selects an order statistic using the quickslect algorithm. 
	 * Quick select uses Lumoto's partitioning algorithm. 
	 * @param values a list of real numbers from which to select an order statistic
	 * @param k the i-th order statistic
	 * @return the k-th order statistic among the values
	 * @throws RuntimeException 
	 */
	public double quickSelect(double [] values, int k) throws RuntimeException {
		if (k <= 0) 
			throw new RuntimeException("Error: k = " + k + "? 0 or negative order "
					+ "statistic  doesn't make sense. Try again");
		else if ( k >  values.length) 
			throw new RuntimeException("Error: " + k + "-th order statistic  doesn't make sense "
					+ "for a list of size " + values.length);
		
		return this.quickSelect(values, 0, values.length, k);
	}
	
	/**
	 * A small test program. 
	 * The test is rudimentary but it shows that the correctness of the implementation
	 * @param args
	 */
	public static void main(String [] args) {
		QuickSelect quickSelect = new QuickSelect();
		double [] values = {5, 2, 6, 3, 1, 7, 4};
		
		// Clearly the  numbers are the first seven numbers. The 'k-th' order
		//statistic is k. 
		//You can change the list and try other numbers
		
		try {
			System.out.println(quickSelect.quickSelect(values, 6));
			System.out.println(quickSelect.quickSelect(values, 5));
			System.out.println(quickSelect.quickSelect(values, 7));
			System.out.println(quickSelect.quickSelect(values, 6));
			System.out.println(quickSelect.quickSelect(values, 2));
			System.out.println(quickSelect.quickSelect(values, 8));
		}catch (RuntimeException e) {
			System.out.println(e.getMessage());
		}
	}
}
