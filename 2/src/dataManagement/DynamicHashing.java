package dataManagement;

public abstract class DynamicHashing {

	public static final int HASHSIZE = 100 ; 
	public LinkedBinaryTree[] HashTable ; 
	public int PerformanceCounter;
	public BTNode node ;  // Κόμβος για διάσχιση 	
		
	public DynamicHashing ()
	{
		HashTable = new LinkedBinaryTree[HASHSIZE] ;	 
		PerformanceCounter = 0 ;
	}		
	public abstract void addKey (int key) ;
	public abstract void removeKey (int key) ;
	
	public BTNode search(int key) // Βρίσκει τον κόμβο που πρέπει να τοποθετηθεί το κλείδι ή που θα έπρεπε να βρίσκεται αν είχε τοποθετηθεί
	{
		int hash = key % HASHSIZE ;
		PerformanceCounter++ ;
		int cnt = BitDemo.NumOfBits ; 
		int [] keybits = (new BitDemo(key)).getBinaryNum() ; // Μετατρέπει τον αριθμό σε δυαδικό ωστε να χρησιμοιποιηθει η hash2
		PerformanceCounter++ ;		
		
		if (this.getClass().getName().equals("dataManagement.DynamicHashingMemory")) PerformanceCounter = PerformanceCounter + 2 ;
		if (HashTable[hash] != null) node = HashTable[hash].getRoot() ;			
	
		while (!node.isLeaf()) // Πρέπει το node να κατευθύνθει στον σωστό κόμβο		
		{
			cnt-- ;
			if (keybits[cnt] == 1) node = node.getLeft() ; 
			else node = node.getRight() ; 	
		}
		return node ; 			
	}
	
	public boolean searchKey (int key)
	{
		node = search(key) ; 		 
		return node.containsKey(key);		
	}
	
	public abstract void printAllElements () ;

	public int getPerformanceCounter() {
		return PerformanceCounter;
	}
	public void setPerformanceCounter(int performanceCounter) {
		PerformanceCounter = performanceCounter;
	}
}
