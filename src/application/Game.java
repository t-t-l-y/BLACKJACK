package application;

public class Game {

	///////////////////////////// FIELDS /////////////////////////////
	
	int Turn = 0; // Turn number
	int Round = 0; // Round number
	int Minimum_Bet = 0; // Minimum amount of money that can be bet by the player during the game
	int Buy_In = 0; // Buy in to play
	int Total_Players; // Number of total players including the dealer
	int Curr_Player = 0; // Number of the current player
	int Number_Of_Decks; // Number of decks to be played with
	String[] Curr_Deck; // State of the current deck
	String[] Junk_Pile; // State of the current junk pile
	Player[] Players; // Array of the players in the game including dealer (Player 0)
	
	///////////////////////////// METHODS /////////////////////////////
	
	// Initiates the game
	public void Initiate() {
		// User inputs game settings
		boolean[] Settings_Check = {false, false};
			
		// Number of players
		do {
			Tools.AskQuestion("How many players are you? Please enter an between 1 and 4.");
			this.Total_Players = 1 + Tools.ReceiveIntAnswer();
			
			Settings_Check = Tools.CheckSettings(this);
			if (!Settings_Check[0]) { System.out.println("The number of players was inappropriate. Please try again."); }
		} while (!Settings_Check[0]);
		
		// Create array of players
		this.Add_Players(); 
			
		// Player names
		Tools.AskQuestion("What is your name? Your name cannot be 'Dealer'");
		for (int i = 0; i < this.Total_Players; i++) {
			if (i == 0) { Players[i].Name = "Dealer"; }
			else {
				do {
					System.out.println("Player " + i + "'s name: ");
					Players[i].Name = Tools.ReceiveStrAnswer();
					if (Players[i].Name.equals("Dealer")) { System.out.println("Your name cannot be 'Dealer.' Please try again."); }
				} while (Players[i].Name.equals("Dealer"));
			}
		}
			
		// Number of decks
		do {
			Tools.AskQuestion("How many decks would you like to play with?");
			this.Number_Of_Decks = Tools.ReceiveIntAnswer();
			if (this.Number_Of_Decks == 0) { System.out.println("Please enter an integer greater than 0."); }
		} while (this.Number_Of_Decks == 0); // Cannot play with 0 cards in the deck
		
		// Build the deck of cards
		this.Build_Deck();
	
		do {
			
			// Set buy-in
			Tools.AskQuestion("What would you like to set the buy-in to?");
			this.Buy_In = Tools.ReceiveIntAnswer();
			for (Player p : this.Players) { p.Set_Earnings(this.Buy_In); }
			
			// Set minimum bet
			Tools.AskQuestion("What would you like to set the minimum bet to? It must be less or equal than " + this.Buy_In + ".");
			this.Minimum_Bet = Tools.ReceiveIntAnswer();
			
			Settings_Check = Tools.CheckSettings(this);
			if (!Settings_Check[1]) { System.out.println("The minimum bet cannot exceed the buy-in. Please try again."); }
			
		} while (!Settings_Check[1]);
	}
	
	// Starts a new round and plays the first turn
	public void Start_Round() {
		
		// Check if player can play
		this.Remove_Players();
		
		// Starts the round if there are remaining players
		if (this.Total_Players != 1) {
			this.Round++;
			this.Turn = 0;
			// Part of algorithm to determine next player
			this.Curr_Player = this.Round % this.Total_Players;
			if (this.Curr_Player == 0) { this.Curr_Player++; }
			
			// Deal two cards to each (remaining) player
			this.Deal_To_Players();
		}
	}
	
	public void End_Round() {
		// Empty hand into junk pile
	}
	
	// Next turn
	public void Play_Turn() {
		this.Turn++;
		String Decision;
		boolean Hide_Card = true;
		
		// Part of algorithm to determine next player
		if ((this.Turn % this.Total_Players) == 0) { 
			this.Curr_Player = 0; 
			Hide_Card = false;
		}
		else if (this.Turn != 1) { 
			this.Curr_Player = (this.Curr_Player + 1) % this.Total_Players;
			if (this.Curr_Player == 0) { this.Curr_Player++; }
			}
		
		System.out.println("It is " + this.Players[Curr_Player].Name + "'s turn.");
		Tools.Show_State(this, Curr_Player);
		Tools.Show_Dealer_Hand(this, Hide_Card);
		Tools.Show_Hands(this, Curr_Player);
		
		do {
			Decision = this.Players[Curr_Player].Hit_Or_Stay();
			if (Decision.equals("y")) {
				int Random = Tools.Random_Card(this);
				
				// Check if there are remaining cards in the deck. Otherwise, add junk pile to the current deck
				if (this.Curr_Deck.length == 0) { this.Empty_Junk(); }
				
				this.Players[Curr_Player].Receive_Card(this.Curr_Deck[Random]);
				this.Remove_Card(Random);
				this.Players[Curr_Player].Sum_Hand();
				Tools.Show_Hands(this, Curr_Player);
			}
		} while (Decision.equals("y"));
	}
	
	// Create the array of players
	public void Add_Players() {
		this.Players = new Player[this.Total_Players];
		for (int i = 0; i < this.Total_Players; i++) {
			this.Players[i] = new Player();
		}
	}
	
	// Remove players if they can no longer keep playing
	public void Remove_Players() {
		int Remove = 0; // Number of players to remove
		
		for (Player p : this.Players) {
			p.Keep_Playing(this);
			if (!p.Can_Play) { Remove++; }
		}
		
		this.Total_Players -= Remove;
		
		// If there are no more players left
		if (this.Total_Players == 1) {
			System.out.println("Game is over. No one can keep playing!");
		}
		
		// Otherwise, if a player is eliminated
		else if (Remove != 0) {
			Player[] Temp = new Player[this.Total_Players];
			int Index = 0;
			for (Player p : this.Players) {
				if (p.Can_Play) {
					Temp[Index] = p;
					Index++;
				}
			}
			this.Players = Temp;
		}

	}
	
	// Settle wins/losses of each player
	public void Award_Players() {
		
	}
	
	// Deal two cards to each player at the start of a round
	public void Deal_To_Players() {
		int Random;
		for (int i = 0; i < 2; i++) {
			for (Player p : Players) {
				// Draw a random card
				Random = Tools.Random_Card(this);
				
				// Check if there are remaining cards in the deck. Otherwise, add junk pile to the current deck
				if (this.Curr_Deck.length == 0) { this.Empty_Junk(); }
				
				// Give it to the player and remove from the current deck
				p.Receive_Card(this.Curr_Deck[Random]);
				p.Sum_Hand();
				this.Remove_Card(Random);
			}
		}
	}
	
	// Builds the starting deck to be played with
	public void Build_Deck() {
		if (this.Number_Of_Decks == 1) { this.Curr_Deck = Tools.Single_Deck; }
		else {
			String[] Temp_Deck = Tools.Single_Deck;
			for (int i = 0; i < this.Number_Of_Decks - 1; i++) {
				this.Curr_Deck = Tools.Concat_Arr(Temp_Deck, Tools.Single_Deck);
				Temp_Deck = this.Curr_Deck;
			}
		}
	}

	// Removes a card from the current deck (Game.Curr_Deck)
	public void Remove_Card(int Index) {
		String[] Temp_Deck = new String[this.Curr_Deck.length - 1];
		int Skip = 0;
		
		for (int i = 0; i < this.Curr_Deck.length - 1; i++) {
			if (i == Index) { Skip = 1; }
			Temp_Deck[i] = this.Curr_Deck[i + Skip];
		}
		
		this.Curr_Deck = Temp_Deck;
	}
	
	// Empty player hand into junk pile at the end of the round
	public void Hand_To_Junk() {
		int Player_Cards = 0; int Index = 0;
		
		for (int i = 1; i < this.Total_Players + 1; i++) { Player_Cards += Players[i].Hand.length; }
		String[] Temp_Deck = new String[Player_Cards + this.Junk_Pile.length];
		for (int i = 1; i < this.Total_Players + 1; i++) {
			for (int j = 0; j < this.Players[i].Hand.length; j++) {
				Temp_Deck[Index] = this.Players[i].Hand[j];
				Index++;
			}
		}
		
		for (int i = 0; i < this.Junk_Pile.length; i++) {
			Temp_Deck[Index] = this.Junk_Pile[i];
			Index++;
		}
		
		this.Junk_Pile = Temp_Deck;
	}
	
	// Empties the junk pile and transfers the cards to the current deck 
	public void Empty_Junk() {
		String[] Temp_Deck = new String[this.Curr_Deck.length + this.Junk_Pile.length];
		String[] Empty_Junk = {};
		for (int i = 0; i < Temp_Deck.length; i++) {
			if (i < this.Curr_Deck.length) { Temp_Deck[i] = this.Curr_Deck[i]; }
			else { Temp_Deck[i] = this.Junk_Pile[i - this.Curr_Deck.length]; }
		}
		
		this.Curr_Deck = Temp_Deck;
		this.Junk_Pile = Empty_Junk;
	}
}
