package dataManagement;

public class DataNode extends IndexNode implements Comparable<Object>  {
	
	private String Word ;
	
	public DataNode (String Word,String File , int bytePosition)
	{		 
		super(File,bytePosition);
		this.Word = Word ; 		
	} 	
	
	public DataNode ()
	{}
	
	public void printNode ()
	{
		System.out.println("\nWord : " + Word + "\nFile : " + getFile() +  "\nByte Position : " + this.getBytePosition());
	}
	
	public String getWord() {
		return Word;
	}
	public void setWord(String word) {
		Word = word;
	}	

	public int compareTo(Object o) {
		
		if (this.getWord().compareTo(((DataNode) o).getWord()) > 0) return 1;
		else if (this.getWord().compareTo(((DataNode) o).getWord()) < 0)	return -1;
		else return 0;
		
	}

	
	
	
}
