import java.util.*;
import java.io.*;

public class RouletteManager
{
	final static File gameFile = new File("F:\\games.txt");
	final static File playerFile = new File("F:\\players.txt");
	final static File reportFile = new File("F:\\reports.txt");
	final static int MIN_BUY_IN = 100;
	final static int GAME_CAPACITY = 10;
	static int activeGames = 0;
	static Roulette100A[] games = new Roulette100A[GAME_CAPACITY];
	static Queue<Player> waitlist = new LinkedList<Player>();
	public static PrintWriter outFile ;
	
	public static void main(String[] args)
	{
		launch();
	}
	
	private static boolean initializeGames() throws FileNotFoundException
	{	
		System.out.println("Initialize games. Please wait ...");
		Scanner fileScan = new Scanner(gameFile);
		boolean validData = true;
		while (fileScan.hasNextLine() && validData)
		{
			try
			{
				Scanner lineScan = new Scanner(fileScan.nextLine());
				String gameVer = lineScan.next();
				int numGames = lineScan.nextInt();
				if (numGames < 1)
					validData = false;
				boolean validGameVer = gameVer.equals("100A");
				int count = 1;
				while (fileScan.hasNextLine() && count <= numGames)
				{
					lineScan = new Scanner(fileScan.nextLine());
					if (validGameVer)
					{
						double minBet = lineScan.nextDouble();
						double maxBet = lineScan.nextDouble();
						int playerCapacity = lineScan.nextInt();
						validData = minBet > 0 && minBet <= maxBet &&
								playerCapacity > 0;
						if (validData && activeGames < games.length)
						{
							games[activeGames] = new Roulette100A("100A" + count,
									minBet, maxBet, playerCapacity);
							++count;
							++activeGames;
						}
					}
				}
			} catch(Exception exception) {
				validData = false;
			}
		}
		fileScan.close();
		return validData;
	}
	
	private static void initializeWaitlist() throws FileNotFoundException
	{
		Scanner fileScan = new Scanner(playerFile);
		while (fileScan.hasNextLine())
		{
			try
			{
				Scanner lineScan = new Scanner(fileScan.nextLine());
				Player player = null;
				int playerType = lineScan.nextInt();
				double money = lineScan.nextDouble();
				String name = lineScan.next();
				if (money >= 0)
				{
					switch(playerType)
					{
					case 0:
						player = new Player(name, money);
						break;
					case 1:
						String idVIP = lineScan.next();
						player = new VIP(name, money, idVIP);
						break;
					case 2:
						String idSuperVIP = lineScan.next();
						double credit = lineScan.nextDouble();
						if (credit >= 0)
							player = new SuperVIP(name, money, idSuperVIP,
									credit);
						break;
					default:
						break;
					}
					if (player != null)
						waitlist.add(player);
				}
			} catch(Exception exception) {}
		}
		fileScan.close();
	}
	
	private static void printGames()
	{
		System.out.print("Available game(s): " + games[0].getId());
		for (int i = 1; i < activeGames; ++i)
			System.out.print(", " + games[i].getId());
		System.out.println();
	}
	
	private static void displayMainMenu()
	{
		System.out.println("\nMain Menu");
		System.out.println("1. Select an available game");
		System.out.println("2. Add a new player to the wait list");
		System.out.println("3. Quit\n");
	}
	
	private static int findGame(String id)
	{
		int pos = 0;
		boolean found = false;
		while (pos < activeGames && !found)
		{
			if (games[pos].getId().equals(id))
				found = true;
			else
				++pos;
		}
		return found ? pos : -1;
	}
	
	private static Roulette100A selectGame()
	{
		Roulette100A game = null;
		Scanner scan = new Scanner(System.in);
		boolean valid = false;
		do
		{
			printGames();
			System.out.print("Select a game --> ");
			int pos = findGame(scan.nextLine());
			if (pos != -1)
			{
				game = games[pos];
				valid = true;
			}
			else
				System.out.println("Unavailable game. Please try again.\n");
		} while(!valid);
		return game;
	}
	
	private static void displayGameMenu()
	{
		System.out.println("\nGame Menu");
		System.out.println("1. Add a player to the game");
		System.out.println("2. Remove a player from the game");
		System.out.println("3. Play one round");
		System.out.println("4. Return to the main menu\n");
	}
	
	private static int selectGameOption(Roulette100A game)
	{
		int option = 0;
		Scanner scan = new Scanner(System.in);
		boolean valid = false;
		do
		{
			displayGameMenu();
			System.out.print("Option --> ");
			try
			{
				option = Integer.parseInt(scan.nextLine());
				switch(option)
				{
				case 1:
					game.addPlayer();
					valid = true;
					break;
				case 2:
					game.removePlayer();
					valid = true;
					break;
				case 3:
					game.playRound();
					valid = true;
					break;
				case 4:
					valid = true;
					break;
				default:
					break;
				}
			} catch(Exception exception) {}
			if (!valid)
				System.out.println("Invalid option. Please try again.");
		} while(!valid);
		return option;
	}
	
	private static void launchGame()
	{
		Roulette100A game = selectGame();
		int option = 0;
		do
		{
			option = selectGameOption(game);
		} while(option != 4);
	}
	
	private static void addPlayer()
	{
		Scanner scan = new Scanner(System.in);
		System.out.print("Enter name: ");
		String name = scan.nextLine();
		double money = 0;
		boolean valid = false;
		do
		{
			System.out.print("Enter money: ");
			try
			{
				money = Double.parseDouble(scan.nextLine());
				if (money >= 0)
					valid = true;
			} catch(Exception exception) {}
			if (!valid)
				System.out.println("Invalid amount. Please try again.\n");
		} while(!valid);
		valid = false;
		do
		{
			System.out.println("\nPlayer Types");
			System.out.println("1. Regular");
			System.out.println("2. VIP");
			System.out.println("3. Super VIP\n");
			System.out.print("Select a player type --> ");
			try
			{
				int playerType = Integer.parseInt(scan.nextLine());
				switch(playerType)
				{
				case 1:
					waitlist.add(new Player(name, money));
					valid = true;
					break;
				case 2:
					System.out.print("Enter id: ");
					String idVIP = scan.nextLine();
					waitlist.add(new VIP(name, money, idVIP));
					valid = true;
					break;
				case 3:
					System.out.print("Enter id: ");
					String idSuperVIP = scan.nextLine();
					double credit = 0;
					do
					{
						System.out.print("Enter credit: ");
						try
						{
							credit = Double.parseDouble(scan.nextLine());
							if (credit >= 0)
								valid = true;
						} catch(Exception exception) {}
						if (!valid)
							System.out.println("Invalid amount. " +
									"Please try again.\n");
					} while(!valid);
					waitlist.add(new SuperVIP(name, money, idSuperVIP,
							credit));
					break;
				default:
					break;
				}
			} catch(Exception exception) {}
			if (!valid)
				System.out.println("Invalid option. Please try again.");
		} while(!valid);
	}
	
	private static int selectMainOption()
	{
		int option = 0;
		Scanner scan = new Scanner(System.in);
		boolean valid = false;
		do
		{
			displayMainMenu();
			System.out.print("Option --> ");
			try
			{
				option = Integer.parseInt(scan.nextLine());
				switch(option)
				{
				case 1:
					launchGame();
					valid = true;
					break;
				case 2:
					addPlayer();
					valid = true;
					break;
				case 3:
					valid = true;
					break;
				default:
					break;
				}
			} catch(Exception exception) {}
			if (!valid)
				System.out.println("Invalid option. Please try again.");
		} while(!valid);
		return option;
	}
	
	// WIP
	private static void launch()
	{
		System.out.println("Authors: Rian Lopez, Tran Nguyen, Dean Martin Solideo \n");
		Roulette100A.welcomeMessage();
		try
		{
			if (initializeGames())
			{
				System.out.println("All games are ready.");
				printGames();
				initializeWaitlist();
				int option = 0;
				do
				{
					option = selectMainOption();
				} while(option != 3);
				if (!waitlist.isEmpty())
				{
					System.out.println("Remaining players on waitlist: ");
					Iterator<Player> it = waitlist.iterator();
					System.out.print(it.next().getName());
					while (it.hasNext())
						System.out.print(", " + it.next().getName());
					System.out.println("Removing players from existing " +
							"games ...");
					System.out.println("Generating report ...\n");
					PrintWriter outFile = new PrintWriter(reportFile);
					for (int i = 0; i < activeGames; ++i)
					{
						games[i].printReport();
					}
					System.out.println();
					System.out.println("Closing all games.");
					
					
					
				}
			}
			else
				System.out.println("Unable to initialize games.");
		} catch(FileNotFoundException exception) {
			System.out.println("Unable to open data files.");
		}
	}
}