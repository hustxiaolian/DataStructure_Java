package chapterFour;

public class BinaryTreeTest {

	public static void main(String[] args) {
		Integer[] arr = {3,1,4,6,9,2,5,7};
		BinarySearchTree<Integer> tree = new BinarySearchTree<Integer>(arr);
		
		tree.remove(3);
		tree.printTree();
	}

}
