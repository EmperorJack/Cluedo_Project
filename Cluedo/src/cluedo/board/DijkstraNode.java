package cluedo.board;
import cluedo.tiles.Tile;

public class DijkstraNode {
	Tile tile;
	int distance;
	DijkstraNode previous;
	
	/**
	 * Given a tile, constructs a Dijkstra node with distance set at the largest possible value.
	 * @param tile Tile to construct node from.
	 */
	public DijkstraNode(Tile tile){
		this.tile = tile;
		distance = Integer.MAX_VALUE;
		previous = null;
	}
	
	/**
	 * Gets the tile in this node.
	 * @return The tile in this node.
	 */
	public Tile getTile(){
		return tile;
	}
	
	/**
	 * Get the shortest distance from the starting node to this node.
	 * @return The shortest distance, MAX_VALUE if no path has been constructed.
	 */
	public int getDistance(){
		return distance;
	}
	
	/**
	 * Set distance of this node to the start node.
	 * @param distance Distance to start node.
	 */
	public void setDistance(int distance){
		this.distance = distance;
	}
	
	/**
	 * Sets previous DijkstraNode in path.
	 * @param prev The previous DijkstraNode in the path.
	 */
	public void setPrevious(DijkstraNode prev){
		this.previous = prev;
	}
	
	public DijkstraNode getPrevious(){
		return previous;
	}

}
