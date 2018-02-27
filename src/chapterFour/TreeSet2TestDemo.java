package chapterFour;

public class TreeSet2TestDemo {

	public static void main(String[] args) {
		MyTreeSet2<Integer> tree = new MyTreeSet2<>();
		
		Integer[] arr = {3,1,4,6,9,2,5,7};
		
		for (int i = 0; i < arr.length; i++) {
			//tree.insert((int)(Math.random() * 100));
			tree.insert(arr[i]);
		}
		
		for(Integer oneItem:tree) {
			System.out.println(oneItem);
		}
		
		tree.remove(3);
		System.out.println("==================");
		for(Integer oneItem:tree) {
			System.out.println(oneItem);
		}
	}

}
