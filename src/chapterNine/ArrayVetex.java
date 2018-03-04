package chapterNine;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用数组实现邻接表，完成书中的拓扑排序
 * @author 25040
 *
 */
public class ArrayVetex<vertexType> {
	
	/**
	 * 顶点邻接表
	 * 使用List来存储当前点相邻
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
	 * 顶点类
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

