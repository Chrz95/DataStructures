package fileManagement;
import java.io.*;
import dataManagement.*;
import java.util.Vector;

public class FileReader {
	
	private String [] tokens  ;
	private Vector<DataNode> totalWordsList ;  // Ολες οι λέξεις μαζι με διπλότυπες αλλα με μικρά γράμματα
	private Vector<String> directories ;  // Ολες οι λέξεις μαζι με διπλότυπες αλλα με μικρά γράμματα
	
	public FileReader (Vector<String> directories) 
	{	
		totalWordsList = new Vector<DataNode>() ;
		this.directories = directories ;
	}	
	
	public void FilesRead() throws FileNotFoundException ,  IOException
	{	
		for (int j = 0 ; j < directories.size() ; j++)
		{
			File file = new File(directories.get(j));
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line = null;
            int byte_count;             
            int fromIndex;
            int total_byte_count=0;
            while( (line = br.readLine())!= null )
            {      
            		fromIndex=0;
            	 	String [] tokens = line.split("[,|'.:*)(_?!\"\n\t--\\]\\[;{} ]+");
            	 	String line_rest=line;
                    for (int i=1; i <= tokens.length; i++) 
                    {                    	
                    		byte_count = line_rest.indexOf(tokens[i-1]);
                            if ( tokens[i-1].length() != 0) totalWordsList.add(new DataNode (tokens[i-1].toLowerCase(),file.getName(),(total_byte_count + fromIndex)));    
                            fromIndex = fromIndex + byte_count + 1 + tokens[i-1].length();
                            if (fromIndex < line.length())
                              line_rest = line.substring(fromIndex);
                            if (tokens[i-1].length() >= (FileWriter.LexiconRegistrationSize -4) ) totalWordsList.remove(totalWordsList.lastElement())  ; // Αφαίρεση λέξεων μεγαλύτερες απο το επιτρεπόμενο
                    }
                    total_byte_count += fromIndex;
            }
            br.close();
		}            
	}	
	
	public int getNumOfWords ()
	{
		if (tokens != null)	return tokens.length;
		else return 0 ;		
	} 
	
	public void printList ()
	{
		for (int i=0 ; i < totalWordsList.size() ; i++)		
			totalWordsList.get(i).printNode();	
	}

	public Vector<DataNode> getTotalWordsList() {
		return totalWordsList;
	}

	public String[] getTokens() {
		return tokens;
	}

	public Vector<String> getDirectories() {
		return directories;
	}
}
