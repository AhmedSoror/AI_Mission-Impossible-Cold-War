package code.generic;

import java.util.*;

public abstract class SearchProblem {
	Node root;
	public HashSet<String> visitedStates = new HashSet<String>();

	public int visitedStatesCount;
//	 ArrayList<String> availableActions;

	public SearchProblem(Node root) {
		this.root = root;
		visitedStates = new HashSet<String>();
//		MissionImpossibleState initialState = (MissionImpossibleState) root.getState();
//		visitedStates.add(initialState.getStringState());
	}

	public Node getRoot() {
		return root;
	}

	public void setRoot(Node root) {
		this.root = root;
	}

	public int getVisitedStatesCount() {
		return visitedStatesCount;
	}

	public void setVisitedStatesCount(int visitedStatesCount) {
		this.visitedStatesCount = visitedStatesCount;
	}

	public HashSet<String> getVisitedStates() {
		return visitedStates;
	}

	public void setVisitedStates(HashSet<String> visitedStates) {
		this.visitedStates = visitedStates;
	}

	public Node search(String strategy) {
		Node goal = null;
		switch (strategy) {
		case "BF":
			goal = solver_BFS();
			break;
		case "DF":
			goal = solver_DFS();
			break;
		case "ID":
			goal = solver_IDS();
			break;
		case "UC":
			goal = solver_UCS();
			break;
		case "GR1":
			goal = solver_Greedy1();
			break;
		case "GR2":
			goal = solver_Greedy2();
			break;
		case "AS1":
			goal = solver_AS1();
			break;
		case "AS2":
			goal = solver_AS2();
			break;
		}
		return goal;

	}

	public abstract Node transition(Node parentNode, String action);

	public abstract ArrayList<Node> expandNode(Node n);

	public abstract boolean goalTest(Node n);

	public abstract int costFunction(State state, String action);

	Node solver_BFS() {
		Queue<Node> q = new LinkedList<>();
		q.add(root);
		visitedStatesCount = 0;
		while (!q.isEmpty()) {
			Node n = q.remove();
			if (goalTest(n)) {
				return n;
			}
			ArrayList<Node> children = expandNode(n);
			visitedStatesCount = visitedStates.size();
			q.addAll(children);
		}
		return null;
	}

	Node solver_DFS() {
		Stack<Node> s = new Stack<>();
		s.push(root);

		while (!s.isEmpty()) {
			Node n = s.pop();
			if (goalTest(n)) {
				return n;
			}
			ArrayList<Node> children = expandNode(n);
			for (int i = children.size() - 1; i >= 0; i--) {
				s.push(children.get(i));
			}
			visitedStatesCount = visitedStates.size();
		}
		return null;
	}

	Node solver_UCS() {
		PriorityQueue<Node> pq = new PriorityQueue<Node>();
		pq.add(root);

		while (!pq.isEmpty()) {
			Node n = pq.remove();
//			System.out.println("Node: " + n);
			if (goalTest(n)) {
				return n;
			}
			ArrayList<Node> children = expandNode(n);
			pq.addAll(children);
			visitedStatesCount = visitedStates.size();
		}
		return null;
	}

//	Node solver_IDS() {
//		int maxDepth = 0;
//		while (true) {
//			Node n = DFS_Itereate(root, maxDepth);
//			if (n != null) { 
//				return n; 
//			} 
//			maxDepth++;
//			visitedStates = new HashSet<String>();
//		}
//	}
	Node solver_IDS() {
		int maxDepth =0;
		visitedStatesCount = 0;
		Stack<Node> s = new Stack<>();
		s.push(root);	
		while(true){
			while (!s.isEmpty()){
					Node n = s.pop();
					if (goalTest(n)) {
						return n;
					}
					if(n.depth==maxDepth) continue;
					ArrayList<Node> children = expandNode(n);
					for (int i=children.size()-1;i>=0;i--) {
						s.push(children.get(i));
					}
				}
				maxDepth++;
				visitedStatesCount += visitedStates.size();
				visitedStates.clear();
				s.push(root);
			}
	}

	Node DFS_Itereate(Node node, int maxDepth) {
		Stack<Node> s = new Stack<>();
		s.push(node);
		while (!s.isEmpty()) {
			Node n = s.pop();
			if (goalTest(n)) {
				return n;
			}
			ArrayList<Node> children = expandNode(n);
			for (int i = children.size() - 1; i >= 0; i--) {
				if (children.get(i).depth <= maxDepth) {
					s.push(children.get(i));
				}
			}
		}
		return null;
	}

	Node solver_Greedy1() {
		Comparator<Node> comparator = new Comparator<Node>() {

			@Override
			public int compare(Node n1, Node n2) {
				if (n1.getHeurestic1() < n2.getHeurestic1())
					return -1;
				else if (n1.getHeurestic1() > n2.getHeurestic1())
					return 1;
				return 0;
			}
		};
		PriorityQueue<Node> pq = new PriorityQueue<Node>(comparator);
		pq.add(root);

		while (!pq.isEmpty()) {
			Node n = pq.remove();
			if (goalTest(n)) {
				return n;
			}
			ArrayList<Node> children = expandNode(n);
			visitedStatesCount = visitedStates.size();
			pq.addAll(children);
		}
		return null;

	}

	Node solver_Greedy2() {
		Comparator<Node> comparator = new Comparator<Node>() {

			@Override
			public int compare(Node n1, Node n2) {
				if (n1.getHeurestic2() < n2.getHeurestic2())
					return -1;
				else if (n1.getHeurestic2() > n2.getHeurestic2())
					return 1;
				return 0;
			}
		};
		PriorityQueue<Node> pq = new PriorityQueue<Node>(comparator);
		pq.add(root);

		while (!pq.isEmpty()) {
			Node n = pq.remove();
			if (goalTest(n)) {
				return n;
			}
			ArrayList<Node> children = expandNode(n);
			visitedStatesCount = visitedStates.size();
			pq.addAll(children);
		}
		return null;

	}

	Node solver_AS1() {
		Comparator<Node> comparator = new Comparator<Node>() {

			@Override
			public int compare(Node n1, Node n2) {
				if (n1.getHeurestic1() + n1.getPathCost() < n2.getHeurestic1() + n2.getPathCost())
					return -1;
				else if (n1.getHeurestic1() + n1.getPathCost() > n2.getHeurestic1() + n2.getPathCost())
					return 1;
				return 0;
			}
		};
		PriorityQueue<Node> pq = new PriorityQueue<Node>(comparator);
		pq.add(root);

		while (!pq.isEmpty()) {
			Node n = pq.remove();
			if (goalTest(n)) {
				return n;
			}
			ArrayList<Node> children = expandNode(n);
			visitedStatesCount = visitedStates.size();
			pq.addAll(children);
		}
		return null;

	}

	Node solver_AS2() {
		Comparator<Node> comparator = new Comparator<Node>() {

			@Override
			public int compare(Node n1, Node n2) {
				if (n1.getHeurestic2() + n1.getPathCost() < n2.getHeurestic2() + n2.getPathCost())
					return -1;
				else if (n1.getHeurestic2() + n1.getPathCost() > n2.getHeurestic2() + n2.getPathCost())
					return 1;
				return 0;
			}
		};
		PriorityQueue<Node> pq = new PriorityQueue<Node>(comparator);
		pq.add(root);

		while (!pq.isEmpty()) {
			Node n = pq.remove();
			if (goalTest(n)) {
				return n;
			}
			ArrayList<Node> children = expandNode(n);
			visitedStatesCount = visitedStates.size();
			pq.addAll(children);
		}
		return null;

	}

}