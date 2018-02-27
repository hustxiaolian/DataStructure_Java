package chapterThree;

public class MyDoubleStack<AnyType> {
	
	@SuppressWarnings("unused")
	private int capacity;
	@SuppressWarnings("unused")
	private int topOfStack1;
	@SuppressWarnings("unused")
	private int topOfStack2;
	@SuppressWarnings("unused")
	private AnyType[] theItems;
	
	@SuppressWarnings("unchecked")
	public MyDoubleStack(int capacity) {
		this.capacity = capacity;
		this.theItems = (AnyType[]) new Object[capacity];
		
		topOfStack1 = 0;
		topOfStack2 = capacity;
	}
}
