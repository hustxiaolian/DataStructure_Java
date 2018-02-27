package chapterSeven;

public class Sort {
	
	
	/**
	 * 按照从小到大的顺序进行插入排序
	 * 时间界限为O（N^2）
	 * @param arr
	 */
	public static <AnyType extends Comparable<? super AnyType>> void insertionSort(AnyType[] arr) {
		int insertionPos;
		
		for(int nowPos = 1;nowPos < arr.length; ++nowPos) {
			AnyType nowItem = arr[nowPos];
			
			for(insertionPos = nowPos - 1;insertionPos >= 0 && arr[insertionPos].compareTo(arr[insertionPos + 1]) > 0;--insertionPos) {
				arr[insertionPos + 1] =  arr[insertionPos];
			}
			
			arr[insertionPos] = nowItem;
		}
	}
}
