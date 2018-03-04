package chapterFour;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Abstract Node.
 * 抽象的节点类，定义一些无论是树叶抑或是节点都通用的方法
 * @author 25040
 *
 * @param <K>
 */
public abstract class AbstractBTreeNode<K extends Comparable<? super K>> {
	
	protected final int degree;
	
	AbstractBTreeNode(int degree){
		if(degree < 2) {
			throw new IllegalArgumentException("degree must >= 2");
		}
		this.degree = degree;
	}
	
	/**
	 * if the node is leaf.
	 * 
	 * @return true if is leaf,false if not.
	 */
	abstract boolean isLeaf();
	
	/**
	 * Search key in the B-tree node
	 * 
	 * First,in current node, search the key.if yes{@code index > 0}},return the key directly. 
	 * Else,get the correct child and recursion search to next node.
	 * 
	 * @param key the key to search
	 * @return key in the B-tree or null if key does not exist in the tree.
	 */
	abstract K search(K key);
	
	/**
	 * Insert a key in to a node when the node is not full.
	 * 
	 * @param key the key to insert.
	 * @throws java.lang.RuntimeException if node is full.
	 */
	abstract void insertNotFull(K key); 
	
	/**
	 * Delete a key when the {@code keys >= degree}.
	 * if key to delete does not exist in current node,
	 * internal node will find a subtree 
	 * 
	 * @param key the key to delete.
	 */
	abstract void deleteNotEmpty(K key);
	
	void insertKey(K key) {
		checkNotFull();//判断节点非空
		int i = this.nkey();
		
		//move key
		while(i > 0 && key.compareTo(this.getKey(i - 1)) < 0) {
			this.setKey(this.getKey(i - 1),i);
			i--;
		}
		
		this.setKey(key, i);
		this.setNKey(this.nkey() + 1);
	}
	
	/**
	 * Get a key with index of {@code idx} in current node.
	 * 
	 * @param idx idx of key to get.
	 * @return key of given index.
	 * @throws java.lang.RuntimeException if {@code idx < 0}} or {@code idx >= degree * 2 - 1}}
	 */
	abstract K getKey(int idx);
	
	/**
	 * delete given key in current node.
	 * 
	 * @param key the key to delete.
	 * @return the key deleted or null if key does not exist.
	 */
	protected K deleteKey(K key) {
		int index = indexOfKey(key);
		if(index >= 0) {
			return this.deleteKey(index);
		}
		return null;
	}
	
	/**
	 * delete a key with given index.
	 * 
	 * @param index index of key to delete.
	 * @return key deleted or null if index is invalid.
	 * @throws java.lang.RuntimeException if index is invalid.
	 */
	protected K deleteKey(int index) {
		if (index < 0 || index > this.nkey()) {
			throw new RuntimeException("index is invalid.");
		}
		K result = this.getKey(index);
		while (index < this.nkey() - 1) {
			this.setKey(this.getKey(index + 1), index);
			index++;
		}
		this.setKey(null, this.nkey() - 1);
		this.setNKey(this.nkey() - 1);
		return result;
	}
	
	/**
	 * check if current exists given key.
	 * 
	 * @param key key to check.
	 * @return true is given key exists in current node.
	 */
	boolean existsKey(K key) {
		return indexOfKey(key) >= 0;
	}
	
	/**
	 * replace one key with new key.
	 * 
	 * @param oldKey
	 * @param newKey
	 */
	protected void replaceKey(K oldKey,K newKey) {
		int index = indexOfKey(oldKey);
		if(index >= 0) {
			this.setKey(newKey, index);
		}
	}
	
	/**
	 * replace given index key with a new key.
	 * 
	 * @param key 			the new key to insert in.
	 * @param oldKeyIndex	old key index.
	 * @return	the key be replaced or null if index is invalid.
	 */
	protected abstract K setKey(K key, int oldKeyIndex);
	
	/**
	 * set one of current child with given index.
	 * 
	 * @param sub child subtree
	 * @param index index of child to set.
	 */
	protected abstract void setChild(AbstractBTreeNode<K> sub,int index);
	
	/**
	 * insert a child at given index.
	 * 
	 * @param sub child subtree to insert
	 * @param index index of child to insert.
	 */
	protected void insertChild(AbstractBTreeNode<K> sub,int index) {
		int i = this.nChild();
		while(i > index) {
			this.setChild(this.getChild(i - 1), i);
			i--;
		}
		this.setChild(sub, index);
		this.setNChild(this.nChild() + 1);
	}
	
	/**
	 * get child with given index.
	 * 
	 * @param index index of child to get.
	 * @return child subtree of null if index is invalid.
	 */
	abstract AbstractBTreeNode<K> getChild(int index);
	
	/**
	 * delete given child in current node.
	 * 
	 * @param child child subtree to delete.
	 */
	protected void deleteChild(AbstractBTreeNode<K> child) {
		int index = -1;
		for(int i = 0; i < this.nChild();++i) {
			if(this.getChild(i) == child) {
				index = i;
				break;
			}
		}
		if(index >= 0) {
			deleteChild(index);
		}
	}
	
	/**
	 * delete child with given index
	 * 
	 * @param index index of child to delete
	 * @return child subtree or null if index is invalid.
	 */
	protected AbstractBTreeNode<K> deleteChild(int index) {
		AbstractBTreeNode<K> result = null;
		if(index >= 0 && index < this.nChild()) {
			result = this.getChild(index);
			//估计是使用数组保存的，因此有这样一个数组迁移的过程。
			while(index < this.nChild() - 1) {
				this.setChild(this.getChild(index + 1), index);
				++index;
			}
			this.setChild(null, index);
			this.setNChild(this.nChild() - 1);
		}
		return result;
	}
	
	/**
	 * split current node to two node.
	 * 
	 * @param child child index to split
	 * @throws java.lang.RuntimeException will throw when given child to split is not full.
	 * 
	 */
	protected abstract void splitChild(int child);
	
	/**
	 * Split current node to two node.
	 * 
	 * @param newNode new node.
	 * @return middle of current node before split.
	 * @throws java.lang.RuntimeException will throw when given child to split is not full.
	 */
	protected abstract K splitSelf(AbstractBTreeNode<K> newNode);
	
	/**
	 * merge current node with another.
	 * 
	 * @param middle middle key of the two node.
	 * @param sibling sibling node to merge.
	 * @throws java.lang.RuntimeException if keys of either node exceed degree - 1.
	 * 
	 */
	protected abstract void merge(K middle,AbstractBTreeNode<K> sibling); 
	
	/**
	 * Set key amount of current node.
	 * 
	 * @param neky key amount to set.
	 * @return old key amount.
	 */
	protected abstract int setNKey(int nkey);
	
	/**
	 * key amount of current node.
	 * 
	 * @return key amount of current node.
	 */
	abstract int nkey();
	
	/**
	 * child amount of current node.
	 * 
	 * @return child amount of current node.
	 */
	abstract int nChild();
	
	/**
	 * set child amount of current node.
	 * 
	 * @param nchild child amount.
	 * @return old child amount.
	 */
	protected abstract int setNChild(int nchild);

	/**
	 * get index of given key.
	 * @param key
	 * @return
	 */
	protected int indexOfKey(K key) {
		for(int i = 0;i < this.nkey();++i) {
			if(key.equals(this.getKey(i))) {
				return i;
			}
		}
		return -1;//error number
	}
	
	/**
	 * Check whether current node is full.
	 * 
	 * @return true if current node is full ,else false.
	 */
	protected boolean isFull() {
		return this.nkey() == degree * 2 - 1;
	}
	
	/**
	 * check current node is not full.
	 * 
	 * @throws java.lang.RuntimeException if current is full.
	 */
	protected final void checkNotFull() {
		if(isFull()) {
			throw new RuntimeException(this.toString() + "is full.");
		}
	}
	
	/**
	 * 很有意思，它使用队列的特性，使得它以层序从上至下的打印输出B树
	 * @param root
	 * @return
	 */
	static <K extends Comparable< ? super K>> String BTreeToString(AbstractBTreeNode<K> root) {
		StringBuffer sb = new StringBuffer();
		AbstractBTreeNode<K> node;
		Queue<AbstractBTreeNode> queue = new LinkedList<>();
		queue.add(root);
		//String newLine = System.getProperty("line.sparator");
		while(!queue.isEmpty()) {
			node = queue.poll();
			sb.append(node).append('\n');
			int i = 0;
			while(node.getChild(i) != null) {
				queue.offer(node.getChild(i));
				++i;
			}
		}
		return sb.toString();
	}
}
