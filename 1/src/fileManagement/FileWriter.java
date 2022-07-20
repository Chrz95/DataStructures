package fileManagement;
import java.util.Vector;
import dataManagement.*;
import java.io.*;

public class FileWriter {

	public static final int DataPageSize = 128; // Default Data Page size	
	public static final int LexiconRegistrationSize = 21; // String : 17 , Integer : 4
	public static final int IndexRegistrationSize = 26; // String : 22 , Integer : 4
	private int NumOfSortedWords , NumOfLexiconDatapages,NumOfIndexDatapages, cnt , BeginLoop ;  
	private int[] LexiconNumbers ; 
	
	private DataStructure DatStr ;
	private ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
	private DataOutputStream out = new DataOutputStream(bos);
	private RandomAccessFile MyFile ; 
	
	public FileWriter (Vector<DataNode> ListOfWords) throws IOException
	{	
		DatStr = new DataStructure(ListOfWords) ;  // �� ���� ��� ������ �������������� �� lexicon ��� �� index ���� ��� Constructor ��� DataStructure
		NumOfSortedWords = DatStr.getNumOfLexiconWords() ;		 
		LexiconNumbers = new int[NumOfSortedWords] ;
		
		File f = new File("Lexicon"); // �������� ��� ������� ��� �������������� ��� ������������ ���������� ��� ������������
		f.delete();
		File f1 = new File("Index");
		f1.delete();
	}
	
	public void writeLexiconFile ()
	{
		cnt = 0 ; 
		try 
		{
			MyFile = new RandomAccessFile ("Lexicon","rw");
			MyFile.seek(0); // ��������� ��� ������ ���� ���� ��� �������
		} catch (FileNotFoundException e1) {e1.printStackTrace();} 
		  catch (IOException e) {e.printStackTrace();}  							 
		
		while (cnt< NumOfSortedWords)
		{			
			bos = new ByteArrayOutputStream() ;
			DataOutputStream out = new DataOutputStream(bos);
			
			for (int i=0 ; (i< (DataPageSize/LexiconRegistrationSize)) & (cnt<NumOfSortedWords) ; i++) // Filling and emptying the buffer , �� �������� ������ �� ����� < 128 bytes �� ���� datapage
			{							 
				String s = (String) DatStr.getLexicon()[cnt][0] ;
				byte dst[] = new byte[LexiconRegistrationSize - 4]; // ������� String = ������� ����������� - ������� ��������
			    byte src[] = s.getBytes();
			    System.arraycopy(src, 0, dst, 1, src.length);
			    try 
			    {
					out.write(dst, 0,LexiconRegistrationSize - 4); // Write fixed size string
					out.writeInt(LexiconNumbers[cnt++]); // Write integer (4 bytes)
			    } catch (IOException e) {e.printStackTrace();}  
			 }			
			
			// ������� ��� �� buffer ��� ������			
			byte [] buffer = bos.toByteArray(); // Creates a newly allocated byte array.
		    byte[] DataPage = new byte[DataPageSize];
		    System.arraycopy(buffer, 0, DataPage, 0, buffer.length); // Copy buffer data to DataPage of DataPageSize	  
		    try {MyFile.write(DataPage);} catch (IOException e) {e.printStackTrace();}	
		    NumOfLexiconDatapages++;
		}		
		try {out.close(); bos.close(); MyFile.close();}
		catch (IOException e) {e.printStackTrace();}		
	}	
	
	public void writeIndexFile()
	{	
		NumOfIndexDatapages = 1 ; // ������� � �������� ��� ������� �� ������� ��� �� 1
		cnt = 0 ; 
		BeginLoop = 0 ; 
		
		try 
		{
			MyFile = new RandomAccessFile ("Index","rw");
			MyFile.seek(0); // ��������� ��� ������ ���� ���� ��� �������
		} catch (FileNotFoundException e1) {e1.printStackTrace();} 
		  catch (IOException e) {e.printStackTrace();}  							 
		
		while (cnt < NumOfSortedWords) // ������� ������ = ������� ������������� ������
		{			
			bos = new ByteArrayOutputStream() ;
			DataOutputStream out = new DataOutputStream(bos);	 	
			int CurrentNode = cnt ; 
			int addition = BeginLoop ;			
			
			if (BeginLoop == 0 ) // ������� ������� ���� ������� ��� ����
				LexiconNumbers[cnt] = NumOfIndexDatapages ;	 // �� ������� ��� �� ����� ����� ���� ���������� ���� ��� ������ ��� �������
			
			for (int i=BeginLoop ; (i< ((DataPageSize/IndexRegistrationSize) + addition)) & (i < DatStr.getIndex().get(cnt).size()) ; i++) // Filling and emptying the buffer , a datapage
			{									 
					BeginLoop++ ;
					String s = DatStr.getIndex().get(cnt).get(i).getFile();					
					byte dst[] = new byte[IndexRegistrationSize - 4];
			        byte src[] = s.getBytes();
			        System.arraycopy(src, 0, dst, 1, src.length);
			        try 
			        {
						out.write(dst, 0, IndexRegistrationSize - 4);
						out.writeInt(DatStr.getIndex().get(cnt).get(i).getBytePosition()) ;	
					} 
			        catch (IOException e) {e.printStackTrace();}  // Write fixed size string 	
			 }
			
			NumOfIndexDatapages++ ; 			
			
			/* E������� ������� ��� ����� ��� ������� ��� ������� �� ���� ������ � 0 �� ��� ���������� */
						
			if ((BeginLoop ==  DatStr.getIndex().get(CurrentNode).size()) & (DatStr.getIndex().get(cnt).size() > ((DataPageSize/IndexRegistrationSize))))
			{
				BeginLoop = 0 ; // ������ ������ ������ / �����
				cnt++ ; 
				try {out.writeInt(0) ;}	catch (IOException e) {	e.printStackTrace();}	
			}		
			else if ((BeginLoop !=  DatStr.getIndex().get(CurrentNode).size()) & (DatStr.getIndex().get(cnt).size() > ((DataPageSize/IndexRegistrationSize))))
			{
				try {out.writeInt(NumOfIndexDatapages);} // Write number at the end of datapage IF needed else write 0
				catch (IOException e) {	e.printStackTrace();} 
			}
			
			try
			{
				if (DatStr.getIndex().get(CurrentNode).size() <= ((DataPageSize/IndexRegistrationSize))) 
				{									
					BeginLoop = 0 ;  // ������ ������ ������ / �����
					cnt ++ ; 					
					out.writeInt(0) ;	// ���� ���� �� �������� ��� ����� ������ �� ��� datapage , ��� ���������� ���� ������ ��� ���� ��� ����
				}				
			} catch (IOException e){e.printStackTrace();}			
			
			// ������� ��� ������		
			
			byte [] buffer = bos.toByteArray(); // Creates a newly allocated byte array.
			byte[] DataPage = new byte[DataPageSize];
			System.arraycopy( buffer, 0, DataPage, 0, buffer.length); // Copy buffer data to DataPage of DataPageSize	  
			try {MyFile.write(DataPage);} catch (IOException e) {e.printStackTrace();}	
		}	
		
		try {out.close(); bos.close(); MyFile.close();} catch (IOException e) {e.printStackTrace();}		
	}			

	public int getNumOfLexiconDatapages() {
		return NumOfLexiconDatapages;
	}
	
	public int getNumOfIndexDatapages() {
		return NumOfIndexDatapages;
	}

	public int getNumOfSortedWords() {
		return NumOfSortedWords;
	}

	public void setNumOfSortedWords(int numOfSortedWords) {
		NumOfSortedWords = numOfSortedWords;
	}

	public int[] getLexiconNumbers() {
		return LexiconNumbers;
	}

	public DataStructure getDatStr() {
		return DatStr;
	}

	public void setNumOfLexiconDatapages(int numOfLexiconDatapages) {
		NumOfLexiconDatapages = numOfLexiconDatapages;
	}

	public void setNumOfIndexDatapages(int numOfIndexDatapages) {
		NumOfIndexDatapages = numOfIndexDatapages;
	}
}
