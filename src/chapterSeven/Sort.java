package chapterSeven;

public final class Sort {
	
	
	/**
	 * @since 2018-3-5 ��һ��review
	 * 
	 * ���մ�С�����˳����в�������
	 * ����������ڣ�������λ��0-i���������Ѿ�����������λ��i+1��Ԫ��ֻ��Ҫ������ǰ�ȶԣ����뵽ǰ����������ʵ�λ�ü��ɡ�
	 * ���մ˹��ɵ������������һ��Ԫ�ؼ��ɡ�
	 * ʱ�����ΪO��N^2��
	 * ʵ�ʲ��ԣ�ʮ�򼶣�20000ms
	 * 
	 * @param arr ��Ҫ���������
	 */
	public static <AnyType extends Comparable<? super AnyType>> void insertionSort(AnyType[] arr) {
		insertionSort(arr, 0, arr.length - 1);
	}
	
	/**
	 * @since 2018-3-5 review1
	 * 
	 * �ֲ�������������̡���������������÷�Χ�涨��left��right֮�䡣
	 * ֵ��ע����ǣ�����left��right��ָ���Ԫ�ض��������������ڡ�
	 * 
	 * ֵ��ע��ĵ㣺
	 * 1. �ڱ�д�����ʱ��һ��Ҫ�������ע��߽�ȷ����ϸ�ڣ���Ҫ�뵱Ȼ��
	 * 2. �ڲ��������ѭ���У����ݴ�λ��p��Ԫ�أ�Ȼ���λ��0-��p-1���еĲ���Ԫ�غ���һλ��Ҳ����arr[i] = arr[i-1];
	 * 		�˴���i��λ��p��λ��1�����ܱ�֤�������������Խ���쳣������ҵ����ʵ�λ�����ٰ�֮ǰ���ݴ�ֵ��ֵ����Ӧ��λ���ϡ�
	 * 
	 * 
	 * @param arr
	 * @param left
	 * @param right
	 */
	private static <AnyType extends Comparable<? super AnyType>> void insertionSort(AnyType[] arr,int left,int right) {
		for(int i = left + 1;i <= right;++i) {
			AnyType temp = arr[i];
			
			int j;
			for( j = i;j > left && temp.compareTo(arr[j - 1]) < 0;--j) {
				arr[j] = arr[j - 1];
			}
			
			arr[j] = temp;
		}
	}
	
	/**
	 * @since 2018-3-5 review1
	 * ʹ��ϣ����������ϣ������
	 * ϣ������ĵĺ�����hk��������֮������������б��ֲ���
	 * hk����ľ��庬��Ϊ(������Ҫ�������С��������)��arr[i + k] >= arr[i]��
	 * 
	 * ˼·���裺
	 * 1. ���ȱ����н����������ⲿ����һ���������У����һ��ֵ��Ҳ�������hk�����ֵ����Ϊ������Ϊ1.
	 * 2. �ٰ��ղ��������˼·������hk����
	 * 
	 * ʵ�ʲ��ԣ�ʮ�򼶣�86ms
	 * @param arr ��Ҫ���������
	 */
	public static <AnyType extends Comparable<? super AnyType>> void shellSort(AnyType[] arr) {
		for(int gap = arr.length / 2 ;gap > 0 ;gap /= 2) {
			int j;
			
			//����ϣ��������֮���˼·�Ͳ�����������
			for(int i = gap;i < arr.length; ++i) {
				AnyType nowItem = arr[i];
				//ע��˴�j�ĵݼ��仯����-1�ˣ�����-gap
				for(j = i; j - gap >= 0 && nowItem.compareTo(arr[j - gap]) < 0; j -= gap) {
					arr[j] = arr[j - gap];
				}
				
				arr[j] = nowItem;
			}
		}
	}
	
	/**
	 * @since 2018-3-5 review1
	 * 
	 * 1.����debug���ԣ�������һ�����ϴ��󣺵�һ��for����ֹ����Ӧ����>0������>=0.
	 * �����˶�����Ǵ�����λ��1��ʼ�ġ���hole = 0�������ˣ�childҲΪ0����ѭ����
	 * 
	 * 2.��Ȼ���֣��һ������˶���ѽṹ����Ҫ���ʣ�����ͬ�������⣬���������1��ʼ��
	 * Ϊ��������Ժͽṹ�ԣ������Ȱ����е�Ԫ�غ���һλ��ʹ�õ�һλ��ЧԪ�ش�λ��1��ʼ��
	 * ���߸����������������е����������¡�������,����û��ͨ�������������ٿ��������������е㽩Ӳ��
	 * 
	 * �����򣬻�δ��ͨ���߳�һֱû�н�������������������ѭ��������������޸Ĳ���ͨ��
	 * ˼·��
	 * 1. ����ʹ�ô��������������˲��������������С������ѡ�
	 * 2. ʹ�ö���ѵ�deleteMin��������������а�����һ�ε����˲����������ж�Σ����Ұѷ���ֵ��������ʹ������ĺ��棬��ʡ�ռ䡣
	 * 
	 * ʵ�ʲ��ԣ�ʮ�򼶣�
	 * 
	 */
	public static <AnyType extends Comparable<? super AnyType>> void binaryHeapSort(AnyType[] arr) {
		//ѭ�����˽��������
		for(int i = arr.length / 2 - 1;i >= 0;--i) {
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
		
		for(i = hole; i * 2 + 1 < heapLength;i = child) {
			child = i * 2 + 1;
			//Ѱ�Ҷ����и���ģ��Խ����������
			if(child + 1 < arr.length && arr[child + 1].compareTo(arr[child]) > 0) {
				++child;
			}
			//����ǰ��hole�Ƿ����������Ҫ��
			if(percDownItem.compareTo(arr[child]) < 0) {
				arr[i] = arr[child];
			}
			//����hole�������Ŷ���·������
			else {
				break;
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
	private static final <AnyType> void arraySwap(AnyType[] arr,int i,int j) {
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
	 * 
	 * ʵ�ʲ��ԣ�ʮ�򼶣�116ms
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
	 * ����������򡣽�����S����Ļ����㷨�����м򵥵��Ĳ���ɣ�
	 * 1. ���S��Ԫ�صĸ���Ϊ0����1����ֱ�ӷ���
	 * 2. ȡS����һԪ��v,��֮Ϊ��ŦԪ��pivot����
	 * 3. ��S����v����Ϊ�������ཻ�ļ��ϣ�S1��С����S2����
	 * 4. ����quickSort(S1)���v���̶�����quickSort(S2)
	 * 
	 * ��������͹鲢������һ�ַ��β��Բ����ĵݹ��㷨����ͬ�ڹ鲢����һ������ݹ�ط�Ϊ������ȵ������֡�
	 * ����������������ѡȡ���ʵ���ŦԪ��������ָ���������ܲ���ȵ��������ཻ���ϣ�����һ�����ϵ�Ԫ�ر���ŦԪ��
	 * ����һ�����ϵ�Ԫ�ض�����ŦԪС��
	 * 
	 * ��������ȹ鲢��������ԭ��
	 * ��������ʵ�λ�õݹ�طָ������������벻���ĸ�Ч�����ָ�Ч�������ܹ��ֲ�������ָ�����Ĳ��㣬��������������������
	 * 
	 * ��ˣ�����ѡ����ŦԪ���ָ������Ե���Ϊ��Ҫ��
	 * ���ִ������ŦԪѡ��ʽ��
	 * 1. ѡ���һ��Ԫ����Ϊ��ŦԪ->����Ԥ���򣨺ܳ������ͷ���ľ�ɵ���ˡ�
	 * 2. ѡ��ǰ��������Ĺؼ����еĽϴ�����Ϊ��ŦԪ->ͬ�ϣ�����Ԥ����ͷ�����൱�����ˣ�ʱ�����ֱ�ӱ���˶��η���
	 * 
	 * һ�ְ�ȫ����Ч�ʽϵ͵ķ�ʽ��
	 * 1. ʹ����������ѡȡ��ŦԪ->����������ɿ���ͦ��ģ��޷������㷨���ಿ�ֵ�����ʱ�䡣
	 * 
	 * �Ƽ��ķָ���ԣ�������ֵ�ָ��ʹ����ˣ��м���Ҷ�����Ԫ�ص���ֵ��Ϊ��ŦԪ��
	 * 
	 * ȷ����ŦԪ�󣬽���������ν������Ԫ�ػ��ֵ���ŦԪ�����ߣ����Ҳ�����������ڴ�ռ�á�
	 * 1.�Ȱ���ŦԪ�ƶ�����������ʹ����ŦԪ�뿪���ָ������
	 * 2.��i = 0��j = arr.length - 2����iָ���Ԫ��С����ŦԪ��i�����ƶ��ӹ���ͬ����jָ���Ԫ�ش�����ŦԪʱ��j�����ƶ���
	 * 3.��֮����iָ���Ԫ�ش�����ŦԪ��iͣ�£���jָ���Ԫ��С����ŦԪ��jͣ�¡�Ȼ�󽻻�i��jָ���Ԫ�ء�
	 * 4.��i > jʱ��ͣ�£�������ʱλ��i����������Ԫ�ء�
	 * 
	 * ǰ��Ĳ��������һ����Ҫ�����⣬����δ�������ŦԪ��ͬ��Ԫ�ء�
	 * ��һ�¼��˵������������鶼����ͬ��Ԫ�ء������i����j������ŦԪ��ʱ��ͣ�£�����������������Ľ������������ǣ��������
	 * ��ͣ�£���ô�ͱ���Ҫָ��һ����ֹi��j����Ĳ��ԡ�ͬʱ�������ָ�����ļ��ϻ���������ǳ�������ļ��ϡ���ˣ�����ѡ��
	 * ʹ��һЩ����Ĳ�����ȡ�Է��յĹ�ܡ�
	 * 
	 * ��󣬵�����N=10���ҵ�С���飬ֱ��ѡ���������õöࡣ
	 * 
	 * ʵ�ʲ��ԣ�46-82ms
	 * @param arr
	 */
	private static final int CUTOFF = 10;//���ٽ��п�������������С
	
	public static <AnyType extends Comparable<? super AnyType>> void quickSort(AnyType[] arr) {
		quickSort(arr, 0, arr.length - 1);
	}
	
	/**
	 * ���ŵ�������
	 * @param arr
	 * @param left
	 * @param right
	 */
	private static <AnyType extends Comparable<? super AnyType>> void quickSort(AnyType[] arr,int left,int right) {
		//û���ضϷ�Χʱ��ֱ�ӿ���
		if(left + CUTOFF <= right) {
			//��ֻ�ָѡ����ŦԪ
			AnyType pivot = median3Split(arr, left, right);
			
			int i = left;
			int j = right - 1;
			while(true) {
				while( arr[++i].compareTo(pivot) < 0) {}
				while( arr[--j].compareTo(pivot) > 0) {}
				
				if(i < j) {
					arraySwap(arr, i, j);
				}
				else {
					break;
				}
			}
			//����ŦԪ��������ȷ��λ��
			arraySwap(arr, i, right - 1);
			//�ݹ�ض������ָ�ļ��Ͻ��п���
			quickSort(arr, left, i - 1);
			quickSort(arr, i + 1, right);
		}
		else {
			//С�������ֱ�ӵ��ò��������ö�
			insertionSort(arr,left,right);
		}
	}
	
	/**
	 * ��ֵ�ָ��㷨.˳���Ѿ���������Ԫ�ص����򡣽�ʡ��һ�����൱��ֻ��Ҫ��left + 1���Լ�right - 1���зָ
	 * ��ˣ�����ǰ�������Ĳ��ԣ����ǿ��Խ���ŦԪ����right - 1��λ���ϣ�������right�ϡ�
	 * ͬʱ������ߺ����ұ�Ҳ���Ϊһ�־����־��i��jһ����Ҫ�����������Խ���������ε�������������൱����㷨
	 * @param arr
	 * @param left
	 * @param right
	 * @return
	 */
	private static <AnyType extends Comparable<? super AnyType>> AnyType median3Split(AnyType[] arr,int left,int right) {
		
		int center = (left + right) / 2;
		
		if(arr[left].compareTo(arr[center]) > 0) {
			arraySwap(arr, left, center);
		}
		if(arr[left].compareTo(arr[right]) > 0) {
			arraySwap(arr, left, right);
		}
		if(arr[center].compareTo(arr[right]) > 0) {
			arraySwap(arr, center, right);
		}
		//
		arraySwap(arr, center, right - 1);
		return arr[right - 1];
	}
}
