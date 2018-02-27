package chapterOne;

public class GenericMemory<T> {
	
	private T storedValue;
	
	public T read() {
		return storedValue;
	}
	
	public void wirte(T x) {
		storedValue = x;
	}
}

