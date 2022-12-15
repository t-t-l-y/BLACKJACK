package application;

public class Player {

	///////////////////////////// FIELDS /////////////////////////////
	
	String Name; // Name of the player.
	boolean Can_Play; // Becomes false when player earnings are below the minimum bet
	int Earnings; // Player's current total money
	int Bet; // Amount of money bet by the player for the round
	int Push; // Amount of bet money pushed to the next round following a tie
	int Sum_Of_Hand; // Sum of player's cards
	String Hand[]; // Player's hand
	
	///////////////////////////// METHODS /////////////////////////////
	
	public void Set_Can_Play(boolean KeepPlaying) {
		this.Can_Play = KeepPlaying;
	}
	
	// Sets the amount of money bet by a player for the round
	public void Set_Bet(int Bet) {
		this.Bet = Bet;
	}
	
	// Sets the player's starting money
	public void Set_Earnings(int Earnings) {
		this.Earnings = Earnings;
	}
	
	// Sets the amount of money pushed to the next round following a tie 
	public void Set_Push(int Push) {
		this.Push = Push;
	}
	
	// Resets player's hand
	public void Reset_Hand() {
		String[] Temp = {};
		this.Hand = Temp;
	}
	// Changes amount of money earned by a player. Positive means the player won money. Negative means the player lost money
	public void Delta_Money(int MoneyEarned) {
		this.Earnings += MoneyEarned;
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
		for (int i = 0; i < this.Hand.length; i++) {
			switch (Integer.parseInt(this.Hand[i].substring(0,2))) {
			case 1:
				this.Sum_Of_Hand += 11;
				break;
			case 2, 3, 4, 5, 6, 7, 8, 9, 10:
				this.Sum_Of_Hand += Integer.parseInt(this.Hand[i].substring(0,2));
				break;
			default:
				this.Sum_Of_Hand += 10;
			}
		}
		if (this.Sum_Of_Hand > 21) {
			for (int i = 0; i < this.Hand.length; i++) {
				if (this.Hand[i].substring(0, 2) == "01") { this.Sum_Of_Hand -= 10; }
				if (this.Sum_Of_Hand < 21) { break; }
			}
		}
	}
	
	// Ask player if they will hit or stay
	public String Hit_Or_Stay() {
		if (this.Name == "Dealer") {
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
	public void Receive_Card(String Card, Game Game) {
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
