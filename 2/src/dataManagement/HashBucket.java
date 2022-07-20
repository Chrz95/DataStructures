package dataManagement;

public class HashBucket {

	public static final int SIZE = 2 ; 	
	private int[] Bucket ; 
	private int NumOfElements =  0 ; 
	
	public HashBucket ()
	{
		Bucket = new int[SIZE];
	}
	
	public void addValue (int value)
	{
		for (int i = 0 ; i < Bucket.length ; i++)
		{
			if (Bucket[i] == 0)
			{
				Bucket[NumOfElements++] = value ;
				break ; 
			}				
		}
	}
	
	public void removeValue (int value)
	{
		for (int i=0 ; i< SIZE ; i++)
			if (Bucket[i] == value) Bucket[i] = 0 ; 
		
		NumOfElements--;
	}
	
	public boolean isEmpty()
	{
		return (NumOfElements==0) ;
	}

	public int getNumOfElements() {
		return NumOfElements;
	}
	
	public int getSize() {
		return SIZE;
	}

	public int[] getBucketValues() {
		return Bucket;
	}

	public void setNumOfElements(int numOfElements) {
		NumOfElements = numOfElements;
	}
	

	
	
	
	
	
	
	
}
