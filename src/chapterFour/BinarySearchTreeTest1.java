package chapterFour;

public class BinarySearchTreeTest1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Integer[] arr = {5,4,3,6,7,1,9,8,0,2};
		
		BinarySearchTree<Integer> tree = new BinarySearchTree<>(arr);
		tree.printTree();
		
		//tree.remove(1);
		System.out.println("-------------------------------");
		//tree.printTree();
		
		tree.levelOrderPrintTree();
		
		//BinarySearchTree.testTimeRandomTree(100000000);
	}

}
