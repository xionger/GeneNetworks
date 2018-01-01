package graph;

public class BioEdge {
	/** The two end points of the edge */
	private BioNode start;
	private BioNode end;
		
	/** The length of the connection segment */
	private double length;
	
	static final double DEFAULT_LENGTH = 100000.0;
	
	
	/** Create a new BioEdge object
	 * @param n1  The point at one end of the segment
	 * @param n2  The point at the other end of the segment
	 */
	public BioEdge(BioNode n1, BioNode n2)
	{
		this(n1, n2, DEFAULT_LENGTH);
	}
	
	/** 
	 * Create a new MapEdge object
	 * @param n1 The point at one end of the segment
	 * @param n2 The point at the other end of the segment
	 * @param length The length of the connection segment
	 */	
	public BioEdge(BioNode n1, BioNode n2, double length) 
	{
		start = n1;
		end = n2;
		this.length = length;
	}
	
	/**
	 * return the BioNode for the start point
	 * @return the BioNode for the start point
	 */
	public BioNode getStartNode() {
	   return start;
	}
	
	public void setStartNode(BioNode start) {
	   this.start = start;
	}
	
	/**
	 * return the BioNode for the end point
	 * @return the BioNode for the end point
	 */
	public BioNode getEndNode() {
	   return end;
	}
	
	public void setEndNode(BioNode end) {
		   this.end = end;
		}
	
	/**
	 * Return the length of this connection segment
	 * @return the length of the connection segment
	 */
	public double getLength()
	{
		return length;
	}
	
	public void setLength(double length){
		this.length = length;
	}
	
	/**
	 * Given one of the nodes involved in this edge, get the other one
	 * @param node The node on one side of this edge
	 * @return the other node involved in this edge
	 */
	BioNode getOtherNode(BioNode node)
	{
		if (node.equals(start)) 
			return end;
		else if (node.equals(end))
			return start;
		throw new IllegalArgumentException("Looking for " +
			"a point that is not in the edge");
	}
	
	/**
	 * Return a String representation for this edge.
	 */
	@Override
	public String toString()
	{
		String toReturn = "[EDGE between ";
		toReturn += "\n\t" + getStartNode();
		toReturn += "\n\t" + getEndNode();
		toReturn += "\nSegment length: " + String.format("%.3g", length);
		
		return toReturn;
	}
}
