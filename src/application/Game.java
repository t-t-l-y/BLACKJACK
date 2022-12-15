package application;

public class Game {

	///////////////////////////// FIELDS /////////////////////////////
	
	int Turn = 0; // Turn number
	int Round = 0; // Round number
	int Minimum_Bet = 0; // Minimum amount of money that can be bet by the player during the game
	int Buy_In = 0; // Buy in to play
	int Total_Players; // Number of total players including the dealer
	int Curr_Player = 0; // Number of the current player
	int Start_Player = 0; // Number of the starting player
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
	
	// Starts a new round
	public void Start_Round() {
		
	}
	
	// Next turn
	public void Next_Turn() {
		
	}
	
	// Create the array of players
	public void Add_Players() {
		this.Players = new Player[this.Total_Players];
		for (int i = 0; i < this.Total_Players; i++) {
			this.Players[i] = new Player();
		}
	}
	
	// Deal two cards to each player at the start of a round
	public void Deal_To_Players() {
		for (int i = 0; j < this.Total_Players; j++) {
			for (int j = 0; j < this.Total_Players; j++) {
				
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
	
	// Empty player hand into junk pile
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
