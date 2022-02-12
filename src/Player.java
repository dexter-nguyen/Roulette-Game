import java.util.Scanner;
import java.text.NumberFormat;

public class Player
{
	private String name, Btype = "";
	private double money;
	private double bet;
	private int  betType, number ;
	private double earning = 0;
	private double buyIn = 0 ;
    private double usedMoney = 0;
    private int betCount = 0, betPerRound = 0;
    protected int normalBetCount = 0;
    private double payment = 0;
    private static Transaction report;
    private String color;
    NumberFormat fmt = NumberFormat.getCurrencyInstance();
   
	
	public Player(String name, double money)
	{
		this.name = name;
		this.money = money;
		
	}
	
	
	//getters
	
	public String getName(){
		return name;
	}
	
	public double getMoney(){
		return money;
	}
	
	public String getBtype() {
		return Btype;
	}

	public double getBet(){	
		return bet;
	}
	
	public int getBetType() {
		return betType;
	}


	public int getNumber() {
		return number;
	}

	public double getEarning() {
		return earning;
	}

	public double getBuyIn() {
		return buyIn;
	}

	public double getUsedMoney() {
		return usedMoney;
	}

	public int getBetCount() {
		return betCount;
	}

	public void setBuyIn(int buyIn)
	{
		this.buyIn = buyIn;
	}
	
	public void setMoney(double amt)
	{
		money = amt;
	}
	
	public void updateMoney(double amt)
	{
		money += amt;
	}
	
	
	
	public boolean setBuyIn()
	{
		Scanner scan = new Scanner(System.in);
		NumberFormat fmt = NumberFormat.getCurrencyInstance();
		boolean success = false;
		if (money >= RouletteManager.MIN_BUY_IN)
		{
			boolean valid = false;
			do
			{
				System.out.println("Buy-in must be at least " +
						fmt.format(RouletteManager.MIN_BUY_IN) +
						" and be a multiple of " + RouletteManager.MIN_BUY_IN);
				System.out.print("How much do you want to start with? ");
				try
				{
					int amount = Integer.parseInt(scan.nextLine());
					if (amount >= RouletteManager.MIN_BUY_IN &&
							amount % RouletteManager.MIN_BUY_IN == 0 && amount <= money)
					{
						updateMoney(-amount);
						
						// update player's buy-in
						buyIn = amount;
						valid = success = true;
					}
				} catch(Exception exception) {}
				if (!valid)
					System.out.println("Invalid amount. Please try again.\n");
			} while(!valid);
		}
		
		return success;
	}
//  Prompts the user and reads betting information.
    public void makeBet(Scanner scan, double minBet, double maxBet)
    {
    	boolean valid = false;
    	do
    	{
  		Roulette100A.betOptions();
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
    	}
    	while(!valid);
    	

  		if(betType == 1)
  		{
  			Btype = "B";
  		}
  		else if (betType == 2)
  		{
  			Btype = "R";
  		}
  		
  		valid = false;
  		System.out.println("You chose option: " + betType);
  		
    	
  		
  		if (betType == Roulette100A.NUMBER) //Choose a bet number
  		{	
  			 System.out.print("Enter the number you would like to bet on: ");
	      	do
	      	{
	      	 number = scan.nextInt();
	      	 
	      	 if (number > (Roulette100A.MIN_NUM - 1) && number < (Roulette100A.MAX_NUM + 1))
	      	 {
	      		 break;
	      	 }
	      	 else	 
	      	 System.out.print("Please bet on a number from " + Roulette100A.MIN_NUM + " to " + Roulette100A.MAX_NUM + ": ");	 
	      	}
	      	while( scan.hasNextInt());
	      	Btype = ""+number;
	      	
  		}
  		
  		//****************************************************
  		//earning current buyIn
  		if(buyIn < 1)
  		{
  			setBuyIn();
  		}
  		
  		System.out.print("How much to bet: ");
      	do
      	{
      		bet = scan.nextInt();
      		if (bet <= 1 || bet > buyIn)
      		{
      			System.out.print("Please the bet amount from " + Roulette100A.MIN_BET + " to " + buyIn+ ": ");	
      		}
      		
      		else
      		{
      			break;
      		}
      	}
      	while(scan.hasNextInt() );
      	
      	
      	usedMoney += bet; // update total bets for cashback
      	buyIn -= bet; // money used in the game
      	betCount++; //for SuperVip
      	earning -= bet;
      	normalBetCount++;
      	Roulette100A.updateCasinoEarnings(bet);
      	
      	if ( this instanceof VIP || this instanceof SuperVIP  && betPerRound < 3)
      	{
      		System.out.println("Do you want to bet again ? (Y/N)");
      		String ans = scan.next();
      		if (ans.equals("Y") || ans.equals("y"))
      		{
      			makeBet(scan, minBet, maxBet);
      			betPerRound++;
      			
      		}
      		
      	}
      	
      	
    } 

    
    
    
    public void payment()
    {
    	
    	
    	if (betType == 1)
    	{
    		if (Roulette100A.getColor() == Roulette100A.BLACK && Roulette100A.getBallPosition() != 0 )
    		{
    			payment = Roulette100A.payoff(bet, betType, -2) ;
    			buyIn = buyIn + payment + bet; // update buyIn
    			
    			earning += (payment + bet);
    			
    		}
    		
    		
    	}
    	else if (betType == 2)
    	{
	    	 if( Roulette100A.getColor() == Roulette100A.RED)
	    	 {
	    			payment = Roulette100A.payoff(bet, betType, -2) ;
	    			buyIn = buyIn + payment + bet;	
	    			earning += (payment + bet);
	    	 }
	    	
    	}
    	
    	else if (betType == Roulette100A.NUMBER)
    	{
    		payment = Roulette100A.payoff(bet, betType, number) ;
    		if( payment > 0)
    		{
    		buyIn = buyIn + payment + bet;
    		earning += (payment + bet);
    		}
    	
    	}
    	 endRound();
    	 if ( Roulette100A.getBallPosition() == 0 || Roulette100A.getBallPosition() == Roulette100A.MAX_NUM + 1)
     		color = "GREEN";
     	else if (Roulette100A.getBallPosition() % 2 == 0)
     		color = "BLACK";
     	else
     	{
     		color = "RED";
     	}
    	
    	 report = new Transaction( getName() , getBtype(), payment , bet );
    	
    	
    	
    }
    
    public static Transaction getReport( )
    {
    	return  report;
    }
    
   
    
    
    //Parent methods for VIP
    public void updateCredit(double amt )
    {
    	
    }
    
    public double cashBack()
    {
    	return 0;
    }
    
    public double getCredit()
    {
    	return 0;
    }
   
    
    public void endRound()
    {
    	if (payment > 0)
    	System.out.println( name + " won " + payment);
    	else if ( payment < 0)
    	{
    		System.out.println( name + " lost " + payment);	
    	}
    	
    }
    
    public void leave()
    {
    	// add buyIn to money
    	updateMoney(buyIn);
    	
    	buyIn = 0;
    	
    	
    	if ( earning > 0)
    	{
    		
    		
    		System.out.println(this.getName() + " left the game with winning amount of " + Math.abs(earning) + "$");
    		
    	}
    	else if (earning < 0)
    	{
    		
    		
    		System.out.println(this.getName() + " left the game with losing amount of " + Math.abs(earning) + "$");
    	}
    	else
    	{
    		
    		System.out.println(this.getName() + " left the game with a tie.");
    		
    	}
    }
    
  
    
    
    
    
    
    
	
}
