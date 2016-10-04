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
		String[] menuList = {"Create new team","Add player to team","Remove player from team","Quit"};
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
				default:
					System.out.println("Thanks for using " + APP_NAME + "!");
					choice = -1;
			}
		} while (choice != -1);
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
