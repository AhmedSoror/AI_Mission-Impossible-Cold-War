package code.mission;

import java.util.*;
import code.generic.Node;
import code.generic.SearchProblem;
import code.generic.State;
import code.mission.MissionImpossibleState;

public class MissionImpossible extends SearchProblem {
	public static int sx;
	public static int sy;
	public static int membersCount;
	public static int truckLimit;
	static TreeMap<Pos, Integer> membersIndeciesMap;		// used for printing members with the same index as input
	static Pos[] agentsLoc;

	public MissionImpossible(Node root) {
		super(root);
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {

//		m,n; ex,ey; sx,sy;x1,y1, ...,xk,yk;h1,...,hk;c
		String grid = "8,14; 0,8; 4,5; 4,7,5,2,6,5,6,10,7,11; 65, 7, 44, 92, 63; 4";
		grid = "1,4; 0,3; 0,1; 0,0,0,2; 93, 20; 2";

//		grid  = genGrid();
		System.out.println("Input Grid: " + grid);
		// grid = "1,5;0,0;0,4;0,1,0,2,0,3;10,20,30;3";
		System.out.println("Solving BFS");
		solve(grid, "BF", false);
		System.out.println("------------------------------------------------");

		System.out.println("Solving DFS");
		solve(grid, "DF", false);
		System.out.println("------------------------------------------------");

		System.out.println("Solving IDS");
		solve(grid, "ID", false);
		System.out.println("------------------------------------------------");

		System.out.println("Solving UC");
		solve(grid, "UC", false);
		System.out.println("------------------------------------------------");
//
		System.out.println("Solving Greedy with first H");
		solve(grid, "GR1", false);
		System.out.println("------------------------------------------------");
//
//
		System.out.println("Solving Greedy with second H");
		solve(grid, "GR2", false);
		System.out.println("------------------------------------------------");
//
		System.out.println("Solving A* with first H");
		solve(grid, "AS1", false);
		System.out.println("------------------------------------------------");
//
		System.out.println("Solving A* with second H");
		solve(grid, "AS2", false);
		System.out.println("------------------------------------------------");

	}

	public static String genGrid() {
		Random rand = new Random();
		// grid of m x n in range 5*5 and 15*15
		int m = rand.nextInt(10) + 5;
		int n = rand.nextInt(10) + 5;
		// members count
		int k = rand.nextInt(5) + 5;
		// truck limit
		int c = rand.nextInt(k - 1) + 1;

		// generate random positions for Ethan, submarine, members
		TreeSet<Pos> members_pos = new TreeSet<Pos>();
		while (members_pos.size() < k + 2) {
			members_pos.add(new Pos(rand.nextInt(m), rand.nextInt(n)));
		}

		// health array between 1-99
		int[] h = new int[k];
		for (int i = 0; i < k; i++) {
			h[i] = rand.nextInt(98) + 1;
		}

		Pos[] posArray = members_pos.toArray(new Pos[members_pos.size()]);

		// output format
		String l1 = m + "," + n + "; " + posArray[0] + "; " + posArray[1] + "; ";
		String positionsString = "";
		for (int i = 0; i < k; i++) {
			Pos p = posArray[i + 2];
			positionsString += p.toString() + ",";
		}
		positionsString = positionsString.substring(0, positionsString.length() - 1) + "; ";

		String healthString = Arrays.toString(h);
		healthString = healthString.substring(1, healthString.length() - 1) + "; ";

		String results = l1 + positionsString + healthString + "" + c;
		return results;
	}

	// Extracting data from string grid
	static int[][] grid2matrix(String g) {
		membersIndeciesMap = new TreeMap<Pos, Integer>();

		String[] gridSplitted = g.split(";");

		int m = Integer.parseInt(gridSplitted[0].split(",")[0].trim());
		int n = Integer.parseInt(gridSplitted[0].split(",")[1].trim());
		int[][] grid = new int[m][n];

		int ex = Integer.parseInt(gridSplitted[1].split(",")[0].trim());
		int ey = Integer.parseInt(gridSplitted[1].split(",")[1].trim());

		sx = Integer.parseInt(gridSplitted[2].split(",")[0].trim());
		sy = Integer.parseInt(gridSplitted[2].split(",")[1].trim());

		String[] positions = gridSplitted[3].split(",");
		String[] health = gridSplitted[4].split(",");

		grid[sx][sy] = -1;
		membersCount = health.length;

		int j = 0;
		for (int i = 0; i < membersCount; i++) {
			int x = Integer.parseInt(positions[j++].trim());
			int y = Integer.parseInt(positions[j++].trim());
			grid[x][y] = Integer.parseInt(health[i].trim());
			membersIndeciesMap.put(new Pos(x, y), i);
		}
		truckLimit = Integer.parseInt(gridSplitted[5].trim());
		return grid;
	}

	static Pos getEthanPosition(String g) {
		String[] lines = g.split("\n");
		String[] l1 = lines[0].split(";");
		int ex = Integer.parseInt(l1[1].split(",")[0].trim());
		int ey = Integer.parseInt(l1[1].split(",")[1].trim());
		return new Pos(ex, ey);
	}

	// Recursive method to print the grid from root to goal
	public static void showPathToGoal(Node node) {
		if (node == null)
			return;
		showPathToGoal(node.getParentNode());
		((MissionImpossibleState) node.state).show_grid();
	}

	public static String solve(String grid, String strategy, boolean visualize) {

		int[][] gridMatrix = grid2matrix(grid);
		Pos ethanPos = getEthanPosition(grid);
		agentsLoc = new Pos[membersCount];
		int ex = ethanPos.x;
		int ey = ethanPos.y;

		// Printing the initial grid
		//show_grid(gridMatrix);

		String st = ex + "," + ey + ",0,0";
		MissionImpossibleState initialState = new MissionImpossibleState(st, gridMatrix, 0);
		initialState.setFixedDamages(new int[membersCount]);

		// Initializing a mission impossible problem
		MissionImpossible MI = new MissionImpossible(new Node(initialState));

		Node goal = MI.search(strategy);

		if (visualize) {
			showPathToGoal(goal);
		}

		if (goal == null) {
			return ("No Solution found :(");
		}

		// Actions taken from root to goal
		String output = goal.toString().substring(2);

		MissionImpossibleState s = (MissionImpossibleState) (goal.state);

		int dead = 0;
		String damages = "";
		for (int i : s.fixedDamages) {
			if (i >= 100)
				dead++;
			damages += i + ",";
		}

		damages = damages.substring(0, damages.length() - 1);
		damages += ";";

//		output += ";\n"+ dead + ";" + damages + MI.visitedStates.size();
		output += ";\n" + dead + ";" + damages + MI.getVisitedStatesCount();
		System.out.println("Output: " + output);
		return (output);

	}

	// override parent expand node method 
	public ArrayList<Node> expandNode(Node n) {
		ArrayList<Node> expandedList = new ArrayList<Node>();
		// create node for every action
		Node carry_node = transition(n, "carry");
		Node drop_node = transition(n, "drop");
		Node up_node = transition(n, "up");
		Node down_node = transition(n, "down");
		Node left_node = transition(n, "left");
		Node right_node = transition(n, "right");

		if (carry_node != null)
			expandedList.add(carry_node);
		if (drop_node != null)
			expandedList.add(drop_node);
		if (up_node != null)
			expandedList.add(up_node);
		if (down_node != null)
			expandedList.add(down_node);
		if (left_node != null)
			expandedList.add(left_node);
		if (right_node != null)
			expandedList.add(right_node);
		return expandedList;
	}
	// transition function, handles illegal actions as well	
	public Node transition(Node parentNode, String action) {
		String newStateString;
		MissionImpossibleState parentState = (MissionImpossibleState) parentNode.getState();

		// Extracting info from the state string
		// state: ex, ey, cell content, submarineMembersCount, indecies of members in the truck separated by #
		String[] stateContent = parentState.getStringState().split(",");
		int ex = Integer.parseInt(stateContent[0].trim());
		int ey = Integer.parseInt(stateContent[1].trim());
		int cellContent = Integer.parseInt(stateContent[2].trim());
		int submarineMembersCount = Integer.parseInt(stateContent[3].trim());
		ArrayList<String> truckMembers = new ArrayList<String>();
		if (stateContent.length > 4) {
			// the truck is not empty
			truckMembers = new ArrayList<>(Arrays.asList(stateContent[4].split("#")));
		}
		int newDeath = parentState.getDeathCount();
		int[] newFixedDamages = parentState.getFixedDamages().clone();

		// take actions and prevent illegal actions
		switch (action) {
		case "up":
			ex = ex - 1;
			if (ex < 0) {
				return null;
			}
			cellContent = parentState.grid[ex][ey];
			// because this cell isn't updated in the parent grid using updateGrid()
			if (cellContent > 0) {
				cellContent += 2;
			}
			break;
		case "down":
			ex = ex + 1;
			if (ex >= parentState.grid.length) {
				return null;
			}
			cellContent = parentState.grid[ex][ey];
			if (cellContent > 0) {
				cellContent += 2;
			}
			break;
		case "left":
			ey = ey - 1;
			if (ey < 0) {
				return null;
			}
			cellContent = parentState.grid[ex][ey];
			if (cellContent > 0) {
				cellContent += 2;
			}
			break;

		case "right":
			ey = ey + 1;
			if (ey >= parentState.grid[0].length) {
				return null;
			}
			cellContent = parentState.grid[ex][ey];
			if (cellContent > 0) {
				cellContent += 2;
			}

			break;

		case "carry":
			if (parentState.grid[ex][ey] == 0 || truckMembers.size() == truckLimit || (ex == sx && ey == sy)) {
				return null;
			}
			if (parentState.grid[ex][ey] >= 100)
				newDeath++;
			cellContent = 0;
			int memberIndex = membersIndeciesMap.get(new Pos(ex, ey));

			newFixedDamages[memberIndex] = parentState.grid[ex][ey];
			truckMembers.add(memberIndex + "");
			Collections.sort(truckMembers);
			break;

		case "drop":
			if (ex != sx || ey != sy || truckMembers.size() == 0) {
				return null;
			}
			cellContent = -1;
			submarineMembersCount += truckMembers.size();
			truckMembers = new ArrayList<String>();
			break;
		}
		// Stop the damage at 100
		if (cellContent > 100) {
			cellContent = 100;
		}
		
		// generate new state
		String truckState = "";
		for (String memb : truckMembers) {
			truckState += memb;
			truckState += "#";
		}
		if (truckState.length() > 0) {
			truckState = truckState.substring(0, truckState.length() - 1);
			newStateString = ex + "," + ey + "," + cellContent + "," + submarineMembersCount + "," + truckState;
		} else {
			newStateString = ex + "," + ey + "," + cellContent + "," + submarineMembersCount;
		}
		// Handle repeated states		
		if (visitedStates.contains(newStateString)) {
			return null;
		}
		
		
		// Calculate cost
		int pathCost = costFunction(parentNode.getState(), action) + parentNode.getPathCost();
		// copy the grid to the new state (deep clone)
		int[][] grid = copyMatrix(parentState.grid);
		MissionImpossibleState newState = new MissionImpossibleState(newStateString, updateGrid(grid), newDeath);		//update grid adds new damage to alive members
		newState.setFixedDamages(newFixedDamages);
		newState.grid[ex][ey] = cellContent; 		//update the content of the current cell in the grid

		Node newNode = new Node(newState, parentNode, action, pathCost);
		
		int heuresticValue1 = calcHeurestic1(newNode);
		newNode.setHeurestic1(heuresticValue1);
		
		int heuresticValue2 = calcHeurestic2(newNode);
		newNode.setHeurestic2(heuresticValue2);

		visitedStates.add(newStateString);
		return newNode;
	}

	// ------------------------- Heuristic Function 1//
	// -----------------------------------
	public static int calcHeurestic1(Node n) {
		MissionImpossibleState s = (MissionImpossibleState) n.getState();
		int[][] grid = s.grid;
		int[] fixedDamages = s.fixedDamages;			//damages of members in the truck (their damage is fixed=> fixedDamges)
		ArrayList<String> membersIndices = s.getMembersIndices();
		// Summing the damages of the agents in the grid
		int totalDamage = 0;
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				totalDamage += grid[i][j];
			}
		}

		// Summing the damages of the agents in the truck
		int sum = 0;
		for (int i = 0; i < membersIndices.size(); i++) {
			int index = Integer.parseInt(membersIndices.get(i));
			sum += fixedDamages[index];
		}

		int value = totalDamage + 1 + sum;

		return value;
	}

	// ------------------------- Heuristic Function 2//
	// -----------------------------------
	public static int calcHeurestic2(Node n) {

		MissionImpossibleState s = (MissionImpossibleState) n.getState();
		int totalDamage = 0;
		for (int i : s.fixedDamages) {
			totalDamage += (i == 0) ? 2 : 0;
		}
		totalDamage += s.membersIndices.size();
		return totalDamage;
	}

	public boolean goalTest(Node n) {
		String[] stateContent = ((MissionImpossibleState) (n.getState())).getStringState().split(",");
		int ex = Integer.parseInt(stateContent[0].trim());
		int ey = Integer.parseInt(stateContent[1].trim());
		int submarineMembersCount = Integer.parseInt(stateContent[3].trim());
		return (ex == sx && ey == sy && submarineMembersCount == membersCount);	  //Ethan at submarine and all members are in the submarine
	}

	// Small helperss
	public static int[][] updateGrid(int[][] g) {
		//increase damage of all members by 2
		for (int i = 0; i < g.length; i++) {
			for (int j = 0; j < g[0].length; j++) {
				if (g[i][j] > 0) {
					g[i][j] += 2;
				}
				if (g[i][j] > 100) {
					g[i][j] = 100;
				}
			}
		}
		return g;
	}

	static int get_c(String grid) {
		// return truck limit
		String[] lines = grid.split(";");
		return Integer.parseInt(lines[lines.length - 1]);
	}

	static void show_grid(int[][] grid) {
		// visualize the grid
		System.out.println("Showing Grid!!");
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				System.out.print(((grid[i][j] > 9 || grid[i][j] == -1) ? grid[i][j] + " " : grid[i][j] + "  ") + "| ");
			}
			System.out.println("\n");
		}

	}

	static int[][] copyMatrix(int[][] matrix) {
		//deep clone of a 2d array
		int[][] newMat = new int[matrix.length][];
		for (int i = 0; i < matrix.length; i++)
			newMat[i] = matrix[i].clone();
		return newMat;
	}

//	// ------------------------- Cost Function -----------------------------------//
	@Override
	public int costFunction(State state, String action) {
		// TODO Auto-generated method stub

		String strState = ((MissionImpossibleState) state).getStringState();
		String[] stateContent = strState.split(",");
		int cellContent = Integer.parseInt(stateContent[2].trim());

		switch (action) {
		case "up":
			return 5;
		case "down":
			return 5;
		case "left":
			return 5;
		case "right":
			return 5;
		case "carry":
			if (cellContent >= 100)
				return 1000;
			else
				return cellContent;
		case "drop":
			return 1;
		}
		return 0;

	}

}

class Pos implements Comparable<Pos> {
	int x;
	int y;

	public Pos(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return x + "," + y;
	}

	@Override
	public int compareTo(Pos o) {
		if (this.x == o.x && this.y == o.y)
			return 0;
		if (this.x == o.x)
			return this.y - o.y;
		return this.x - o.x;
	}
}


