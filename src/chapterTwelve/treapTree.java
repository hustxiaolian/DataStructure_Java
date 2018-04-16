package chapterTwelve;

import java.util.Random;

/**
 * 一个treap树就是一个二叉搜索树，但是其节点优先级满足堆序性。任意节点的优先级必须至少和它父节点的优先级一样大。
 * 按照我自己的理解，它就是一个在priority数据域上额外添加信息的最小二叉堆。但是是用树的形式实现，而不是数组。
 * 值得注意的是,标记nullNode节点的优先级为正无穷。
 * 
 * 
 * @author 25040
 *
 * @param <T>
 */
public class treapTree<T extends Comparable<? super T>> {
	
	public static void main(String[] args) {
		treapTree<Integer> t = new treapTree<>();
		
		for(int i = 0;i < 10;++i) {
			t.insert(i);
		}
		
		t.printTree();
		
		t.remove(5);
		
		t.printTree();
		
		System.out.println(t.contain(5));
		
	}
	
	//这里我参考下红黑树的实现
	private TreapNode<T> header;
	private TreapNode<T> nullNode;
	
	/**
	 * 节点形式跟二叉搜索树很像，多了一个优先级的数据域。
	 * 一个节点的优先级是随机指定的。
	 * 
	 * @author 25040
	 *
	 * @param <T>
	 */
	private static class TreapNode<T>{
		//引入随机数种子
		private static Random randomObj = new Random();
		
		//节点形式跟二叉搜索树很像，多了一个优先级的数据域
		private T element;
		private TreapNode<T> left;
		private TreapNode<T> right;
		private int priority;
		
		public TreapNode(T element) {
			this(element,null,null);
		}

		public TreapNode(T element, TreapNode<T> left, TreapNode<T> right) {
			super();
			this.element = element;
			this.left = left;
			this.right = right;
			//随机定义一个优先级
			this.priority = randomObj.nextInt(Integer.MAX_VALUE - 1);
		}
	}
	
	/**
	 * 初始化header节点和null节点，把header节点的优先级置为最低，null置为最大
	 */
	public treapTree() {
		this.nullNode = new TreapNode<T>(null);
		this.nullNode.priority = Integer.MAX_VALUE;
		this.header = new TreapNode<T>(null,nullNode,nullNode);
		this.header.priority = Integer.MIN_VALUE;
		
	}
	/**
	 * treap树的插入例程。
	 * @param x
	 */
	public void insert(T x) {
		insert(x, header);
	}
	
	/**
	 * treap树的删除例程。
	 * 也算是一种从前没有见到过的方式。
	 * 总结来说，二叉搜索树中，我已经学习了普通的二叉树，AVL平衡树，红黑树，以及如下的treap树的插入和删除例程。
	 * 下次总结下。同时B+树，还是不熟悉。
	 * @param x
	 */
	public void remove(T x) {
		remove(x, header);
	}
	
	/**
	 * 二叉搜索树的中序遍历
	 */
	public void printTree() {
		printTree(header.right, 0);
	}
	
	/**
	 * treap树的递归插入例程。
	 * 其主要思想是：
	 * 我们将他加入到树的树叶中，然后向上旋转到它到满足优先级堆序性为止。
	 * 
	 * 其实就是二叉搜索树的老套路。
	 * 
	 * @param x
	 * @param t
	 * @return
	 */
	private TreapNode<T> insert(T x, TreapNode<T> t){
		//当递归到树叶时，创建新的treapNode，并且它的两个儿子都置为nullNode
		//bug 1 header节点的element数据域也是null，因此会出错。
		if(t == nullNode) {
			t = new TreapNode<T>(x, nullNode, nullNode);
			return t;
		}
		
		int compareResult = compare(x, t);
		if(compareResult < 0) {
			t.left = insert(x, t.left);
			//每次递归出来都要检查下是否满足堆序性，也就是当前节点的优先级是否小于等于两个儿子的优先级。
			if(t.priority > t.left.priority) {
				//如果不满足堆序性，我们只需要通过旋转，改变节点之间的关系即可。
				t = simpleRotateWithLeftChild(t);
			}
		}
		else {
			if(compareResult > 0) {
				t.right = insert(x, t.right);
				if(t.priority > t.right.priority) {
					t = simpleRotateWithRightChild(t);
				}
			}
			//如果树中，已经有了重复节点，那么不做任何动作。
		}
		
		return t;
	}
	
	/**
	 * 重新定义下compare方法，使得在header的时候，总是把新节点插入到header节点的右子树中。
	 * @param x
	 * @param t
	 * @return
	 */
	private int compare(T x, TreapNode<T> t) {
		if(t.priority == Integer.MIN_VALUE) {
			return 1;
		}
		else {
			return x.compareTo(t.element);
		}
	}
	
	/**
	 * 左单旋转例程
	 * @param k2
	 * @return
	 */
	private TreapNode<T> simpleRotateWithLeftChild(TreapNode<T> k2) {
		TreapNode<T> k1 = k2.left;
		
		k2.left = k1.right;
		k1.right = k2;
		
		return k1;
	}
	
	/**
	 * 右单旋转例程
	 * @param k1
	 * @return
	 */
	private TreapNode<T> simpleRotateWithRightChild(TreapNode<T> k1) {
		TreapNode<T> k2 = k1.right;
		
		k1.right = k2.left;
		k2.left = k1;
		
		return k2;
	}
	
	
	
	/**
	 * 树的中序遍历的递归私有例程。
	 * 这里为了方便我私有的观察，先打印右子树，后打印左子树。
	 * @param t
	 */
	private void printTree(TreapNode<T> t,int tabNum) {
		if(t.element != null) {
			printTree(t.right, tabNum + 1);
			for(int i = 0;i < tabNum; ++i)
				System.out.print("\t");
			System.out.println(t.element);
			printTree(t.left, tabNum + 1);
		}
	}
	
	/**
	 * 书中描述的思想：
	 * 当找到书中有该节点时，把它的优先级提到正无穷大，然后通过旋转直到树叶后，释放掉。
	 * 
	 * 这里节点二叉堆的思想，但是其形式还是像二叉搜索树删除例程形式，并且它时递归例程。
	 * 
	 * 值得注意的是，在找到欲删除节点后的处理方法上，和以前的方法不太一样。在找到当前的节点后。
	 * 我们可以归一化处理之前的三种情况（两个儿子，一个儿子和没有儿子）
	 * 都直接先比较左旋转或者右旋转一次。
	 * 
	 * 还是分为那三种情况讨论，我们就可以直到，这种方法是有效的。
	 * 1.当t没有儿子时，即它的两个儿子都是nullNode节点，那么我们任意选择左旋转或者右旋转，那么nullNode会替代当前节点的位置。
	 * 	然后我们判断t是否为nullNode，则说明已经递归到树的底层。可以终止了。
	 * 
	 * 2.如果只有一个儿子，由于nullNode的优先级为正无穷大，所以，一定是旋转那条有儿子的边。然后我们在把remove例程递归到当前节点。
	 * 		由此，可以回到情况1处理
	 * 
	 * 3.如果有两个儿子，我们选择两个儿子中优先级更小的一个，使treap树保持堆序性。然后我们把删除例程递归到t。这样可以会到情况1-3的一种。
	 * 最终都会回到情况1.
	 * 
	 * 现在我们觉得递归例程的强大之处，就在于那个return t。自动的处理了好多情况。
	 * 
	 * @param x
	 * @param t
	 */
	private TreapNode<T> remove(T x, TreapNode<T> t) {
		if(t == nullNode) {
			return t;
		}
		
		int compareResult = compare(x, t);
		if(compareResult < 0) {
			t.left = remove(x, t.left);
		}
		else {
			if(compareResult > 0) {
				t.right = remove(x, t.right);
			}
			else {
				//找到了，那好，我们将其的优先级提到正无穷，然后根据其儿子情况的不同。
				if(t.left.priority < t.right.priority) {
					t = simpleRotateWithLeftChild(t);
				}
				else {
					t = simpleRotateWithRightChild(t);
				}
				
				//判断是否已经到了情况1
				if(t == nullNode) {
					//由于是树叶的情况下，两个nullNode节点的优先级都是无强大，相等，因此，它会右旋转。
					t.left = nullNode;
				}
				else {
					//如果是情况2或者情况3，我们在t上递归remove例程，这样它会在自动判断一次路径后，回到这个地方。
					//一切的精髓都在于这个return t和这里的配合。
					t = remove(x, t);
				}
			}
		}
		
		return t;
	}

	public boolean contain(T x) {
		return contain(x, header);
	}
	
	/**
	 * treap树的查找例程。
	 * 
	 * @param x
	 * @param t
	 * @return
	 */
	private boolean contain(T x, TreapNode<T> t) {
		if(t == nullNode) {
			return false;
		}
		
		int compareResult = compare(x, t);
		if(compareResult < 0) {
			return contain(x, t.left);
		}
		else {
			if(compareResult > 0) {
				return contain(x, t.right);
			}
			else {
				return true;
			}
		}
	}
	
}
