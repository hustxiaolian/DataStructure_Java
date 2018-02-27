package chapterFour;

public class BTreeInternalNode<K extends Comparable<? super K>> extends AbstractBTreeNode<K> {
	/*
	 * 节点属性
	 */
	private final Object[] keys;//关键字
	private final AbstractBTreeNode<K>[] children;//儿子们，指向下个节点或者树叶
	private int nkey = 0;
	private int nchild = 0;
	
	@SuppressWarnings("unchecked")
	BTreeInternalNode(int degree) {
		super(degree);
		keys = new Object[degree * 2 - 1];
		children = new AbstractBTreeNode[2 * degree];
	}

	@Override
	boolean isLeaf() {
		return false;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	K search(K key) {
		int index = indexOfKey(key);
		if(index > 0) {
			return (K) keys[index];
		}
		index = 0;
		while(key.compareTo((K) keys[index++]) > 0);
		return children[index].search(key);
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	void insertNotFull(K key) {
		checkNotFull();
		int i = 0;
		while(i < nkey && key.compareTo((K) keys[i]) >= 0) {
			i++;
		}
		
		if(this.children[i].isFull()) {
			this.splitChild(i);
			if(key.compareTo((K) this.keys[i]) > 0) {
				i++;
			}
		}
		//recursion,until this.children[i] is a leaf node,then use BTreeLeaf's insertNotFull method.
		this.children[i].insertNotFull(key);
	}

	@SuppressWarnings("unchecked")
	@Override
	void deleteNotEmpty(K key) {
		//key in this node  
        if (this.existsKey(key)) {  
            int index = indexOfKey(key);  
            AbstractBTreeNode<K> node;  
            //predecessor child could delete  
            if ((node = children[index]).nkey() >= degree) {  
                K repKey = node.getKey(node.nkey() - 1);                  //maximum key in predecessor  
                node.deleteNotEmpty(repKey);  
                setKey(repKey, index);  
            }  
            //follow child could delete a key  
            else if ((node = children[index + 1]).nkey() >= degree) {  
                K repKey = node.getKey(0);                              //minimum key in follow  
                node.deleteNotEmpty(repKey);  
                setKey(repKey, index);  
            }  
  
            //merge predecessor with follow  
            else {  
                node = children[index];  
                node.merge(key, children[index + 1]);  
                this.deleteKey(index);  
                this.deleteChild(index + 1);  
                node.deleteNotEmpty(key);  
            }  
        }  
        
        //key may exist in child  
        else{  
            int i = 0;  
            //find proper child the key may exists in  
            while (i < nkey) {  
                if (key.compareTo((K) keys[i]) < 0)  
                    break;  
                i++;  
            }  
            AbstractBTreeNode<K> target = children[i];  
            //child has enough key  
            if (target.nkey() >= degree) {  
                target.deleteNotEmpty(key);  
            } else {  
                AbstractBTreeNode<K> sibling;  
                //try to find replacement from predecessor  
                if (i > 0 && (sibling = children[i - 1]).nkey() >= degree) {  
                    if (!target.isLeaf()) {  
                        AbstractBTreeNode<K> sub = sibling.deleteChild(nchild); //last child  
                        target.insertChild(sub, 0);  
                    }  
                    K repKey = sibling.deleteKey(sibling.nkey() - 1);    //maximum key  
                    repKey = setKey(repKey, i - 1);  
                    target.insertKey(repKey);  
                    target.deleteNotEmpty(key);  
                }  
                //try to find replacement from follower  
                else if (i < nkey && (sibling = children[i + 1]).nkey() >= degree) {  
                    if (!target.isLeaf()) {  
                        AbstractBTreeNode<K> sub = sibling.deleteChild(0);  //first child  
                        target.insertChild(sub, target.nChild());  
                    }  
                    K repKey = sibling.deleteKey(0);                    //minimum key  
                    repKey = setKey(repKey, i);  
                    target.insertKey(repKey);  
                    target.deleteNotEmpty(key);  
                }  
                //merge child with one of it's sibling  
                else {  
                    //merge with predecessor sibling  
                    if (i > 0) {  
                        K repKey = this.deleteKey(i - 1);  
                        sibling = children[i - 1];  
                        sibling.merge(repKey, target);  
                        this.deleteChild(target);  
                        sibling.deleteNotEmpty(key);  
                    } else {  
                        K repKey = this.deleteKey(i);  
                        sibling = children[i + 1];  
                        target.merge(repKey, sibling);  
                        this.deleteChild(i + 1);  
                        target.deleteNotEmpty(key);  
                    }  
                }  
            }  
        }
    
		
	}

	@SuppressWarnings("unchecked")
	@Override
	K getKey(int idx) {
		return (K)keys[idx];
	}

	@SuppressWarnings("unchecked")
	@Override
	protected K setKey(K key, int oldKeyIndex) {
		K oldKey = (K)keys[oldKeyIndex];
		keys[oldKeyIndex] = key;
		return oldKey;
	}

	@Override
	protected void setChild(AbstractBTreeNode<K> sub, int index) {
		children[index] = sub;
	}

	@Override
	AbstractBTreeNode<K> getChild(int index) {
		if(index >= 0 && index < children.length) {
			return children[index];
		}
		return null;
	}

	@Override
	protected void splitChild(int child) {
		AbstractBTreeNode<K> old = children[child];
		AbstractBTreeNode<K> neo = old.isLeaf()? 
				new BTreeLeaf<>(degree):new BTreeInternalNode<>(degree);
		
		K middle = old.splitSelf(neo);
		this.insertKey(middle);
		this.insertChild(neo, child + 1);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected K splitSelf(AbstractBTreeNode<K> newNode) {
		if(! (newNode instanceof BTreeInternalNode)) {
			throw new RuntimeException("instance not match.");
		}
		
		if(!isFull()) {
			throw new RuntimeException("Node is not full.");
		}
		
		K middle = (K)keys[degree - 1];
		BTreeInternalNode<K> node = (BTreeInternalNode<K>) newNode;
		int i = 0;
		while(i < degree - 1) {
			node.keys[i] = this.keys[i + degree];
			this.keys[i + degree] = null;
			i++;
		}
		this.keys[degree - 1] = null;
		
		i = 0;
		while(i < degree) {
			node.children[i] = this.children[i + degree];
			this.children[i + degree] = null;
			i++;
		}
		
		this.nkey = degree - 1;
		node.nkey = degree - 1;
		this.nchild = degree;
		node.nchild = degree;
		return middle;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void merge(K middle, AbstractBTreeNode<K> sibling) {
		if(! (sibling instanceof BTreeInternalNode)) {
			throw new RuntimeException("Sibling is not internal node");
		}
		BTreeInternalNode<K> node = (BTreeInternalNode<K>)sibling;
		this.insertKey(middle);
		for(int i = 0;i < node.nkey();++i) {
			this.insertKey((K)node.keys[i]);
		}
		for(int i = 0;i < node.nChild();++i) {
			this.insertChild(node.children[i], i + degree);
		}
	}

	@Override
	protected int setNKey(int nkey) {
		int old = this.nkey;
		this.nkey = nkey;
		return old;
	}

	@Override
	int nkey() {
		return this.nkey;
	}

	@Override
	int nChild() {
		return this.nchild;
	}

	@Override
	protected int setNChild(int nchild) {
		int old = this.nchild;
		this.nchild = nchild;
		return old;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("internal node----").append("size: ").append(nkey).append(" keys:[");
		for(int i = 0;i < nkey();++i) {
			sb.append(keys[i]).append(",");
		}
		sb.append("]");
		return sb.toString();
	}
}
