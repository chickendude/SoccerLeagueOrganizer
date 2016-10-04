import com.teamtreehouse.model.Player;
import com.teamtreehouse.model.Players;
import com.teamtreehouse.model.Prompter;
import com.teamtreehouse.model.Team;

import java.util.ArrayList;
import java.util.List;

public class LeagueManager {
	private static final String APP_NAME = "Soccer League Organizer";
	private static Prompter prompt;
	private static List<Team> teams;

	public static void main(String[] args) {
		prompt = new Prompter();
		teams = new ArrayList<>();
		Player[] players = Players.load();
		System.out.printf("There are currently %d registered players.%n", players.length);
		// Load main menu
		String[] menuList = {"Create new team","Quit"};
		int choice;
		do {
			choice = prompt.drawMenu("Welcome to " + APP_NAME + "!", menuList);
			switch(choice) {
				case 0:
					createTeam();
					break;
				default:
					System.out.println("Thanks for using " + APP_NAME + "!");
			}
		} while (choice != 1);
	}

	private static void createTeam() {
		prompt.printTitle("Create a team");
		String teamName = prompt.getLine("Team name");
		String coach = prompt.getLine("Coach");
		Team team = new Team(teamName, coach);
		teams.add(team);
	}

}
