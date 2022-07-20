package dataManagement;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Vector;

public class BTNode 
{
	private Object element ; 
	private BTNode left , right , parent ; 
	private int PerfCnt ; 
	
	public BTNode (Object element , BTNode left ,BTNode right ,BTNode parent) // Constructor 1
	{ 
		setElement (element) ; // ��� �� object ����� ��� HashBucket � ���� �������� ��� �� ������� �� ��� datapage
		setParent (parent) ; 
		setLeft (left) ;
		setRight (right) ;
	}
	
	public BTNode (Object Bucket) // Constructor 3
	{ 	
		setLeft (null) ;
		setRight (null) ;
		setElement (Bucket) ;
	}
	
	public boolean hasLeft ()
	{
		return  (this.getLeft() != null) ; 
	}
	
	public int addValue (int value) // �������� ��� ��� ��������� addkey ��� dynamicHashingMemory
	{				
		if  (this.isFull()) splitNode (value);	// Split					
		else this.getHashBucket().addValue(value);
		return PerfCnt ; // ������� ��� ������ �������� ��� ���� ����� ��� key ���� ��� �������� ��������
	}
	
	private void splitNode (int value)
	{
		BTNode node = this  ;	
		int [] keybits  ;
	
		do
		{
			Vector<Integer> Data = node.getKeys() ; // ������� ������� ��� ��������� ��� ������ �� ��� �������� ������ (Vector)
			
			if (!node.hasLeft()) node.setLeft(new BTNode(new HashBucket()));						
			if(!node.hasRight()) node.setRight(new BTNode(new HashBucket()));
			
			PerfCnt++ ;
			for (int i=0 ; i< Data.size() ; i++) // ������ �� �������� �� ���������� ������� ����� ��� ������� 
			{		
				if (Data.get(i) != 0) 
				{
					keybits = new BitDemo(Data.get(i)).getBinaryNum() ; // ���������� ��� ������ �� ������� ���� �� ��������������� � hash2					
					if (keybits[BitDemo.NumOfBits - (node.getNodeDepth()+1)] == 1) node.getLeft().getHashBucket().addValue(Data.get(i));
					else node.getRight().getHashBucket().addValue(Data.get(i)) ; 		
				}			
			}
			
			node.setElement(new HashBucket()); // O ������ ��� ����������� ��� �������� ����� �������� (��� ����� �����)
			
			// �������� ��� ���� �����
			keybits = new BitDemo(value).getBinaryNum() ; // ���������� ��� ������ �� ������� ���� �� ��������������� � hash2 	
			
			PerfCnt++ ; 
			
			if (keybits[BitDemo.NumOfBits - (node.getNodeDepth()+1)] == 1) // ���������� ��� ���� ����� ����� � �������� 
			{	
				if (!node.getLeft().isFull()) node.getLeft().getHashBucket().addValue(value);					
				else node = node.getLeft() ;				
			}
			else if (keybits[BitDemo.NumOfBits - (node.getNodeDepth()+1)] == 0)
			{ 
				if (!node.getRight().isFull()) node.getRight().getHashBucket().addValue(value);				
				else node = node.getRight() ;
			}		
		} while  (node.isFull() ) ;	// ������� ��� ��� ��������� ��� �� ������� ��� ������������ ������ ��� �� ��� ������ ������ �� ����� ���� ���� ���� ����� , ��� ���� split
	}
	
	public boolean hasRight ()
	{
		return  (this.getRight() != null) ; 
	}	
	
	public void setElement(Object element) {
		this.element = element;
	}

	public BTNode getLeft() {
		return left;
	}

	public void setLeft(BTNode left) {
		if (left!=null) left.parent = this ; 
		this.left = left;
	}

	public BTNode getRight() {
		return right;
	}

	public void setRight(BTNode right) {
		if (right!=null) right.parent = this ; 
		this.right = right;
	}

	public BTNode getParent() {
		return parent;
	}

	public void setParent(BTNode parent) {
		this.parent = parent;
	}
	
	public boolean isLeaf() // Returns true if this is a leaf node
	{ return (!this.hasLeft()) & (!this.hasRight()); }

	public boolean isSpilt() 
	{
		return (!isLeaf()) ;
	}

	public int getNodeDepth() // ��������� ��� � ���� ���� ����� 0 , ������� ������ ��������� ���� ���� ������
	{
		int cnt = 0 ; 
		BTNode find = this ; 
		while (find.getParent()!=null)
		{
			find = find.getParent() ; 
			cnt++ ; 
		} 
		return cnt;		
	}
	
	public boolean isFull ()
	{
		if (TypeOfElement ()) return this.getKeys().size() == HashBucket.SIZE;
		else return this.getKeys().size() == DynamicHashingDisk.PAGEMAX ;
	}
	
	public boolean isEmpty ()
	{
		if (TypeOfElement ()) 
		{
			if (element !=null)	return this.getKeys().size() == 0;
			else return true ; 
		}			
		else return this.getKeys().size() == 0 ;		
	}
	
	public BTNode getSibling() {
		
		if (getParent() != null)
		{
			if  (getParent().getLeft().equals(this)) 
				if (getParent().hasRight()) return getParent().getRight();
			else if (getParent().getRight().equals(this))
				if (getParent().hasLeft()) return getParent().getLeft();
		}
		return null ;
	}
	
	public boolean containsKey  (int key) // 
	{
		Vector<Integer> Data = this.getKeys() ;
		if (TypeOfElement ())
		{			
			for (int i=0 ; i< Data.size() ; i++)
				if (Data.get(i) == key) return true ; 			
			
			return false ; 
		}
		else
		{		
			if (Data.contains(new Integer(key))) return true ;
			return false;
		}		
	}
	
	public Vector<Integer> getKeys () // ���������� �� ��� Vector �� ������� ��� �������� �� Bucket � � ������ ��� ������� � ������
	{		
		if (!TypeOfElement ())   
		{
			Vector<Integer> PageData = new Vector<Integer>() ; 
			if (getPage() != 0 ) 
			{
				int Page =  getPage();				
				try 
				{
					RandomAccessFile BucketList = new RandomAccessFile ("ListOfPages","r");
					BucketList.seek(0); // ��������� ��� ������ ���� ���� ��� �������
					BucketList.seek((Page-1)*DynamicHashingDisk.DataPageSize) ;
					byte[] ReadDataPage = new byte[DynamicHashingDisk.DataPageSize];
					BucketList.read(ReadDataPage);
				    ByteArrayInputStream bis= new ByteArrayInputStream(ReadDataPage);
				    DataInputStream ois= new DataInputStream(bis);
					
					// Read
					int ReadInt;			
					for (int i= 0 ; i < DynamicHashingDisk.PAGEMAX ; i++ )
					{
						ReadInt = ois.readInt();
						if (ReadInt != 0 ) PageData.add(new Integer(ReadInt)) ;
					}
					 
			    	BucketList.close();					   
				} 
				catch (FileNotFoundException e1) {e1.printStackTrace();} 
				catch (IOException e) {e.printStackTrace();}
				return PageData; 
			}		
			else return PageData ; 
		}
		else 
		{
			Vector<Integer> Data = new Vector<Integer>() ;
			for (int i=0 ; i < this.getHashBucket().getBucketValues().length ; i++)
				if (this.getHashBucket().getBucketValues()[i] !=0)
					Data.add(new Integer(this.getHashBucket().getBucketValues()[i]));
			
			return Data ;
		}
	}	
	
	public int getNumOfKeys ()
	{
		return this.getKeys().size() ;
	}
	
	public void setNumOfKeys (int numOfElements)
	{
		if (TypeOfElement ()) this.getHashBucket().setNumOfElements(numOfElements);
		else System.out.println("Not appropriate function!");
	}
	
	public boolean TypeOfElement () // True if hashBucket , false : integer . ��� ����� ��� �������� ����������� ���� �� ����������� ��� ������ ����� hashbucket ���� ��������
	{
		if (this.element.getClass().getName().equals("dataManagement.HashBucket")) return true; 
		else return false ; // java.lang.Integer
	}
	
	public int getPage () 
	{
		if (!TypeOfElement ()) return ((Integer)this.element).intValue() ;
		else 
		{
			System.out.println("Use getBucket() function!");
			return 0 ; 
		}
	}
	
	public HashBucket getHashBucket () // True if hashBucket , false : integer
	{
		if (TypeOfElement ()) return ((HashBucket)this.element) ;
		else 
		{
			System.out.println("Use getPage() function!");
			return null ; 
		}
	}

	public Object getElement() {
		return element;
	}
}
