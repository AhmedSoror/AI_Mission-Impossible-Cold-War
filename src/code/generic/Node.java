package code.generic;
import code.generic.State;

public class Node implements Comparable<Node> {
	public State state;
	public Node parentNode;
	public  String action;
	public  int depth;
	public  int pathCost;
	public  int heuristic1;
	public  int heuristic2;



	// constructor to create root
	public Node(State initialState) {
		this.state = initialState;
		parentNode = null;
		action = null;
		depth = 0;
		pathCost = 0;
	}

	// constructor for all other nodes
	public Node(State state, Node parentNode, String action, int pathCost) {
		this.state = state;
		this.parentNode = parentNode;
		this.action = action;
		this.pathCost = pathCost;
		this.depth = parentNode.depth + 1;
	}

	@Override
	public String toString() {
//		return  "a: "+action+", p: "+parentNode;
		if(parentNode  != null) {
			return parentNode + ", " + action;			
		}
		return "";
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Node getParentNode() {
		return parentNode;
	}

	public void setParentNode(Node parentNode) {
		this.parentNode = parentNode;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public int getPathCost() {
		return pathCost;
	}

	public void setPathCost(int pathCost) {
		this.pathCost = pathCost;
	}

	public int getHeurestic1() {
		return this.heuristic1;
	}

	public void setHeurestic1(int value) {
		this.heuristic1 = value;
	}

	public int getHeurestic2() {
		return this.heuristic2;
	}

	public void setHeurestic2(int value) {
		this.heuristic2 = value;
	}

	public int compareTo(Node other) {
		if (this.getPathCost() < other.getPathCost())
			return -1;
		else if (this.getPathCost() > other.getPathCost())
			return 1;
		return 0;
	}

}