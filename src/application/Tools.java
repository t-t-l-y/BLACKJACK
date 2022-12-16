package application;
import java.util.Random;
import java.util.Scanner;

public class Tools {

	///////////////////////////// FIELDS /////////////////////////////
	
	// Input received from user
	static Scanner input = new Scanner(System.in); 
	
	// One deck of cards (52 cards) 
	static String[] Single_Deck = {"01S", "02S", "03S", "04S", "05S", "06S", "07S", "08S", "09S", "10S", "11S", "12S", "13S",
			"01H", "02H", "03H", "04H", "05H", "06H", "07H", "08H", "09H", "10H", "11H", "12H", "13H",
			"01C", "02C", "03C", "04C", "05C", "06C", "07C", "08C", "09C", "10C", "11C", "12C", "13C",	
			"01D", "02D", "03D", "04D", "05D", "06D", "07D", "08D", "09D", "10D", "11D", "12D", "13D"};	
	
	///////////////////////////// METHODS /////////////////////////////
	
	// Asks a question to the user
	static void AskQuestion(String Question) {
		System.out.println(Question);
	}
	
	// Returns the integer input of a user
	static int ReceiveIntAnswer() {

		boolean pass = false;
		int Answer = 0;
		while (!pass) {
			try {
				Answer = input.nextInt();
				pass = true;
			} catch (Exception e) {
				System.out.println("Please enter an integer.");
				input.nextLine();
			}
		}
		input.nextLine(); // Skip new line
		return Answer;
	}
	
	// Returns the string input of a user
		static String ReceiveStrAnswer() {
			String Answer;
			Answer = input.nextLine();
			return Answer;
		}
	
	// Checks game settings inputed from user
	static boolean[] CheckSettings(Game Game) {
		boolean[] Settings_Check = new boolean[2];
		
		Settings_Check[0] = (Game.Total_Players >= 2 && Game.Total_Players <= 5);
		Settings_Check[1] = (Game.Minimum_Bet <= Game.Buy_In);
		
		return Settings_Check;
	}
	
	// Returns an array of strings that is the concatenation of two other arrays of strings
	static String[] Concat_Arr(String[] Array1, String[] Array2) {
		int length1; int length2; // Lengths of arrays 1 and 2
		
		// For null array, set length to 0
		if (Array1 == null) { length1 = 0; } else {length1 = Array1.length;}
		if (Array2 == null) { length2 = 0; } else {length2 = Array2.length;}
		
		// Declare return value
		String[] New_Array = new String[length1 + length2];
		
		// Fill up the return value array
		for (int i = 0; i < length1 + length2; i++) {
			if (i < length1) { New_Array[i] = Array1[i]; }
			else { New_Array[i] = Array2[i - length1]; }
		}
		
		return New_Array;
	}

	// Returns the name of a card from its coded form
	static String Translate_Code(String Code) {
		String Card_Name = "";
		
		// Translate number
		switch (Integer.parseInt(Code.substring(0,2))) {
		case 1:
			Card_Name = "Ace";
			break;
		case 2, 3, 4, 5, 6, 7, 8, 9, 10:
			Card_Name = "" + Integer.parseInt(Code.substring(0,2));
			break;
		case 11:
			Card_Name = "Jack";
			break;
		case 12:
			Card_Name = "Queen";
			break;
		case 13:
			Card_Name = "King";
		}
		
		// Translate suit
		switch (Code.substring(2)) {
		case "S":
			Card_Name += " of Spades";
			break;
		case "H":
			Card_Name += " of Hearts";
			break;
		case "C":
			Card_Name += " of Clubs";
			break;
		case "D":
			Card_Name += " of Diamonds";
		}

		return Card_Name;
	}

	// Shows the current deck (Game.Curr_Deck) in console
	static void Show_Deck(Game Game, int Rows) {
		int Length = Game.Curr_Deck.length; int Display_Row = 1;
		for (int i = 0; i < Length; i++) {
			System.out.print("| Index " + i + ": " + Game.Curr_Deck[i] + " ");
			if (i == Math.ceil(Length / Rows) * Display_Row) {
				Display_Row++;
				System.out.println("");
			}
		}
		System.out.println("");
	}
	
	// Show player's current state unless it's the dealer
	static void Show_State(Game Game, int Index) {
		if (Index != 0) {
		System.out.println(Game.Players[Index].Name + " has " + Game.Players[Index].Earnings + "$.");
		System.out.println(Game.Players[Index].Name + " is currently betting " + Game.Players[Index].Bet + "$.");
		System.out.println(Game.Players[Index].Name + "'s current hand sums to " + Game.Players[Index].Sum_Of_Hand + ".");
		}
	}	
	// Shows the hand of all players in console
	static void Show_Hands(Game Game) {
		for (Player p : Game.Players) {
			System.out.println(p.Name + " has the following hand: ");
			for (int j = 1; j < p.Hand.length + 1; j++) {
				System.out.println("	Card " + j + ": " + Tools.Translate_Code(p.Hand[j - 1]) );
			}
		}
	}
	
	// Shows the hand of all players in console while hiding one of the dealer's cards
	static void Show_Dealer_Hand(Game Game) {
		for (Player p : Game.Players) {
			
			// Hide one card for dealer
			if (p.Name.equals("Dealer")) {
				Show_Dealer_Hand(Game, true);
			}
			else {
				
				// Show all for other players
				System.out.println(p.Name + " has the following hand: ");
				for (int j = 1; j < p.Hand.length + 1; j++) {
					System.out.println("	Card " + j + ": " + Tools.Translate_Code(p.Hand[j - 1]) );
				}
			}
		}
	}
	
	// Shows the hand of the dealer with the option of hiding the first card when the boolean is true and does nothing when boolean is false
	static void Show_Dealer_Hand(Game Game, boolean Hide_One) {
		if (Hide_One) {
			System.out.println(Game.Players[0].Name + " has the following hand: ");
			System.out.println("	Card 1: Hidden,");
			System.out.println("	Card 2: " + Tools.Translate_Code(Game.Players[0].Hand[1]));
		}
	}
	
	// Shows the hand of a single player in console
		static void Show_Hands(Game Game, int Index) {
			
			System.out.println(Game.Players[Index].Name + " has the following hand: ");
			for (int j = 1; j < Game.Players[Index].Hand.length + 1; j++) {
				System.out.println("	Card " + j + ": " + Tools.Translate_Code(Game.Players[Index].Hand[j - 1]) );
			}
		}
	
	// Returns a random index from the array representing the current deck (Game.Curr_Deck)
	static int Random_Card(Game Game) {
		Random Random = new Random();
		int RandomIndex = Random.nextInt(Game.Curr_Deck.length); 
		
		return RandomIndex;
	}
}
