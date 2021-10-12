package code.mission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.TreeMap;

import code.generic.State;

public class MissionImpossibleState extends State {
//	ex, ey, cellContect, #members in submarine, #indecies of members in the truck
	// global variables

	public int ex;
	public int ey;
	public int cellContent;
	public int submarineMembersCount;
	public ArrayList<String> membersIndices;
	public String state;
	public int deathCount;
	public int[][] grid;
	public int[] fixedDamages;

	public MissionImpossibleState(String state, int[][] grid, int deathCount) {
		this.state = state;
		this.grid = grid;
		this.deathCount = deathCount;
		String[] stateContent = this.state.split(",");
		ex = Integer.parseInt(stateContent[0].trim());
		ey = Integer.parseInt(stateContent[1].trim());
		setMembersIndices();

	}

	public void setMembersIndices() {
		String[] stateContent = state.split(",");
		membersIndices = new ArrayList<String>();
		if (stateContent.length > 4) {
			membersIndices = new ArrayList<>(Arrays.asList(stateContent[4].split("#")));
		}
	}

	public int[] getFixedDamages() {
		return fixedDamages;
	}

	public void setFixedDamages(int[] fixedDamages) {
		this.fixedDamages = fixedDamages;
	}

	public void show_grid() {
		// visualize the grid
		System.out.println("Visualize a Step!!");
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				if(i==ex && j == ey) {
					System.out.print(" E | ");
//					System.out.println("7da98ssf65sd");
				}
				else {
					System.out.print(((grid[i][j] > 9 || grid[i][j] == -1) ? grid[i][j] + " " : grid[i][j] + "  ") + "| ");					
				}
			}
			System.out.println("\n");
		}
	}

	public int getEx() {
		return ex;
	}

	public void setEx(int ex) {
		this.ex = ex;
	}

	public int getEy() {
		return ey;
	}

	public void setEy(int ey) {
		this.ey = ey;
	}

	public int getCellContent() {
		return cellContent;
	}

	public void setCellContent(int cellContent) {
		this.cellContent = cellContent;
	}

	public int getSubmarineMembersCount() {
		return submarineMembersCount;
	}

	public void setSubmarineMembersCount(int submarineMembersCount) {
		this.submarineMembersCount = submarineMembersCount;
	}

	public ArrayList<String> getMembersIndices() {
		return membersIndices;
	}

	public void addMembersIndices(String index) {
		this.membersIndices.add(index);
	}

	public String getStringState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getDeathCount() {
		return deathCount;
	}

	public void setDeathCount(int deathCount) {
		this.deathCount = deathCount;
	}

	public int[][] getGrid() {
		return grid;
	}

	public void setGrid(int[][] grid) {
		this.grid = grid;
	}

}
