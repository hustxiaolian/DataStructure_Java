package chapterTwelve;

import java.util.Random;

/**
 * һ��treap������һ��������������������ڵ����ȼ���������ԡ�����ڵ�����ȼ��������ٺ������ڵ�����ȼ�һ����
 * �������Լ�����⣬������һ����priority�������϶��������Ϣ����С����ѡ���������������ʽʵ�֣����������顣
 * ֵ��ע�����,���nullNode�ڵ�����ȼ�Ϊ�����
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
	
	//�����Ҳο��º������ʵ��
	private TreapNode<T> header;
	private TreapNode<T> nullNode;
	
	/**
	 * �ڵ���ʽ���������������񣬶���һ�����ȼ���������
	 * һ���ڵ�����ȼ������ָ���ġ�
	 * 
	 * @author 25040
	 *
	 * @param <T>
	 */
	private static class TreapNode<T>{
		//�������������
		private static Random randomObj = new Random();
		
		//�ڵ���ʽ���������������񣬶���һ�����ȼ���������
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
			//�������һ�����ȼ�
			this.priority = randomObj.nextInt(Integer.MAX_VALUE - 1);
		}
	}
	
	/**
	 * ��ʼ��header�ڵ��null�ڵ㣬��header�ڵ�����ȼ���Ϊ��ͣ�null��Ϊ���
	 */
	public treapTree() {
		this.nullNode = new TreapNode<T>(null);
		this.nullNode.priority = Integer.MAX_VALUE;
		this.header = new TreapNode<T>(null,nullNode,nullNode);
		this.header.priority = Integer.MIN_VALUE;
		
	}
	/**
	 * treap���Ĳ������̡�
	 * @param x
	 */
	public void insert(T x) {
		insert(x, header);
	}
	
	/**
	 * treap����ɾ�����̡�
	 * Ҳ����һ�ִ�ǰû�м������ķ�ʽ��
	 * �ܽ���˵�������������У����Ѿ�ѧϰ����ͨ�Ķ�������AVLƽ��������������Լ����µ�treap���Ĳ����ɾ�����̡�
	 * �´��ܽ��¡�ͬʱB+�������ǲ���Ϥ��
	 * @param x
	 */
	public void remove(T x) {
		remove(x, header);
	}
	
	/**
	 * �������������������
	 */
	public void printTree() {
		printTree(header.right, 0);
	}
	
	/**
	 * treap���ĵݹ�������̡�
	 * ����Ҫ˼���ǣ�
	 * ���ǽ������뵽������Ҷ�У�Ȼ��������ת�������������ȼ�������Ϊֹ��
	 * 
	 * ��ʵ���Ƕ���������������·��
	 * 
	 * @param x
	 * @param t
	 * @return
	 */
	private TreapNode<T> insert(T x, TreapNode<T> t){
		//���ݹ鵽��Ҷʱ�������µ�treapNode�����������������Ӷ���ΪnullNode
		//bug 1 header�ڵ��element������Ҳ��null����˻����
		if(t == nullNode) {
			t = new TreapNode<T>(x, nullNode, nullNode);
			return t;
		}
		
		int compareResult = compare(x, t);
		if(compareResult < 0) {
			t.left = insert(x, t.left);
			//ÿ�εݹ������Ҫ������Ƿ���������ԣ�Ҳ���ǵ�ǰ�ڵ�����ȼ��Ƿ�С�ڵ����������ӵ����ȼ���
			if(t.priority > t.left.priority) {
				//�������������ԣ�����ֻ��Ҫͨ����ת���ı�ڵ�֮��Ĺ�ϵ���ɡ�
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
			//������У��Ѿ������ظ��ڵ㣬��ô�����κζ�����
		}
		
		return t;
	}
	
	/**
	 * ���¶�����compare������ʹ����header��ʱ�����ǰ��½ڵ���뵽header�ڵ���������С�
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
	 * ����ת����
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
	 * �ҵ���ת����
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
	 * ������������ĵݹ�˽�����̡�
	 * ����Ϊ�˷�����˽�еĹ۲죬�ȴ�ӡ�����������ӡ��������
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
	 * ����������˼�룺
	 * ���ҵ������иýڵ�ʱ�����������ȼ��ᵽ�������Ȼ��ͨ����תֱ����Ҷ���ͷŵ���
	 * 
	 * ����ڵ����ѵ�˼�룬��������ʽ���������������ɾ��������ʽ��������ʱ�ݹ����̡�
	 * 
	 * ֵ��ע����ǣ����ҵ���ɾ���ڵ��Ĵ������ϣ�����ǰ�ķ�����̫һ�������ҵ���ǰ�Ľڵ��
	 * ���ǿ��Թ�һ������֮ǰ������������������ӣ�һ�����Ӻ�û�ж��ӣ�
	 * ��ֱ���ȱȽ�����ת��������תһ�Ρ�
	 * 
	 * ���Ƿ�Ϊ������������ۣ����ǾͿ���ֱ�������ַ�������Ч�ġ�
	 * 1.��tû�ж���ʱ���������������Ӷ���nullNode�ڵ㣬��ô��������ѡ������ת��������ת����ônullNode�������ǰ�ڵ��λ�á�
	 * 	Ȼ�������ж�t�Ƿ�ΪnullNode����˵���Ѿ��ݹ鵽���ĵײ㡣������ֹ�ˡ�
	 * 
	 * 2.���ֻ��һ�����ӣ�����nullNode�����ȼ�Ϊ����������ԣ�һ������ת�����ж��ӵıߡ�Ȼ�������ڰ�remove���̵ݹ鵽��ǰ�ڵ㡣
	 * 		�ɴˣ����Իص����1����
	 * 
	 * 3.������������ӣ�����ѡ���������������ȼ���С��һ����ʹtreap�����ֶ����ԡ�Ȼ�����ǰ�ɾ�����̵ݹ鵽t���������Իᵽ���1-3��һ�֡�
	 * ���ն���ص����1.
	 * 
	 * �������Ǿ��õݹ����̵�ǿ��֮�����������Ǹ�return t���Զ��Ĵ����˺ö������
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
				//�ҵ��ˣ��Ǻã����ǽ�������ȼ��ᵽ�����Ȼ��������������Ĳ�ͬ��
				if(t.left.priority < t.right.priority) {
					t = simpleRotateWithLeftChild(t);
				}
				else {
					t = simpleRotateWithRightChild(t);
				}
				
				//�ж��Ƿ��Ѿ��������1
				if(t == nullNode) {
					//��������Ҷ������£�����nullNode�ڵ�����ȼ�������ǿ����ȣ���ˣ���������ת��
					t.left = nullNode;
				}
				else {
					//��������2�������3��������t�ϵݹ�remove���̣������������Զ��ж�һ��·���󣬻ص�����ط���
					//һ�еľ��趼�������return t���������ϡ�
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
	 * treap���Ĳ������̡�
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
