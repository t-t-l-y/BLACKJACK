package application;

public class test {

	public static void main(String[] args) {
		// Declaring objects
		Game Game = new Game();
		
		// Set up the game
		Game.Initiate();
		
		// Play a round
		while (Game.Total_Players != 1) {
			Game.Start_Round(); 
			if (Game.Total_Players != 1) {
				Game.Play_Round();
				Game.End_Round();
			}
			else {
				System.out.println("Game over!");
			}
		}
	}

}
