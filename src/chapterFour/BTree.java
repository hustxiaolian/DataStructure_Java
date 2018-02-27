package chapterFour;

public class BTree<K extends Comparable<? super K>> {
	
	public static void main(String[] args) {
		BTree<Integer> t = new BTree<>(5);
		for(int i = 1;i <= 100;++i) {
			t.insert(i);
			System.out.println("-----------insert:"+i+"-------------");
			System.out.println(t);
			
		}
		System.out.println(t);
	}
	
	private final int degree;
	private AbstractBTreeNode<K> root;
	
	public BTree(int degree) {
		if(degree < 2) {
			throw new IllegalArgumentException("degree must >= 2");
		}
		this.degree = degree;
		root = new BTreeLeaf<>(degree);
	}

	public AbstractBTreeNode<K> getRoot() {
		return root;
	}
	
	public void insert(K key) {
		AbstractBTreeNode<K> n = root;
		if(root.isFull()) {
			AbstractBTreeNode<K> newRoot = new BTreeInternalNode<>(degree);
			newRoot.insertChild(n, 0);
			newRoot.splitChild(0);
			n = newRoot;
			root = newRoot;
		}
		n.insertNotFull(key);
	}
	
	
	public void delete(K key) {
		AbstractBTreeNode<K> node = root;
		node.deleteNotEmpty(key);
		if(node.nkey() == 0) {
			root = node.getChild(0);
			if(root == null) {
				root = new BTreeLeaf<>(degree);
			}
		}
	}
	
	public String toString() {
		return AbstractBTreeNode.BTreeToString(root);
	}
}
