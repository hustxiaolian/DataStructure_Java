package chapterSeven;

import java.util.Arrays;

public class QuickSelect {

	public static void main(String[] args) {
		Integer[]  arr = {3,9,18,15,6,7,8,3,5,2,1,11};
		//Integer[]  arr = {1,1,1,1,1,1,1,1,1,1,1,1,1};
		//int pivotIndex = quickSelectKthElement(arr, 3);
		System.out.println(quickSelectKthElement(arr, 10));
		/*
		for (Integer integer : arr) {
			System.out.print(integer + " ");
		}
		System.out.println("pivot:" + arr[pivotIndex] + ",pivotIndex:" + pivotIndex);
		*/
	}
	
	public static <T extends Comparable<? super T>> T quickSelectKthElement(T[] arr, int kth) {
		return quickSelectKthElement(arr, kth, 0, arr.length - 1);
	}
	
	/**
	 * ����ѡ���ʵ������
	 * @param arr
	 * @param k
	 * @return
	 */
	public static <T extends Comparable<? super T>> T quickSelectKthElement(T[] arr, int i, int left, int right) {
		if(left + 3 <= right)
		{
			int r = partition(arr, left, right);
			int k = r - left + 1;
			if(i ==  k)
				//lucky,ֱ���ҵ���
				return arr[r];
			else {
				if(i < k) {
					return quickSelectKthElement(arr, i, left, r - 1);
				}
				else {
					return quickSelectKthElement(arr, i - k, r + 1, right);
				}
			}
		}
		else {
			Sort.insertionSort(arr, left, right);
			return arr[left + i - 1];
		}
		
	}
	
	/**
	 * ʹ����ֵ�ָ�ķ���ѡȡ��ŦԪ��֮�󣬰�����������ݴ�С��ϵ����С����ŦԪ��Ԫ�ط�����ŦԪ����ߣ�
	 * ������ŦԪ��Ԫ�ط�����ŦԪ���ұߡ�
	 * 
	 * Ҫ�����ֵ�ָ����ֻ��ѡȡ��ŦԪ�����ң���Ϊ��Ҫ���������˱߽磬ʹ��i��j��Զ���ᷢ��Խ�������
	 * @param arr
	 * @param left
	 * @param right
	 * @return
	 */
	public static <T extends Comparable<? super T>> int partition(T[] arr, int left, int right) {
		int i,j;
		
		T pivot = median3(arr, left, right);
		
		i = left;j = right - 1;
		for(;;) {
			//���λ��i��Ԫ��С����ŦԪ���ӹ������ơ����������ڵ�����ŦԪ��Ԫ��ʱ����ͣ��
			//���λ��j��Ԫ�ش�����ŦԪ���ӹ������ơ�������С�ڵ�����ŦԪ��Ԫ��ʱ����ͣ�¡�
			while(arr[++i].compareTo(pivot) < 0) {}
			while(arr[--j].compareTo(pivot) > 0) {}
			//��i��j�����ʱ�򣬾�֪����Ҫ��������Ԫ�أ���֮�����i > j���˳���
			//��ϸ��i == jʱ�����Ҹ�Ԫ�� == ��ŦԪ�����û��Ҫ���һ�٣��ٽ���һ�Ρ�
			if(i < j) {
				arraySwap(arr, i, j);
			}
			else {
				break;
			}
		}
		//����i����ŦԪ��Ϊʲô������i��
		//��Ϊ���������㷨���˳�ѭ��ʱiΪ������ŦԪ������Ԫ����λ������ߵ��Ǹ��������ĸ���߶��Ѿ��������㷨
		//�������ұ�ȥ�ˡ�ͬʱ����ʱ��ŦԪ��λ�þ���i��
		arraySwap(arr, i, right - 1);
		return i;
	}
	
	/**
	 * ��ֵ�ָ
	 * @param arr
	 * @param left
	 * @param right
	 */
	public static <T extends Comparable<? super T>> T median3(T[] arr, int left, int right) {
		int median = (left + right) / 2;
		//����������������ȷ��λ��
		if(arr[left].compareTo(arr[median]) > 0)
			arraySwap(arr, left, median);
		if(arr[left].compareTo(arr[right]) > 0)
			arraySwap(arr, left, right);
		if(arr[median].compareTo(arr[right]) > 0)
			arraySwap(arr, median, right);
		//����ŦԪ���������ָ�����֮��
		arraySwap(arr, median, right - 1); 
		//������ŦԪ��ֵ
		return arr[right - 1];
	}
	
	/**
	 * ������������ָ�������Ԫ��
	 * @param arr ��Ҫ���н�������������
	 * @param i ��������
	 * @param j ��������
	 * @throws java.lang.IndexOutOfBoundsException ��������Խ��
	 */
	private static final <AnyType> void arraySwap(AnyType[] arr,int i,int j) {
		AnyType temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}
}
