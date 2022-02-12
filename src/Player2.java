
import java.util.*;

//   Class Player represents one roulette player.
class Player
{
	private static final int RELOAD_AMOUNT = 100;
	private int netEarnings = 0;
    private int bet, money, betType, number;
    private String name;  

    //  The Player constructor sets up  name and initial available money.
    public Player (String playerName, int initialMoney)
    {
		name = playerName;
      	money = initialMoney;
   	} 

    //  Returns this player's name.
    public String getName()
    {
      	return name;
    } 

    //  Returns this player's current available money.
    public int getMoney()
    {
      	return money;
    } 

    //  Prompts the user and reads betting information.
    public void makeBet(Scanner scan)
    {
    	boolean valid = false;
    	do
    	{
    		Roulette.betOptions();
    		System.out.print("Enter a bet option, " + name + " (1, 2, or 3): ");
  			betType = scan.nextInt();
  			switch (betType)
  			{
  			case 1:
  				valid = true;
  				break;
  			case 2:
  				valid = true;
  				break;
  			case 3:
  				valid = true;
  				break;
  			default:
  				System.out.println(betType + " is an invalid bet option.");
  				System.out.println("Please try again.");
  				break;
  			}
  		} while (!valid);
    	System.out.println("You chose option " + betType);
    	valid = false;
    	do
    	{
    		System.out.print("How much to bet: ");
    		bet = scan.nextInt();
    		if (bet < Roulette.MIN_BET || bet > money)
    		{
    			System.out.println(bet + " is an invalid bet amount.");
    			System.out.println("Bet amount must be between "
    				+ Roulette.MIN_BET + " and " + money + ".");
    			System.out.println("Please try again.");
    		}
    		else
    			valid = true;
    	} while (!valid);
      	money = money - bet;
      	Roulette.updateCasinoEarnings(bet);
      	if (betType == Roulette.NUMBER)
    	{
    		valid = false;
    		do
    		{
    			System.out.println("Enter the number you would like to bet on:");
    			number = scan.nextInt();
    			if (number < Roulette.MIN_NUM || number > Roulette.MAX_NUM)
    			{
    				System.out.println(number + " is an invalid number.");
    				System.out.println("Number must be between "
    						+ Roulette.MIN_NUM + " and " + Roulette.MAX_NUM + ".");
    				System.out.println("Please try again.");
    			}
    			else
    				valid = true;
    		} while (!valid);
    	}
    } 

    //  Determines if the player wants to play again.
    public boolean playAgain(Scanner scan)
    {
      	String answer;

      	System.out.print ("Play again, " + name + "? [y/n] ");
      	answer = scan.next();
      	return (answer.equals("y") || answer.equals("Y"));
    } 
    
    // payment method (determines winnings)
    public void payment()
    {
    	int payoff = Roulette.payoff(bet, betType, number);
    	System.out.print(name);
    	if (payoff > 0)
    	{
    		int winnings = payoff - bet;
    		System.out.println(" won " + winnings);
    		money += payoff;
    		netEarnings += winnings;
    	}
    	else
    	{
    		System.out.println(" lost -" + bet);
    		netEarnings -= bet;
    	}
    }
    
    public void reload()
    {
    	money += RELOAD_AMOUNT;
    }
    
    public void displayStatus()
    {
    	System.out.print(name);
    	if (netEarnings > 0)
    		System.out.print(" won " + netEarnings);
    	else if (netEarnings < 0)
    		System.out.print(" lost " + Math.abs(netEarnings));
    	else
    		System.out.print(" went even");
    	System.out.println(" for this game.");
    	System.out.println("Thanks for playing.\n");
    }
}
