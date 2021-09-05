/*Nashita Bhuiyan
 * Node Heap Implementation
 * node based implementation of a heap
 */
import java.util.*;
public class NashitaBhuiyan_NodeHeapImplementation<E extends Comparable<E>> implements PriorityQueue <E>{

	private int numElements = 0;
	private TreeNode root;
	
	//return the smallest element in the heap
	public E peek() {
		
		if(isEmpty())
			throw new NoSuchElementException();
		
		return root.value;
	}

	//determine if the heap is empty
	public boolean isEmpty() {

		if(numElements==0)
			return true;
		
		return false;
	}

	//add new item to the heap
	public void add(E item) {
		
		if(isEmpty()) {
			root = new TreeNode(item, null, null, null);
			numElements++;
			return;
		}
		
		String binaryElements = Integer.toBinaryString(numElements+1);
		TreeNode newRoot = root;
		
		//traverse through heap until end is reached and add new node 
		do {
			
			binaryElements = binaryElements.substring(1);
			int num = Integer.parseInt(binaryElements.substring(0, 1));
			TreeNode parentNode = newRoot;
			
			//examine left child
			if(num==0) 
				newRoot = newRoot.left;
			
			//examine right child
			else 
				newRoot = newRoot.right;
			
			//add new node to heap
			if(binaryElements.length()==1 && num==0) {
				newRoot = new TreeNode(item, null, null, parentNode);
				newRoot.parent = new TreeNode(parentNode.value, newRoot, parentNode.right, parentNode.parent);
			}
			
			else if(binaryElements.length()==1 && num==1) {
				newRoot = new TreeNode(item, null, null, parentNode);
				newRoot.parent = new TreeNode(parentNode.value, parentNode.left, newRoot, parentNode.parent);
			}
		}while(binaryElements.length()>1);
		
		
		//switch the place of the new node with its parents until all parents are smaller than children
		while(newRoot.parent!=null && newRoot.value.compareTo(newRoot.parent.value)<0) {

			E child = newRoot.value;
			E parent = newRoot.parent.value;
			int num;
			
			//determine if the child is a left or right child
			if(newRoot.parent.left.value.equals(child))
				num = 0;
			else
				num = 1;
			
			//switch nodes
			newRoot.value = parent;
			newRoot.parent.value = child;
			TreeNode switching = newRoot;
			newRoot = newRoot.parent;
			
			if(num==0)
				newRoot.left = switching;
			else
				newRoot.right = switching;
		}
		
		//make sure newRoot points to the very first node in the heap
		while(newRoot.parent!=null) {
			
			newRoot = newRoot.parent;
		}
		
		root = newRoot;
		numElements++;	
	}

	//remove the smallest item from the heap 
	public E remove() {
		
		if(isEmpty())
			throw new NoSuchElementException();
		
		//if there is only one element in the heap
		if(numElements==1) {
			
			E toReturn = root.value;
			root = null;
			numElements--;
			return toReturn;
		}
			
		//get the value of the last element in the heap and remove the first element
		String binaryElements = Integer.toBinaryString(numElements);
		TreeNode newRoot = root;
		
		//traverse through heap until end is reached 
		while(binaryElements.length()>1) {
			
			binaryElements = binaryElements.substring(1);
			int num = Integer.parseInt(binaryElements.substring(0, 1));
			TreeNode parentNode = newRoot;
			
			//examine left child
			if(num==0) 
				newRoot = newRoot.left;
			
			//examine right child
			else 
				newRoot = newRoot.right;
		}
		
		E toReturn = newRoot.value;
		
		//put the last element in the heap as the first element in the heap
		if(numElements%2==0)
			newRoot.parent = new TreeNode(toReturn, null, null, newRoot.parent.parent);
		else
			newRoot.parent = new TreeNode(toReturn, newRoot.parent.left, null, newRoot.parent.parent);
		
		numElements--;
		root = new TreeNode(newRoot.value, root.left, root.right, null);
		newRoot = root;
		
		//while there is a left child and the current root is greater than the left or right child
		while(newRoot.left!=null && (newRoot.value.compareTo(newRoot.left.value)>0) || newRoot.value.compareTo(newRoot.right.value)>0){
			
			E parent = newRoot.value;
			
			//swap parent and child to the left
			if(newRoot.right==null || newRoot.left.value.compareTo(newRoot.right.value)<0) {
				
				E child = newRoot.left.value;
				newRoot.value = child; 
				newRoot.left.value = parent;
				TreeNode switching = newRoot;
				newRoot = newRoot.left;
				newRoot.parent = switching;
			}
			
			//swap parent and child to the right
			else {
				
				E child = newRoot.right.value;
				newRoot.value = child;
				newRoot.right.value = parent;
				TreeNode switching = newRoot;
				newRoot = newRoot.right;
				newRoot.parent = switching;
			}
		}
		
		//make sure newRoot is pointing to the first element in the heap
		while(newRoot.parent!=null) {
			
			newRoot = newRoot.parent;
		}

		root = newRoot;
		return toReturn;
	}
	
	public class TreeNode{

		private E value;
		private TreeNode left;
		private TreeNode right;
		private TreeNode parent;

		public TreeNode(E v, TreeNode l, TreeNode r, TreeNode p){
			value = v;
			left = l;
			right = r;
			parent = p;
		}
	}

}
