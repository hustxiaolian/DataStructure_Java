package chapterSeven;

public class Sort {
	
	
	/**
	 * ���մ�С�����˳����в�������
	 * ʱ�����ΪO��N^2��
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
