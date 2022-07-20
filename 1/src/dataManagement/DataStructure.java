package dataManagement;
import java.util.Vector;
import java.util.ArrayList;
import java.util.Collections;

public class DataStructure {
	
	private Object [][] Lexicon ; //1st : String , 2st Integer 
	private ArrayList<Vector<IndexNode>> Index ; //1st : String , 2st Integer 
	
	private Vector<DataNode> ListOfWordsSorted , ListOfWordsUnsorted ; 	 
	private Vector<IndexNode> IndexEntry ;
	private int NumOfLexiconWords , NumOfIndexWords ; 
		 
	@SuppressWarnings("unchecked")
	public DataStructure(Vector<DataNode> ListOfWords)
	{
		this.ListOfWordsSorted = ListOfWords ; 
		this.ListOfWordsUnsorted = (Vector<DataNode>) ListOfWords.clone() ; // Δημιουργείται ένα αντίγραφο της λίστας , το οποίο θα έχει διπλότυπα και δεν θα είναι ταξινομημενο αλφαβητικά
		Index = new ArrayList<Vector<IndexNode>> ();		
		this.writeLexiconMemory();
		this.writeIndexMemory();
		NumOfLexiconWords = ListOfWordsSorted.size();
		NumOfIndexWords = ListOfWordsUnsorted.size() ;
	}
	
	private void writeLexiconMemory () // Δημιουργεί τον array Lexicon
	{		
		this.removeDuplicates() ;	
		Collections.sort(ListOfWordsSorted);		
		Lexicon = new Object[ListOfWordsSorted.size()][2];
		this.VectorToArray(); // Αντίγραφή των στοιχείων του ListOfWordsSorted στο Lexicon
	}	
	
	private void writeIndexMemory() // Δημιουργεί την λίστα Index
	{		
		for (int i = 0 ; i < ListOfWordsSorted.size() ; i++ )
		{
			IndexEntry = new Vector<IndexNode>() ;					
			for (int j = 0 ; j < ListOfWordsUnsorted.size() ; j++)			
				if (ListOfWordsSorted.get(i).getWord().equals(ListOfWordsUnsorted.get(j).getWord()))					
					IndexEntry.add(new IndexNode (ListOfWordsUnsorted.get(j).getFile(),ListOfWordsUnsorted.get(j).getBytePosition()));
			if (!IndexEntry.isEmpty()) Index.add(IndexEntry);
		}
	} 
	
	private void removeDuplicates ()
	{
		for (int i = 0 ; i < ListOfWordsSorted.size()-1 ; i++)
		{
			for (int j = 0 ; j < ListOfWordsSorted.size()-1 ; j++)
				if ((ListOfWordsSorted.get(i).getWord().equals(ListOfWordsSorted.get(j).getWord())) & (i!=j)) 
					ListOfWordsSorted.remove(j) ;	
		}			
	}		
	
	private void VectorToArray ()
	{
		for (int i = 0 ; i<ListOfWordsSorted.size() ; i++ )	
		{
			Lexicon [i][0] = ListOfWordsSorted.get(i).getWord();			
			Lexicon [i][1] = new Integer(i+1); ; // Θέλουμε η αρίθμηση των κόμβων να αρχίζει απο το 1
		}			
	}
	
	public void LexiconPrint ()
	{
		for (int i = 0 ; i<Lexicon.length ; i++ )	
			System.out.println(Lexicon [i][1] + "."+ Lexicon [i][0]);
	}

	public void IndexPrint ()
	{
		for (int i = 0 ; i< Index.size() ; i++ )
		{
			System.out.println("Word : " + i );
			System.out.println(ListOfWordsSorted.get(i).getWord());
			for (int j = 0 ; j < Index.get(i).size() ; j++)			
				System.out.println("(" + Index.get(i).get(j).getFile() +  "," + Index.get(i).get(j).getBytePosition() + ")" );
		}			
	}

	public Object[][] getLexicon() {return Lexicon;}
	public ArrayList<Vector<IndexNode>> getIndex() {return Index;}	
	public int getNumOfLexiconWords() {return NumOfLexiconWords;}	
	public int getNumOfIndexWords()	{return NumOfIndexWords;}

	public Vector<DataNode> getListOfWordsSorted() {return ListOfWordsSorted;}

	public Vector<DataNode> getListOfWordsUnsorted() {	return ListOfWordsUnsorted;
	}
}
