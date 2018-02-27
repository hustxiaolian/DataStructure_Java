package chapterFive;

public interface CuckooHashFamily<T> {
	int hash(T element,int which);
	int getNumberOfFunction();
	void generateNewFunction();
}
