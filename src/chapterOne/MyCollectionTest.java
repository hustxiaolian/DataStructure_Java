package chapterOne;

public class MyCollectionTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Object[] a = {1,2,3,4,5,6};
		MyCollection mm = new MyCollection(a);
		System.out.println(mm);
		System.out.println(mm.isPresent(7));
		mm.add(7);
		System.out.println(mm);
		mm.insert(10, 8);
		System.out.println(mm);
	}

}
