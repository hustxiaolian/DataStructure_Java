package chapterThree;

public class MyLinkedListCrossAndTest {

	public static void main(String[] args) {
		Integer[] a = {1,2,3,4,5};
		Integer[] b = {2,4,6,7};
		Integer[] c = {};
		
		MyLinkedList<Integer> lst1 = new MyLinkedList<Integer>(a);
		MyLinkedList<Integer> lst2 = new MyLinkedList<Integer>(b);
		MyLinkedList<Integer> lst3 = new MyLinkedList<Integer>(c);
		
		System.out.println(lst1);
		System.out.println(lst2);
		System.out.println(lst3);
		
		lst3.removeFirst();
		
	}
	
	public static MyLinkedList<Integer>crossWithSortedNumberList(MyLinkedList<Integer> lst1,
			MyLinkedList<Integer> lst2) {
		
		MyLinkedList<Integer> cross = new MyLinkedList<Integer>();

		
		return cross;
	}

}
