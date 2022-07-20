package dataManagement;

import java.util.Vector;

public class DynamicHashingMemory extends DynamicHashing {

	private BTNode node ; // Κόμβος για διάσχιση 	
	
	public DynamicHashingMemory (int NumOfInitialElements) 
	{
		for (int i=0 ; i< HASHSIZE ; i++) HashTable[i] = new LinkedBinaryTree(new HashBucket()) ;
		for (int i=1 ; i <= NumOfInitialElements ; i++) addKey(i) ;		
	}

	public void addKey(int key) 
	{	
		PerformanceCounter += search(key).addValue(key); // O ελεγχος αν είναι γεμάτος ή όχι και η διάσπαση αν χρειαστεί , εμπεριέχεται στην addValue
	}

	public void removeKey(int key) 
	{
		node = search(key) ;	
		if (node.containsKey(key))
		{			 
			node.getHashBucket().removeValue(key);
			Vector <Integer> Data = node.getKeys();				
			
			// Έλεγχος αν πρέπει να γίνει ένωση με το αδελφάκι του και με τον γονεα κατ επεκταση	
			if ((node.getSibling()!=null) && ((HashBucket.SIZE - node.getSibling().getNumOfKeys())) >= node.getNumOfKeys()) // Merge αν ο αριθμός των κενων θέσεων του αδελφακιου είναι μεγαλύτερες ή ισες απο τα στοιχεία του κόμβου
			{
				for (int i= 0 ; i < Data.size(); i++)
					if (node.getHashBucket().getBucketValues()[i] != 0)					
						node.getSibling().addValue(Data.get(i));
				
				//Πρέπει ο κόμβος που πήρε όλα τα στοιχεια node.getSibling() να γίνει ο γονέας και μετα να διαγραφούν
				node.getParent().setElement(node.getSibling().getElement());
				node.getParent().setNumOfKeys(node.getSibling().getNumOfKeys());
				
				node = node.getParent() ;
				if ((node.getParent()==null) & (node.isEmpty())) node = null ; 
				
				node.setRight(null);
				node.setLeft(null);
			}
			//System.out.println("The key " +  key + " is deleted!");		
		}
		//else System.out.println("The key " + key + " is not found!");
	}	
	
	public void printAllElements() 
	{ 
		System.out.println("\nThe elements are (preorder) : ");
		for (int i = 0 ; i<HashTable.length ; i++)
		{
			if (HashTable[i] != null)
			{
				if ((HashTable[i].getRoot().isSpilt()) | (!HashTable[i].getRoot().isEmpty()) )
				{
					System.out.print("\nTree " + i + " : ");
					HashTable[i].printElements();
				}
			}
		}								
	}
	
}
