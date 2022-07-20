import java.util.Random;
import dataManagement.*;

// ������� ������������� , 2014030056

public class MyMain  {

	public static void main(String args[])  
	{		
		   DynamicHashing[] Memory  = new DynamicHashing[3]  ;
		   DynamicHashing[] Disk  = new DynamicHashing[3]  ;
		   final int NumOfKeys = 20 ; 
			
		   System.out.println("=============================================="); 
		   System.out.println("MEMORY : ");
		   System.out.println("=============================================="); 
		
			for (int i = 0 ; i < 3 ; i++)  // �������� , �������� ��� ��������� 20 �������� �� ���� dynamic hashing �� ����� ��� �������� 100,1000 ��� 10000 ������� 	
			{
				System.out.println("\n********************** Dynamic Hashing in Memory (Number of elements = " + (int) java.lang.Math.pow(10,i+2) + ") ****************************\n");
				Memory[i] = new DynamicHashingMemory((int) java.lang.Math.pow(10,i+2)) ;
				Memory[i].setPerformanceCounter(0);
				DHAdd (NumOfKeys,Memory[i]) ;			
				System.out.println("The number of key comparisons after adding " + NumOfKeys + " elements is : " + Memory[i].getPerformanceCounter());
				Memory[i].setPerformanceCounter(0);
				DHSearch (NumOfKeys,Memory[i]) ;
				System.out.println("The number of key comparisons after searching for " + NumOfKeys + " elements is : " + Memory[i].getPerformanceCounter());
				Memory[i].setPerformanceCounter(0);
				DHRemove (NumOfKeys,Memory[i]) ;
				System.out.println("The number of key comparisons after deleting " + NumOfKeys + " elements is : " + Memory[i].getPerformanceCounter());
			}		
			
			System.out.println("\n=============================================="); 
			System.out.println("DISK: "); 
			System.out.println("=============================================="); 
			
			for (int i = 0 ; i < 3 ; i++) // �������� , �������� ��� ��������� 20 �������� �� ���� dynamic hashing �� ����� ��� �������� 100,1000 ��� 10000 ������� 	
			{
				System.out.println("\n********************** Dynamic Hashing in Disk (Number of elements = " + (int) java.lang.Math.pow(10,i+2) + ") ****************************\n");
				Disk[i] = new DynamicHashingDisk((int) java.lang.Math.pow(10,i+2)) ;
				Disk[i].setPerformanceCounter(0);
				DHAdd (NumOfKeys,Disk[i]) ;			
				System.out.println("The number of key comparisons after adding " + NumOfKeys + " elements is : "  + Disk[i].getPerformanceCounter());
				Disk[i].setPerformanceCounter(0);
				DHSearch (NumOfKeys,Disk[i]) ;
				System.out.println("The number of key comparisons after searching for " + NumOfKeys + " elements is : " + Disk[i].getPerformanceCounter());
				Disk[i].setPerformanceCounter(0);
				DHRemove (NumOfKeys,Disk[i]) ;
				System.out.println("The number of key comparisons after deleting " + NumOfKeys + " elements is : " + Disk[i].getPerformanceCounter());
			}		
	} 

	private static int RandomIntGenerator (int min , int max) // ��������� ������� �������	
	{
		return new Random().nextInt((max - min) + 1) + min ;
	}
	
	private static void DHAdd (int NumOfKeys, DynamicHashing dh)
	{
		final int min = 1 , max = (int) java.lang.Math.pow(2,BitDemo.NumOfBits) - 1 ; 
		for (int i = 0 ; i < NumOfKeys ; i++)  // �������� 20 ������� �������	
			dh.addKey(RandomIntGenerator(min,max));
		
		//dh.printAllElements();
	}
	
	private static void DHSearch (int NumOfSearches,DynamicHashing dh)
	{
		final int min = 1 , max = (int) java.lang.Math.pow(2,BitDemo.NumOfBits) - 1 ; 
		for (int i = 0 ; i< NumOfSearches ; i++)	// ��������� 20 ������� �������	
			dh.searchKey(RandomIntGenerator(min,max));
	}
	
	private static void DHRemove (int NumOfKeys,DynamicHashing dh)
	{
		final int min = 1 , max = (int) java.lang.Math.pow(2,BitDemo.NumOfBits) - 1 ; 
		for (int i = 0 ; i< NumOfKeys ; i++)	// �������� 20 ������� �������		
			dh.removeKey(RandomIntGenerator(min,max));			
			
		//dh.printAllElements();
	}
}

