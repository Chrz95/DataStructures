import java.io.File;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Vector;

import fileManagement.*;

// Χρήστος Ζαχαριουδάκης , 2014030056 , LAB21129000


public class MyMain {
	
	public static void main(String[] args) throws IOException
	{		
		@SuppressWarnings("resource")
		Scanner user_input = new Scanner( System.in );
		int NumOfFiles=0 ;	
		Vector<String> directories = new Vector<String> () ; 
		String InputDirectory = null , WordToSearch = null ; 
				
		do 
		{
			System.out.print("Please enter the number of files , you wish to add : ");
			try {NumOfFiles = user_input.nextInt();	} 
			catch (InputMismatchException e)
			{
				System.out.println("Error! Not an integer! Run the program again !");
				System.exit (0) ; 
			}
			
			if (NumOfFiles < 1) System.out.println("Please enter a number greater than zero!");
		} while (NumOfFiles < 1);				
		
		System.out.print("Please enter " + NumOfFiles + " directories : " );	
		
		for (int i = 0 ; i < NumOfFiles ; i++) 
		{
				InputDirectory = user_input.next( );	
				File test = new File (InputDirectory);
				if ((test.getName().length() < (FileWriter.IndexRegistrationSize -4)) & (test.length()  != 0) & (test.isFile()))  // Aν το ονομά του είναι μικρότερο απο 22 χαρακτήρες ΚΑΙ δεν είναι άδειο ΚΑΙ είναι directory αρχείου
					directories.add(InputDirectory) ;	
		}		
		
		if (directories.size() == 0) // Δεν εισάχθηκε κανένα έκγυρο directory
		{
			System.out.println("No valid directory was entered!");
			System.exit(0); 
		}
		else 
		{
			FileReader FileInput = new FileReader(directories) ;
			FileInput.FilesRead();
			
			FileWriter FileOutput = new FileWriter(FileInput.getTotalWordsList()) ; 
			FileOutput.writeIndexFile();	
			FileOutput.writeLexiconFile();
			
			while (true)
			{
				System.out.print("\nPlease enter the word , you wish to search for (To exit , enter '|') : ");						
					WordToSearch = new String(user_input.next()) ;				
					if (WordToSearch.equals("|"))
					{							
						System.out.println("You have terminated the software!");
						System.exit (0) ; 
					}					
					if (WordToSearch.length() >= (FileWriter.LexiconRegistrationSize -4))
					{
						System.out.println ("The length of the word is over " + (FileWriter.LexiconRegistrationSize - 6) +  " characters. ") ;
						continue ;
					}
					
					FileSearcher FileSearch = new FileSearcher(WordToSearch,FileOutput.getNumOfLexiconDatapages()) ;					
					FileSearch.findIndexPage (FileSearch.binaryFileSearch(0,FileSearch.getLexiconPages()));		
					System.out.println("\nΠροσβάσεις στον δίσκο :  " + FileSearch.getDiskAccesses() );
					System.out.println("=================================");
			}
		}
		
	}

}
