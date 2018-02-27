package chapterThree;

public interface MyIterator<AnyType> {
	boolean hasNext();
	AnyType next();
	void remove();
}
