package dataManagement;

public class LinkedBinaryTree { 	
	 
	private BTNode root , node ; 
	private int size  ; 
	
	public LinkedBinaryTree(Object Element) 
	{
		root = new BTNode(Element) ; 
		node = null ; 
		size =  0; 
	}
	
	public void preorder (BTNode tree)  // Binary Tree Traversal
	{
		  if (tree == null) return;
		  else 
		  {				  	 
			  for (int i =0 ; i < tree.getNumOfKeys() ; i++  )
				  if (tree.getKeys().get(i) != 0 )
					  System.out.print(tree.getKeys().get(i) + " ");
		  
			  preorder (tree.getLeft());
			  preorder (tree.getRight());		
		  }		 
	 }
	
	public void preorderWithoutPrint (BTNode tree)  // Binary Tree Traversal , Returns size
	{  
		  if (tree == null) return;
		  else 
		  {		  
			  size ++ ; 		  
			  preorder (tree.getLeft( ) );
			  preorder (tree.getRight( ) );			  
		  }
	 }

	public BTNode getRoot() {
		return root;
	}

	public boolean isRoot ()
	{
		return  (node.getParent() != null) ; 
	}
	
	public boolean isLeaf ()
	{
		return (node.getLeft() == null) & (node.getRight() == null) ;
	}
	
	public boolean isEmpty ()
	{
		return (size == 0) ; 
	}

	public void printElements() 
	{
		if ((this.getRoot().isSpilt()) | (!this.getRoot().isEmpty()))
			this.preorder(this.getRoot());	
	}

	public int getSize() 
	{
		size = 0 ; 
		this.preorderWithoutPrint(root);
		return size;
	}
	
}