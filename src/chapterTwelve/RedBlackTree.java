package chapterTwelve;

import java.util.*;

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
 * @version <3> 2018-3-22 09:13
 * �����MIT�γ��ᵽ�Ĵӵ����ϵĲ������̡�
 * 
 * @version <4> 2018-3-25 22:41
 * ������Զ����µ�ɾ�����̣����Ҳ���ͨ����
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
		this.nullNode.left = this.nullNode.right = nullNode;//���ڿ��������������ʵ����̫���ˡ�
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
	 * 3. ѭ��ֱ��compare����0��Ҳ���Ǵﵽnull�ڵ�����ں�������Ѿ��ҵ��������ж��Ƿ����ظ��ڵ㣬����ǣ���ִ�в���4�����򷵻ء�
	 * 4. �����½ڵ㣬���뵽���ʵ�λ�ã�Ȼ��������һ��handleReorient����������ʱ�䡣
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
			printTree(t.right, tabNum + 1);
			for(int i = 0;i < tabNum;++i){
				System.out.print("\t");
			}
			System.out.println(t.element + (t.color == 1? "(B)":"(R)"));
			printTree(t.left, tabNum + 1);
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
	 *  ʹ���Ե����ϵĲ������̣���Ҫһ��ջʹ�����ܹ����ϻ��ݲ���ڵ�ĸ�����
	 *  
	 * @param x
	 */
	public void insertMIT(T x) {
		//ʹ�ö�ջ����¼·��
		Stack<Node<T>> s = new Stack<>();
		//ʹ�ÿ�������compare = 0���ж�ѭ����ֹ��������
		nullNode.element = x;
		//���õ�ǰ���ڵ����Ľڵ�
		Node<T> current = header;
		
		/*
		 * �����൱��ʵ���˶������ķǵݹ�������̣���α�����Tree-insert
		 */
		while(!(compare(x, current) == 0)) {
			//ѹ���ջ����¼����·����header�ڵ�Ҳ��ѹ���˶�ջ
			s.push(current);
			//�����������ʣ��ж�����
			current = compare(x, current) < 0 ? current.left : current.right;
		}
		nullNode.element = null;
		//�ж��ǲ��ǵ�����null�ڵ㣬�����м���ҵ��˵�ֵ�Ľڵ�
		if(current.element != null) {
			//�ҵ��˵�ֵ�ڵ㣬ֱ�ӷ���
			return;
		}
		/*
		 * α�����Ӧ��color[x] = red;
		 */
		//�����µĽڵ㣬�����½ڵ㣬��ɫΪ��ɫ�����Ҳ�����ȷ�ĸ��ڵ�λ��
		//bug 3 ��������current = new Node<>(x, null, null, RED);
		current = new Node<>(x, nullNode, nullNode, RED);
		Node<T> father = s.peek();//����1����֮ǰ��pop���ƻ���father·����
		if(compare(x, father) < 0) {
			father.left = current;
		}
		else {
			father.right = current;
		}
		
		/*
		 * �Ե����ϵ�fix���̣���ʵ���ǹ�ע�ؾ�������β��ƻ�����4������£������޸�����3
		 */
		//bug 2 ����������еĽڵ���ٵ�ʱ����ȫû��Ҫfix��ֱ�Ӳ����ڵ㣬�úڸ��ڵ㼴�ɡ�
		//������������һ��Ԫ�غ͵ڶ���Ԫ��ʱ����û��Ҫfix,ע�⵽��ջ�л���header�ڵ㡣
		if(s.size() >= 3) {
			downToUpFix(current, s, x);	
		}
		else {
			header.right.color = BLACK;
		}
			
	}
	
	/**
	 * �Ե����ϵ�fix���̣���ʵ���ǹ�ע�ؾ�������β��ƻ�����4������£������޸�����3
	 * ���Ʒ�Ϊ����������������ֺ��Ұ�����־���Գơ�
	 * 
	 * @param current �²���ڵ������
	 * @param s       ����·���Ķ�ջ
	 * @param x       �²���ڵ�ľ���ֵ
	 */
	private void downToUpFix(Node<T> current, Stack<Node<T>> s , T x) {
		//�ж��Ƿ����ϵ����˸��ڵ���ߵ�ǰ�ڵ��Ƿ������ɫ
		boolean isEnd = (s.peek().color == RED);
		//bug 7 ��Ӧ������������һ�Σ�Ӧ�ü�鵱ǰ����ĺ�ɫ�ڵ��Ƿ�ֱ�������˺���ԣ��������κα仯
		//��ֻ�в���ڵ�ĸ���Ҳ�Ǻ�ɫ��ʱ�����Ҫ���б任
		while(isEnd) {
			Node<T> father = s.pop();
			Node<T> grand = s.pop();
			//x == left[p[p[x]]],�������Ϊ���ߵ�����������Ұ�ߵ��������
			//�жϵ�ǰ�ڵ��Ƿ���үү�ڵ����������
			if(compare(x, grand) < 0) {
				Node<T> uncle = grand.right;
				//�ж��������ڵ��Ƿ��Ǻ�ɫ
				if(uncle.color == RED) {
					//case 1 ����ɫ
					father.color = BLACK;
					uncle.color = BLACK;
					grand.color = RED;
					//continue�����޸�
					current = grand;
					//ȥ��continue;
				}
				else {
					//�жϵ�ǰ�ڵ��Ƿ��Ǹ��ڵ���Ҷ���
					if(compare(x, father) > 0) {
						//case 2 ��ת�����ֳ������Ϊ���3,ע��������ת���ƻ���grand��father��current����ȷ·����ϵ
						//bug 4 Ӧ������������ӻ����Ҷ��ӡ�grand = simpleRotateWithLeftChild(father);
						grand.left = simpleRotateWithRightChild(father);
					}
					//case 3 ��ת����������ɫ, ������ȷ���ӵ����游����ڵ�����ҽڵ�
					grand.color = RED;//ͨ���۲죬���Ƿ���үү�ڵ�����Ҫ��ɺ�ɫ�ġ�
				    Node<T> great = s.peek();//��Ҫ�Ӷ�ջ��ȡ����
				    
				    if(compare(x, great) < 0) {
				    	great.left = simpleRotateWithLeftChild(grand);
				    	great.left.color = BLACK;
				    	current = great.left;
				    }
				    else {
				    	great.right = simpleRotateWithLeftChild(grand);
				    	great.right.color = BLACK;
				    	//bug 6 ���ǵð�current������ȷ��λ���ϣ���Ȼ�������һ��
				    	current = great.right;
				    }
				}
			}
			//�Ұ��3��������㷨˼·��һ����ֻ��Ҫ���һ���������С��ϸ�µ�ʱ����
			else {
				Node<T> uncle = grand.left;
				//�жϵ�ǰ�ڵ������ڵ��Ƿ�Ϊ��ɫ��
				if(uncle.color == RED) {
					//case 4 ����ɫ���������
					father.color = BLACK;
					uncle.color = BLACK;
					grand.color = RED;
					
					//continue�����޸�
					current = grand;
					//ȥ��continue
				}
				else {
					//�жϵ�ǰ�ڵ��Ƿ��Ǹ��ڵ�
					if(compare(x, father) < 0) {
						//case 5 ��ת������6.ע��������ת���ƻ���father��current����ȷ·����ϵ
						//bug 4 Ӧ������������ӻ����Ҷ��ӡ�grand = simpleRotateWithLeftChild(father);
						grand.right = simpleRotateWithLeftChild(father);						
					}
					//case 6 ��ת������ɫ��Ȼ�����ת����������ӵ����游�ڵ���ʵ�λ���ϡ�
					grand.color = RED;
					Node<T> great = s.peek();
					
					if(compare(x, great) < 0) {
						great.left = simpleRotateWithRightChild(grand);
						great.left.color = BLACK;
						current = great.left;
					}else {
						//bug 5 �����ˣ�д����simpleRotateWithLeftChild
						great.right = simpleRotateWithRightChild(grand);
						great.right.color = BLACK;
						current = great.right;
					}
				}
			}
			//�ж����ڵ�����������
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
	 * ��д�������ɾ�����̡�
	 * 
	 * ������ɾ���ڵ���ӵķֲ�����������������������
	 * case 1. ������������->������������С�ڵ�����������ǶԸýڵ��������ʹ��delete���̡�
	 *         ע�⴫����ȷ�ĸ��ڵ㣬�游�ڵ����ã��Ա��ڶ���ɾ�����̿��ܷ�������תʹ�á�
	 *         ������������С�ڵ��Ȼ���ֻ��һ���Ҷ��ӡ���������case 2
	 * case 2. ֻ��һ�����ӣ����Ի�ͼ��������������£��ýڵ�ֻ�п����Ǻ�ɫ�ģ���������һ�������Ǻ�ɫ�ġ�
	 *         ֱ���ö����滻��ǰֵ��Ȼ��Ѷ��ӹҵ������ˡ�
	 * case 3. ����Ҷ��ֱ��ɾ����
	 * 
	 * 
	 * ����������δ��ϵ��±�֤ɾ���ڼ���Ҷ���Ǻ�ɫ�ġ�
	 * ��XΪ��ǰ�ڵ㣬TΪX���ֵܽڵ㣬PΪX�ĸ��ڵ㣬GΪ�游�ڵ�
	 * 1.��ʼʱ�����ǰѸ��ڵ�Ϳ�ɺ�ɫ�ġ����������±���ʱ�������跨��֤X�Ǻ�ɫ�ġ�
	 * 2.������һ���½ڵ�ʱ������Ҫȷ��P�Ǻ�ɫ����X��T�Ǻڵ�(��Ϊ���������������ĺ�ɫ�ڵ�)��
	 * 
	 * �����µݹ�Ĺ����У����ǿ��ܻ��������¼��������
	 * ע��ǰ�᣺P�Ǻ�ɫ�ģ�x�Ǻ�ɫ�ģ�T �Ǻ�ɫ�ġ�
	 * 
	 * ��һ�������X�������ڶ��ӡ�case A
	 * 1.��ʱ����������������TҲ�������ڶ��ӣ���ô���Է�תX��T��P����ɫ���������ֲ����ԡ�case A-1
	 * 2.����T�Ķ���֮һ�Ǻ�ġ�case A-2
	 *   //���T�ڵ�Ķ���֮һ�Ǻ�ɫ�ġ�case A-2.����������£��ֿ��Է�Ϊ�������
	 *	  //���P��T��R�γ���һ���Σ�ֻ��Ҫһ�ε���ת+����ɫ���ɡ�
	 *	  //���P,T,R�γ���֮���Σ���ô��Ҫһ��˫��ת+����ɫ���С�
	 *    //���Ը��ò������̵�rotate
	 * 
	 * �ڶ��������case B ��X�Ķ���֮һ�Ǻ�ġ�
	 * 1. �����������£���������һ�֣��õ��µ�X��T��P��������ˣ�X���ں�����ϣ��õ��µ�X��T��P,�Ǿ�������һ�֡�case B-1
	 * 2. ���������������ô����֪��T�Ǻ�ģ���X��P���Ǻڵġ�������תT��P�����ҽ�P��Ϊ��ɫ��T��Ϊ��ɫ�������лص���case A��������
	 * 
	 * ����ɾ����fix��·����¼����ǰ�ڵ�����һ�ֺ�ɾ���жϵ��Ⱥ�˳�����Ҫ��
	 * 1.����д�������У���ʼ״̬��current�ڵ�����Ϊheader.right,Ҳ����ʵ�ʵĸ��ڵ㣬������Ϊ��ɫ��
	 *   ������fix������һ�����������ڵ���һ�֣������κδ���ֱ��return��������father�ڵ㣬grand�ڵ�ȫ����Ϊheader��
	 * 2.�������µ�whileѭ����
	 *   ��fix��current��Ϊ��ɫ(���ڵ��������һ��)��Ȼ�����ɾ���жϣ��жϵ�ǰ�ڵ��Ƿ�ΪҪɾ���Ľڵ㣬���ҷ����������
	 *   �ټ�¼����·��������father��grand���������current����һ�㡣
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
	 * �����˳��Ӧ���ǣ�
	 * 1.�ȶԵ�ǰ�ڵ����������жϣ����fix��
	 * 2.���жϵ�ǰ�ڵ��Ƿ����Ҫ��ɾ���Ľڵ㡣���������������ɾ��
	 * 3.������ǣ��������µ�����
	 * 
	 * @param t
	 * @param x
	 * @return
	 */
	private void delete(Node<T> root, Node<T> rootFather, Node<T> rootGrand, T x) {
		//ʹ������������ݲ�ͬ����֮��Ĳ������������
		current = root;
		father = rootFather;
		//bug 3 ���ú���������ʵ����������Ϊ��grand���ڵ������Ա����
		//grand = grand
		grand = rootGrand;
		/*
		//bug 1 Ӧ�����ϲ��delete��������£���Ϊ��delete���ܻ��õ��ݹ����Ρ�
		//����ǰ���ĸ��ڵ�Ϳ�ɺ�ɫ
		//root.color = RED;
		*/
		//��ʼ���µ���,ֱ��������nullNode��
		while(current.element != null) {
			//
			//fix��x��Ϊ��ɫ
			judgeAndFix(x);
			//��Ϊ������ı�ɫ���������ܰ�null�ڵ�Ҳ����˺�ɫ�����δ֪��Ϊ�˷�ֹ�������⣬ǿ�Ʊ�Ϊ��ɫ��
			nullNode.color = BLACK;
			
			//
			int compareResult = compare(x, current);
			if(compareResult == 0) {
				//�����˫���ӵ����
				if(current.left.element != null && current.right.element != null) {
					//˫���ӵ�����£����õ�ǰ�ڵ�������������ֵ���滻
					current.element = findMin(current.right).element;
					
					//bug 2 �������ֻ������Ԫ�ص�ʱ�򣬻������������delete�Ĵ�����ʽ���¡�
					//׷����grand�����������������������Ҫrotate��ʱ�򣬲������ͬʱҲ������Ҫ����ֵ��
					//current.right = delete(current.right, current, current.element)
					delete(current.right, current, father, current.element);
					
					//ע�⵽������ݹ�delete֮��currentɶ�Ķ�����ȫ���ҵģ������ã�������Ϊdelete�������ֻ��ִ�����֣����Բ���Ӱ�졣
					//������ɺ�ֱ��break������
				}
				//�����ӻ���û���ӵ����,���ܰѵ����Ӻ�û���������������һ��
				else {
					//��Ҷ��û���ӣ�ֱ�Ӹɵ�
					if(current.left.element == null && current.right.element == null) {
						if(compare(current.element, father) < 0) {
							father.left = nullNode;
						}
						else {
							father.right = nullNode;
						}
					}
					//�����ӣ�����ע�⵽�������ӵ�����£�ɾ���ڵ����ֻ��һ�����ӡ�
					//������������£���ɾ���ڵ��Ȼ�Ǻ�ɫ�ģ���������ӻ����Ҷ��ӱ�Ȼ�Ǻ�ɫ�ġ�
					//ֱ��������ӻ����Ҷ��ӵ�ֵ���滻ɾ���ڵ��ֵ����ɾ���Ҷ��ӻ����Ҷ��ӡ�
					else {
						Node<T> temp = current.left.element != null ? current.left : current.right;
						current.element = temp.element;
						current.left = current.right = nullNode;
					}
				}
				//ɾ����ֱ���˳�
				break;
			}
			
			//����׷��·��
			grand = father;
			father = current;
			
			
			//���µ���һ��,�ж�x�͵�ǰ�ڵ�,С�������󣬴������ң����ڲ�������
			if(compareResult < 0) {
				current = current.left;
			}
			else {
				current = current.right;
			}
		
		}
		//bug 1 
		//����ǰ�����ĸ��ڵ�������ɫΪ��ɫ��
		//root.color = BLACK;
		//return subTreeRoot;
	}
	
	/**
	 * Ѱ�ҵ�ǰ������Сֵ�����ҷ������Ľڵ�����
	 * @param right
	 * @return
	 */
	private Node<T> findMin(Node<T> t) {
		//һֱ����ֱ����null�ڵ㡣�����Ǹ�������Сֵ��
		while(t.left.element != null) {
			t = t.left;
		}
		
		return t;
	}

	/**
	 * Ϊ�˷��㣬�趨XΪ��ǰ�ڵ㣬PΪ���ڵ㣬TΪ�ֵܽڵ㣬GΪ�游�ڵ㡣
	 * 
	 * ���ݵݹ���������ǵ�Ŀ���Ǳ�֤������ɾ���Ĺ����У��跨��֤X�ڵ�Ϊ��ɫ������ֱ��XΪ����Ҷ���Ϳ���ֱ��ɾ����
	 * Ҳ����˵����X��������һ��ڵ�ʱ��P����Ϊ��ɫ�Ĳ���X��TΪ��ɫ�ġ�
	 * 
	 * �жϵ�ǰ�ڵ���ӵ��������������Ӧ�Ĵ������ƿ��Է�ΪA��B���ִ������
	 * A��X�������ڶ���
	 * 1.TҲ�������ڶ��ӡ���ô��X,T,P����ɫȫ����תһ�鼴�ɡ�
	 * 2.T������֮һ�Ǻ�ɫ�ġ���ô��Ҫ��һЩ��ת��������ɫ�Ĵ���
	 *    //���T�ڵ�Ķ���֮һ�Ǻ�ɫ�ġ�case A-2.����������£��ֿ��Է�Ϊ�������
	 *	  //���P��T��R�γ���һ���Σ�ֻ��Ҫһ�ε���ת+����ɫ���ɡ�
	 *	  //���P,T,R�γ���֮���Σ���ô��Ҫһ��˫��ת+����ɫ���С�
	 *    //���Ը��ò������̵�rotate
	 * 
	 * B��X����������֮һ�Ǻ�ɫ�ģ�����X�Ķ���ȫ�Ǻ�ɫ������������ȣ���������һ�㡣
	 * 1.������˵Ļ�,x���µ������˺���ӣ���ô���Ǽ�������һ�㣬��ʱX�ض�Ϊ��ɫ�ˡ��ص�������жϵ�ԭʼ״̬
	 * 2.����������˵Ļ���x���µ������˺ڶ��ӣ����Ǿ���Ҫ������תP��T����������ɫT��P������x�ĸ����ֱ�غ�ɫ�ˡ��ص����A��
	 * 
	 * 
	 * @param x ��ɾ���ڵ��ֵ����Ҫ�����ж�X��P��T��G֮��·����״����ͬ��·����״����Ҫ��ͬ����ת����ɫ����
	 * ͬʱ���ú�����ʹ�������Ա����(current,father,grand)������·����Ϣ��
	 */
	private void judgeAndFix(T x) {
		//���B������������������������Ҫ������һ�֡�Ŀ���Ѵﵽ���ʹ˰���
		if(current.color == RED) {
			return;
		}
		
		//���B�Ĳ������,ֻ�д�ʱ��P�ڵ���Ǻ�ɫ
		if(father.color == BLACK) {
			//������ɫ
			Node<T> brother = compare(x, father) < 0 ? father.right : father.left;
			brother.color = BLACK;
			father.color = RED;
			//��ȷ��ת������������,���ҽ�current��father��grand֮��������˳��ȷ����ȷ����ͼ��⡣
			if(compare(x, grand) < 0) {
				if(compare(x, father) > 0) {
					grand.left = simpleRotateWithLeftChild(grand.left);
				}
				else {
					grand.left = simpleRotateWithRightChild(grand.left);
				}
				//ע��������ȷ���������ú�grand��ֵ��
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
	
		//���A��B���ж�
		if(hasTowBlackChild(current)) {
			//���A
			//��ȡ��ǰ�ڵ���ֵܶ����ж����������ӵ�״̬
			Node<T> brother = compare(x, father) < 0 ? father.right : father.left;
			if(hasTowBlackChild(brother)) {
				//���T�ڵ�Ҳ��������ɫ�Ķ��ӣ���ôcase A-1��������ɫ����
				father.color = BLACK;
				current.color = RED;
				brother.color = RED;
			}
			else {
				//���T�ڵ�Ķ���֮һ�Ǻ�ɫ�ġ�case A-2.����������£��ֿ��Է�Ϊ�������
				//���P��T��R�γ���һ���Σ�ֻ��Ҫһ�ε���ת+����ɫ���ɡ�
				//���P,T,R�γ���֮���Σ���ô��Ҫһ��˫��ת+����ɫ���С�
				//���Ը��ò������̵�rotate
				//��ȡbrother�ڵ��еĺ�ڵ�
				Node<T> redNodeWithBrother = brother.left.color == RED ? brother.left : brother.right;
				//�����Ǹ���T�ڵ�����������
				grand = rotate(redNodeWithBrother.element, grand);
				//�ٸ���R��T��P�γɵ�·��������������ɫ
				recolor(father, brother, redNodeWithBrother,current);
				
				//TEST
				this.printTree();
			}
		}
		else {
			//���B���ȵ�����һ�֡�
			return;
		}
		
	}
	
	
	/**
	 * case A-2����ת���������ɫ��������P��T��R��redredNodeWithBrother���γɵ�·����״�жϡ�
	 * ͨ���۲��֪����·��Ϊ֮����ʱ��ֻ�øı�X��P����
	 * ��·��Ϊһ���Σ�����Ҫ��תȫ���ڵ����ɫ��P��T��X��R��
	 * 
	 * @param father
	 * @param brother
	 * @param redNodeWithBrother
	 * @param current
	 */
	private void recolor(Node<T> father, Node<T> brother, Node<T> redNodeWithBrother, Node<T> current) {
		//�ж�·������״���������ʹ������ж����ҹ�ϵ�Ƿ���ͬ
		if((compare(brother.element, father) < 0) ^ (compare(redNodeWithBrother.element, brother) < 0)) {
			//֮����
			current.color = RED;
			father.color = BLACK;
		}
		else {
			//һ����
			current.color = RED;
			brother.color = RED;
			father.color = BLACK;
			redNodeWithBrother.color = BLACK;
		}
	}

	/**
	 * �жϵ�ǰ�ڵ��Ƿ���������ɫ�Ķ��ӡ�
	 * ֵ��ע����ǣ�����Ķ���Ҳ����null�ڵ㣬Ҳ����˵�����ǰ�ڵ�����Ҷ����ôһ���᷵��true
	 * @param current2
	 * @return
	 */
	private boolean hasTowBlackChild(Node<T> current) {
		return current.left.color == BLACK && current.right.color == BLACK;
	}
}


