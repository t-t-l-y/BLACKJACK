package application;

public class test {

	public static void main(String[] args) {
		// Declaring objects
		Game Game = new Game();
		
		// Set up the game
		Game.Initiate();
		Game.Start_Round();
		
		System.out.println(Game.Players[1].Hit_Or_Stay());
		
	}

}
