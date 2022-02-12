import java.util.*;

public class Transaction 
{
	private String playerName;
	private double bet;
	private String betType;
	private double pay;
	
	ArrayList<Transaction> report = new ArrayList();
	
	
	public Transaction(String name, String btype, double payment, double bet )
	{
		playerName = name;
		betType = btype;
		pay = payment;
		this.bet = bet;
		
	}
	
	

	public void addReport(Transaction round)
	{
		report.add(round);
	}
	
	public String toString()
	{
	
		return playerName + "\t" + bet + "\t" + betType + "\t" + pay + "\n";
	}
	
	
	
}