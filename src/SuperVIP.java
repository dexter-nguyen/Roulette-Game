import java.util.Scanner;
import java.text.NumberFormat;

public class SuperVIP extends VIP
{
	public SuperVIP(String name, double money, String id, double credit)
	{
		super(name, money, id);
	}
	
	@Override
	public boolean setBuyIn()
	{
		Scanner scan = new Scanner(System.in);
		NumberFormat fmt = NumberFormat.getCurrencyInstance();
		boolean success = false;
		boolean useCredit = false;
		System.out.println("You have a credit balance of " +
				fmt.format(getCredit()));
		if (getCredit() >= RouletteManager.MIN_BUY_IN)
		{
			boolean valid = false;
			do
			{
				System.out.println("Enter \"y\" for yes or \"n\" for no.");
				System.out.print("Do you want to use your credit " +
						"balance? ");
				String response = scan.nextLine();
				if (response.equalsIgnoreCase("y"))
					valid = useCredit = true;
				else if (response.equalsIgnoreCase("n"))
					valid = true;
				else
					System.out.println("Invalid response. " +
							"Please try again.\n");
			} while(!valid);
		}
		else
			System.out.println("Credit balance is less than the minimum " +
					"buy-in of " + fmt.format(RouletteManager.MIN_BUY_IN) +
					". Unable to use credit balance.");
		if (useCredit || getMoney() >= RouletteManager.MIN_BUY_IN)
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
							amount % RouletteManager.MIN_BUY_IN == 0)
					{
						if (useCredit)
							updateCredit(-amount);
						else
						{
							updateMoney(-amount);
						// update player's buy-in
							setBuyIn(amount);
							
						}
						valid = success = true;
					}
				} 
				catch(Exception exception) {}
				if (!valid)
					System.out.println("Invalid amount. Please try again.\n");
			} while(!valid);
		}
		return success;
	}
	@Override
	public double cashBack()
	{
		double cashBack = super.getUsedMoney() * CASH_BACK_RATE;
		cashBack = cashBack  * 100;
		cashBack = Math.round(cashBack);
		cashBack = cashBack /100;
		return cashBack;
		 // round to the nearest cents
	}
	
	//Bet bonus number of bets in each game
	public int betBonus()
	{
		int bonus = 0;
		if (getBetCount() >= 3 && getBetCount() < 5)
		{
			bonus = 1;
		}
		else if (getBetCount() >= 5)
		{
			bonus = 5;
		}
		
		
		
		return bonus;
	}
	
	//Option to get cash back in credit
	@Override
	public void resultCredit()
	{
		//Deduc
		super.updateMoney(-getEarning());
		
		//credit = earningg + cash back + bonus
		super.updateCredit(super.getCredit() + getEarning() +this.cashBack() + this.betBonus());
		super.setCredit(0);
	
	}
	
	//Option to get cash back in cash = earning + cash back + bonus
	public void resultCash()
	{
		super.setMoney(super.getMoney() + getEarning() +
				this.cashBack() + this.betBonus());
		super.setCredit(0);
		
	}
	
	
	
}
