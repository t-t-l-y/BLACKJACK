package application;
import java.util.Scanner;

public class table {
	int num_player; // Number of players seated 
	
	// Ask user how many players there are and how many decks to play with
	public void how_many() {
		// scanner
		Scanner input = new Scanner(System.in);
							
		// enter a number
		do {
			System.out.println("Enter a number of players between 1 and 4: ");
			this.num_player = input.nextInt();
		} while (this.num_player < 1 || this.num_player > 4);
	}
	

}
