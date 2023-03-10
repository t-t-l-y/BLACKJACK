package application;

public class Player {

	///////////////////////////// FIELDS /////////////////////////////
	
	String Name; // Name of the player.
	boolean Can_Play; // Becomes false when player earnings are below the minimum bet
	boolean Blackjack; // Becomes true when player is dealt an ace and a 10-value card
	double Earnings; // Player's current total money
	double Bet; // Amount of money bet by the player for the round
	int Sum_Of_Hand; // Sum of player's cards
	String Hand[]; // Player's hand
	
	///////////////////////////// METHODS /////////////////////////////
	
	public void Set_Can_Play(boolean KeepPlaying) {
		this.Can_Play = KeepPlaying;
	}
	
	// Resets player's hand
	public void Reset_Hand() {
		String[] Temp = {};
		this.Hand = Temp;
	}
	
	// Checks if the player can play the following round
	public void Keep_Playing(Game Game) {
		if (this.Name == "Dealer") {
			this.Can_Play = true;
		}
		else {
			if (this.Earnings < Game.Minimum_Bet) {
				this.Can_Play = false;
			}
			else {
				this.Can_Play = true;
			}
		}
	}
	
	// Calculates the sum of the player's current hand
	public void Sum_Hand() {
		this.Sum_Of_Hand = 0;
		
		for (String Card : this.Hand) {
			switch (Integer.parseInt(Card.substring(0,2))) {
			case 1:
				this.Sum_Of_Hand += 11;
				break;
			case 2, 3, 4, 5, 6, 7, 8, 9, 10:
				this.Sum_Of_Hand += Integer.parseInt(Card.substring(0,2));
				break;
			case 11, 12, 13:
				this.Sum_Of_Hand += 10;
				break;
			}
		}
		if (this.Sum_Of_Hand > 21) {
			for (String Card : this.Hand) {
				if (Card.substring(0, 2).equals("01")) { this.Sum_Of_Hand -= 10; }
				if (this.Sum_Of_Hand < 21) { break; }
			}
		}
	}
	
	// Ask player if they will hit or stay
	public String Hit_Or_Stay() {
		if (this.Name.equals("Dealer")) {
			if (this.Sum_Of_Hand < 17) { return "y"; }
			else { return "n"; }
		}
		else {
			String Player_Choice = null;
			Tools.AskQuestion("Would you like to hit? Type 'y' to hit and 'n' to stay.");
			do {
				Player_Choice = Tools.ReceiveStrAnswer();
				if (!(Player_Choice.equals("y") || Player_Choice.equals("n"))) { System.out.println("Please try again."); }
			} while (!(Player_Choice.equals("y") || Player_Choice.equals("n")));
			return Player_Choice;
		}
	}
	
	// Adds a card to the player's hand. Called twice when starting a round and once when hitting
	public void Receive_Card(String Card) {
		int Length;
		String[] Temp_Deck;
		
		if (this.Hand == null) { 
			Length = 0; 
			Temp_Deck = new String[Length + 1];
		}
		else { 
			Length = this.Hand.length; 
			Temp_Deck = new String[Length + 1];
			for (int i = 0; i < Length; i++) { Temp_Deck[i] = this.Hand[i]; }
		}
		Temp_Deck[Length] = Card;
		this.Hand = Temp_Deck;
	}
}
