package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;

import util.BioGraphLoader;

public class BioGraph {
	
	private HashMap<String,BioNode> geneMap;
	private HashSet<BioEdge> edges;
	
	private final double HUGE_DISTANCE = Double.POSITIVE_INFINITY;
	
	/** 
	 * Create a new empty Graph
	 */
	public BioGraph () {
		geneMap = new HashMap<String,BioNode>();
		edges = new HashSet<BioEdge>();
	}

	/**
	 * Add a new vertex into the graph
	 * @param gene The name of the vertex to be added
	 * @return whether the vertex is added
	 */
	public boolean addVertex(String gene) {

		if (gene == null) {
			return false;
		}
		BioNode n = geneMap.get(gene);
		if (n == null) {
			n = new BioNode(gene);
			geneMap.put(gene, n);
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Get all the vertices (genes) in this graph.
	 * @return The vertices in this graph as String
	 */
	public Set<String> getVertices()
	{
		return geneMap.keySet();
	}
	
	/**
	 * Adds a directed edge to the graph from gene1 to gene2.  
	 * Precondition: Both genes have already been added to the graph
	 * @param from The starting point of the edge
	 * @param to The ending point of the edge
	 * @param length The length of the connection
	 * @throws IllegalArgumentException If the points have not already been
	 *   added as nodes to the graph, if any of the arguments is null,
	 *   or if the length is less than 0.
	 */
	public void addEdge(String from, String to, double length) throws IllegalArgumentException {

		BioNode n1 = geneMap.get(from);
		BioNode n2 = geneMap.get(to);

		// check nodes are valid
		if (n1 == null)
			throw new NullPointerException("addEdge: gene1:"+from+"is not in graph");
		if (n2 == null)
			throw new NullPointerException("addEdge: gene2:"+to+"is not in graph");

		BioEdge edge = new BioEdge(n1, n2, length);
		edges.add(edge);
		n1.addEdge(edge);	
	}
	
	/** Remove connection between two vertices (genes)
	 * @param from The start point of the edge
	 * @param to The end point of the edge
	 * @throws IllegalArgumentException If the points have not already been
	 *   added as nodes to the graph, if any of the arguments is null
	 */
	public void removeEdges(String from, String to) throws IllegalArgumentException{
		
		BioNode startNode = geneMap.get(from);
		BioNode endNode = geneMap.get(to);
		// check nodes are valid
		if (startNode == null)
			throw new NullPointerException("addEdge: gene1:"+from+"is not in graph");
		if (endNode == null)
			throw new NullPointerException("addEdge: gene2:"+to+"is not in graph");
		
		for(Iterator<BioEdge> it = edges.iterator(); it.hasNext();){
			BioEdge e = it.next();
			if (e.getStartNode().gene.equals(from) & e.getEndNode().gene.equals(to)){
				it.remove();
			}
		}
		
		for(Iterator<BioEdge> st = startNode.getEdges().iterator(); st.hasNext();){
			BioEdge e = st.next();
			if (e.getStartNode().gene.equals(from) & e.getEndNode().gene.equals(to)){
				st.remove();
			}
		}
	}
	
	/** 
	 * Get a set of neighbor nodes from a mapNode
	 * @param node  The node to get the neighbors from
	 * @return A set containing the MapNode objects that are the neighbors 
	 * 	of node
	 */
	private Set<BioNode> getNeighbors(BioNode node) {
		return node.getNeighbors();
	}
	
	/** Find the path from start to goal using Dijkstra's algorithm
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<String> dijkstra(String start, String goal) {
		// Dummy variable for calling the search algorithms
        Consumer<String> temp = (x) -> {};
        return dijkstra(start, goal, temp);
	}
	
	/** Find the path from start to goal using Dijkstra's algorithm
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<String> dijkstra(String start, String goal, Consumer<String> nodeSearched)
	{
		
		if (start == null || goal == null)
			throw new NullPointerException("Cannot find route from or to null node");
		
		BioNode startNode = geneMap.get(start);
		BioNode endNode = geneMap.get(goal);
		
		if (startNode == null) {
			System.err.println("Start node " + start + " does not exist");
			return null;
		}
		if (endNode == null) {
			System.err.println("End node " + goal + " does not exist");
			return null;
		}

		// setup to begin BFS
		HashMap<BioNode,BioNode> parentMap = new HashMap<BioNode,BioNode>();
		PriorityQueue<BioNode> toExplore = new PriorityQueue<BioNode>();
		HashSet<BioNode> visited = new HashSet<BioNode>();
		
		for (BioNode n : geneMap.values()) {
			n.setDistance(startNode, HUGE_DISTANCE);
		}
		
		startNode.setDistance(startNode, 0.0);
		
		toExplore.add(startNode);
		BioNode curr = null;
		
		int dcount = 0;

		while (!toExplore.isEmpty()) {
			curr = toExplore.poll();
			//dcount ++;
			
			 // hook for visualization
			nodeSearched.accept(curr.getGene());
			
			if(!visited.contains(curr)) {
				visited.add(curr);
				dcount++;
				
				if (curr.equals(endNode)) {
					break;
				}
				
				Set<BioNode> neighbors = getNeighbors(curr);
				
				HashMap<BioNode, Double> distanceMap = edgeDistanesMap(curr);
				
				for(BioNode neighbor : neighbors) {
					if(!visited.contains(neighbor)) {
						double neighborDist = curr.getDistance() + distanceMap.get(neighbor);
						if(neighborDist < neighbor.getDistance()) {
							parentMap.put(neighbor, curr);
							neighbor.setDistance(startNode, neighborDist);
							toExplore.add(neighbor);
						}
					}
				}
			}
		}
		if (!curr.equals(endNode)) {
			System.out.println("No path found from " +start+ " to " + goal);
			return new ArrayList<String>();
		}
		// Reconstruct the parent path
		List<String> path = reconstructPath(parentMap, startNode, endNode);

		System.out.println("path for dijkstra: \n" + path + "\n Total nodes pathed: " + path.size());
		System.out.print("Nodes visited for dijkstra: " + dcount + "\n");
		return path;
	
	}
	
	/** Get distances of all the edges from a vertex
	 * @param node The vertex
	 * @return A map representing all the edges from a specific node and the corresponding distances
	 */
	private HashMap<BioNode, Double> edgeDistanesMap(BioNode node) {
		HashMap<BioNode, Double> distancesMap = new HashMap<>();

		for (BioEdge e : node.getEdges()) {
			distancesMap.put(e.getEndNode(), e.getLength());
		}
		return distancesMap;
	}
	
	/** Reconstruct a path from start to goal using the parentMap
	 *
	 * @param parentMap the HashNode map of children and their parents
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of vertices that form the shortest path from
	 *   start to goal (including both start and goal).
	 */
	private List<String> reconstructPath(HashMap<BioNode,BioNode> parentMap, BioNode start, BioNode goal)
	{
		LinkedList<String> path = new LinkedList<String>();
		BioNode current = goal;

		while (!current.equals(start)) {
			path.addFirst(current.getGene());
			current = parentMap.get(current);
		}

		// add start
		path.addFirst(start.getGene());
		return path;
	}
	
	@Override
	public String toString(){
		String graphText = "";
		for (Entry<String, BioNode> entry: geneMap.entrySet()){
			graphText += entry.getKey() + "->";
			for (BioEdge edge : entry.getValue().getEdges()){
				graphText += edge.getEndNode().getGene() + ", ";
				graphText += "distance: " + edge.getLength() + "; ";
			}
			graphText += "\n";
		}
		return graphText;
	}
	
	/** Determine the connected components in the graph
	 * @return List of String lists, each of which contains 
	 * genes in a connected component in the graph
	 */
	public List<List<String>> conponentSets(){
		Set<String> nds = getVertices();
		
		List<List<String>> scl = new ArrayList<List<String>>();
		
		if(nds == null || nds.size() == 0){
			return scl;
		}
		
		HashMap<String, Boolean> mp = new HashMap<String, Boolean>();
		
		for (String s : nds){
			mp.put(s, false);
		}
		
		for(String s : nds){
			if(!mp.get(s)){
				bfsConnections(scl, geneMap.get(s), mp);
			}
		}
		
		return scl;
	}
	
	/** Helper method for detecting the connected components in the graph
	 * It search through all the connected vertices from a vertex @param n
	 */
	public void bfsConnections(List<List<String>> scl, BioNode n, HashMap<String, Boolean> mp){
		Queue<BioNode> qu= new LinkedList<BioNode>();
		List<String> ls = new ArrayList<String>();
		
		qu.add(n);
		mp.put(n.gene, true);
		
		BioNode tn;
		
		while(!qu.isEmpty()){
			tn = qu.poll();
			ls.add(tn.gene);
			for(BioNode bn : getNeighbors(tn)){
				if (!mp.get(bn.gene)){
					qu.offer(bn);
					mp.put(bn.gene, true);
				}
			}
		}
		Collections.sort(ls);
		scl.add(ls);
	}
	
	/** Calculate the betweenness weights of all the edges in the graph
	 * @return a map containing all the edges and the corresponding weights of edge betweenness
	 */
	public HashMap<BioEdge, Double> getEdgeBetweennessWeight(){
		// init the betweenness-values for each edge with 0.0:
	    HashMap<BioEdge, Double> betweenness = new HashMap<BioEdge,Double>();
	    for (BioEdge e : edges) {
	        betweenness.put(e, 0.0);
	    }
	    
	    if(getVertices().size() == 0){
	    	System.out.println("No connection in the graph.");
	    	return betweenness;
	    }
	    
	    for (String startGene : getVertices()){
	    	for(String endGene : getVertices()){
	    		List<String> shortestPath = dijkstra(startGene, endGene);
	    		
	    		int numOfPathElements = shortestPath.size();
	    		
	    		if (numOfPathElements >= 2){
	    			for(int i=0; i< numOfPathElements - 1; i++){
	    				String frontGene = shortestPath.get(i);
	    				String behindeGene = shortestPath.get(i+1);
	    				
	    				for(Entry<BioEdge, Double> bwMp: betweenness.entrySet()){
	    					BioEdge e = bwMp.getKey();
	    					if(e.getStartNode().gene.equals(frontGene) & e.getEndNode().gene.equals(behindeGene)){
	    						betweenness.put(e, betweenness.get(e) + 1.0);
	    					}
	    				}
	    			}
	    		}
	    	}
	    	
	    }
    
    return betweenness;
	}
	
	/** Helper method to sort edge betweenness
	 * @param map The betweenness map containing all the edges and corresponding weights of edge betweenness
	 * @return a new betweenness map sorted by value (weight)
	 */
	public static HashMap<BioEdge, Double> sortEdgesByBetweenness(HashMap<BioEdge, Double> map){
		List<Entry<BioEdge, Double>> mapList = new LinkedList<Entry<BioEdge, Double>>(map.entrySet());
		
		Collections.sort(mapList, new Comparator<Entry<BioEdge, Double>>(){
			public int compare(Entry<BioEdge, Double> obj1, Entry<BioEdge, Double> obj2){
				return obj1.getValue().compareTo(obj2.getValue());
			}
		});
		
		HashMap<BioEdge, Double> sortedMap = new LinkedHashMap<BioEdge,Double>();
		for(Entry<BioEdge, Double> entry : mapList){
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		
		// Output all the edges and their betweenness weights
		/*
		System.out.println("from: " + "to: " + "betweenness: ");
		for (Entry<BioEdge, Double> entry : sortedMap.entrySet()){
			System.out.println(entry.getKey().getStartNode().gene + " " 
					+ entry.getKey().getEndNode().gene + " " + entry.getValue());
		}
		*/
		return sortedMap;
	}
	
	/** Get the list of sub communities
	 * @param numOfCommunites Number of sub communities expected to get
	 * @return a list of String lists. Each inner list represents a sub community.
	 */
	public List<List<String>> getSubcommunites(int numOfCommunites){
		List<List<String>> subCom = conponentSets();
		
		if(subCom.size() >= numOfCommunites){
			return subCom;
		}
		
		HashMap<BioEdge, Double> edgeBetweennessWeight = getEdgeBetweennessWeight();
		HashMap<BioEdge, Double> sortedBetweenness = sortEdgesByBetweenness(edgeBetweennessWeight);
		
		List<Entry<BioEdge, Double>> sortedBetweennessList = 
				new ArrayList<Map.Entry<BioEdge,Double>>(sortedBetweenness.entrySet());
		
		while(subCom.size() < numOfCommunites & !sortedBetweenness.isEmpty()){
			Entry<BioEdge,Double> lastEntry = sortedBetweennessList.get(sortedBetweennessList.size()-1);
			BioEdge e = lastEntry.getKey();
			
			removeEdges(e.getStartNode().gene, e.getEndNode().gene);
	
			System.out.println("remove edge between " + e.getStartNode().gene + " and " + e.getEndNode().gene);
			
			sortedBetweennessList.remove(lastEntry);
			
			subCom = conponentSets();
		}
		
		return subCom;
	}
	
	/** main method of the class
	 * Run this method can answer the questions proposed in the project
	 * Two file are provided:
	 * i. "data/genetest01.txt": an artificial data for testing
	 * ii. "data/genenetworks.txt": real data for the project 
	 * 
	 * some test codes are also provided in the comments part 
	 */
	public static void main(String...strings){
		BioGraph g = new BioGraph();
		System.out.print("Loading the map...\n");
		
		// Load the real data: "data/genenetworks.txt"
		//BioGraphLoader.loadGraph(g, "data/genenetworks.txt");
		
		// Load the artificial data: "data/genetest01.txt"
		// The graph looks like the following. Connections are bi-directions.
		// A ----- B ----- E ---- F
		// |       |       |      |
		// |       |       |      |
		// C ----- D       G ---- H
		
		BioGraphLoader.loadGraph(g, "data/genetest01.txt");
		
		// output the graph
		System.out.println(g.toString());
		
		System.out.println("LOADING DONE.");
		
		
		// --------------------------------------------------------
		// QUESTION 1:
		// --------------------------------------------------------
		System.out.println("\nQUESTION 1: ");
		
		// Uncomment the below code for real data, your can change start/end to any string in the graph
		/*
		String start = "ERK";
		String end = "ALB";
		List<String> connections = g.dijkstra(start, end);
		*/
		
		// Uncomment the below code for artificial data, your can change start/end to any of "A" ~ "H"
		///*
		String start = "A";
		String end = "H";
		List<String> connections = g.dijkstra(start, end);
		//*/
		
		System.out.println("\nEND OF QUESTION 1");
		// --------------------------------------------------------
		// END OF QUESTION 1
		// --------------------------------------------------------
		
		// ---------------------------------------------------------------------------
		// QUESTION 2:
		// You can change the numOfSubCom to the least number of components expected 
		// Note: It takes around 1 hour when you use real data and numOfSubCom > 4
		// ---------------------------------------------------------------------------
		
		System.out.println("\nQUESTION 2: ");
		
		// The least number of sub components expected
		int numOfSubCom = 2;
		
		int numOfVer = g.getVertices().size();
		
		if (numOfSubCom > numOfVer) {
			System.out.println("You can not get such many of components.");
			
		} else {
			List<List<String>> subCommunities = g.getSubcommunites(numOfSubCom);
			
			System.out.println("\nTotal subcommunites in the graph: " + subCommunities.size());
			
			int countSubCommunites = 1;
			for (List<String> comp: subCommunities){
				System.out.println("Subcommunity " + countSubCommunites + ":");
				System.out.println(comp);
				countSubCommunites++;
			}
		}
		
		System.out.println("\nEND OF QUESTION 2");
		// ----------------------------------------------------------------------------
		// END OF QUESTION 2
		// ----------------------------------------------------------------------------
		
		// ----------------------------------------------------------------------------
		// Code for testing
		// ----------------------------------------------------------------------------
		
		//(1) test method removeEdge()
		// case 1: load empty graph "genetest00.txt" and run the method
		// case 2: load non-empty graph "genetest01.txt" and run the method
		// case 3: one or two nodes don't exist in the graph
		/*
		g.removeEdges("A", "B");
		for (BioEdge e: g.edges){
			String s = "from: " + e.getStartNode().gene + " to: " + e.getEndNode().gene;
			System.out.println(s);
		}
		System.out.println(g.toString());
		*/
		
		// (2) Test method conponentSets()
		// case 1: empty graph, "genetest00.txt"
		// case 2: graph has only one connected components, "genetest01.txt"
		// case 3: graph has two connected components, "genetest02.txt"
		/*
		System.out.println(g.conponentSets());
		*/
		
		// (3) test method getEdgeBetweennessWeight()
		// case 1: empty graph, "genetest00.txt"
		// case 2: all the vertices are connected together in the graph, "genetest01.txt"
		// case 3: some vertices are not connected together in the graph, "genetest02.txt"
		/*
		HashMap<BioEdge, Double> bw = g.getEdgeBetweennessWeight();
		
		for(Entry<BioEdge, Double> e: bw.entrySet()){
			String s = "Betweenness between " + e.getKey().getStartNode().gene;
			s += " and " + e.getKey().getEndNode().gene;
			s += " is: " + e.getValue();
			System.out.println(s);
		}
		*/
		
	}
}
