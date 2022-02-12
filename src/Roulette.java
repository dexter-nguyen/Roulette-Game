import java.text.*;
import java.util.*;

public abstract class Roulette
{
	// public name constants -- accessible to others
    public final static int BLACK     =  0;			// even numbers
    public final static int RED       =  1;			// odd numbers
    public final static int GREEN     =  2;			// 00 OR 0
    public final static int NUMBER    =  3;			// number bet
    public final static int MIN_NUM   =  1;			// smallest number to bet
    public final static int MAX_NUM   = 36;			// largest number to bet
    public final static int MIN_BET   =  1;			// minimum amount to bet

    // private name constants -- internal use only
    private final static int MAX_POSITIONS = MAX_NUM + 2; // number of positions on wheel
    private final static int NUMBER_PAYOFF = MAX_NUM - 1; // payoff for number bet
    private final static int COLOR_PAYOFF  = 2;		// payoff for color bet

    // private variables -- internal use only
    private static int ballPosition = 0;			// 00, 0, 1 .. MAX_NUM
    private static int color = GREEN;				// GREEN, RED, OR BLACK

    private static double casinoEarnings = 0;
    
    private String id; // id of the game.
	private double minimumBet;
	private double maximumBet;
	private int playerCapacity; // Max amount of players that are allowed to play the game.
	private int activePlayers; // Active Players that are in the game
	private Player[] players; // Players array
    
    //=====================================================================
    //  Presents welcome message
    //=====================================================================
    public static void welcomeMessage()
    {
      	System.out.println("Welcome to a simple version of roulette game.");
      	System.out.println("You can place a bet on black, red, or a number.");
      	System.out.println("A color bet is paid " + COLOR_PAYOFF + " the bet amount.");
      	System.out.println("A number bet is paid " + NUMBER_PAYOFF + " the bet amount.");
      	System.out.println("You can bet on a number from " + MIN_NUM + " to " + MAX_NUM + ".");
      	System.out.println("Gamble responsibly.  Have fun and good luck!\n");
    }

    //=====================================================================
    //  Presents betting options
    //=====================================================================
    public static void betOptions()
    {
      	System.out.println("Betting Options:");
      	System.out.println("    1. Bet on black (even numbers)");
      	System.out.println("    2. Bet on red (odd numbers)");
      	System.out.println("    3. Bet on a number between " + MIN_NUM +
      			" and " + MAX_NUM);
      	System.out.println();
    }
    
    public static int getBallPosition() {
		return ballPosition;
	}

	
	public static int getColor() {
		return color;
	}
	
	// Payoff method for number bet
    public static double payoff(double betAmt, int betType, int numberBet)
    {
    	double pay = 0;
    	if (betType == 1 && color == BLACK)
    	{
    		pay = COLOR_PAYOFF * betAmt;
    		casinoEarnings -= pay;
    	}
    	else if (betType == 2 && color == RED)
    	{
    		pay = COLOR_PAYOFF * betAmt;
    		casinoEarnings -= pay;
    	}
    	else if (betType == NUMBER && numberBet == ballPosition)
    	{
    		pay = NUMBER_PAYOFF * betAmt;
    		casinoEarnings -= pay;
    	}
    	return pay;
    }

    /*
    public static void updateCasinoEarnings(int amt)
    {
    	casinoEarnings += amt;
    }*/
	
	public Roulette(String gameId, double minBet, double maxBet, int maxNumPlayers)
	{
		id = gameId;
		minimumBet = minBet;
		maximumBet = maxBet;
		playerCapacity = maxNumPlayers;
		activePlayers = 0;
		players = new Player[playerCapacity];
	}
	
	public String getId() // Gets the iD of the type of Roulette games. 100A1, 100A2, 100A3.
	{
		return id;
	}
	
	public int getMoney()
	{
		return money;
	}
	
	public boolean full() // Fills to the max capacity of active players.
	{
		return activePlayers == playerCapacity;
	}
	
	public boolean empty() // Restarts the amount of active players playing.
	{
		return activePlayers == 0;
	}
	
	public void addPlayer()
	{
		if (!full())
		{
			boolean addedPlayer = false;
			while (!RouletteManager.waitlist.isEmpty() && !addedPlayer)
			{
				Player player = RouletteManager.waitlist.remove();
				Scanner scan = new Scanner(System.in);
				boolean valid = false;
				do
				{
					System.out.println("Enter \"y\" for yes or \"n\" for no.");
					System.out.print("Is " + player.getName() +
							" available to play? ");
					String response = scan.nextLine();
					if (response.equalsIgnoreCase("y"))
					{
						if (player.setBuyIn())
						{
							// add player
							addedPlayer = true;
							System.out.println("Adding " + player.getName() +
									" to the game.");
						}
						else
							System.out.println("Player has insufficient " +
									"funds. Unable to add player.");
						valid = true;
					}
					else if (response.equalsIgnoreCase("n"))
						valid = true;
					else
						System.out.println("Invalid response. " +
								"Please try again.");
					System.out.println();
				} while(!valid);
			}
			if (!addedPlayer)
				System.out.println("Waitlist is empty. " +
						"Unable to add player.\n");
		}
		else
			System.out.println("Game is already full. " +
					"Unable to add player.\n");
	}
	
	public void removePlayer()
	{
		if (!empty())
		{
			Scanner scan = new Scanner(System.in);
			boolean proceed = false;
			boolean valid = false;
			do
			{
				printPlayers();
				System.out.print("Which player do you want to remove? ");
				int pos = findPlayer(scan.nextLine());
				if (pos != -1)
				{	
					// allocate cash back if applicable
					if (players[pos] instanceof VIP || players[pos] instanceof SuperVIP ) // Round to the nearest dollar
					{
						players[pos].updateCredit(players[pos].cashBack());
						System.out.println("A cash back of " + players[pos].getCredit() + " is credited to account " + id);
					}
					
					do // Asks player if he want to move to cash or credit balance.
					{
						System.out.print("You have the option to move bet money to cash or credit? ");
						System.out.println("Enter \"c\" for cash " + "or \"b\" for credit balance.");
						String response = scan.nextLine();
						if (response.equalsIgnoreCase("c"))
							valid = true;
						else if (response.equalsIgnoreCase("b"))
							valid = proceed = true;
						else
							System.out.println("Invalid response. " + "Please try again.\n");
					} while(!valid);
					
					proceed = true;
				}
				else
				{
					System.out.println("Unavailable player. " +
							"Unable to remove player.");
					 valid = false;
					do
					{
						System.out.println("Enter \"y\" for yes " +
								"or \"n\" for no.");
						System.out.print("Would you like to try again? ");
						String response = scan.nextLine();
						if (response.equalsIgnoreCase("y"))
							valid = true;
						else if (response.equalsIgnoreCase("n"))
							valid = proceed = true;
						else
							System.out.println("Invalid response. " +
									"Please try again.\n");
					} while(!valid);
				}
			} while(!proceed);
		}
		else
			System.out.println("Game is already empty. " +
					"Unable to remove player.\n");
	}
	
	public void playRound() // remove player if money/credit is less than the minimum buy-in
	{
		for(int pos = 0; pos < activePlayers; pos++)
		{
			if (players[pos].getBuyIn() < RouletteManager.MIN_BUY_IN)
			{
				players[pos].leave();
			}
		}
		spin();
		
		for(int pos = 0; pos < activePlayers; pos++)
		{
			players[pos].payment();
		}
	}
	
	private int findPlayer(String name)
	{
		int pos = 0;
		boolean found = false;
		while (pos < activePlayers && !found)
		{
			if (players[pos].getName() == name)
			{
				found = true;
			}
			else
			{
				++pos;
			}
		}
		return found ? pos : -1;
	}
	
	private void printPlayers() // Prints the names of players that are still active in the game.
	{
		System.out.print("Available player(s): " + players[0].getName());
		for (int i = 1; i < players.length; ++i)
			System.out.print(", " + players[i].getName());
		System.out.println();
	}
	
    // Spins the wheel
    private void spin()
    {
    	// Generates a number from 0 to MAX_NUM + 1
    	ballPosition = (int) (Math.random() * MAX_POSITIONS);
    	if (ballPosition == 0 || ballPosition == MAX_NUM + 1)
    		color = GREEN;
    	else if (ballPosition % 2 == 0)
    		color = BLACK;
    	else
    		color = RED;
    	System.out.println("Results of the spin:");
    	System.out.print("\tColor: ");
    	switch (color)
    	{
    	case BLACK:
    		System.out.println("Black");
    		break;
    	case RED:
    		System.out.println("Red");
    		break;
    	default:
    		System.out.println("Green");
    		break;
    	}
    	System.out.print("\tNumber: ");
    	if (ballPosition == MAX_NUM + 1)
    		System.out.println("00");
    	else
    		System.out.println(ballPosition);
    }
}