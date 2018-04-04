package chapterEight;

import java.util.HashMap;

/**
 * 泛型的不相交集类。
 * 1.可以用来复习下不相交的相关知识。
 * 2.在后面求解最小生成树的相关问题时，可以复用这部分代码
 * 
 * @version <1> 
 * 我有些想法:
 * 1. 使用hashMap<T, Integer> + int数组来组实不相交集类。
 * 2. 直接使用hashMap<T, T>来组织。
 * 3. 使用T[]+int[] 来组织.使用索引下标来建立泛型数据与编号之间的联系。
 *    好处是：空间和时间复杂度都能控制得比较好，但是基本不支持动态得插入和删除。
 *    
 * 考虑在实际使用不相交集类得时候，往往是已知了全部元素，动态插入得频率低得吓人。
 * 我选择先用第三种想法，同时，也算是review下过去得代码
 * 
 * 改进型想法，我再使用一个HashMap<T,Integer>来弥补T[]不能良好的支持T->int的映射需求，每次都要遍历寻找。
 * 如果数组的数目比较大，这个线性时间就搞得我很蛋疼，这里算是直接用空间换时间。
 * 
 * 
 * 
 * @author 25040
 *
 * @param <T>
 */
public class DisjSets<T> {
	
	private T[] indexToObj;
	private int[] s;
	private HashMap<T, Integer> objToindex;

	/**
	 * 泛型不相交集类的构造函数。
	 * 其工作职能很简单就是分配内存空间，然后初始化两个数组的变量。
	 * 在s数组中，使用-1来表示该元素为根；用>0的数组来表达等价关系；用<-1的数字来表达当前等价树的大小
	 * 在indexToObj数组中，在构造阶段就建立起数组下标和元素之间的关系。
	 * 
	 * @param size 
	 */
	@SuppressWarnings("unchecked")
	public DisjSets(int size, T[] allElements) {
		//分布内存空间
		this.indexToObj = (T[]) new Object[size];
		this.s = new int[size];
		this.objToindex = new HashMap<>();
		
		//初始化各变量
		//看了深入理解计算机系统的后遗症，想到了必须写出局部性良好的代码。不知道这点c和java是不是一样的？
		for(int i = 0; i < size;++i) {
			//从减少瞎几把内存引用的角度来说，应该是设置一个临时变量来存储allElements[i]
			//也算是养成这种习惯吧，尽量减少不必要的内存引用和函数调用
			T temp = allElements[i];
			this.indexToObj[i] = temp;
			this.objToindex.put(temp, i);
		}
		
		for(int i = 0;i < size;++i) {
			this.s[i] = -1;
		}
		
	}
	
	/**
	 * union两个集合。
	 * 
	 * 思路和步骤都非常简单：
	 * 1. 从map中获取元素对应的编号
	 * 2. 然后执行按照树大小灵巧求并算法。注意是负数的大小判断问题。
	 * 
	 * @param ele1
	 * @param ele2
	 */
	public void union(T ele1, T ele2) {
		int num1 = this.objToindex.get(ele1);
		int num2 = this.objToindex.get(ele2);
		
		//如果元素1的树更大，即1<2,那我们让元素2的嫁接到元素1的树上
		if(s[num1] < s[num2]) {
			//元素1的树更大
			s[num2] = num1;
			s[num1]--;
		}
		else {
			//元素2的树更大
			s[num1] = num2;
			s[num2]--;
		}
	}
	
	
	/**
	 * 
	 * 该find查找例程的核心思想就是我们不要在乎它返回的数字，而是要求两个等价的元素应该返回同样的数字。
	 * 
	 * 
	 * @param ele1
	 * @return 该等价树的根节点
	 */
	public int find(T ele) {
		int num1 = this.objToindex.get(ele);
		return findInPathCompresion(num1);
	}
	
	/**
	 * 私有的递归find例程。
	 * 非路径压缩的递归例程。
	 * @param num1
	 * @return
	 */
	@SuppressWarnings("unused")
	private int find(int num1) {
		if(s[num1] < 0) {
			return num1;
		}
		else {
			return find(s[num1]);
		}
	} 
	
	/**
	 * 路径压缩算法。其核心思想和上面的find例程差不多。
	 * 但是该算法多了一步。
	 * 仔细思考下，递归一层就相当于将s[num1]=s[num2]=s[num2]= root
	 * 这样，将递归的过程中，把路径上所有的节点的父类引用都直接指向了根节点。
	 * 关键是，它与按树大小灵巧求并算法完美兼容。
	 * @return
	 */
	private int findInPathCompresion(int num1) {
		if(s[num1] < 0) {
			return num1;
		}
		else {
			//关键而风骚的一步
			return s[num1] = findInPathCompresion(s[num1]);
		}
	}
	
	/**
	 * 判断两个元素是否等价。也就是它们是否在一个集合中。
	 * @param ele1 
	 * @param ele2
	 */
	public boolean inSameSet(T ele1, T ele2) {
		int num1 = objToindex.get(ele1);
		int num2 = objToindex.get(ele2);
		
		return findInPathCompresion(num1) == findInPathCompresion(num2);
	}
}
