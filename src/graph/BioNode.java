package graph;

import java.util.HashSet;
import java.util.Set;

public class BioNode implements Comparable {
	/** The list of edges out of this node */
	private HashSet<BioEdge> edges;
	
	/** Name of a node */
	String gene;
		
	/** the distance between this Node and another specific Node*/
	private double distance;
	
	/** the distance between this Node and some goal Node*/
	private double distanceToGoal;
	
	/** 
	 * Create a new BioNode for a given gene
	 * @param gene the name of a gene
	 */
	public BioNode(String gene)
	{
		this.gene = gene;
		edges = new HashSet<BioEdge>();
	}
		
	/**
	 * Get the gene name that this node represents
	 * @return the gene name of this node
	 */
	public String getGene()
	{
		return gene;
	}
	
	public void setGene(String gene){
		this.gene = gene;
	}
	
	/**
	 * Add an edge that is outgoing from this node in the graph
	 * @param edge The edge to be added
	 */
	public void addEdge(BioEdge edge)
	{
		edges.add(edge);
	}
	
	/**
	 * Remove an edge that is outgoing from this node in the graph
	 * @param edge The edge to be removed
	 */
	public void removeEdge(BioEdge edge)
	{
		edges.remove(edge);
	}
		
	/**
	 * Get the edges out of the node
	 * @return a HashSet contianing all the edges out of this node.
	 */
	public HashSet<BioEdge> getEdges()
	{
		return edges;
	}
	
	public void setDistance(BioNode node, double dis){
		this.distance = dis;
	}
	
	public double getDistance(){
		return distance;
	}
	
	public void setDistanceToGoal(BioNode node, double dis){
		this.distanceToGoal = dis;
	}
	
	public Double getDistanceToGoal() {
		return distanceToGoal;
	}
	
	/**  
	 * Return the neighbors of this MapNode 
	 * @return a set containing all the neighbors of this node
	 */
	public Set<BioNode> getNeighbors()
	{
		Set<BioNode> neighbors = new HashSet<BioNode>();
		for (BioEdge edge : edges) {
			neighbors.add(edge.getOtherNode(this));
		}
		return neighbors;
	}
	
	/** Returns whether two nodes are equal.
	 * Nodes are considered equal if their locations are the same, 
	 * even if their street list is different.
	 * @param o the node to compare to
	 * @return true if these nodes are at the same location, false otherwise
	 */
	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof BioNode) || (o == null)) {
			return false;
		}
		BioNode node = (BioNode)o;
		return node.gene.equals(this.gene);
	}
	
	/** Because we compare nodes using their location, we also 
	 * may use their location for HashCode.
	 * @return The HashCode for this node, which is the HashCode for the 
	 * underlying point
	 */
	@Override
	public int hashCode()
	{
		return gene.hashCode();
	}
	
    /* implement Comparable AST method compareTo() */
	public int compareTo(Object o) {
		// convert to map node, may throw exception
		BioNode m = (BioNode)o; 
		return ((Double)this.getDistance()).compareTo((Double)m.getDistance());
	}
	
	
	/** ToString to print out a MapNode object
	 *  @return the string representation of a MapNode
	 */
	@Override
	public String toString()
	{
		return "";
	}

	// For debugging, output roadNames as a String.
	public String roadNamesAsString()
	{
		return "";
	}
}
