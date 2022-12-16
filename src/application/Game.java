package application;

public class Game {

	///////////////////////////// FIELDS /////////////////////////////
	
	int Turn = 0; // Turn number
	int Round = 0; // Round number
	int Minimum_Bet = 0; // Minimum amount of money that can be bet by the player during the game
	int busted; // Number of players that bust in a round
	int Buy_In = 0; // Buy in to play
	int Total_Players; // Number of total players including the dealer
	int Curr_Player = 0; // Number of the current player
	int Number_Of_Decks; // Number of decks to be played with
	String[] Curr_Deck; // State of the current deck
	String[] Junk_Pile = {}; // State of the current junk pile
	Player[] Players; // Array of the players in the game including dealer (Player 0)
	
	///////////////////////////// METHODS /////////////////////////////
	
	// Initiates the game
	public void Initiate() {
		// User inputs game settings
		boolean[] Settings_Check = {false, false};
			
		// Setting 1: Number of players
		do {
			Tools.AskQuestion("How many players are you? Please enter an between 1 and 4.");
			this.Total_Players = 1 + Tools.ReceiveIntAnswer();
			
			Settings_Check = Tools.CheckSettings(this);
			if (!Settings_Check[0]) { System.out.println("The number of players was inappropriate. Please try again."); }
		} while (!Settings_Check[0]);
		
		// Create array of players
		this.Add_Players(); 
			
		// Setting 2: Player names
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
			
		// Setting 3: Number of decks
		do {
			Tools.AskQuestion("How many decks would you like to play with?");
			this.Number_Of_Decks = Tools.ReceiveIntAnswer();
			if (this.Number_Of_Decks == 0) { System.out.println("Please enter an integer greater than 0."); }
		} while (this.Number_Of_Decks == 0); // Cannot play with 0 cards in the deck
		
		// Build the deck of cards
		this.Build_Deck();
	
		do {
			
			// Setting 4: Set buy-in
			Tools.AskQuestion("What would you like to set the buy-in to?");
			this.Buy_In = Tools.ReceiveIntAnswer();
			for (Player p : this.Players) { p.Earnings = this.Buy_In; }
			
			// Setting 5: Set minimum bet
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
		
		// Reset number of players that have busted 
		this.busted = 0;
		
		// Starts the round if there are remaining players
		if (this.Total_Players != 1) {
			this.Round++;
			this.Turn = 0;
			
			System.out.println("Round " + this.Round + " has begun!");
			
			// Ask players for their bets
			this.Player_Bets();
			
			// Deal two cards to each (remaining) player
			this.Deal_To_Players();
			this.Check_Blackjack();
			
		}
	}
	
	public void End_Round() {
		// Empty hand into junk pile
		this.Hand_To_Junk();
		
		// Settle the player's bets
		this.Award_Players();
	}
	
	// Play all turns of the round
	public void Play_Round() {
		
		// Case 1: Dealer has a Blackjack
		if (this.Players[0].Blackjack) {
			Tools.Show_Hands(this);
			System.out.println("Dealer has a Blackjack! The round is over.");
			Tools.input.nextLine(); // Pause for players to read
		}
		// Case 2: Dealer does not have a Blackjack
		else {
			if (this.Total_Players != 2) { Tools.Show_Dealer_Hand(this); Tools.input.nextLine(); }
			for (int i = 0; i < this.Total_Players; i++) {
				this.Play_Turn();
				
				// If all betting players bust
				if (this.busted == (this.Total_Players - 1)) {
					System.out.println("Everyone has busted!");
					Tools.input.nextLine(); // Pause for players to read
					break;
				}
			}
		}
		
	}
	
	// Next turn
	public void Play_Turn() {
		this.Turn++;
		String Decision;
		boolean Hide_Card = true;
		
		// Determine the current player
		if (this.Turn == this.Total_Players) { Curr_Player = 0; Hide_Card = false; }
		else { Curr_Player = this.Turn; }
		
		// Case 1: Current player has a Blackjack, they do not need to play the round
		if (this.Players[Curr_Player].Blackjack) {
			Tools.Show_Hands(this, Curr_Player);
			System.out.println("Player " + this.Players[Curr_Player].Name + " has a Blackjack!");
			Tools.input.nextLine(); // Pause for players to read
		}
		
		// Case 2: Current player does not have a Blackjack
		else {
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
					System.out.println(this.Players[Curr_Player].Name + "'s new hand sums to " + this.Players[Curr_Player].Sum_Of_Hand + ".");
					Tools.Show_Hands(this, Curr_Player);
					
					// If the dealer hits, pause to allow players to read
					if (this.Players[Curr_Player].Name.equals("Dealer")) { Tools.input.nextLine(); }
					
					// If the player busts after hitting
					if (this.Players[Curr_Player].Sum_Of_Hand > 21) {
						this.busted++;
						System.out.println(this.Players[Curr_Player].Name + " has busted!");
						Tools.input.nextLine(); // Pause for players to read
						break;
					}
					
					// If the player gets 21
					if (this.Players[Curr_Player].Sum_Of_Hand == 21) {
						System.out.println(this.Players[Curr_Player].Name + " has hit 21! Congratulations!");
						Tools.input.nextLine(); // Pause for players to read
						break;
					}
				}
				if (Decision.equals("n")) { System.out.println(this.Players[Curr_Player] + " has decided to stay!"); Tools.input.nextLine(); }
			} while (Decision.equals("y"));
		}
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
			System.out.println("Game is over. No one can keep playing! The virtual house thanks you. :)");
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
				else {
					System.out.println(p.Name + " has insufficient funds. They have dropped out!");
					Tools.input.nextLine(); // Pause for players to read
				}
			}
			this.Players = Temp;
		}
	}
	
	// Assign bets to players
	public void Player_Bets() {
		for (int i = 1; i < this.Total_Players; i++) {
			Tools.AskQuestion("How much would " + this.Players[i].Name + " like to bet? The minimum is " + this.Minimum_Bet + "$.");
			do {
				this.Players[i].Bet = Tools.ReceiveIntAnswer();
				if (this.Players[i].Bet < this.Minimum_Bet) { System.out.println("Please bet at least " + this.Minimum_Bet + "$."); }
				if (this.Players[i].Bet > this.Players[i].Earnings) { System.out.println("You only have " + this.Players[i].Earnings + "$ to bet."); }
			} while (this.Players[i].Bet < this.Minimum_Bet || this.Players[i].Bet > this.Players[i].Earnings);
			System.out.println("Player " + this.Players[i].Name + " has bet " + this.Players[i].Bet + "$.");
		}
	}
	
	// Settle wins/losses of each player
	public void Award_Players() {
		
		for (Player p : this.Players) {
			if (p.Sum_Of_Hand > 21) { p.Sum_Of_Hand = -1; }
		}
		
		for (int i = 1; i < this.Total_Players; i++) {
			
			// CASE 1: Dealer and player have a Blackjack or have the same sum of hand (tie)
			if (this.Players[0].Sum_Of_Hand == this.Players[i].Sum_Of_Hand) {
				System.out.println(this.Players[i].Name + " has tied! Their bet has been returned to them. They now have " + this.Players[i].Earnings + "$.");
			}
			
			// CASE 2: Dealer has a Blackjack but not the player
			else if (this.Players[0].Blackjack && (!this.Players[i].Blackjack)) {
				this.Players[i].Earnings -= this.Players[i].Bet;
				System.out.println("Since the dealer has a Blackjack, " + this.Players[i].Name + " has lost their bet of " + this.Players[i].Bet + ". They now have " + this.Players[i].Earnings + "$.");
			}
		
			// CASE 3: Player has a Blackjack but not the dealer
			else if (!this.Players[0].Blackjack && this.Players[i].Blackjack) {
				this.Players[i].Earnings += 1.5 * this.Players[i].Bet;
				System.out.println(this.Players[i].Name + " has won with a Blackjack! Congratulations! They have earned " + (1.5 * this.Players[i].Bet) + "$. They now have " + this.Players[i].Earnings + "$.");
			}
			
			// CASE 4: Neither have a Blackjack, but dealer has superior hand
			else if (this.Players[0].Sum_Of_Hand > this.Players[i].Sum_Of_Hand) {
				this.Players[i].Earnings -= this.Players[i].Bet;
				System.out.println(this.Players[i].Name + " has lost their bet of " + this.Players[i].Bet + "$. They now have " + this.Players[i].Earnings + "$.");
			}
			
			// CASE 5: Neither have a Blackjack, but the player has a superior hand
			else if (this.Players[0].Sum_Of_Hand < this.Players[i].Sum_Of_Hand) {
				this.Players[i].Earnings += this.Players[i].Bet;
				System.out.println(this.Players[i].Name + " has won! They have earned " + this.Players[i].Bet + "$. They now have " + this.Players[i].Earnings + "$.");
			}

			this.Players[i].Bet = 0;
		}
		Tools.input.nextLine(); // Pause for players to read
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
	
	// Check for blackjack hand
	public void Check_Blackjack() {
		for (Player p : this.Players) {
			if (p.Hand.length == 2 && p.Sum_Of_Hand == 21) {
				p.Blackjack = true;
			}
			else { p.Blackjack = false; }
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
		String[] Empty_Hand = {};
		
		for (Player p : this.Players) { Player_Cards += p.Hand.length; }
		String[] Temp_Deck = new String[this.Junk_Pile.length + Player_Cards];
		
		for (Player p : this.Players) {
			for (String Card : p.Hand) {
				Temp_Deck[Index] = Card;
				Index++;
			}
			p.Hand = Empty_Hand;
		}
		
		for (String Card : this.Junk_Pile) {
			Temp_Deck[Index] = Card;
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
