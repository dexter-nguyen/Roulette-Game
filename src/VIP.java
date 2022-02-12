
public class VIP extends Player
{
	protected String id;
	private double credit = 0;
	protected final double CASH_BACK_RATE = 0.05;
	
	
	public VIP(String name, double money, String id)
	{
		super(name, money);
		this.id = id;
	}
	
	
	public String getId()
	{
		return id;
	}



	public double getCredit()
	{
		return  credit;
	}
	
	public void setCredit(double amt)
	{
		credit = amt;
	}
	
	public void updateCredit(double amt)
	{
		credit += amt;
	}
	
	
	public double cashBack() 
	{
		
		double cashBack = (int) (getUsedMoney() * CASH_BACK_RATE ); // Truncated to whole a dollar	
		return cashBack;
	}
	
	//take cash back in credit
	public void resultCredit()
	{
		credit += cashBack() ;
	
	}
	
}	

