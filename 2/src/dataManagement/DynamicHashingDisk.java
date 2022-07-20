package dataManagement;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Vector;

public class DynamicHashingDisk extends DynamicHashing {
	
	public static final int DataPageSize = 128; // Default Data Page size
	public static final int RegistrationSize = 4 ; // sizeof(int)
	public static final int PAGEMAX = DataPageSize/RegistrationSize ;
	private int NumOfPages = 1; 
	
	public DynamicHashingDisk (int NumOfInitialElements) 
	{	
		new File("ListOfPages").delete() ;// Διαγραφή των αρχείων που δημιουργήθηκαν απο προηγούμενες εκτελέσεις του προγράμματος
		PerformanceCounter++ ; 
		
		for (int i=0 ; i< HASHSIZE ; i++) // Αρχικοποιήση όλων των δεντρων , των οποίων οι ρίζες δείχνουν στις σελίδες 1 εως 100 του αρχείου
			HashTable[i] = new LinkedBinaryTree(new Integer(i+1)) ;			 
				
		NumOfPages = HASHSIZE ; 
				
		for (int i=1 ; i <= NumOfInitialElements ; i++)	// Eισαγωγή των αρχικών κλειδιών	
			addKey(i) ; 	
	}
	
	public void addKey (int key) // Θα διαβάζει την σελίδα , θα την τροποποιει και θα την ξαναγράφει μαζί με τα προηγούμενα δεδομένα
	{		
		node = search(key) ; 	// Επιστροφή του κομβου που πρέπει να τοποθετηθεί το κλειδί με βάση τις δυο hash functions		
		
		try 
		{
			RandomAccessFile BucketList = new RandomAccessFile ("ListOfPages","rw");
			BucketList.seek(0); // Τοποθετεί τον δεικτη στην αρχη του αρχείου
			ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
			DataOutputStream out = new DataOutputStream(bos);		
			
			Vector<Integer> NewData = node.getKeys () ; // Ανάγνωση datapage 				
			NewData.add(new Integer(key)) ;  // Νεα τιμή 
			
			PerformanceCounter++ ; 
			
			// To Vector NewData περιέχει όλα τα στοιχεία της σελίδας και την προηγούμενη τιμή
			
			if (NewData.size() <=  PAGEMAX)
			{	
				// Eγγραφη στην σελίδα
				BucketList.seek((node.getPage()-1)*DataPageSize) ;
				for (int i= 0 ; i < NewData.size() ; i++ )		// Εγγραφή των δεδομένων στην έξοδο
					out.writeInt(NewData.get(i).intValue());
				
				// Εγγραφη απο το buffer στο αρχείο	
			    
				byte [] buffer = bos.toByteArray(); // Creates a newly allocated byte array.
				byte[] DataPage = new byte[DataPageSize];
			    System.arraycopy(buffer, 0, DataPage, 0, buffer.length); // Copy buffer data to DataPage of DataPageSize	  
			   
			    BucketList.write(DataPage);
			    PerformanceCounter++ ; 
			    
			    out.close(); 
		    	bos.close(); 
		    	BucketList.close();		
			}
			else // Split
			{
				Vector<Integer> LeftPage = new Vector<Integer>() ;
				Vector<Integer> RightPage =  new Vector<Integer>() ; 
				
				do
				{ 					
					LeftPage = new Vector<Integer>() ;
					RightPage =  new Vector<Integer>() ; 
								
					if (!node.hasLeft())  node.setLeft(new BTNode(new Integer(node.getPage()))); // Ο αριστερός κόμβος δείχνει στην ίδια σελίδα με πρίν	
					if (!node.hasRight()) node.setRight(new BTNode(new Integer(++NumOfPages)));	 // Ο δεξιός κόμβος δείχνει στην πρώτη σελίδα που δεν έχει εγγραφει	
					
					for (int i = 0 ; i< NewData.size() ; i++)
					{						
						if (NewData.get(i).intValue() != 0) 
						{
							int[] keybits = new BitDemo(NewData.get(i).intValue()).getBinaryNum() ;  // Μετατρέπει τον αριθμό σε δυαδικό ωστε να χρησιμοιποιηθει η hash2
							if (keybits[BitDemo.NumOfBits - (node.getNodeDepth()+1)] == 1) 	LeftPage.add(NewData.get(i));
							else RightPage.add(NewData.get(i));
						}			
					}
					
					node.setElement(new Integer(0)); // O γονέας δεν δείχνει πλέον σε σελίδα			
					
					BTNode startNode = node ; 
					
					if (LeftPage.size() > PAGEMAX)  // Αν η αριστερή είναι γεματη
					{
						node = startNode.getLeft() ;
						NewData = LeftPage ;
					}
					else 
					{
						// Eγγραφη στην αριστερή σελίδα
						BucketList.seek((startNode.getLeft().getPage()-1)*DataPageSize) ;
						bos = new ByteArrayOutputStream() ;
						out = new DataOutputStream(bos);
						for (int i= 0 ; i < LeftPage.size() ; i++ )		// Εγγραφή των δεδομένων στην έξοδο
							out.writeInt(LeftPage.get(i).intValue());					
						
						// Εγγραφη απο το buffer στο αρχείο	
						
						byte [] buffer = bos.toByteArray(); // Creates a newly allocated byte array.
						byte[] DataPage = new byte[DataPageSize];
						System.arraycopy(buffer, 0, DataPage, 0, buffer.length); // Copy buffer data to DataPage of DataPageSize	  
						
						BucketList.write(DataPage);
						PerformanceCounter++ ; 
					}
										 
					if (RightPage.size() > PAGEMAX)  // Αν η δεξια είναι γεματη
					{
						node = startNode.getRight() ; 
						NewData = RightPage ; 
					}
					else
					{
						// Eγγραφη στην δεξία σελίδα
						BucketList.seek((startNode.getRight().getPage()-1)*DataPageSize) ;
						bos = new ByteArrayOutputStream() ;
						out = new DataOutputStream(bos);
						for (int i= 0 ; i < RightPage.size() ; i++ )		// Εγγραφή των δεδομένων στην έξοδο
							out.writeInt(RightPage.get(i).intValue());
						
						// Εγγραφη απο το buffer στο αρχείο	
					    
						byte [] buffer = bos.toByteArray(); // Creates a newly allocated byte array.
						byte[] DataPage = new byte[DataPageSize];
					    System.arraycopy(buffer, 0, DataPage, 0, buffer.length); // Copy buffer data to DataPage of DataPageSize	  
					    try 
					    {
					    	BucketList.write(DataPage);
					    	PerformanceCounter++ ; 					    	
					    } catch (IOException e) {e.printStackTrace();}					    
					}
				} while ((RightPage.size() > PAGEMAX) | (LeftPage.size() > PAGEMAX)) ; // Αν η δεξιά ή η αριστερη σελίδα του κόμβου είναι γεμάτη , split σε αυτή την σελίδα
				
				out.close(); 
		    	bos.close(); 
		    	BucketList.close();				
			}					
		} catch (FileNotFoundException e1) {e1.printStackTrace();} 
		  catch (IOException e) {e.printStackTrace();}		
	}
	
	public void removeKey(int key) 
	{		
		node = search(key);
		if (node.containsKey(key))
		{
			 //System.out.println("The key " + key + " is deleted!");
			try 
			{		
				RandomAccessFile BucketList = new RandomAccessFile ("ListOfPages","rw");
				BucketList.seek(0); // Τοποθετεί τον δεικτη στην αρχη του αρχείου
				ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
				DataOutputStream out = new DataOutputStream(bos);		
				
				// Read
				
				Vector<Integer> PageData = new Vector<Integer>() ; 
				PageData = node.getKeys () ; 
				PerformanceCounter++ ; 
				
				PageData.remove(new Integer(key)) ;// Αφαιρεση του στοιχείου
				
				// Write
			    
				for (int i= 0 ; i < PageData.size() ; i++ )			
					out.writeInt(PageData.get(i).intValue());				
				
				BucketList.seek((node.getPage()-1)*DataPageSize) ;
				
				// Εγγραφη απο το buffer στο αρχείο			
				byte [] buffer = bos.toByteArray(); // Creates a newly allocated byte array.
				byte[] DataPage = new byte[DataPageSize];
			    System.arraycopy(buffer, 0, DataPage, 0, buffer.length); // Copy buffer data to DataPage of DataPageSize	  
			    
			    BucketList.write(DataPage);
			    PerformanceCounter++ ; 
			    out.close(); 
			    bos.close(); 
			    BucketList.close();			
			} 
			catch (FileNotFoundException e1) {e1.printStackTrace();} 
			catch (IOException e) {e.printStackTrace();} 
		}
		// else System.out.println("The key " + key + " is not found!");
	}
	
	public void printPage (int Page) // Εκτυπώνει τις καταχωρήσεις μιας σελίδας
	{			
			RandomAccessFile BucketList;
			int registration ; 
			System.out.println("Page " + Page + " : ");
			try 
			{
				BucketList = new RandomAccessFile ("ListOfPages", "r");
				BucketList.seek (0) ; 
				BucketList.seek ((Page-1)*DataPageSize) ;
				byte[] ReadDataPage = new byte[DataPageSize];
				BucketList.read(ReadDataPage);
				PerformanceCounter++ ; // Ανάγνωση datapage = + 1 πρόσβαση δίσκου
			    ByteArrayInputStream bis= new ByteArrayInputStream(ReadDataPage);
			    DataInputStream ois= new DataInputStream(bis);
			    for (int i=0 ; i<PAGEMAX ; i++)	
			    {
			    	registration = ois.readInt() ;
			    	if (registration != 0 ) System.out.println(registration);   
			    }
			    	 			    
			    bis.close(); 
		    	ois.close(); 
		    	BucketList.close();				    		    
			} catch (IOException e) {e.printStackTrace();}
	}		
		
	public void printAllElements() // Εκτυπώνει όλα τα στοιχεία , ανα δεντρο
	{
		System.out.println("\nThe elements are (preorder) : ");
		for (int i = 0 ; i < HashTable.length ; i++)
		{
			if (HashTable[i] != null)
			{
				if ((HashTable[i].getRoot().isSpilt()) | (!HashTable[i].getRoot().isEmpty()) )
				{
					System.out.print("\nTree " + i + " : ");
					HashTable[i].preorder(HashTable[i].getRoot());
				}
			}
		}
	}
	 
	public void printAllPages() 
	{
		System.out.println("\nThe elements are (preorder) : ");
		for (int i = 1 ; i< NumOfPages ; i++)		
			printPage(i);			
	}
}
