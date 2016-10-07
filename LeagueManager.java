import com.sun.org.apache.bcel.internal.classfile.SourceFile;
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
		// load players and keep track of "free agents"
		players = Players.load();
		freeAgents = new TreeSet<>(Arrays.asList(players));
		System.out.printf("There are currently %d registered players.%n", players.length);
		// Load main menu
		// TODO:chickendude add missing players to waiting list
		// TODO:chickendude automatically build fair teams
		// TODO:chickendude remove players from teams and fill in with waiting list players (FIFO)
		String[] menuList = {"Create new team",	// 0
				"Add player to team",			// 1
				"Remove player from team",		// 2
				"View team roster",				// 3
				"League Balance Report",		// 4
				"Height Chart",					// 5
				"Build teams automatically",	// 6
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
				case 6:
					buildTeams();
					break;
				default:
					System.out.println("Thanks for using " + APP_NAME + "!");
					choice = -1;
			}
		} while (choice != -1);
	}

	private static void buildTeams() {
		if (teams.size()>0) {
			System.out.printf("Building %d teams, please wait...%n", teams.size());

			List<Player> playerList = new ArrayList<>(Arrays.asList(players));

			playerList.sort((p1, p2) ->
					(p1.getHeightInInches() + (p1.isPreviousExperience() ? 100 : 0)) -
					(p2.getHeightInInches() + (p2.isPreviousExperience() ? 100 : 0)));

			// We'll loop through each player adding "least valuable" player rotating through the teams like this:
			// 0-max then max-0
			int i = 0;
			int next = 1;	// this gets added to i, either +1 or -1
			for (Player player : playerList) {
				System.out.printf("%s :%n  Experience = %s : Height = %d%n",
						player.getFirstName() + " " + player.getLastName(),
						player.isPreviousExperience(),
						player.getHeightInInches());
				System.out.printf("...added to team: %s%n",teams.get(i).getName());
				teams.get(i).addPlayer(player);
				i += next;
				if (i == teams.size()-1)
					next--;
				else if (i == 0)
					next++;
			}

			prompt.pause();
		} else {
			System.out.println("Please create at least one team, first.");
			prompt.pause();
		}
	}

	private static void viewHeightChart() {
		Team team = getTeam();
		Map<String,List<Player>> heightMap = new TreeMap<>();
		String[] sizes = {"35-40","41-46","47-50"};
		int[] sizeCount = {0,0,0};
		for (Player player : team.getPlayers()) {
			// convert the height into an index for the string
			int height = player.getHeightInInches();
			int index = 0;
			if (height > 40)
				index++;
			if (height > 46)
				index++;
			List<Player> players = heightMap.get(sizes[index]);
			if (players == null) {
				players = new ArrayList<>();
				heightMap.put(sizes[index],players);
			}
			sizeCount[index]++;
			players.add(player);
		}

		for (Map.Entry<String,List<Player>> entry : heightMap.entrySet()) {
			int i = Arrays.asList(sizes).indexOf(entry.getKey());
			System.out.printf("%s: %d%n", entry.getKey(), sizeCount[i]);
			for (Player player : entry.getValue()) {
				System.out.printf("   -%s: %d%n",player, player.getHeightInInches());
			}
		}
		prompt.pause();
	}

	private static void viewLeagueBalanceReport() {
		prompt.printTitle("League Balance Report");
		Collections.sort(teams);
		for (Team team : teams) {
			Set<Player> playerSet = team.getPlayers();
			int total = playerSet.size();
			int experienced = 0;
			int inexperienced = 0;
			int height = 0;
			int[] heightCount = {0,0,0};
			for (Player player : playerSet) {
				int playerHeight = player.getHeightInInches();
				int index = 0;
				if (playerHeight > 40)
					index++;
				if (playerHeight > 46)
					index++;
				heightCount[index]++;
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
							"    Experienced ratio: %.2f%n" +
							"    Average height: %.2f%n" +
							"    35-40\": %d%n" +
							"    41-46\": %d%n" +
							"    47-50\": %d%n",
					team.getName(),
					total,
					experienced,
					inexperienced,
					ratio,
					averageHeight,
					heightCount[0],
					heightCount[1],
					heightCount[2]);
		}
		prompt.pause();
	}

	private static void viewTeamRoster() {
		Team team = getTeam();
		System.out.printf("%nTeam roster for team '%s':%n",team);
		for (Player player : team.getPlayers()) {
			System.out.println(player);
		}
		prompt.pause();
	}

	private static Team getTeam() {
		List<String> teamList = new ArrayList<>();
		Collections.sort(teams);

		for (Team team : teams) {
			teamList.add(team.getName());
		}
		int choice = prompt.drawMenu("View Team Roster", teamList);
		// display the roster
		return teams.get(choice);
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
			// get a sorted set of players
			List<Player> playerList = new ArrayList<>(new TreeSet<Player>(Arrays.asList(players)));
			// players with a team will be added here
			List<String> playerNameList = new ArrayList<>();
			List<Player> playersOnTeamList = new ArrayList<>();
			// sort through players and extract those already on a team
			for (Player player : playerList) {
				String teamName = "none";
				for (Team team : teams) {
					if (team.hasPlayer(player)) {
						teamName = team.getName();
						playersOnTeamList.add(player);
						playerNameList.add(String.format("%s, %s - Team: %s (Height: %s in, Prev. Exp: %s)",
								player.getLastName(),
								player.getFirstName(),
								teamName,
								player.getHeightInInches(),
								player.isPreviousExperience()));
					}
				}
			}
			if (playerNameList.size() > 0) {
				int choice = prompt.drawMenu("Remove player from team", playerNameList);
				Player player = playersOnTeamList.get(choice);
				// find the player amongst the teams and remove them/readd them to the free agents
				for (Team team : teams) {
					if (team.hasPlayer(player)) {
						team.removePlayer(player);
						freeAgents.add(player);
						System.out.printf("'%s' removed from team '%s'", player, team.getName());
					}
				}
			} else {
				System.out.println("No players have been assigned teams yet.");
				prompt.pause();
			}
		} else {
			System.out.println("No teams. Please create some teams first.");
			prompt.pause();
		}
	}

	private static void createTeam() {
		if (teams.size() < players.length) {
			prompt.printTitle("Create a team");
			String teamName = prompt.getLine("Team name");
			String coach = prompt.getLine("Coach");
			Team team = new Team(teamName, coach);
			teams.add(team);
		} else {
			System.out.println("Sorry, you can't create any more teams.");
		}
	}

}
