package chapterSeven;

public final class Sort {
	
	
	/**
	 * @since 2018-3-5 第一次review
	 * 
	 * 按照从小到大的顺序进行插入排序
	 * 插入排序基于，在数组位置0-i的子数组已经完成排序，因此位置i+1的元素只需要依次向前比对，插入到前面子数组合适的位置即可。
	 * 按照此规律迭代到数组最后一个元素即可。
	 * 时间界限为O（N^2）
	 * 实际测试：十万级：20000ms
	 * 
	 * @param arr 需要排序的数组
	 */
	public static <AnyType extends Comparable<? super AnyType>> void insertionSort(AnyType[] arr) {
		insertionSort(arr, 0, arr.length - 1);
	}
	
	/**
	 * @since 2018-3-5 review1
	 * 
	 * 局部插入排序的例程。将插入排序的作用范围规定在left和right之间。
	 * 值得注意的是，这里left和right所指向的元素都包括排序数组内。
	 * 
	 * 值得注意的点：
	 * 1. 在编写代码的时候，一定要想清楚，注意边界等方面的细节，不要想当然。
	 * 2. 在插入排序的循环中，先暂存位置p的元素，然后把位置0-（p-1）中的部分元素后移一位，也就是arr[i] = arr[i-1];
	 * 		此处的i从位置p到位置1，才能保证不出现数组访问越界异常。最后，找到合适的位置了再把之前的暂存值赋值到相应的位置上。
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
	 * 使用希尔增量进行希尔排序
	 * 希尔排序的的核心在hk排序性在之后的增量排序中保持不变
	 * hk排序的具体含义为(假设需要将数组从小到大排序)：arr[i + k] >= arr[i]。
	 * 
	 * 思路步骤：
	 * 1. 首先必须有建立，或者外部传入一个增量序列，其第一个值（也就是最后hk排序的值必须为）必须为1.
	 * 2. 再按照插入排序的思路，进行hk排序。
	 * 
	 * 实际测试：十万级：86ms
	 * @param arr 需要排序的数组
	 */
	public static <AnyType extends Comparable<? super AnyType>> void shellSort(AnyType[] arr) {
		for(int gap = arr.length / 2 ;gap > 0 ;gap /= 2) {
			int j;
			
			//有了希尔增量后，之后的思路和插入排序类似
			for(int i = gap;i < arr.length; ++i) {
				AnyType nowItem = arr[i];
				//注意此处j的递减变化不是-1了，而是-gap
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
	 * 1.经过debug测试，发现了一个智障错误：第一次for的终止条件应该是>0而不是>=0.
	 * 忘记了二叉堆是从数组位置1开始的。当hole = 0进行下滤，child也为0，死循环。
	 * 
	 * 2.再然后发现，我还忽略了二叉堆结构的重要性质，还是同样的问题，必须从索引1开始。
	 * 为满足堆序性和结构性，必须先把所有的元素后移一位，使得第一位有效元素从位置1开始。
	 * 或者跟书上那样，把所有的索引调整下。我试下,还是没调通，晕死，下午再看看，现在脑子有点僵硬。
	 * 
	 * 堆排序，还未调通，线程一直没有结束，估计是陷入了死循环。经过上面的修改测试通过
	 * 思路：
	 * 1. 首先使用传入的数组进行下滤操作，建立最大（最小）二叉堆。
	 * 2. 使用二叉堆的deleteMin操作（这个操作中包含又一次的下滤操作），进行多次，并且把返回值放入二叉堆使用数组的后面，节省空间。
	 * 
	 * 实际测试：十万级：
	 * 
	 */
	public static <AnyType extends Comparable<? super AnyType>> void binaryHeapSort(AnyType[] arr) {
		//循环下滤建立二叉堆
		for(int i = arr.length / 2 - 1;i >= 0;--i) {
			percDown(arr, i, arr.length);
		}
		//使用二叉堆的deleteMin操作
		for(int i = arr.length - 1;i > 0;--i) {
			arraySwap(arr, 0, i);
			percDown(arr, 0, i);
		}
	}
	
	/**
	 * 堆排序在建立二叉堆过程中使用的下滤操作。
	 * @param arr
	 * @param hole
	 */
	private static <AnyType extends Comparable<? super AnyType>> void percDown(AnyType[] arr , int hole , int heapLength) {
		int i,child;
		AnyType percDownItem = arr[hole];
		
		for(i = hole; i * 2 + 1 < heapLength;i = child) {
			child = i * 2 + 1;
			//寻找儿子中更大的，以建立最大二叉堆
			if(child + 1 < arr.length && arr[child + 1].compareTo(arr[child]) > 0) {
				++child;
			}
			//看当前的hole是否满足堆序性要求
			if(percDownItem.compareTo(arr[child]) < 0) {
				arr[i] = arr[child];
			}
			//否则将hole继续沿着儿子路径下滤
			else {
				break;
			}
		}
		arr[i] = percDownItem;
	}
	
	/**
	 * 交换数组两个指定坐标的元素
	 * @param arr 需要进行交换操作的数组
	 * @param i 数组坐标
	 * @param j 数组坐标
	 * @throws java.lang.IndexOutOfBoundsException 传入坐标越界
	 */
	private static final <AnyType> void arraySwap(AnyType[] arr,int i,int j) {
		AnyType temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}
	
	/**
	 * 归并排序，是后面快速排序的基础。
	 * 归并排序的原理：
	 * 1. 递归划分两半。
	 * 2. 对两半进行排序。实际上，当递归进入到基准情形（只有一个元素时），
	 * 根本不需要排序，而具有两个元素的子数组，可以通过两个一元素子数组通过{@code mergeTwoSortedArray}方法
	 * 完成排序。
	 * 3. 完成之后对已经排序过的子数组进行合并操作。
	 * 
	 * 同时，值得注意的是，这里有一个不起眼但是很重要的亮点。就是事先就声明了一个等长度
	 * 的暂时数组，用作排序过程中的临时缓冲区，
	 * 
	 * 实际测试：十万级：116ms
	 */
	@SuppressWarnings("unchecked")
	public static <AnyType extends Comparable<? super AnyType>> void mergeSort(AnyType[] arr) {
		AnyType[] tempArray = (AnyType[]) new Comparable[arr.length];
		
		mergeSort(arr, tempArray, 0, arr.length - 1);
	}
	
	/**
	 * 归并排序的实际运作程序，程序思路很简单清晰。分别对左右两个子数组进行排序，然后合并。
	 * 当left < right时，说明子数组至少有两个元素，因此需要排序。
	 * 当left == right时，说明子数组只有一个元素了，啥也不用干，直接退出。
	 * @param arr
	 * @param temp
	 * @param left
	 * @param right
	 */
	private static <AnyType extends Comparable<? super AnyType>> void 
	mergeSort(AnyType[] arr, AnyType[] temp ,int left, int right) {
		//当left < right时，说明子数组至少有两个元素，因此需要排序
		//当left == right时，说明子数组只有一个元素了，啥也不用干，直接退出
		if(left < right) {
			int center = (left + right) / 2;
			mergeSort(arr, temp, left, center);
			mergeSort(arr, temp, center + 1, right);
			mergeTwoSortedArray(arr, temp, left, center + 1, right);
		}
	}
	
	/**
	 * 将两个已经完成排序的相邻数组进行合并。值得注意的是，当传入的子数组大小都是1时，就相当于完成了基准情形的排序。
	 * 
	 * @param arr 需要进行归并排序的数组
	 * @param tempArray 暂时缓冲数组
	 * @param leftPos 左子数组的起始位置
	 * @param rightPos 右子数组的起始位置
	 * @param rightEnd 右子数组的终止位置
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
	 * 经典快速排序。将数组S排序的基本算法由下列简单的四步组成：
	 * 1. 如果S中元素的个数为0或者1，则直接返回
	 * 2. 取S中任一元素v,称之为枢纽元（pivot）。
	 * 3. 将S按照v划分为两个不相交的集合：S1（小）和S2（大）
	 * 4. 返回quickSort(S1)后跟v，继而返回quickSort(S2)
	 * 
	 * 快速排序和归并排序都是一种分治策略产生的递归算法。不同于归并排序将一个数组递归地分为几乎相等的两部分。
	 * 快速排序在数组中选取合适的枢纽元，将数组分割成两个可能不想等的两个不相交集合，其中一个集合的元素比枢纽元大。
	 * 另外一个集合的元素都比枢纽元小。
	 * 
	 * 快速排序比归并排序更快的原因：
	 * 在数组合适的位置递归地分割数组会产生意想不到的高效，这种高效不仅仅能够弥补不均衡分割产生的不足，还能在性能有所超出。
	 * 
	 * 因此，合理选择枢纽元，分割数组显得尤为重要。
	 * 几种错误的枢纽元选择方式：
	 * 1. 选择第一个元素作为枢纽元->碰到预排序（很常见）和反序的就傻逼了。
	 * 2. 选择前两个互异的关键字中的较大者作为枢纽元->同上，碰上预排序和反序就相当尴尬了，时间界限直接变成了二次方。
	 * 
	 * 一种安全但是效率较低的方式：
	 * 1. 使用随机数随机选取枢纽元->随机数的生成开销挺大的，无法降低算法其余部分的运行时间。
	 * 
	 * 推荐的分割策略：三数中值分割法，使用左端，中间和右端三个元素的中值作为枢纽元。
	 * 
	 * 确定枢纽元后，接下俩的如何将数组的元素划分到枢纽元的两边，并且不产生额外的内存占用。
	 * 1.先把枢纽元移动到数组的最后。使得枢纽元离开待分割的区域。
	 * 2.令i = 0，j = arr.length - 2。当i指向的元素小于枢纽元，i向右移动掠过，同理，当j指向的元素大于枢纽元时，j向左移动。
	 * 3.反之，当i指向的元素大于枢纽元，i停下，当j指向的元素小于枢纽元，j停下。然后交换i，j指向的元素。
	 * 4.当i > j时，停下，交换此时位置i和数组最后的元素。
	 * 
	 * 前面的步骤忽略了一个重要的问题，即如何处理与枢纽元相同的元素。
	 * 想一下极端的情况，如果数组都是相同的元素。如果当i或者j等于枢纽元的时候停下，则会产生看起来多余的交换操作。但是，如果我们
	 * 不停下，那么就必须要指定一个防止i，j出界的策略。同时，这样分割产生的集合会产生两个非常不均衡的集合。因此，我们选择
	 * 使用一些额外的操作换取对风险的规避。
	 * 
	 * 最后，当处理N=10左右的小数组，直接选择插入排序好得多。
	 * 
	 * 实际测试：46-82ms
	 * @param arr
	 */
	private static final int CUTOFF = 10;//不再进行快速排序的数组大小
	
	public static <AnyType extends Comparable<? super AnyType>> void quickSort(AnyType[] arr) {
		quickSort(arr, 0, arr.length - 1);
	}
	
	/**
	 * 快排的主例程
	 * @param arr
	 * @param left
	 * @param right
	 */
	private static <AnyType extends Comparable<? super AnyType>> void quickSort(AnyType[] arr,int left,int right) {
		//没到截断范围时，直接快排
		if(left + CUTOFF <= right) {
			//三只分割法选择枢纽元
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
			//把枢纽元交换会正确的位置
			arraySwap(arr, i, right - 1);
			//递归地对两个分割的集合进行快排
			quickSort(arr, left, i - 1);
			quickSort(arr, i + 1, right);
		}
		else {
			//小数组情况直接调用插入排序快得多
			insertionSort(arr,left,right);
		}
	}
	
	/**
	 * 三值分割算法.顺便已经完成了这个元素的排序。节省了一步。相当于只需要对left + 1，以及right - 1进行分割。
	 * 因此，按照前面描述的策略，我们可以将枢纽元放在right - 1的位置上，而不是right上。
	 * 同时，最左边和左右边也会成为一种警戒标志，i和j一定不要出现数组访问越界这种尴尬的情况，这真是相当妙的算法
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
