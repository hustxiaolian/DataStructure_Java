package chapterTwelve;

import java.util.*;

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
 * @version <3> 2018-3-22 09:13
 * 完成了MIT课程提到的从底向上的插入例程。
 * 
 * @version <4> 2018-3-25 22:41
 * 完成了自顶向下的删除例程，并且测试通过。
 * 
 * @author 25040
 *
 * @param <T>
 */
public class RedBlackTree<T extends Comparable<? super T>> {
	
	public static void main(String[] args) {
		RedBlackTree<Integer> t = new RedBlackTree<>();
		Integer[] arr = {10,85,15,70,20,60,30,50,65,80,90,40,5,55,20,25,35};
		//Integer[] arr = {10,15,20};
		for(int i = 0;i < arr.length;++i) {
			t.insertMIT(arr[i]);
			
		}
		t.printTree();
		t.delete(55);
		t.printTree();
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
		this.nullNode.left = this.nullNode.right = nullNode;//现在看来这里的这个设计实在是太妙了。
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
	 * 3. 循环直到compare返回0，也就是达到null节点或者在红黑树种已经找到了它，判断是否是重复节点，如果是，则执行步骤4，否则返回。
	 * 4. 生成新节点，插入到合适的位置，然后最后进行一次handleReorient函数，常数时间。
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
		current = new Node<>(x, nullNode, nullNode,RED);
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
				//father左下(↘)，路径为左 + 左一字形。情况1.
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
			printTree(t.right, tabNum + 1);
			for(int i = 0;i < tabNum;++i){
				System.out.print("\t");
			}
			System.out.println(t.element + (t.color == 1? "(B)":"(R)"));
			printTree(t.left, tabNum + 1);
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
	 *  使用自底向上的插入例程，需要一个栈使我们能够向上回溯插入节点的父链。
	 *  
	 * @param x
	 */
	public void insertMIT(T x) {
		//使用堆栈来记录路径
		Stack<Node<T>> s = new Stack<>();
		//使得可以用用compare = 0来判断循环终止的条件。
		nullNode.element = x;
		//引用当前正在迭代的节点
		Node<T> current = header;
		
		/*
		 * 这里相当于实现了二叉树的非递归插入例程，即伪代码的Tree-insert
		 */
		while(!(compare(x, current) == 0)) {
			//压入堆栈，记录向下路径，header节点也被压入了堆栈
			s.push(current);
			//根据数的性质，判断左右
			current = compare(x, current) < 0 ? current.left : current.right;
		}
		nullNode.element = null;
		//判断是不是到达了null节点，还是中间就找到了等值的节点
		if(current.element != null) {
			//找到了等值节点，直接返回
			return;
		}
		/*
		 * 伪代码对应的color[x] = red;
		 */
		//创建新的节点，生成新节点，上色为红色，并且插入正确的父节点位置
		//bug 3 不能这样current = new Node<>(x, null, null, RED);
		current = new Node<>(x, nullNode, nullNode, RED);
		Node<T> father = s.peek();//错误1，改之前是pop，破坏了father路径。
		if(compare(x, father) < 0) {
			father.left = current;
		}
		else {
			father.right = current;
		}
		
		/*
		 * 自底向上的fix例程，其实我们关注地就是在如何不破坏性质4的情况下，向上修复性质3
		 */
		//bug 2 ：当红黑树中的节点过少的时候，完全没必要fix，直接插入红节点，置黑根节点即可。
		//即红黑树插入第一个元素和第二个元素时根本没必要fix,注意到堆栈中还有header节点。
		if(s.size() >= 3) {
			downToUpFix(current, s, x);	
		}
		else {
			header.right.color = BLACK;
		}
			
	}
	
	/**
	 * 自底向上的fix例程，其实我们关注地就是在如何不破坏性质4的情况下，向上修复性质3
	 * 共计分为六种情况。左半边三种和右半边三种镜像对称。
	 * 
	 * @param current 新插入节点的引用
	 * @param s       回溯路径的堆栈
	 * @param x       新插入节点的具体值
	 */
	private void downToUpFix(Node<T> current, Stack<Node<T>> s , T x) {
		//判断是否向上到达了根节点或者当前节点是否继续红色
		boolean isEnd = (s.peek().color == RED);
		//bug 7 不应该无条件进入一次，应该检查当前插入的红色节点是否直接满足了红黑性，不用做任何变化
		//即只有插入节点的父亲也是红色的时候才需要进行变换
		while(isEnd) {
			Node<T> father = s.pop();
			Node<T> grand = s.pop();
			//x == left[p[p[x]]],这是情况为左半边的三种情况，右半边的三种情况
			//判断当前节点是否是爷爷节点的左子树中
			if(compare(x, grand) < 0) {
				Node<T> uncle = grand.right;
				//判断你的叔叔节点是否是红色
				if(uncle.color == RED) {
					//case 1 重上色
					father.color = BLACK;
					uncle.color = BLACK;
					grand.color = RED;
					//continue向上修复
					current = grand;
					//去掉continue;
				}
				else {
					//判断当前节点是否是父节点的右儿子
					if(compare(x, father) > 0) {
						//case 2 旋转，将现场情况变为情况3,注意这里旋转后，破坏了grand，father和current的正确路径关系
						//bug 4 应该是它的左儿子或者右儿子。grand = simpleRotateWithLeftChild(father);
						grand.left = simpleRotateWithRightChild(father);
					}
					//case 3 旋转并且重新上色, 并且正确连接到曾祖父的左节点或者右节点
					grand.color = RED;//通过观察，我们发现爷爷节点总是要变成红色的。
				    Node<T> great = s.peek();//不要从堆栈中取出来
				    
				    if(compare(x, great) < 0) {
				    	great.left = simpleRotateWithLeftChild(grand);
				    	great.left.color = BLACK;
				    	current = great.left;
				    }
				    else {
				    	great.right = simpleRotateWithLeftChild(grand);
				    	great.right.color = BLACK;
				    	//bug 6 还是得把current放在正确得位置上，虽然它是最后一步
				    	current = great.right;
				    }
				}
			}
			//右半边3种情况，算法思路都一样，只需要左右互换。考查小心细致的时候到了
			else {
				Node<T> uncle = grand.left;
				//判断当前节点的叔叔节点是否为红色。
				if(uncle.color == RED) {
					//case 4 重上色后继续向上
					father.color = BLACK;
					uncle.color = BLACK;
					grand.color = RED;
					
					//continue向上修复
					current = grand;
					//去掉continue
				}
				else {
					//判断当前节点是否是父节点
					if(compare(x, father) < 0) {
						//case 5 旋转变成情况6.注意这里旋转后，破坏了father和current的正确路径关系
						//bug 4 应该是它的左儿子或者右儿子。grand = simpleRotateWithLeftChild(father);
						grand.right = simpleRotateWithLeftChild(father);						
					}
					//case 6 旋转，重上色。然后把旋转后的子树链接到曾祖父节点合适的位置上。
					grand.color = RED;
					Node<T> great = s.peek();
					
					if(compare(x, great) < 0) {
						great.left = simpleRotateWithRightChild(grand);
						great.left.color = BLACK;
						current = great.left;
					}else {
						//bug 5 智障了，写成了simpleRotateWithLeftChild
						great.right = simpleRotateWithRightChild(grand);
						great.right.color = BLACK;
						current = great.right;
					}
				}
			}
			//判断现在的树满足红黑性
			isEnd =  !( current.element.equals(header.right.element)) ;
			if(isEnd) {
				if(current.color == RED) {
					if(s.peek().color == BLACK)
						isEnd = false;
				}
				else {
					isEnd = false;
				}
			}
		}
		header.right.color = BLACK;
	}
	
	/**
	 * 编写红黑树的删除例程。
	 * 
	 * 分析欲删除节点儿子的分布情况，具有以下四种情况：
	 * case 1. 带有两个儿子->用右子树的最小节点替代它。我们对该节点的右子树使用delete例程。
	 *         注意传递正确的父节点，祖父节点引用，以被第二次删除例程可能发生的旋转使用。
	 *         该右子树的最小节点必然最多只有一个右儿子。情况变成了case 2
	 * case 2. 只有一个儿子，可以画图分析，这种情况下，该节点只有可能是黑色的，并且另外一个儿子是红色的。
	 *         直接用儿子替换当前值，然后把儿子挂掉就行了。
	 * case 3. 红树叶。直接删除。
	 * 
	 * 
	 * 下面是是如何从上到下保证删除期间树叶都是红色的。
	 * 设X为当前节点，T为X的兄弟节点，P为X的父节点，G为祖父节点
	 * 1.开始时，我们把根节点涂成红色的。当沿树向下遍历时，我们设法保证X是红色的。
	 * 2.当到达一个新节点时，我们要确定P是红色并且X和T是黑的(因为不能有两个相连的红色节点)。
	 * 
	 * 在向下递归的过程中，我们可能会遇到以下几种情况：
	 * 注意前提：P是红色的，x是黑色的，T 是黑色的。
	 * 
	 * 第一种情况，X有两个黑儿子。case A
	 * 1.此时有三种子情况。如果T也有两个黑儿子，那么可以翻转X，T和P的颜色来保持这种不变性。case A-1
	 * 2.否则，T的儿子之一是红的。case A-2
	 *   //如果T节点的儿子之一是红色的。case A-2.在这种情况下，又可以分为两种情况
	 *	  //如果P，T，R形成了一字形，只需要一次单旋转+重上色即可。
	 *	  //如果P,T,R形成了之字形，那么需要一次双旋转+重上色才行。
	 *    //可以复用插入例程的rotate
	 * 
	 * 第二种情况。case B 。X的儿子之一是红的。
	 * 1. 在这种情形下，继续向下一轮，得到新的X，T和P。如果幸运，X落在红儿子上，得到新的X，T和P,那就再向下一轮。case B-1
	 * 2. 如果不是这样，那么我们知道T是红的，而X和P将是黑的。我们旋转T和P，并且将P置为红色，T置为黑色。这样有回到了case A继续处理。
	 * 
	 * 迭代删除中fix，路径记录，当前节点向下一轮和删除判断的先后顺序很重要。
	 * 1.在我写的例程中，初始状态，current节点设置为header.right,也就是实际的根节点，并且置为红色。
	 *   这样，fix函数，一定会跳过根节点这一轮，不做任何处理，直接return回来。而father节点，grand节点全部置为header。
	 * 2.迭代向下的while循环中
	 *   先fix将current置为红色(根节点会跳过这一轮)，然后进行删除判断（判断当前节点是否为要删除的节点，并且分情况处理），
	 *   再记录迭代路径（更新father和grand），最后让current向下一层。
	 * 
	 * 
	 * @param x 
	 * @return 
	 */
	public void delete(T x) {
		header.right.color = RED;
		delete(header.right, header, header, x);
		header.right.color = BLACK;
	}
	
	/**
	 * 
	 * 步骤和顺序应该是：
	 * 1.先对当前节点的情况进行判断，完成fix，
	 * 2.再判断当前节点是否等于要欲删除的节点。如果是则分情况进行删除
	 * 3.如果不是，继续向下迭代。
	 * 
	 * @param t
	 * @param x
	 * @return
	 */
	private void delete(Node<T> root, Node<T> rootFather, Node<T> rootGrand, T x) {
		//使用类变量来传递不同函数之间的参数，避免混乱
		current = root;
		father = rootFather;
		//bug 3 将该函数第三个实参名字设置为了grand，遮挡了类成员变量
		//grand = grand
		grand = rootGrand;
		/*
		//bug 1 应该在上层的delete来干这件事，因为该delete可能会用到递归两次。
		//将当前树的根节点涂成红色
		//root.color = RED;
		*/
		//开始向下迭代,直到迭代到nullNode。
		while(current.element != null) {
			//
			//fix将x变为红色
			judgeAndFix(x);
			//因为，上面的变色操作，可能把null节点也变成了红色。后果未知，为了防止出现意外，强制变为黑色。
			nullNode.color = BLACK;
			
			//
			int compareResult = compare(x, current);
			if(compareResult == 0) {
				//如果是双儿子的情况
				if(current.left.element != null && current.right.element != null) {
					//双儿子的情况下，先用当前节点的右子树的最大值来替换
					current.element = findMin(current.right).element;
					
					//bug 2 当红黑树只有三个元素的时候，会出错。修正，把delete的传参形式改下。
					//追加了grand参数，这样就算特殊情况需要rotate的时候，不会出错。同时也不再需要返回值。
					//current.right = delete(current.right, current, current.element)
					delete(current.right, current, father, current.element);
					
					//注意到，这里递归delete之后，current啥的都是完全混乱的，不能用，但是因为delete例程最多只会执行两轮，所以不受影响。
					//这里完成后，直接break跳出了
				}
				//单儿子或者没儿子的情况,不能把单儿子和没儿子两种情况放在一起
				else {
					//树叶，没儿子，直接干掉
					if(current.left.element == null && current.right.element == null) {
						if(compare(current.element, father) < 0) {
							father.left = nullNode;
						}
						else {
							father.right = nullNode;
						}
					}
					//单儿子，这里注意到，单儿子的情况下，删除节点最多只有一个儿子。
					//而且这种情况下，欲删除节点必然是黑色的，它的左儿子或者右儿子必然是红色的。
					//直接拿左儿子或者右儿子的值来替换删除节点的值，再删除右儿子或者右儿子。
					else {
						Node<T> temp = current.left.element != null ? current.left : current.right;
						current.element = temp.element;
						current.left = current.right = nullNode;
					}
				}
				//删除后直接退出
				break;
			}
			
			//用于追溯路径
			grand = father;
			father = current;
			
			
			//向下迭代一次,判断x和当前节点,小于则向左，大于向右，等于不动作。
			if(compareResult < 0) {
				current = current.left;
			}
			else {
				current = current.right;
			}
		
		}
		//bug 1 
		//将当前子树的根节点重新上色为黑色。
		//root.color = BLACK;
		//return subTreeRoot;
	}
	
	/**
	 * 寻找当前树的最小值，并且返回它的节点引用
	 * @param right
	 * @return
	 */
	private Node<T> findMin(Node<T> t) {
		//一直迭代直到到null节点。它就是该树的最小值。
		while(t.left.element != null) {
			t = t.left;
		}
		
		return t;
	}

	/**
	 * 为了方便，设定X为当前节点，P为父节点，T为兄弟节点，G为祖父节点。
	 * 
	 * 根据递归情况，我们的目的是保证在向下删除的过程中，设法保证X节点为红色，这样直到X为红树叶。就可以直接删除。
	 * 也就是说，当X迭代到下一层节点时，P必须为红色的并且X，T为黑色的。
	 * 
	 * 判断当前节点儿子的情况，并且作相应的处理，共计可以分为A和B两种大情况。
	 * A：X有两个黑儿子
	 * 1.T也有两个黑儿子。那么把X,T,P的颜色全部反转一遍即可。
	 * 2.T的两个之一是红色的。那么需要作一些旋转和重新上色的处理。
	 *    //如果T节点的儿子之一是红色的。case A-2.在这种情况下，又可以分为两种情况
	 *	  //如果P，T，R形成了一字形，只需要一次单旋转+重上色即可。
	 *	  //如果P,T,R形成了之字形，那么需要一次双旋转+重上色才行。
	 *    //可以复用插入例程的rotate
	 * 
	 * B：X的两个儿子之一是红色的（包含X的儿子全是红色的情况）。首先，继续向下一层。
	 * 1.如果幸运的话,x向下迭代到了红儿子，那么我们继续向下一层，此时X必定为黑色了。回到了情况判断的原始状态
	 * 2.如果不够幸运的话，x向下迭代到了黑儿子，我们就需要经过旋转P和T，并且重上色T和P。这样x的父亲又变回红色了。回到情况A。
	 * 
	 * 
	 * @param x 欲删除节点的值，主要用于判断X，P，T，G之间路径形状。不同的路径形状，需要不同的旋转和上色处理。
	 * 同时，该函数还使用了类成员变量(current,father,grand)来回溯路径信息。
	 */
	private void judgeAndFix(T x) {
		//情况B的幸运情况的特殊情况，不需要处理，下一轮。目的已达到，就此罢手
		if(current.color == RED) {
			return;
		}
		
		//情况B的不幸情况,只有此时，P节点才是黑色
		if(father.color == BLACK) {
			//先重上色
			Node<T> brother = compare(x, father) < 0 ? father.right : father.left;
			brother.color = BLACK;
			father.color = RED;
			//正确旋转并且重新连接,并且将current，father，grand之间重新理顺，确保正确。画图理解。
			if(compare(x, grand) < 0) {
				if(compare(x, father) > 0) {
					grand.left = simpleRotateWithLeftChild(grand.left);
				}
				else {
					grand.left = simpleRotateWithRightChild(grand.left);
				}
				//注意这里正确地重新设置好grand的值。
				grand = grand.left;
			}
			else {
				if(compare(x, father) > 0) {
					grand.right = simpleRotateWithLeftChild(grand.right);
				}
				else {
					grand.right = simpleRotateWithRightChild(grand.right);
				}
				grand.right = grand;
			}
		}
	
		//情况A和B的判断
		if(hasTowBlackChild(current)) {
			//情况A
			//获取当前节点的兄弟对象，判断它两个儿子的状态
			Node<T> brother = compare(x, father) < 0 ? father.right : father.left;
			if(hasTowBlackChild(brother)) {
				//如果T节点也有两个黑色的儿子，那么case A-1，重新上色即可
				father.color = BLACK;
				current.color = RED;
				brother.color = RED;
			}
			else {
				//如果T节点的儿子之一是红色的。case A-2.在这种情况下，又可以分为两种情况
				//如果P，T，R形成了一字形，只需要一次单旋转+重上色即可。
				//如果P,T,R形成了之字形，那么需要一次双旋转+重上色才行。
				//可以复用插入例程的rotate
				//获取brother节点中的红节点
				Node<T> redNodeWithBrother = brother.left.color == RED ? brother.left : brother.right;
				//由于是根据T节点的情况来进行
				grand = rotate(redNodeWithBrother.element, grand);
				//再根据R，T，P形成的路径来进行重新上色
				recolor(father, brother, redNodeWithBrother,current);
				
				//TEST
				this.printTree();
			}
		}
		else {
			//情况B，先调到下一轮。
			return;
		}
		
	}
	
	
	/**
	 * case A-2下旋转后的重新上色处理。根据P，T，R（redredNodeWithBrother）形成的路径形状判断。
	 * 通过观察可知，当路径为之字形时，只用改变X和P即可
	 * 当路径为一字形，则需要翻转全部节点的颜色（P，T，X，R）
	 * 
	 * @param father
	 * @param brother
	 * @param redNodeWithBrother
	 * @param current
	 */
	private void recolor(Node<T> father, Node<T> brother, Node<T> redNodeWithBrother, Node<T> current) {
		//判断路径的形状，这里可以使用异或判断左右关系是否相同
		if((compare(brother.element, father) < 0) ^ (compare(redNodeWithBrother.element, brother) < 0)) {
			//之字形
			current.color = RED;
			father.color = BLACK;
		}
		else {
			//一字形
			current.color = RED;
			brother.color = RED;
			father.color = BLACK;
			redNodeWithBrother.color = BLACK;
		}
	}

	/**
	 * 判断当前节点是否有两个黑色的儿子。
	 * 值得注意的是，这里的儿子也包括null节点，也就是说如果当前节点是树叶，那么一定会返回true
	 * @param current2
	 * @return
	 */
	private boolean hasTowBlackChild(Node<T> current) {
		return current.left.color == BLACK && current.right.color == BLACK;
	}
}


