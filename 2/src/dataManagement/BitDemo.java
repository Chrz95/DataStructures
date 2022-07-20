package dataManagement;

public class BitDemo {
	
	private int[] BinaryNum  ;
	private int value ;
	public static final int NumOfBits = 14; 
	
	public BitDemo (int value)
	{
		this.value = value ; 		
		BinaryNum = new int[NumOfBits] ;
		int bitmask = 1;  // Mask starts with the binary value 00000001		
		
		for (int j=0; j<NumOfBits; j++) // Check j last bits of integer value starting from the least significant bit
		{ 
		   if ((value & bitmask) == 0) BinaryNum[j] = 0 ; 	
		   else BinaryNum[j] = 1 ; 	
		   bitmask = bitmask << 1; // prepare bitmask to check next bit
		}
	}
	
	public void printBinNum ()
	{
		System.out.print(value  + " = " );
		int j = NumOfBits - 1  ;
		while (j>=0) System.out.print(BinaryNum[j--]);
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int[] getBinaryNum() {
		return BinaryNum;
	}
    
}
