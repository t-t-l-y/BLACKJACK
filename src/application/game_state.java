package application;
import java.util.Random;
import java.util.Scanner;

public class game_state {
	
	///////////////////////////// FIELDS /////////////////////////////
	
	// One deck of cards (52 cards) 
	static String[] single_deck = {"01S", "02S", "03S", "04S", "05S", "06S", "07S", "08S", "09S", "10S", "11S", "12S", "13S",
									"01H", "02H", "03H", "04H", "05H", "06H", "07H", "08H", "09H", "10H", "11H", "12H", "13H",
									"01C", "02C", "03C", "04C", "05C", "06C", "07C", "08C", "09C", "10C", "11C", "12C", "13C",	
									"01D", "02D", "03D", "04D", "05D", "06D", "07D", "08D", "09D", "10D", "11D", "12D", "13D"};					
	int number_of_decks; // Number of decks to be played with
	int turn; // Turn number of the round
	int curr_player; // Current player to stay or hit
	int start_player; // Player who starts the round
	int[] player_score; // Number of wins if more than one player ADD DEALER
	int[] card_sum; // Sum of current hand for each player and dealer
	String[] curr_deck; // Current deck
	String[] junk_pile = {}; // Pile of used cards
	String[][] player_hand; // Hand of each player
	String[] dealer_hand; // Hand of the dealer
	
	
	///////////////////////////// METHODS /////////////////////////////
	
	// Ask user how many decks to play with
	public void how_many() {
		// scanner
		Scanner input = new Scanner(System.in);
							
		// enter a number
		System.out.println("Enter the number of decks you wish to play with: ");
		this.number_of_decks = input.nextInt();
	}
	
	// Start game
	public void start_game(table table) {
		this.turn = 1; // Set turn number to 1
		this.start_player = 0; // Starting player is player 0
		this.curr_player = this.start_player; // Set current player to starting player
		for (int i = 0; i < table.num_player; i++) { this.card_sum[i] = 0; this.player_score[i] = 0; } // Set card sums and scores to 0
		
	}
	
	// New round
	public void new_round(table table) {
		this.start_player = ( this.start_player + 1 ) % table.num_player; // Starting player rotates
		this.curr_player = this.start_player; // Set current player to starting player
		this.turn = 1; // Set turn number to 1
		for (int i = 0; i < table.num_player; i++) { this.card_sum[i] = 0; } // Set card sums to 0
	}
	
	// Concatenate arrays
	static String[] concat_arr(String[] arr_A, String[] arr_B) {
		int len_A; int len_B; // lengths of arrays A and B
		
		// For null array, set length to 0
		if (arr_A == null) { len_A = 0; } else { len_A = arr_A.length; }
		if (arr_B == null) { len_B = 0; } else { len_B = arr_B.length; }
		
		// Declare output
		String[] arr_final = new String[len_A + len_B];
		
		// Filling up the output array
		for (int i = 0; i < len_A + len_B; i++) {
			if (i < len_A) {
				arr_final[i] = arr_A[i];
			}
			else {
				arr_final[i] = arr_B[i - len_A];
			}
		}
		
		return arr_final;
	}
	
	// Give winner one point
	public void award_point(table table) {
		int winner = 0;
		boolean tie = false;
		
		if (table.num_player == 1) { return; }
		
		for (int i = 0; i < table.num_player; i++) {
			if (this.card_sum[i] > 21) { this.card_sum[i] = -1; }
		}
		
		for (int i = 1; i < table.num_player; i++) {
			if (card_sum[winner] > card_sum[i]) { continue; }
			else if (card_sum[winner] == card_sum[i]) { tie = true; }
			else {
				winner = i;
				tie = false;
			}
		}
		
		if (card_sum[winner] < 0) {
			System.out.println("Everyone busts!");
		}
		else if (tie == true) {
			System.out.println("It's a tie! Nobody wins.");
		}
		else { 
			player_score[winner]++; 
			System.out.println("Player " + winner + " wins!");
		}
		
	}
	
	// Calculate player hands
	public void hand_sum(int player_num) {	
		for (int i = 0; i < this.player_hand[player_num].length; i++) {
			switch (Integer.parseInt(this.player_hand[player_num][i].substring(0,2))) {
			case 1:
				this.card_sum[player_num] += 11;
				break;
			case 2, 3, 4, 5, 6, 7, 8, 9, 10:
				this.card_sum[player_num] += Integer.parseInt(this.player_hand[player_num][i].substring(0,2));
				break;
			default: 
				this.card_sum[player_num] += 10;	
			}
		}
		
		if (this.card_sum[player_num] > 21) {
			for (int i = 0; i < this.player_hand[player_num].length; i++) {
				if (this.player_hand[player_num][i].substring(0, 2) == "01") {
					this.card_sum[player_num] -= 10;
				}
				if (this.card_sum[player_num] <= 21) { break; }
			}
		}
	}
	
	// Form final deck of n decks
	public void deck_builder() {
		if (this.number_of_decks == 1) { this.curr_deck = game_state.single_deck; }
		else {
			String[] temp_deck = game_state.single_deck;
			for (int i = 0; i < this.number_of_decks - 1; i++) {
				this.curr_deck = concat_arr(temp_deck, game_state.single_deck);
				temp_deck = this.curr_deck;
			}
		}
	}
	
	// Generate a random card index from the deck
	public int random_card() {
		Random random = new Random();
		int index = random.nextInt(this.curr_deck.length);
		System.out.println("Random index is " + index);
		return index;
	}
	
	// Remove a card from the current deck TODO if length of curr_deck is 0, set curr_deck to junk and junk to empty 
	public void rm_card(int remove) {
		String[] temp_deck = new String[this.curr_deck.length - 1];
		int skip = 0;
		for (int i = 0; i < this.curr_deck.length - 1; i++) {
			if (i == remove) { skip = 1; }
			temp_deck[i] = this.curr_deck[i + skip];
		}
		this.curr_deck = temp_deck;
	}

	// Deal first two cards to each player
	public void deal_two(table table) {
		this.player_hand = new String[table.num_player][2];
		int card_index;
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < table.num_player; j++) {
				card_index = this.random_card();
				this.player_hand[j][i] = this.curr_deck[card_index];
				this.rm_card(card_index);
			}
		}
	}
		
	// Player #player_num decides to hit
	public void hit(table table, int player_num) {
		int random_card = this.random_card();
		String[] new_card = {this.curr_deck[random_card]};
		this.player_hand[player_num] = concat_arr(this.player_hand[player_num], new_card);
		this.rm_card(random_card);
	}
	
	// Hit loop TODO add card sum update
	public void loop_hit(table table, int player_num) {
		// scanner
		Scanner input = new Scanner(System.in);
		String choice; // Hit or Stay
		do {
			System.out.println("Hit or stay? Type 'hit' to hit or 'stay' to stay.");
			choice = input.nextLine();
			if (choice != "hit" || choice != "stay") { continue; }
			else if (choice == "hit") {
				this.hit(table,  player_num);
			}
		} while(choice == "hit");
	}
	
	// Empty player hands at the end of the round
	public void empty_hands(table table) {
		int total_cards = 0, index = 0;
		for (int i = 0; i < table.num_player; i++) {
			total_cards = total_cards + this.player_hand[i].length;
		}
		String[] temp = new String[total_cards];
		for (int i = 0; i < table.num_player; i++) {
			for (int j = 0; j < this.player_hand[i].length; j++) {
				temp[index] = this.player_hand[i][j];
				index++;
			}
		}
		this.junk_pile = game_state.concat_arr(temp,  this.junk_pile);
	}

	// Translate deck index into card name
	public String tl_card(String card) {
		String number = null, suit = null;
		
		// Translate number
		switch (Integer.parseInt(card.substring(0,2))) {
			case 1:
				number = "Ace";
				break;
			case 2, 3, 4, 5, 6, 7, 8, 9, 10:
				number = "" + Integer.parseInt(card.substring(0,2));
				break;
			case 11:
				number = "Jack";
				break;
			case 12:
				number = "Queen";
				break;
			case 13:
				number = "King";
				break;
		}
		// Translate suit
		switch (card.substring(2)) {
			case "S":
				suit = "Spades";
				break;
			case "H":
				suit = "Hearts";
				break;
			case "C":
				suit = "Clubs";
				break;
			case "D":
				suit = "Diamonds";
				break;
		}
		// Return card name
		return number + " of " + suit;
	}
	
	// Visualize deck
	public void show_deck(game_state game_state, int rows) {
		int len = game_state.curr_deck.length; int display_row = 1;
		for (int i = 0; i < len; i++) {
			System.out.print("| Index " + i + ": " + game_state.curr_deck[i] + " ");
			if (i == Math.ceil(len / rows) * display_row) {
				display_row++;
				System.out.println("");
			}
		}
		System.out.println("");
	}
	
	// Visualize hand
	public void show_hand(game_state game_state, int player_num) {
		int position;
		for (int i = 0; i < game_state.player_hand[player_num].length; i++) {
			position = i + 1;
			System.out.println("Card " + position + ": " + game_state.tl_card(game_state.player_hand[player_num][i]));
		}
	}
	

}

