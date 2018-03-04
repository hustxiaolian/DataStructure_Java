package chapterNine;

import java.util.ArrayList;
import java.util.List;

/**
 * ʹ������ʵ���ڽӱ�������е���������
 * @author 25040
 *
 */
public class ArrayVetex<vertexType> {
	
	/**
	 * �����ڽӱ�
	 * ʹ��List���洢��ǰ������
	 * @author 25040
	 *
	 */
	private static class VetexList<vertexType>{
		private vertexType thisVertexElement;
		private List<vertex<vertexType>> vertexList;
		
		
		
		public VetexList(vertexType thisVertexElement, vertex<vertexType>[] array) {
			this.vertexList = new ArrayList<>();
			this.thisVertexElement = thisVertexElement;
			
			for (int i = 0; i < array.length; i++) {
				this.vertexList.add(array[i]);
			}
		}
	}
	
	/**
	 * ������
	 * @author 25040
	 *
	 */
	private static class vertex<vertexType>{
		private vertexType element;
		private int weight;
		
		public vertex(vertexType element) {
			this(element,0);
		}
		
		public vertex(vertexType element, int weight) {
			this.element = element;
			this.weight = weight;
		}
	}
}

