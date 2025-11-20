package assignment3;

import java.awt.Color;

public class PerimeterGoal extends Goal{

	public PerimeterGoal(Color c) {
		super(c);
	}

	@Override
	public int score(Block board) {
		Color[][] block = board.flatten();
		int size = (int) Math.pow(2, board.getMaxDepth());
		int score = 0;

		for (int i = 0; i < size; i++){

			for (int j = 0; j < size; j++){

				if (i == 0 || i == size - 1|| j == 0 || j == size - 1){
					// if block is on x or y axis (perimeter)

					if (block[i][j] == this.targetGoal){
						// if the color matches target color

						if (i == j || (i == 0 && j == size - 1) || (i == size - 1 && j == 0)){
							// if the block is on a corner, add two
							score += 2;
						}
						else {
							score++;
						}
					}
				}
			}
		}

		return score;
	}

	@Override
	public String description() {
		return "Place the highest number of " + GameColors.colorToString(targetGoal)
				+ " unit cells along the outer perimeter of the board. Corner cell count twice toward the final score!";
	}

}
