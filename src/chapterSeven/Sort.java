package chapterSeven;

public final class Sort {
	
	
	/**
	 * ���մ�С�����˳����в�������
	 * ����������ڣ�������λ��0-i���������Ѿ�����������λ��i+1��Ԫ��ֻ��Ҫ������ǰ�ȶԣ����뵽ǰ����������ʵ�λ�ü���
	 * ʱ�����ΪO��N^2��
	 * @param arr ��Ҫ���������
	 */
	public static <AnyType extends Comparable<? super AnyType>> void insertionSort(AnyType[] arr) {
		int insertionPos;
		
		for(int nowPos = 1;nowPos < arr.length; ++nowPos) {
			//��¼��ǰ��Ҫ��ǰ�����Ԫ��
			AnyType nowItem = arr[nowPos];
			
			//��ǰ�ȶԣ�Ѱ�����ĺ���λ��
			for(insertionPos = nowPos - 1;insertionPos >= 0 && 
					arr[insertionPos].compareTo(arr[insertionPos + 1]) > 0;--insertionPos) {
				
				arr[insertionPos + 1] =  arr[insertionPos];
			}
			//����
			arr[insertionPos] = nowItem;
		}
	}
	
	/**
	 * ʹ��ϣ����������ϣ������
	 * ϣ������ĵĺ�����hk��������֮������������б��ֲ���
	 * ˼·���裺
	 * 1. ���ȱ����н����������ⲿ����һ���������У����һ��ֵ����Ϊ1.
	 * 2. �ٰ��ղ��������˼·������hk����
	 * @param arr ��Ҫ���������
	 */
	public static <AnyType extends Comparable<? super AnyType>> void shellSort(AnyType[] arr) {
		for(int gap = arr.length / 2 ;gap > 0 ;gap /= 2) {
			int j;
			
			//����ϣ��������֮���˼·�Ͳ�����������
			for(int i = gap;i < arr.length; ++i) {
				AnyType nowItem = arr[i];
				
				for(j = i; j > 0 && arr[j].compareTo(arr[j - gap]) < 0; --j) {
					arr[j] = arr[j - gap];
				}
				
				arr[j] = nowItem;
			}
		}
	}
	
	/**
	 * ������
	 * ˼·
	 */
	
}
