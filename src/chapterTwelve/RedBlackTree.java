package chapterTwelve;

/**
 * 根据MIT算法导论和数据结构一书，自己编写红黑树的例程。
 * 
 * 红黑树的红黑性：
 * 1. 每个节点的色域不是黑色就是红色。
 * 2. 根节点和所有的树叶都是黑色。
 * 3. 每个红色节点的父节点都是黑色的
 * 4. 从节点x出发向下直到树叶的所有路径，经过的黑色节点的个数都是相同的。黑色节点的个数定义为x的黑高度。
 * 
 * 正是上面这四条性质红黑树是一个平衡树，其期望高度为logn，而是当发生插入和删除后，新树的红黑性容易恢复。
 * 
 * 关于树高的证明：
 * 1. 使用数学归纳法，证明含有n个节点的红黑树的高度<=2log(n+1);
 * 2. merge each red node into its black parent.由此，可以构成一个2-3-4树。
 * 
 * 2-3-4树的一些性质：
 * 1. each internal node has 2-4 children and every leafs has same depth = black height(root)
 * 
 * 首先，我们知道在二叉搜索树中树叶的个数 = n + 1;
 * 而2-3-4树中，高度为h的树，其树叶节点数目为：2^h1 <= n <= 4^h1
 * =>2^h1 <= n + 1  => h1 <= log(n+1);
 * 最后根据红黑性质3，一条路径中，红色节点的个数最多占据路径总节点数目的一半
 * h <= 2 * h1 <= 2log(n+1);
 * 证明完毕。
 * 
 * @version <1> 2018-3-18 16:38
 * 根据自己的理解完成了自顶向下的插入例程。在随后，将完成MIT课程描述的自底向上的插入例程。
 * 按照我的理解，这两种例程是异曲同工之妙。
 * 自顶向下的插入例程精髓和核心思想在于，一定保证当红黑树迭代到树叶时，插入节点的uncle节点不为红色。
 * 这样就直接进入了自底向上例程的case2或者case3，而在自底向上例程中case2或者case3的出现，意味着即将完成上色和旋转的修复工作。
 * 也就是说，自顶向下例程提前做了case1的工作。
 * 
 * 而保证插入节点的uncle不为红色，那么一定只需要执行一次case2或者（case2 + case3）就能完成修复，恢复红黑性。
 * 自顶向下的例程，在树比较大，比较深的时候有利于节省空间，因为自底向上的例程需要通过栈或者其他结构保存从上到下的路径。
 * 
 * 然后这一版插入例程根树上的不太一样，我感觉树上的例程虽然很精妙，真的是佩服。但是其可读性比较大（可能是我太弱了）。
 * 因此，按照我自己的理解，也就是分成8种情况，来写，看起来很冗长，但是我觉得我意思很明确，枚举了所有的路径可能性，
 * 提供相应的旋转和重上色步骤。当然，后续我还是精简一些明显冗余的代码，
 * 
 * @version <2> 2018-3-18 17:05
 * 精简了部分功能和形式冗余的代码。
 * 
 * @author 25040
 *
 * @param <T>
 */
public class RedBlackTree<T extends Comparable<? super T>> {
	
	public static void main(String[] args) {
		RedBlackTree<Integer> t = new RedBlackTree<>();
		Integer[] arr = {10,85,15,70,20,60,30,50,65,80,90,40,5,55,20};
		for(int i = 0;i < arr.length;++i) {
			t.insert(arr[i]);
			t.printTree();
		}
		
	}
	
	private static final int BLACK = 1;
	private static final int RED = 0;
	
	
	private Node<T> header;
	private Node<T> nullNode;
	
	
	/**
	 * 红黑树的节点类对象
	 * 相比于普通的二叉搜索树，只是多了一个色域，非黑既红。
	 * @author 25040
	 *
	 * @param <T>
	 */
	private static class Node<T>{
		private T element;
		private Node<T> left;
		private Node<T> right;
		private int color;
		
		public Node(T element, Node<T> left, Node<T> right, int color) {
			super();
			this.element = element;
			this.left = left;
			this.right = right;
			this.color = color;
		}
	}
	
	/**
	 * 构造红黑树
	 */
	public RedBlackTree() {
		this.nullNode = new Node<>(null,null,null,BLACK);
		this.nullNode.left = this.nullNode.right = nullNode;
		this.header = new Node<>(null,null,null,BLACK);
		this.header.left = this.header.right = nullNode;
	}
	
	
	
	
	private Node<T> current;
	private Node<T> father;
	private Node<T> grand;
	private Node<T> great;
	
	/**
	 * nullNode节点模拟null，每次插入开始，nullNode的element置为x
	 * header节点，为了根节点发生旋转后返回和编程上的方便性。并且定义他的element值为负无穷大。
	 * 所以，整棵红黑树都挂在header的右边。compare函数，定义了当比较节点为header，永远返回1。
	 * 
	 * 自顶向下的插入例程。
	 * 其核心思想和步骤是：
	 * 1. 使用上面四个私有类成员来记录向下搜索迭代的路径。
	 *    分别记录了当前节点，父节点，祖父节点和曾祖父节点。一开始四个变量都在header上，
	 * 2. 从header节点递归向下，通过compare判断路径。同时记录父链路径。最后每次循环判断当前节点的两个儿子是否为红色。是则处理下。
	 * 3. 循环直到compare返回0，也就是达到null节点或者在红黑树种已经找到了它
	 * @param x
	 */
	public void insert(T x) {
		current = father = grand = header;
		nullNode.element = x;//用来判断是否迭代到了叶节点。
		
		//向下迭代，发现如果有节点具有两个红色儿子，则执行一些操作。
		while(compare(x, current) != 0) {
			//保存迭代向下的路径
			great = grand;
			grand = father;
			father = current;
			//找到合适的向下方向。由于一开始在header上，所以这样，由于compare(x,header)=1,所以整棵红黑树都在header的右儿子
			if(compare(x, current) < 0)
				current = current.left;
			else
				current = current.right;
			
			//检查当前节点两个儿子是否都是红色的。
			if(current.left.color == RED && current.right.color == RED) {
				handleReorient(x);
			}
		}
		nullNode.element = null;
		//判断是否迭代到了null节点。
		if(current.element != null) {
			return;
		}
		//生成新节点并且插入到指定位置。
		current = new Node<>(x,nullNode,nullNode,RED);
		if(compare(x, father) < 0) {
			father.left = current;
		}
		else {
			father.right = current;
		}
		handleReorient(x);
		
	}
	
	/**
	 * 当向下迭代的过程中，如果发现某个儿子都是红色，我们就需要进行一波操作
	 * 
	 * 具体步骤为：
	 * 1. 将current节点的两个儿子置为黑色，current置为红色，这样的操作并不会改变红黑树的性质4，
	 *     但是当current的父节点也就是father也是红色的，就会破坏性质3。
	 *     所以，当father为红色时，执行步骤2.否则退出函数。
	 *     
	 * 2. 当father为红色时，破坏了红黑性，需要修复。也就是进行适当的旋转和重新上色。分为以下几种情况
	 *    PS:x为current，P为father, G为grand, E为great.
	 *    当x,p,g为左一字形。simple left rotate(g),然后将其G置为红色，P置为黑色
	 *    当x,p,g为左之字形（即路径形状为先左后右），double left rotate(g)然后g置为红色，x置为黑色。然后x引用改为P.children;
	 *    当x,p,g为右之字形（即路径形状为先右后左），double right rotate(g)然后g置为红色，x置为黑色，然后x引用改为P.children.
	 *    当x,p,g为右一字形，simple left rotate(g)然后g置为红色，P置为黑色。
	 * @param x
	 */
	private void handleReorient(T x) {
		//步骤1
		current.left.color = current.right.color = BLACK;
		current.color = RED;
		
		//判断
		if(father.color == RED) {
			//步骤2
			grand.color = RED;
			current = rotate(x, great);
			current.color = BLACK;
		}
		//将根节点置为黑色
		header.right.color = BLACK;
		
	}
	
	/**
	 * @version 1
	 * 根据great,grand,father和great生成的路径来判断怎么样来做那种旋转方式。
	 * 由于x,p,g变换后的子树必须重新和great正确连接。great又有两个儿子，因此，共计8种情况。
	 * 
	 * 所有路径均从great开始。注意到所有的双旋转都是由单旋转构成的。
	 * 我的天，这太可怕了，虽然代码意义很明确，但是在这么多的条件下，确实很容易写错。
	 * 
	 * @version 2
	 * 精简可以合并的代码
	 * 
	 * 
	 * @param x
	 * @param great2
	 * @return
	 */
	private Node<T> rotate(T x, Node<T> parent) {
		//great左下（↙）
		if(compare(x, parent) < 0) {
			//grand左下(↙)
			if(compare(x, parent.left) < 0) {
				//father右下(↘)
				if(compare(x, parent.left.left) > 0){
					//路径为右 + 左一字形。情况2，将其转换为情况1，然后复用情况1的代码，完成双旋转
					parent.left.left = simpleRotateWithRightChild(parent.left.left);
				}
				//father右下(↘)，路径为左 + 左一字形。情况1.
				parent.left = simpleRotateWithLeftChild(parent.left);
			}
			//grand右下（↘）
			else {
				//father左下（↙）
				if(compare(x, parent.left.right) < 0) {
					//路径为左 + 右之字形.情况3，将其转换为情况4
					parent.left.right = simpleRotateWithLeftChild(parent.left.right);
				}
				//father右下（↘）//路径为左 + 右一字形，情况4
				parent.left = simpleRotateWithRightChild(parent.left);
			}
			//实际上都是返回了great的左儿子，也就是旋转之后最上层的节点
			return parent.left;
		}
		//great右下（↘）.情况完全跟上面镜像。
		else {
			//grand左下(↙)
			if(compare(x, parent.right) < 0) {
				//father左下(↙)
				if(compare(x, parent.right.left) > 0){
					//路径为右 + 左之字形。情况2.将其转换为情况1
					parent.right.left = simpleRotateWithRightChild(parent.right.left);
				}
				//路径为右 + 左一字形。情况1，
				parent.right = simpleRotateWithLeftChild(parent.right);
			}
			//grand右下（↘）
			else {
				//father左下（↙）
				if(compare(x, parent.right.right) < 0) {
					//路径为右 + 右之字形.情况3，将其转换为情况4
					parent.right.right = simpleRotateWithLeftChild(parent.right.right);
				}
				//father右下（↘）
				//路径为右 + 右一字形，情况4
				parent.right = simpleRotateWithRightChild(parent.right);
			}
			//实际上都是返回了great的右儿子，也就是旋转之后最上层的节点
			return parent.right;
		}
	}

	
	/**
	 * 节点的右单旋转。
	 * @param left
	 * @return
	 */
	private Node<T> simpleRotateWithRightChild(Node<T> k1) {
		Node<T> k2 = k1.right;
		
		k1.right = k2.left;
		k2.left = k1;
		
		return k2;
	}

	/**
	 * 节点左单旋转。
	 * @param left
	 * @return
	 */
	private Node<T> simpleRotateWithLeftChild(Node<T> k2) {
		Node<T> k1 = k2.left;
		
		k2.left = k1.right;
		k1.right = k2;
		
		return k1;
	}

	/**
	 * 比较x和节点的值大小，如果t为根节点，那么x永远比较大。
	 * @param x
	 * @param t
	 * @return
	 */
	private final int compare(T x, Node<T> t) {
		if(t == header) {
			return 1;
		}
		else {
			return x.compareTo(t.element);
		}
	}
	
	/**
	 * 打印红黑树
	 */
	public void printTree() {
		if(isEmpty()) {
			System.out.println("impty tree");
		}
		else {
			System.out.println("------------------");
			printTree(header.right, 1);
		}
	}
	
	/**
	 * 递归中序打印红黑树的信息
	 * @param right
	 */
	private void printTree(Node<T> t,int tabNum) {
		if(t.element != null) {
			printTree(t.left, tabNum + 1);
			for(int i = 0;i < tabNum;++i){
				System.out.print("\t");
			}
			System.out.println(t.element + (t.color == 1? "(B)":"(R)"));
			printTree(t.right, tabNum + 1);
		}
		
	}

	/**
	 * 判断红黑树是否为空
	 * @return
	 */
	private boolean isEmpty() {
		return header.right.equals(nullNode);
	}
	
	
	/**
	 * 插入例程。根据算法导论课程学习而来的。
	 * 
	 * 其核心思想就是：
	 * 1. 使用普通二叉搜索树的insert
	 * 2. 将插入的节点色域置为red
	 * 3. 这个时候大概率会破坏红黑树的红黑性。（性质3）
	 * 
	 * x为当前节点，p[x]为其父节点，因此p[p[x]]为其爷爷节点，left[p[p[x]]]为x的爷爷节点的左儿子
	 * 
	 * 其伪代码：
	 * RB-insert(T,x):
	 *   Tree-insert(T,x);
	 *   color[x] <- RED;
	 *   while x != root or color[x] = RED
	 *     do if p[x] = left[p[p[x]]]; // class A
	 *           then y <- right[p[p[x]]]
	 *           if color[y] = RED
	 *              then case<1>
	 *           else if x = right[p[x]]
	 *              then case<2>
	 *              case<3>
	 *              
	 *        else //class B
	 *            if p[x] = right[p[p[x]]]
	 *               then y <- left[p[p[x]]]
	 *               if color[y] = RED
	 *                  then case<4>
	 *               else if x = left[p[x]]
	 *                   then case<5>
	 *                   case<6>
	 *                   
	 *  color[root] = BLACK;
	 *  
	 *  case<1>://recolor
	 *     color[p[x]] <- BLACK;
	 *     color[y] <- Black;
	 *     color[p[p[x]]] <- RED;
	 *     
	 *  case<2>://rotate
	 *     left-rotate p[x]
	 *     
	 *  case<3>://rotate and recolor
	 *     right-rotate(p[p[x]])
	 *     recolor//这里得话题理解。
	 *  case<4>
	 *  
	 *  为了保存insert迭代过程中保存路径，我们需要一些类成员变量来记录。
	 *  
	 * @param x
	 */
	public void insertMIT(T x) {
		
	}
	
}

