package chapterOne;

public class BoxingDemo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GenericMemory<Integer> m = new GenericMemory<>();
		
		m.wirte(37);
		int val = m.read();
		System.out.println("Value="+val);
	}

}
