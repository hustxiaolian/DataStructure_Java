package chapterEight;

/**
 * 第八章 8.3节不相交集类的基本数据结构
 * @author 25040
 *
 */
public class DisjSets {
	
	/**
	 * 这里采用第一种方法：使用一个数组保存每个元素的等价类的名字
	 */
	private int[] s;
	
	/**
	 * 不相交集类的构造函数，主要是完成数组内存分配，以及将所有的等价类初始化为false，即-1；
	 * @param numElemnets
	 */
	public DisjSets(int numElemnets) {
		this.s = new int[numElemnets];
		for(int i = 0;i < s.length;++i) {
			s[i] = -1;
		}
	}
	
	/**
	 * 合并操作,将两个等价类合并为一个等价类，在数组的具体操作为，将root1 的元素 变为指向root2，以此显示他们之间的关系
	 * 这里的union实际上运算了树的概念来解释会比较好。
	 * {@code s[root1] = root2}这表明了root2代表的树成为了root1的子树，即从两个等价类合并为了一个等价类。
	 * 
	 * 当然，书上说这不是最好的方法。后面知道了，使用灵巧求并方法（按大小和高度求并）避免树长的过于糟糕
	 * @param root1
	 * @param root2
	 */
	public void union(int root1, int root2) {
		this.s[root1] = root2; 
	}
	
	/**
	 * find查找例程。由于-1表示树的根，也就是说，在这里等价类的具体名字为数组下标，find例程是为了寻找等价类的名字，因此，就是要
	 * 寻找s[x] == -1的下标，如果s[x] != -1 即该节点不为根，需要继续递归find。
	 * 记住，我们的问题不要求find操作返回特定的名字，而只是要求当且仅当两个元素属于相同集合时作用在这两个元素上的find返回相同的名字。
	 * @param x
	 * @return
	 */
	public int find(int x) {
		if(s[x] < 0) {
			return x;
		}
		else {
			return find(s[x]);
		}
	}
	
	/**
	 * 注意union方法的前提时，root1和root2并没有关系。add方法需要find提前检查
	 * 灵巧求并算法，可以分为按照树的大小求并以及按照树的高度求并。以此来避免等价类树的情况变得很糟糕，深度非常深。
	 * 在按照树的大小进行求并方法中，由于我们使用了数组来存储等价类，我们利用数组元素来存储对应等价类树大小的负值.
	 * 而在按照树的高度来进行求并方法中，同样，我们使用数组元素来存储对应等价树类的相关信息，具体存储为高度的负值再-1（因为树高为0的树非负）
	 */
	public void unionByTreeHeight(int root1, int root2) {
		if(s[root2] < s[root1]) {
			//root2代表树更高，因此让root1成为root2的子树，并且更新高度信息
			s[root1] = root2;
		}
		else {
			//root1代表的树更高,让root2成为root1的子树，或者两树登高，则需要更新高度信息
			if(s[root1] == s[root2]) {
				s[root2]--;//由于数高的储存形式是负值，因此是--而不是++
			}
			s[root2] = root1;
		}
	}
	
	/**
	 * 按照树的大小进行求并方法中，由于我们使用了数组来存储等价类，我们利用数组元素来存储对应等价类树大小的负值.
	 * @param root1 
	 * @param root2
	 */
	public void unionByTreeSize(int root1, int root2) {
		if(s[root1] < s[root2]) {
			//是则表示root1的树比root2的树更大，所以应该让root2成为root1的儿子
			//翻译成容易懂的话就是root2得daddy is root1;
			s[root2] = root1;
			//更新树大小的信息
			s[root1]--;
		}
		else {
			//同理
			s[root1] = root2;
			s[root2]--;
		}
	}
	
	/**
	 * 将x，y两者建立关系。意味着两个集合的合并
	 * @param x
	 * @param y
	 */
	public void add(int x,int y) {
		//判断两者是否已经建立了关系
		if(findInPathCompresion(x) != findInPathCompresion(y)) {
			unionByTreeSize(findInPathCompresion(x), findInPathCompresion(y));
		}
	}
	
	/**
	 * 路径压缩算法。首先，我们为什么使用路径压缩算法？这是基于这样的观察：
	 * 执行合并操作的任何算法都将产生相同的最坏情形的树，因为它必然会随意打破树之间的平衡。
	 * 路径压缩在findInPathCompresion程序运行期间进行而与用来执行union的方法无关。
	 * 路径压缩产生的效果：从x到根的路径上的每一个节点都使其父节点成为该树的根。
	 * 紧紧的抓住一点：s[x] < 0就是表示根节点，同时表明树高；否则就是指向其父节点，
	 * 这相当于一颗指向倒过来的多叉树，只能从儿子到父亲，而不是传统的父亲到儿子。
	 */
	public int findInPathCompresion(int x) {
		if(s[x] < 0) {
			return x;
		}
		else {
			return s[x] = findInPathCompresion(s[x]);
		}
	}
	
	/**
	 * 便于输出观察
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for(int i = 0;i < this.s.length; ++i) {
			sb.append("\t").append(i);
		}
		sb.append("\n");
		
		for(int i = 0;i < this.s.length; ++i) {
			sb.append("\t").append(s[i]);
		}
		sb.append("\n");
		
		return sb.toString();
	}
}
