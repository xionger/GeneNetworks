/**
 * 
 */
package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * @author Your name here.
 * 
 * For the warm up assignment, you must implement your Graph in a class
 * named CapGraph.  Here is the stub file.
 *
 */
public class CapGraph implements Graph {

	/* (non-Javadoc)
	 * @see graph.Graph#addVertex(int)
	 */
	private HashMap<Integer,HashSet<Integer>> graph;
	
	/** 
	 * Create a new empty Graph
	 */
	public CapGraph () {
		graph = new HashMap<Integer,HashSet<Integer>>();
	}
	
	public Set<Integer> getVertices(){
		return graph.keySet();
	}
	
	@Override
	public void addVertex(int num) {
		// TODO Auto-generated method stub
		if (graph.containsKey(num)){
			return;
		}
		graph.put(num, new HashSet<Integer>());	
	}

	/* (non-Javadoc)
	 * @see graph.Graph#addEdge(int, int)
	 */
	@Override
	public void addEdge(int from, int to) {
		// TODO Auto-generated method stub
		if (! graph.containsKey(from)){
			return;
		}
		graph.get(from).add(to);
	}

	/* (non-Javadoc)
	 * @see graph.Graph#getEgonet(int)
	 */
	@Override
	public Graph getEgonet(int center) {
		// TODO Auto-generated method stub
		HashSet<Integer> neighbors = getNeighbors(center);
		HashSet<Integer> inNeighbors = getInNeighbors(center);
		
		Graph egonet = new CapGraph();
		egonet.addVertex(center);
		
		for (int node: neighbors){
			egonet.addVertex(node);
			egonet.addEdge(center, node);
			
			if (isNeighbor(node, center)){
				egonet.addEdge(node, center);
			}
			
			Set<Integer> nodesNeighbors = new HashSet<>(graph.get(node));
			nodesNeighbors.retainAll(neighbors);
			
			for (int i : nodesNeighbors){
				egonet.addVertex(i);
				egonet.addEdge(i, node);
			}
		}
		
		return egonet;
	}

	/* (non-Javadoc)
	 * @see graph.Graph#getSCCs()
	 */
	@Override
	public List<Graph> getSCCs() {
		// TODO Auto-generated method stub

		List<Graph> SCCs = new ArrayList<>();
		
		Stack<Integer> verticesStack = new Stack<>();
		verticesStack.addAll(graph.keySet());
		
		Stack<Integer> finished = dfs(this, verticesStack);
		
		CapGraph transGraph = getTranspose(this);
		
		dfsSccGraph(transGraph, finished, SCCs);
				
		return SCCs;
	}
	
	private Stack<Integer> dfs(CapGraph g, Stack<Integer> vertices){
		Set<Integer> visited = new HashSet<>();
		Stack<Integer> finished = new Stack<>();
		int vertex;
		
		while (!vertices.isEmpty()){
			vertex = vertices.pop();
			
			if(!visited.contains(vertex)){
				dfsVisit(g, vertex, visited, finished);
			}
		}
				
		return finished;
	}
	
	private Stack<Integer> dfsSccGraph(CapGraph g, Stack<Integer> vertices, List<Graph> sccs){
		Set<Integer> visited = new HashSet<>();
		Stack<Integer> finished = new Stack<>();
		int vertex;
		
		List<Graph> sccList = new ArrayList<>();
		
		while (!vertices.isEmpty()){
			vertex = vertices.pop();
			
			Graph scc = new CapGraph();
			if(!visited.contains(vertex)){
				dfsVisit(g, vertex, visited, finished);
				
				int ver = finished.pop();
				scc.addVertex(ver);
				while(!finished.isEmpty()){
					ver = finished.pop();
					scc.addEdge(vertex, ver);
				}
				sccs.add(scc);
			}
			
		}
				
		return finished;
	}
	
	private void dfsVisit(CapGraph g, int v, Set<Integer> visited, Stack<Integer> finished){
		visited.add(v);
		for (int n: g.getNeighbors(v)){
			if (! visited.contains(n)){
				dfsVisit(g, n, visited, finished);
			}
		}
		
		finished.push(v);
	}
 
	/* (non-Javadoc)
	 * @see graph.Graph#exportGraph()
	 */
	@Override
	public HashMap<Integer, HashSet<Integer>> exportGraph() {
		// TODO Auto-generated method stub
		return graph;
	}
	
	/** 
	 * Finding all out-neighbors of a vertex.
	 * If there are multiple edges between the vertex
	 * and one of its out-neighbors, this neighbor
	 * appears once in the list for each of these edges.
	 * 
	 * @param v the index of vertex.
	 * @return List<Integer> a list of indices of vertices.
	 */	
	public HashSet<Integer> getNeighbors(int v) {
		return new HashSet<Integer>(graph.get(v));
	}
	
	/** 
	 * Finding all in-neighbors of a vertex.
	 * If there are multiple edges from another vertex
	 * to this one, the neighbor
	 * appears once in the list for each of these edges.
	 * 
	 * @param v the index of vertex.
	 * @return List<Integer> a list of indices of vertices.  
	 */	
	public HashSet<Integer> getInNeighbors(int v) {
		HashSet<Integer> inNeighbors = new HashSet<Integer>();
		for (int u : graph.keySet()) {
			//iterate through all edges in u's adjacency list and 
			//add u to the inNeighbor list of v whenever an edge
			//with startpoint u has endpoint v.
			for (int w : graph.get(u)) {
				if (v == w) {
					inNeighbors.add(u);
				}
			}
		}
		return inNeighbors;
	}
	
	private boolean isNeighbor(int n1, int n2){
		return graph.get(n1).contains(n2);
	}
	
	public CapGraph getTranspose(CapGraph graph){

		CapGraph transGraph = new CapGraph();
		
		for (int v : graph.getVertices()){
			transGraph.addVertex(v);
			for (int u : graph.getNeighbors(v)){
				transGraph.addVertex(u);
				transGraph.addEdge(u, v);
			}
		}
		return transGraph;
		
	}
	
}
