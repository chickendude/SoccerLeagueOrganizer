import com.teamtreehouse.model.Player;
import com.teamtreehouse.model.Players;
import com.teamtreehouse.model.Prompter;
import com.teamtreehouse.model.Team;

import java.util.*;

public class LeagueManager {
	private static final String APP_NAME = "Soccer League Organizer";
	private static Prompter prompt;
	private static List<Team> teams;
	private static Set<Player> freeAgents;
	private static Player[] players;

	public static void main(String[] args) {
		prompt = new Prompter();
		teams = new ArrayList<>();

		teams.add(new Team("Monkeys","Manfred"));
		teams.add(new Team("Bread","Manfred"));
		teams.add(new Team("Dogs","Manfred"));
		teams.add(new Team("Rockets","Manfred"));
		teams.add(new Team("Apple","Manfred"));
		teams.add(new Team("Whales","Manfred"));
		teams.add(new Team("Tigers","Manfred"));

		// load players and keep track of "free agents"
		players = Players.load();
		freeAgents = new TreeSet<>(Arrays.asList(players));
		System.out.printf("There are currently %d registered players.%n", players.length);
		// Load main menu
		String[] menuList = {"Create new team",	// 0
				"Add player to team",			// 1
				"Remove player from team",		// 2
				"View team roster",				// 3
				"League Balance Report",		// 4
				"Height Chart",					// 5
				"Quit"};
		int choice;
		do {
			choice = prompt.drawMenu("Welcome to " + APP_NAME + "!", menuList);
			switch(choice) {
				case 0:
					createTeam();
					break;
				case 1:
					addPlayerToTeam();
					break;
				case 2:
					removePlayerFromTeam();
					break;
				case 3:
					viewTeamRoster();
					break;
				case 4:
					viewLeagueBalanceReport();
					break;
				case 5:
					viewHeightChart();
					break;
				default:
					System.out.println("Thanks for using " + APP_NAME + "!");
					choice = -1;
			}
		} while (choice != -1);
	}

	private static void viewHeightChart() {
		
	}

	private static void viewLeagueBalanceReport() {
		Collections.sort(teams);
		for (Team team : teams) {
			Set<Player> playerSet = team.getPlayers();
			int total = playerSet.size();
			int experienced = 0;
			int inexperienced = 0;
			int height = 0;
			for (Player player : playerSet) {
				height += player.getHeightInInches();
				if (player.isPreviousExperience())
					experienced++;
				else
					inexperienced++;
			}
			float ratio = 0;
			if (total > 0)
				ratio = experienced / (float) total;
			float averageHeight = 0;
			if (total > 0)
				averageHeight = height / (float) total;
			System.out.printf("%s:%n" +
							"    Total players: %d%n" +
							"    Experienced players: %d%n" +
							"    Inexperienced players: %d%n" +
							"    Experienced ratio: %f%n" +
							"    Average height: %f%n",
					team.getName(),
					total,
					experienced,
					inexperienced,
					ratio,
					averageHeight);
		}
	}

	private static void viewTeamRoster() {
		List<String> teamList = new ArrayList<>();
		Collections.sort(teams);

		for (Team team : teams) {
			teamList.add(team.getName());
		}
		int choice = prompt.drawMenu("View Team Roster", teamList);
		// display the roster
		Team team = teams.get(choice);
		System.out.printf("%nTeam roster for team '%s':%n",team);
		for (Player player : team.getPlayers()) {
			System.out.println(player);
		}
		prompt.pause();
	}

	private static void addPlayerToTeam() {
		if(teams.size() > 0) {
			List<Player> playerList = new ArrayList<>(freeAgents);
			List<String> playerNameList = new ArrayList<>();
			for (Player player : playerList) {
				playerNameList.add(String.format("%s, %s (Height: %s in, Prev. Exp: %s)",
						player.getLastName(),
						player.getFirstName(),
						player.getHeightInInches(),
						player.isPreviousExperience()));
			}
			int choice = prompt.drawMenu("Add player to team", playerNameList);
			Player player = playerList.get(choice);
			System.out.println(player);


			// now select which team to add to
			Collections.sort(teams);
			List<String> teamList = new ArrayList<>();
			for (Team team : teams) {
				teamList.add(team.getName() + " - Coach " + team.getCoach());
			}
			choice = prompt.drawMenu(String.format("Add %s to which team", player.getFirstName()), teamList);
			Team team = teams.get(choice);
			team.addPlayer(player);
			freeAgents.remove(player);
			System.out.printf("'%s' successfully added to team '%s'", player, team.getName());
		} else {
			System.out.println("No teams. Please create some teams first.");
		}
	}

	private static void removePlayerFromTeam() {
		if(teams.size() > 0) {
			List<Player> playerList = new ArrayList<>(new TreeSet<Player>(Arrays.asList(players)));
			List<String> playerNameList = new ArrayList<>();
			for (Player player : playerList) {
				String teamName = "none";
				for (Team team : teams) {
					if (team.hasPlayer(player))
						teamName = team.getName();
				}
				playerNameList.add(String.format("%s, %s - Team: %s (Height: %s in, Prev. Exp: %s)",
						player.getLastName(),
						player.getFirstName(),
						teamName,
						player.getHeightInInches(),
						player.isPreviousExperience()));
			}
			int choice = prompt.drawMenu("Remove player from team", playerNameList);
			Player player = playerList.get(choice);
			// find the player amongst the teams and remove them/readd them to the free agents
			for (Team team : teams) {
				if (team.hasPlayer(player)) {
					team.removePlayer(player);
					freeAgents.add(player);
					System.out.printf("'%s' removed from team '%s'",player, team.getName());
				}
			}
		} else {
			System.out.println("No teams. Please create some teams first.");
		}
	}

	private static void createTeam() {
		prompt.printTitle("Create a team");
		String teamName = prompt.getLine("Team name");
		String coach = prompt.getLine("Coach");
		Team team = new Team(teamName, coach);
		teams.add(team);
	}

}
