package fileManagement;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileSearcher {

	private String WordtoSearch; 
	private int LexiconPages ;
	private int DiskAccesses = 0 ; // k 
	
	public FileSearcher (String WordtoSearch,int LexiconPages) 
	{
		this.WordtoSearch = (WordtoSearch.trim()).toLowerCase() ; // K���� ����� ���� ��� ������ ��������� ��� ���������� �� ����� �������� 
		this.LexiconPages = LexiconPages ;	
	}

	public String getWordtoSearch() {
		return WordtoSearch;
	}
	
	public int binaryFileSearch (int first, int last)  // ������� ��������� ��� ������ lexicon
	{
		int  middle = (last + first)/2  ;  
		String FoundString = null ; 
		int FoundInteger = 0 ;
		 try // Read from Lexicon	        
		 {	
				@SuppressWarnings("resource")
				RandomAccessFile Lexicon = new RandomAccessFile ("Lexicon", "r");
	        	Lexicon.seek (middle*FileWriter.DataPageSize) ; // ������� ��� ������ ������	        	
				byte[] ReadDataPage = new byte[FileWriter.DataPageSize];
				Lexicon.read(ReadDataPage);
				DiskAccesses++ ; // �������� datapage = + 1 �������� ������
				
			    ByteArrayInputStream bis= new ByteArrayInputStream(ReadDataPage);
			    DataInputStream ois= new DataInputStream(bis); 	
			    
			    for (int i = 0 ; i<(FileWriter.DataPageSize/FileWriter.LexiconRegistrationSize) ; i++) // ��������� ���� ��� datapage
			    {
			    	byte bb[] = new byte[FileWriter.LexiconRegistrationSize - 4];	
			        ois.read(bb);
			        String ss = new String(bb);	
			        FoundString = new String (ss.trim());
			        FoundInteger = ois.readInt();
			        if (FoundString.equals(WordtoSearch)) return FoundInteger ;
			     }			    
			    
			    if (first == last - 1 ) {Lexicon.close(); return -1 ; } // A� ��� ������� � ����
		        if (WordtoSearch.compareTo(FoundString) > 0) // ������� ���� ��� ������	(� ���� ��� ��������� ����� ���������� ���������)
		        	return binaryFileSearch (middle,last); 		        	
		        else if (WordtoSearch.compareTo(FoundString) < 0)  // ����� ���� ��� ������	 (� ���� ��� ��������� ����� ���������� ����������)
		        	return binaryFileSearch (first,middle);
			} catch (IOException e) {e.printStackTrace();}		 
		 
		return -1;
		 
	}	
	
	public void setWordtoSearch(String wordtoSearch) {WordtoSearch = wordtoSearch;}
	
	public void findIndexPage (int Page) // �������� ��������� ���� �� ��� ������ ��� ���������� (� �������� �� � ���� ���������� �� ������������ �������)
	{
		if (Page != -1)
		{
			int BytePos, NextPage ; 
			RandomAccessFile Index;
			String sss ; 
			
			try {
				Index = new RandomAccessFile ("Index", "r");
				Index.seek (0) ; 
	        	Index.seek ((Page-1)*FileWriter.DataPageSize) ;
				byte[] ReadDataPage = new byte[FileWriter.DataPageSize];
				Index.read(ReadDataPage);
				DiskAccesses++ ; // �������� datapage = + 1 �������� ������
			    ByteArrayInputStream bis= new ByteArrayInputStream(ReadDataPage);
			    DataInputStream ois= new DataInputStream(bis);				    
			    for (int i=0 ; i<(FileWriter.DataPageSize/FileWriter.IndexRegistrationSize) ; i++)
			    {
			    	byte bb[] = new byte[FileWriter.IndexRegistrationSize - 4];	
			        ois.read(bb);
			        String ss = new String(bb);
			        sss = new String(ss.trim());
			        BytePos = ois.readInt() ;
			        if (sss.length() != 0) System.out.println("�� ������� ' " + sss + " ' �������� ��� ���� ' " + WordtoSearch + " ' ���� ���� " + BytePos + " ."); 
			    }
			    NextPage = ois.readInt() ; 
			    Index.close(); 
			    if (NextPage != 0) findIndexPage (NextPage) ; else return ; 	 // �� � ���� ���������� �� ������������ �������		    
			} catch (IOException e) {e.printStackTrace();}
		}
		else System.out.println ("\nThe word ' " + WordtoSearch +  " ' was not found!") ;
	}
	
	public void printLexiconPage (int Page) 
	{
		 try // Read from Lexicon	
	     {
	        RandomAccessFile Lexicon = new RandomAccessFile ("Lexicon", "r");
	        Lexicon.seek (0) ; 
	        Lexicon.seek ((Page-1)*FileWriter.DataPageSize) ;
			byte[] ReadDataPage = new byte[FileWriter.DataPageSize];
			Lexicon.read(ReadDataPage);
			ByteArrayInputStream bis= new ByteArrayInputStream(ReadDataPage);
			DataInputStream ois= new DataInputStream(bis); 	
			
			for (int i=0 ; i<(FileWriter.DataPageSize/FileWriter.LexiconRegistrationSize) ; i++)
			{
			   byte bb[] = new byte[FileWriter.LexiconRegistrationSize - 4];	
			   ois.read(bb);
			   String ss = new String(bb);			
			   System.out.println("\nstring = " + ss); 	        
			   System.out.println("int  = " + ois.readInt());	 
			}
			Lexicon.close();
		} catch (IOException e) {e.printStackTrace();} 
	}	
	
	public void printIndexPage (int Page)
	{
		 try // Read from Lexicon	
	        {	        	
				RandomAccessFile Index = new RandomAccessFile ("Index", "r");
	        	Index.seek (0) ; 
	        	Index.seek ((Page-1)*FileWriter.DataPageSize) ;
				byte[] ReadDataPage = new byte[FileWriter.DataPageSize];
				Index.read(ReadDataPage);
			    ByteArrayInputStream bis= new ByteArrayInputStream(ReadDataPage);
			    DataInputStream ois= new DataInputStream(bis); 	
			    System.out.println("===============");
			    for (int i=0 ; i<(FileWriter.DataPageSize/FileWriter.IndexRegistrationSize) ; i++)
			    {
			    	byte bb[] = new byte[FileWriter.IndexRegistrationSize - 4];	
			        ois.read(bb);
			        String ss = new String(bb);
			        System.out.println("\nFile = " + ss); 	        
			        System.out.println("BytePosition  = " + ois.readInt());	 
			    }			    
			    System.out.println("===============\nNext Page  = " + ois.readInt());
			    Index.close();
			  
			} catch (IOException e) {e.printStackTrace();} 
		 	
	}

	public int getLexiconPages() {
		return LexiconPages;
	}

	public int getDiskAccesses() {
		return DiskAccesses;
	}
	
}
 