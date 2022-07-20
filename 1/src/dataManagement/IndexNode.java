package dataManagement;

public class IndexNode { // Κάθε κόμβος θα περιέχει ένα ζεύγάρι (File,bytePosition)

	private String File ; 
	private int bytePosition ;
	
	public IndexNode (String File , int bytePosition)
	{
		this.File =  File ; 
		this.bytePosition = bytePosition ;  
	} 	
	
	public IndexNode ()
	{}
	
	public void printNode ()
	{
		System.out.println("\nFile : " + File +  "\nByte Position : " + bytePosition);
	}
	public String getFile() {
		return File;
	}
	public void setFile(String file) {
		File = file;
	}
	public int getBytePosition() {
		return bytePosition;
	}
	public void setBytePosition(int bytePosition) {
		this.bytePosition = bytePosition;
	}	
}
	

