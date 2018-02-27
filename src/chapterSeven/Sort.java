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
	 * ˼·��
	 * 1. ����ʹ�ô��������������˲��������������С������ѡ�
	 * 2. ʹ�ö���ѵ�deleteMin��������������а�����һ�ε����˲����������ж�Σ����Ұѷ���ֵ��������ʹ������ĺ��棬��ʡ�ռ䡣
	 * 
	 */
	public static <AnyType extends Comparable<? super AnyType>> void binaryHeapSort(AnyType[] arr) {
		//ѭ�����˽��������
		for(int i = arr.length / 2;i >= 0;--i) {
			percDown(arr, i, arr.length);
		}
		//ʹ�ö���ѵ�deleteMin����
		for(int i = arr.length - 1;i > 0;--i) {
			arraySwap(arr, 0, i);
			percDown(arr, 0, i);
		}
	}
	
	/**
	 * �������ڽ�������ѹ�����ʹ�õ����˲�����
	 * @param arr
	 * @param hole
	 */
	private static <AnyType extends Comparable<? super AnyType>> void percDown(AnyType[] arr , int hole , int heapLength) {
		int i,child;
		AnyType percDownItem = arr[hole];
		
		for(i = hole; i * 2 < heapLength;i = child) {
			child = i * 2;
			//Ѱ�Ҷ����и�С��
			if(child + 1 < arr.length && arr[child + 1].compareTo(arr[child]) < 0) {
				++child;
			}
			//����ǰ��hole�Ƿ����������Ҫ��
			if(arr[i].compareTo(arr[child]) < 0) {
				break;
			}
			//����hole�������Ŷ���·������
			else {
				arr[i] = arr[child];
			}
		}
		
		arr[i] = percDownItem;
	}
	
	/**
	 * ������������ָ�������Ԫ��
	 * @param arr ��Ҫ���н�������������
	 * @param i ��������
	 * @param j ��������
	 * @throws java.lang.IndexOutOfBoundsException ��������Խ��
	 */
	private static <AnyType> void arraySwap(AnyType[] arr,int i,int j) {
		AnyType temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}
	
	/**
	 * �鲢�����Ǻ����������Ļ�����
	 * �鲢�����ԭ��
	 * 1. �ݹ黮�����롣
	 * 2. �������������ʵ���ϣ����ݹ���뵽��׼���Σ�ֻ��һ��Ԫ��ʱ����
	 * ��������Ҫ���򣬶���������Ԫ�ص������飬����ͨ������һԪ��������ͨ��{@code mergeTwoSortedArray}����
	 * �������
	 * 3. ���֮����Ѿ����������������кϲ�������
	 * 
	 * ͬʱ��ֵ��ע����ǣ�������һ�������۵��Ǻ���Ҫ�����㡣�������Ⱦ�������һ���ȳ���
	 * ����ʱ���飬������������е���ʱ��������
	 */
	@SuppressWarnings("unchecked")
	public static <AnyType extends Comparable<? super AnyType>> void mergeSort(AnyType[] arr) {
		AnyType[] tempArray = (AnyType[]) new Comparable[arr.length];
		
		mergeSort(arr, tempArray, 0, arr.length - 1);
	}
	
	/**
	 * �鲢�����ʵ���������򣬳���˼·�ܼ��������ֱ�����������������������Ȼ��ϲ���
	 * ��left < rightʱ��˵������������������Ԫ�أ������Ҫ����
	 * ��left == rightʱ��˵��������ֻ��һ��Ԫ���ˣ�ɶҲ���øɣ�ֱ���˳���
	 * @param arr
	 * @param temp
	 * @param left
	 * @param right
	 */
	private static <AnyType extends Comparable<? super AnyType>> void 
	mergeSort(AnyType[] arr, AnyType[] temp ,int left, int right) {
		//��left < rightʱ��˵������������������Ԫ�أ������Ҫ����
		//��left == rightʱ��˵��������ֻ��һ��Ԫ���ˣ�ɶҲ���øɣ�ֱ���˳�
		if(left < right) {
			int center = (left + right) / 2;
			mergeSort(arr, temp, left, center);
			mergeSort(arr, temp, center + 1, right);
			mergeTwoSortedArray(arr, temp, left, center + 1, right);
		}
	}
	
	/**
	 * �������Ѿ�������������������кϲ���ֵ��ע����ǣ���������������С����1ʱ�����൱������˻�׼���ε�����
	 * 
	 * @param arr ��Ҫ���й鲢���������
	 * @param tempArray ��ʱ��������
	 * @param leftPos �����������ʼλ��
	 * @param rightPos �����������ʼλ��
	 * @param rightEnd �����������ֹλ��
	 */
	private static <AnyType extends Comparable<? super AnyType>> void 
			mergeTwoSortedArray (AnyType[] arr,AnyType[] tempArray,int leftPos,int rightPos ,int rightEnd) {
		int leftEnd = rightPos - 1;
		int tempPos = leftPos;
		int numElements = rightEnd - leftPos + 1;
		
		while(leftPos <= leftEnd && rightPos <= rightEnd ) {
			if(arr[leftPos].compareTo(arr[rightPos]) < 0) {
				tempArray[tempPos++] = arr[leftPos++];
			}
			else {
				tempArray[tempPos++] = arr[rightPos++];
			}
		}
		
		while(leftPos <= leftEnd) {
			tempArray[tempPos++] = arr[leftPos++];
		}
		
		while(rightPos <= rightEnd) {
			tempArray[tempPos++] = arr[rightPos++];
		}
		
		for(int i = 0;i < numElements;++i,--rightEnd) {
			arr[rightEnd] = tempArray[rightEnd];
		}
	}
	
	/**
	 * ��˵�д��������Ŀ��ţ�
	 * @param arr
	 */
	public static <AnyType extends Comparable<? super AnyType>> void quickSort(AnyType[] arr) {
		
	}
}
