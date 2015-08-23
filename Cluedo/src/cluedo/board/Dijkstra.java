package cluedo.board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cluedo.tiles.Tile;
import cluedo.tiles.WallTile;

public class Dijkstra {
	Map<Tile, DijkstraNode> nodeMap;
	Map<Location, Tile> tileMap;
	Set<DijkstraNode> unvisitedNodes;
	Set<DijkstraNode> visitedNodes;

	/**
	 * Initialises the necessary maps for performing the Dijkstra pathfinding check.
	 * @param tiles A map of Locations to tiles.
	 */
	public Dijkstra(Map<Location, Tile> tiles) {
		nodeMap = new HashMap<Tile, DijkstraNode>();
		unvisitedNodes = new HashSet<DijkstraNode>();
		visitedNodes = new HashSet<DijkstraNode>();
		tileMap = tiles;
		//Initialising Dijkstra Nodes (with default distances of infinity)
		for (Tile t : tiles.values()) {
			nodeMap.put(t, new DijkstraNode(t));
		}
	}

	/**
	 * Given a beginning and end location, return the length of the
	 * shortest possible path between the two on the board.
	 * @param start Starting location.
	 * @param end Ending location.
	 * @return The length of the shortest path, -1 if no path is found.
	 */
	
	public int findPath(Location start, Location end) {
		
		//Initialise starting node with distance 0.
		DijkstraNode startNode = nodeMap.get(tileMap.get(start));
		startNode.setDistance(0);
		unvisitedNodes.add(startNode);
		
		while (unvisitedNodes.size() > 0) {
			DijkstraNode closest = getClosestNode();
			
			if (closest.getTile().getLocation().equals(end)) //Shortest path found here
				return closest.getDistance();
			
			unvisitedNodes.remove(closest);
			visitedNodes.add(closest);
			setDistances(closest);
		}
		return -1; //NO PATH FOUND
	}
	
	public Set<Tile> getValidTiles(Location start, int diceRoll){
		HashSet<Tile> validTiles = new HashSet<Tile>();
		
		DijkstraNode startNode = nodeMap.get(tileMap.get(start));
		startNode.setDistance(0);
		unvisitedNodes.add(startNode);
		
		while (unvisitedNodes.size() > 0) {
			DijkstraNode closest = getClosestNode();
			unvisitedNodes.remove(closest);
			visitedNodes.add(closest);
			setDistances(closest);
		}
		for (Tile t: nodeMap.keySet()){
			if (!(t instanceof WallTile)){
				if (nodeMap.get(t).getDistance() <= (diceRoll)){
					validTiles.add(t);
				}
			}
		}
		return validTiles;
	}

	/**
	 * Updates the distances of the surrounding tiles of the current Dijkstra Node and adds them to the fringe set.
	 * @param current The current DijkstraNode.
	 */
	public void setDistances(DijkstraNode current) {
		List<DijkstraNode> neighbours = getNeighbours(current);
		for (DijkstraNode neighbour : neighbours) {
			if (neighbour.getDistance() > current.getDistance() + 1) {
				neighbour.setDistance(current.getDistance() + 1); //distance between adj. tiles is always 1
				neighbour.setPrevious(current);
				unvisitedNodes.add(neighbour);
			}
		}
	}

	/**
	 * Given a DijsktraNode, returns a list of DijkstraNodes of unexplored path tiles adjacent to it.
	 * @param current The current node.
	 * @return A list of unexplored path nodes adjacent to the current node.
	 */
	public List<DijkstraNode> getNeighbours(DijkstraNode current) {
		List<DijkstraNode> neighbours = new ArrayList<DijkstraNode>();
		List<Location> validLocations = new ArrayList<Location>();
		// Get starting location
		Location start = current.getTile().getLocation();
		// Check if adjacent tile exists
		if (inXRange(start.getX() - 1))
			validLocations.add(new Location(start.getX() - 1, start.getY())); // left
		if (inXRange(start.getX() + 1))
			validLocations.add(new Location(start.getX() + 1, start.getY())); // right
		if (inYRange(start.getY() - 1))
			validLocations.add(new Location(start.getX(), start.getY() - 1)); // up
		if (inYRange(start.getY() + 1))
			validLocations.add(new Location(start.getX(), start.getY() + 1)); // down
		
		//SPECIAL CASES! Removing two neighbouring nodes from the algorithm to preserve walls on corner cases.
		if (start.equals(new Location(6,19))){
			validLocations.remove(new Location(5,19));
		}
		
		if (start.equals(new Location(5,19))){
			validLocations.remove(new Location(6,19));
		}
		
		if (start.equals(new Location(15,20))){
			validLocations.remove(new Location(16,20));
		}
		
		if (start.equals(new Location(16,20))){
			validLocations.remove(new Location(15,20));

		}
		
		
		for (Location loc : validLocations) {
			Tile t = tileMap.get(loc); //if a neighbouring tile has not been visited and is not a wall, add it to the list.
			if (!(t instanceof WallTile)
					&& !visitedNodes.contains(nodeMap.get(t)))
				neighbours.add(nodeMap.get(t));
		}
		return neighbours;
	}

	/**
	 * Makes sure the location is within bounds of the board.
	 * @param check Integer to check.
	 * @return True if in bounds of the board, false if not.
	 */
	public boolean inXRange(int check) {
		return (check >= 0 && check <= 23);
	}
	public boolean inYRange(int check) {
		return (check >= 0 && check <= 24);
	}

	/**
	 * Finds the node on the fringe with the smallest distance.
	 * @return The closest node.
	 */
	public DijkstraNode getClosestNode() {
		DijkstraNode closest = null;
		for (DijkstraNode d : unvisitedNodes) {
			if (closest == null)
				closest = d;
			else if (closest.getDistance() > d.getDistance())
				closest = d;
		}
		return closest;
	}

}
