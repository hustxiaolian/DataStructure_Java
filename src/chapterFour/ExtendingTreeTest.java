package chapterFour;

public class ExtendingTreeTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Integer[] arr = {8,7,6,5,4,3,2,1};
		ExtendingTree<Integer> t = new ExtendingTree<>(arr);
		
		t.findAndRotate(1);
		
		t.printTree();
		
		
	}

}
