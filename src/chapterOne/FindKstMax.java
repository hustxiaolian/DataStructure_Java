package chapterOne;

import java.util.Arrays;
/**
 * 
 * @author 25040
 *chapter 1-1
 */
public class FindKstMax {
	
	public static void main(String[] args) {
		int[] a = {3,4,1,9,8,3,5};
		System.out.println(FindKstMax.solution2(a, a.length/2));
		System.out.println(Arrays.toString(a));
	}

	
	/**
	 * ð�ݷ�����ȫ��������ٷ��ص�k��
	 * ʱ�������N^2
	 * @param arr ���ô��ݣ���ı����������
	 * @return k st integer
	 */
	public static int solution1(int[] arr,int k) {
		//ð�ݷ����Ѵ����������
		FindKstMax.maoPaoSort(arr);
		return arr[k-1];
	}
	/**
	 * ð������
	 * @param arr ��Ҫ����ð�����������
	 */
	
	public static void maoPaoSort(int[] arr) {
		for(int i =0;i < arr.length;++i) {
			for(int j=0;j < arr.length - i - 1;++j) {
				if(arr[j] < arr[j+1]) {
					int temp = arr[j];
					arr[j] = arr[j+1];
					arr[j+1] = temp;
				}
			}
		}
	}
	
	/**
	 * ����һ��size = K�����飬Ȼ�������ɵݼ�����Ȼ�����α��������Ԫ�أ������ǵ���������ĺ��ʵ�λ��
	 * ʱ�����ΪN*K���ռ临�Ӷ�ΪK
	 * ����û�иı�ԭ����
	 */
	public static int solution2(int[] arr,int k) {
		int[] arrK = new int[k];
		for(int i = 0;i < k;++i)
			arrK[i] = arr[i];
		//�����ɵ���������
		FindKstMax.maoPaoSort(arrK);
		System.out.println(Arrays.toString(arrK));
		//Ȼ��ѭ������
		for(int i = k;i < arr.length;++i) {
			//��������Ԫ�ر������ڵ���С���������һ������,����ʹ���˶�����㷨�еļ���
			int j = k - 1;
			for(;j > 0 && arrK[j] < arr[i];--j) {
				if(arrK[j-1] > arr[i])
					break;
				else
					arrK[j-1] = arrK[j];
			}
			arrK[j] = arr[i];
		}
		
		return arrK[k-1];
		
	}

}
