package application;

public class test {

	public static void main(String[] args) {
		// Declaring objects
		Game Game = new Game();
		
		// Set up the game
		Game.Initiate(); 
		Game.Start_Round(); 
		Game.Play_Turn();
		Game.Play_Turn();
		
//		Game.Players = new Player[1];
//		Player yo = new Player();
//		Game.Players[0] = yo;
//		Game.Players[0].Name = "T";
//		String[] temp = {"01S", "04D", "11D"};
//		Game.Players[0].Hand = temp;
//		Game.Players[0].Sum_Hand();
//		Tools.Show_Hands(Game, 0);
		
	}

}
