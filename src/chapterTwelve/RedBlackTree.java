package chapterTwelve;

/**
 * ����MIT�㷨���ۺ����ݽṹһ�飬�Լ���д����������̡�
 * 
 * ������ĺ���ԣ�
 * 1. ÿ���ڵ��ɫ���Ǻ�ɫ���Ǻ�ɫ��
 * 2. ���ڵ�����е���Ҷ���Ǻ�ɫ��
 * 3. ÿ����ɫ�ڵ�ĸ��ڵ㶼�Ǻ�ɫ��
 * 4. �ӽڵ�x��������ֱ����Ҷ������·���������ĺ�ɫ�ڵ�ĸ���������ͬ�ġ���ɫ�ڵ�ĸ�������Ϊx�ĺڸ߶ȡ�
 * 
 * �����������������ʺ������һ��ƽ�������������߶�Ϊlogn�����ǵ����������ɾ���������ĺ�������׻ָ���
 * 
 * �������ߵ�֤����
 * 1. ʹ����ѧ���ɷ���֤������n���ڵ�ĺ�����ĸ߶�<=2log(n+1);
 * 2. merge each red node into its black parent.�ɴˣ����Թ���һ��2-3-4����
 * 
 * 2-3-4����һЩ���ʣ�
 * 1. each internal node has 2-4 children and every leafs has same depth = black height(root)
 * 
 * ���ȣ�����֪���ڶ�������������Ҷ�ĸ��� = n + 1;
 * ��2-3-4���У��߶�Ϊh����������Ҷ�ڵ���ĿΪ��2^h1 <= n <= 4^h1
 * =>2^h1 <= n + 1  => h1 <= log(n+1);
 * �����ݺ������3��һ��·���У���ɫ�ڵ�ĸ������ռ��·���ܽڵ���Ŀ��һ��
 * h <= 2 * h1 <= 2log(n+1);
 * ֤����ϡ�
 * 
 * @version <1> 2018-3-18 16:38
 * �����Լ������������Զ����µĲ������̡�����󣬽����MIT�γ��������Ե����ϵĲ������̡�
 * �����ҵ���⣬����������������ͬ��֮�
 * �Զ����µĲ������̾���ͺ���˼�����ڣ�һ����֤���������������Ҷʱ������ڵ��uncle�ڵ㲻Ϊ��ɫ��
 * ������ֱ�ӽ������Ե��������̵�case2����case3�������Ե�����������case2����case3�ĳ��֣���ζ�ż��������ɫ����ת���޸�������
 * Ҳ����˵���Զ�����������ǰ����case1�Ĺ�����
 * 
 * ����֤����ڵ��uncle��Ϊ��ɫ����ôһ��ֻ��Ҫִ��һ��case2���ߣ�case2 + case3����������޸����ָ�����ԡ�
 * �Զ����µ����̣������Ƚϴ󣬱Ƚ����ʱ�������ڽ�ʡ�ռ䣬��Ϊ�Ե����ϵ�������Ҫͨ��ջ���������ṹ������ϵ��µ�·����
 * 
 * Ȼ����һ��������̸����ϵĲ�̫һ�����Ҹо����ϵ�������Ȼ�ܾ������������������ɶ��ԱȽϴ󣨿�������̫���ˣ���
 * ��ˣ��������Լ�����⣬Ҳ���Ƿֳ�8���������д�����������߳��������Ҿ�������˼����ȷ��ö�������е�·�������ԣ�
 * �ṩ��Ӧ����ת������ɫ���衣��Ȼ�������һ��Ǿ���һЩ��������Ĵ��룬
 * 
 * @version <2> 2018-3-18 17:05
 * �����˲��ֹ��ܺ���ʽ����Ĵ��롣
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
	 * ������Ľڵ������
	 * �������ͨ�Ķ�����������ֻ�Ƕ���һ��ɫ�򣬷ǺڼȺ졣
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
	 * ��������
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
	 * nullNode�ڵ�ģ��null��ÿ�β��뿪ʼ��nullNode��element��Ϊx
	 * header�ڵ㣬Ϊ�˸��ڵ㷢����ת�󷵻غͱ���ϵķ����ԡ����Ҷ�������elementֵΪ�������
	 * ���ԣ����ú����������header���ұߡ�compare�����������˵��ȽϽڵ�Ϊheader����Զ����1��
	 * 
	 * �Զ����µĲ������̡�
	 * �����˼��Ͳ����ǣ�
	 * 1. ʹ�������ĸ�˽�����Ա����¼��������������·����
	 *    �ֱ��¼�˵�ǰ�ڵ㣬���ڵ㣬�游�ڵ�����游�ڵ㡣һ��ʼ�ĸ���������header�ϣ�
	 * 2. ��header�ڵ�ݹ����£�ͨ��compare�ж�·����ͬʱ��¼����·�������ÿ��ѭ���жϵ�ǰ�ڵ�����������Ƿ�Ϊ��ɫ���������¡�
	 * 3. ѭ��ֱ��compare����0��Ҳ���Ǵﵽnull�ڵ�����ں�������Ѿ��ҵ�����
	 * @param x
	 */
	public void insert(T x) {
		current = father = grand = header;
		nullNode.element = x;//�����ж��Ƿ��������Ҷ�ڵ㡣
		
		//���µ�������������нڵ����������ɫ���ӣ���ִ��һЩ������
		while(compare(x, current) != 0) {
			//����������µ�·��
			great = grand;
			grand = father;
			father = current;
			//�ҵ����ʵ����·�������һ��ʼ��header�ϣ���������������compare(x,header)=1,�������ú��������header���Ҷ���
			if(compare(x, current) < 0)
				current = current.left;
			else
				current = current.right;
			
			//��鵱ǰ�ڵ����������Ƿ��Ǻ�ɫ�ġ�
			if(current.left.color == RED && current.right.color == RED) {
				handleReorient(x);
			}
		}
		nullNode.element = null;
		//�ж��Ƿ��������null�ڵ㡣
		if(current.element != null) {
			return;
		}
		//�����½ڵ㲢�Ҳ��뵽ָ��λ�á�
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
	 * �����µ����Ĺ����У��������ĳ�����Ӷ��Ǻ�ɫ�����Ǿ���Ҫ����һ������
	 * 
	 * ���岽��Ϊ��
	 * 1. ��current�ڵ������������Ϊ��ɫ��current��Ϊ��ɫ�������Ĳ���������ı�����������4��
	 *     ���ǵ�current�ĸ��ڵ�Ҳ����fatherҲ�Ǻ�ɫ�ģ��ͻ��ƻ�����3��
	 *     ���ԣ���fatherΪ��ɫʱ��ִ�в���2.�����˳�������
	 *     
	 * 2. ��fatherΪ��ɫʱ���ƻ��˺���ԣ���Ҫ�޸���Ҳ���ǽ����ʵ�����ת��������ɫ����Ϊ���¼������
	 *    PS:xΪcurrent��PΪfather, GΪgrand, EΪgreat.
	 *    ��x,p,gΪ��һ���Ρ�simple left rotate(g),Ȼ����G��Ϊ��ɫ��P��Ϊ��ɫ
	 *    ��x,p,gΪ��֮���Σ���·����״Ϊ������ң���double left rotate(g)Ȼ��g��Ϊ��ɫ��x��Ϊ��ɫ��Ȼ��x���ø�ΪP.children;
	 *    ��x,p,gΪ��֮���Σ���·����״Ϊ���Һ��󣩣�double right rotate(g)Ȼ��g��Ϊ��ɫ��x��Ϊ��ɫ��Ȼ��x���ø�ΪP.children.
	 *    ��x,p,gΪ��һ���Σ�simple left rotate(g)Ȼ��g��Ϊ��ɫ��P��Ϊ��ɫ��
	 * @param x
	 */
	private void handleReorient(T x) {
		//����1
		current.left.color = current.right.color = BLACK;
		current.color = RED;
		
		//�ж�
		if(father.color == RED) {
			//����2
			grand.color = RED;
			current = rotate(x, great);
			current.color = BLACK;
		}
		//�����ڵ���Ϊ��ɫ
		header.right.color = BLACK;
		
	}
	
	/**
	 * @version 1
	 * ����great,grand,father��great���ɵ�·�����ж���ô������������ת��ʽ��
	 * ����x,p,g�任��������������º�great��ȷ���ӡ�great�����������ӣ���ˣ�����8�������
	 * 
	 * ����·������great��ʼ��ע�⵽���е�˫��ת�����ɵ���ת���ɵġ�
	 * �ҵ��죬��̫�����ˣ���Ȼ�����������ȷ����������ô��������£�ȷʵ������д��
	 * 
	 * @version 2
	 * ������Ժϲ��Ĵ���
	 * 
	 * 
	 * @param x
	 * @param great2
	 * @return
	 */
	private Node<T> rotate(T x, Node<T> parent) {
		//great���£��L��
		if(compare(x, parent) < 0) {
			//grand����(�L)
			if(compare(x, parent.left) < 0) {
				//father����(�K)
				if(compare(x, parent.left.left) > 0){
					//·��Ϊ�� + ��һ���Ρ����2������ת��Ϊ���1��Ȼ�������1�Ĵ��룬���˫��ת
					parent.left.left = simpleRotateWithRightChild(parent.left.left);
				}
				//father����(�K)��·��Ϊ�� + ��һ���Ρ����1.
				parent.left = simpleRotateWithLeftChild(parent.left);
			}
			//grand���£��K��
			else {
				//father���£��L��
				if(compare(x, parent.left.right) < 0) {
					//·��Ϊ�� + ��֮����.���3������ת��Ϊ���4
					parent.left.right = simpleRotateWithLeftChild(parent.left.right);
				}
				//father���£��K��//·��Ϊ�� + ��һ���Σ����4
				parent.left = simpleRotateWithRightChild(parent.left);
			}
			//ʵ���϶��Ƿ�����great������ӣ�Ҳ������ת֮�����ϲ�Ľڵ�
			return parent.left;
		}
		//great���£��K��.�����ȫ�����澵��
		else {
			//grand����(�L)
			if(compare(x, parent.right) < 0) {
				//father����(�L)
				if(compare(x, parent.right.left) > 0){
					//·��Ϊ�� + ��֮���Ρ����2.����ת��Ϊ���1
					parent.right.left = simpleRotateWithRightChild(parent.right.left);
				}
				//·��Ϊ�� + ��һ���Ρ����1��
				parent.right = simpleRotateWithLeftChild(parent.right);
			}
			//grand���£��K��
			else {
				//father���£��L��
				if(compare(x, parent.right.right) < 0) {
					//·��Ϊ�� + ��֮����.���3������ת��Ϊ���4
					parent.right.right = simpleRotateWithLeftChild(parent.right.right);
				}
				//father���£��K��
				//·��Ϊ�� + ��һ���Σ����4
				parent.right = simpleRotateWithRightChild(parent.right);
			}
			//ʵ���϶��Ƿ�����great���Ҷ��ӣ�Ҳ������ת֮�����ϲ�Ľڵ�
			return parent.right;
		}
	}

	
	/**
	 * �ڵ���ҵ���ת��
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
	 * �ڵ�����ת��
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
	 * �Ƚ�x�ͽڵ��ֵ��С�����tΪ���ڵ㣬��ôx��Զ�Ƚϴ�
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
	 * ��ӡ�����
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
	 * �ݹ������ӡ���������Ϣ
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
	 * �жϺ�����Ƿ�Ϊ��
	 * @return
	 */
	private boolean isEmpty() {
		return header.right.equals(nullNode);
	}
	
	
	/**
	 * �������̡������㷨���ۿγ�ѧϰ�����ġ�
	 * 
	 * �����˼����ǣ�
	 * 1. ʹ����ͨ������������insert
	 * 2. ������Ľڵ�ɫ����Ϊred
	 * 3. ���ʱ�����ʻ��ƻ�������ĺ���ԡ�������3��
	 * 
	 * xΪ��ǰ�ڵ㣬p[x]Ϊ�丸�ڵ㣬���p[p[x]]Ϊ��үү�ڵ㣬left[p[p[x]]]Ϊx��үү�ڵ�������
	 * 
	 * ��α���룺
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
	 *     recolor//����û�����⡣
	 *  case<4>
	 *  
	 *  Ϊ�˱���insert���������б���·����������ҪһЩ���Ա��������¼��
	 *  
	 * @param x
	 */
	public void insertMIT(T x) {
		
	}
	
}

