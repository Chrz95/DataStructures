package dataManagement;

import java.util.Vector;

public class DynamicHashingMemory extends DynamicHashing {

	private BTNode node ; // ������ ��� �������� 	
	
	public DynamicHashingMemory (int NumOfInitialElements) 
	{
		for (int i=0 ; i< HASHSIZE ; i++) HashTable[i] = new LinkedBinaryTree(new HashBucket()) ;
		for (int i=1 ; i <= NumOfInitialElements ; i++) addKey(i) ;		
	}

	public void addKey(int key) 
	{	
		PerformanceCounter += search(key).addValue(key); // O ������� �� ����� ������� � ��� ��� � �������� �� ��������� , ������������ ���� addValue
	}

	public void removeKey(int key) 
	{
		node = search(key) ;	
		if (node.containsKey(key))
		{			 
			node.getHashBucket().removeValue(key);
			Vector <Integer> Data = node.getKeys();				
			
			// ������� �� ������ �� ����� ����� �� �� �������� ��� ��� �� ��� ����� ��� ��������	
			if ((node.getSibling()!=null) && ((HashBucket.SIZE - node.getSibling().getNumOfKeys())) >= node.getNumOfKeys()) // Merge �� � ������� ��� ����� ������ ��� ���������� ����� ����������� � ���� ��� �� �������� ��� ������
			{
				for (int i= 0 ; i < Data.size(); i++)
					if (node.getHashBucket().getBucketValues()[i] != 0)					
						node.getSibling().addValue(Data.get(i));
				
				//������ � ������ ��� ���� ��� �� �������� node.getSibling() �� ����� � ������ ��� ���� �� ����������
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
