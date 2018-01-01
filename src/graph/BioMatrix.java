package graph;

import java.util.ArrayList;
import java.util.List;

public class BioMatrix {
	private final int defaultNumVertices = 1;
	
	private int numVertices;
	private int numEdges;
	private int[][] adjMatrix;
	
	/** Create a new empty Graph */
	public BioMatrix () {
		adjMatrix = new int[defaultNumVertices][defaultNumVertices];
		numVertices = defaultNumVertices;
		numEdges = 0;
	}
	
	/**
	 * Report size of vertex set
	 * @return The number of vertices in the graph.
	 */
	public int getNumVertices() {
		return numVertices;
	}
	
	/** 
	 * Implement the method for adding a vertex.
	 * If need to increase dimensions of matrix, double them
	 * to amortize cost. 
	 */
	public void implementAddVertex() {
		int v = getNumVertices();
		if (v >= adjMatrix.length) {
			int[][] newAdjMatrix = new int[v*2][v*2];
			for (int i = 0; i < adjMatrix.length; i ++) {
				for (int j = 0; j < adjMatrix.length; j ++) {
					newAdjMatrix[i][j] = adjMatrix[i][j];
				}
			}
			adjMatrix = newAdjMatrix;
		}
	}
	
	/** 
	 * Implement the method for adding an edge.
	 * Allows for multiple edges between two points:
	 * the entry at row v, column w stores the number of such edges.
	 * @param v the index of the start point for the edge.
	 * @param w the index of the end point for the edge.  
	 */	
	public void implementAddEdge(int v, int w) {
		adjMatrix[v][w] += 1;
	}
	
	/** 
	 * Implement the method for finding all 
	 * out-neighbors of a vertex.
	 * If there are multiple edges between the vertex
	 * and one of its out-neighbors, this neighbor
	 * appears once in the list for each of these edges.
	 * 
	 * @param v the index of vertex.
	 * @return List<Integer> a list of indices of vertices.  
	 */	
	public List<Integer> getNeighbors(int v) {
		List<Integer> neighbors = new ArrayList<Integer>();
		for (int i = 0; i < getNumVertices(); i ++) {
			for (int j=0; j< adjMatrix[v][i]; j ++) {
				neighbors.add(i);
			}
		}
		return neighbors;
	}
	
	/** 
	 * Implement the method for finding all 
	 * in-neighbors of a vertex.
	 * If there are multiple edges from another vertex
	 * to this one, the neighbor
	 * appears once in the list for each of these edges.
	 * 
	 * @param v the index of vertex.
	 * @return List<Integer> a list of indices of vertices.  
	 */
	public List<Integer> getInNeighbors(int v) {
		List<Integer> inNeighbors = new ArrayList<Integer>();
		for (int i = 0; i < getNumVertices(); i ++) {
			for (int j=0; j< adjMatrix[i][v]; j++) {
				inNeighbors.add(i);
			}
		}
		return inNeighbors;
	}
	
	/** 
	 * Implement the method for finding all 
	 * vertices reachable by two hops from v.
	 * Use matrix multiplication to record length 2 paths.
	 * 
	 * @param v the index of vertex.
	 * @return List<Integer> a list of indices of vertices.  
	 */	
	public List<Integer> getDistance2(int v) {
		// XXX Implement this method in week 2
		 List<Integer> distanceTwo = new ArrayList<Integer>();
		 List<Integer> secondVer = new ArrayList<Integer>();
		 List<Integer> firstVer = getNeighbors(v);
		 for (Integer i: firstVer){
			 secondVer = getNeighbors(i);
			 for (Integer j: secondVer){
				 distanceTwo.add(j);
			 }
		 }
		 
		 return distanceTwo;
	}
	
	/**
	 * Generate string representation of adjacency matrix
	 * @return the String
	 */
	public String adjacencyString() {
		int dim = getNumVertices();
		String s = "Adjacency matrix";
		s += " (size " + dim + "x" + dim + " = " + dim* dim + " integers):";
		for (int i = 0; i < dim; i ++) {
			s += "\n\t"+i+": ";
			for (int j = 0; j < dim; j++) {
			s += adjMatrix[i][j] + ", ";
			}
		}
		return s;
	}
	
	/** calculate the number of connected components in an graph
	 * 
	 */
	 public int countComponents(BioMatrix bmg){
		 int n = bmg.numVertices;
		 int count = n;
		 
		 int[] root = new int[n];
		 
		 //initialize each vertex is an island
		 for(int i=0; i<n; i++){
			 int x = bmg.adjMatrix[i][0];
			 int y = bmg.adjMatrix[i][1];
			 
			 int xRoot = getRoot(root, x);
			 int yRoot = getRoot(root, y);
			 
			 if (xRoot != yRoot){
				 count--;
				 root[xRoot] = yRoot;
			 }
		 }		 
		 return count;
	 }
	 
	 /*
	  * Helper method
	  */
	 public int getRoot(int[] arr, int i){
		 while(arr[i] != i){
			 arr[i] = arr[arr[i]];
			 i = arr[i];
		 }
		 return i;
	 }
}
