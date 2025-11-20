package assignment3;

import java.awt.Color;

public class BlobGoal extends Goal{

	public BlobGoal(Color c) {
		super(c);
	}

	@Override
	public int score(Block board) {
		Color[][] c = board.flatten();
		boolean[][] visited = new boolean[c.length][c.length];
		int score = 0;

		for (int i = 0; i < c.length; i++){
			for (int j = 0; j < c.length; j++) {
				// iterates color array
				int blob = this.undiscoveredBlobSize(i,j,c,visited);
				if (blob > score) {
					// if new score greater than previous, reassign
					score = blob;
				}
			}
		}
		return score;
	}

	@Override
	public String description() {
		return "Create the largest connected blob of " + GameColors.colorToString(targetGoal)
				+ " blocks, anywhere within the block";
	}


	public int undiscoveredBlobSize(int i, int j, Color[][] unitCells, boolean[][] visited) {
		if (i < 0 || j < 0 || i >= unitCells.length || j >= unitCells[0].length || visited[i][j] || unitCells[i][j] != this.targetGoal){
			// if i or j went past or behind array length in initializer, or if the cell has already been visited, or if it does not match
			// the color goal.
			return 0;
		}

		visited[i][j] = true;
		int sizeBlob = 1;
		// marks blob as visited and adds one to its score

		sizeBlob += undiscoveredBlobSize(i - 1, j, unitCells, visited);
		sizeBlob += undiscoveredBlobSize(i + 1, j, unitCells, visited);
		sizeBlob += undiscoveredBlobSize(i, j-1, unitCells, visited);
		sizeBlob += undiscoveredBlobSize(i,j+1, unitCells, visited);
		// checks beside, below, and above the block

		return sizeBlob;

	}

}
