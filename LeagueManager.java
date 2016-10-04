import com.teamtreehouse.model.Player;
import com.teamtreehouse.model.Players;
import com.teamtreehouse.model.Prompter;

public class LeagueManager {

	public static void main(String[] args) {
		Prompter prompt = new Prompter();
		Player[] players = Players.load();
		System.out.printf("There are currently %d registered players.%n%n", players.length);
		// Your code here!
		String[] menuList = {"Create new team","Quit"};
		int choice;
		do {
			choice = prompt.drawMenu("Welcome to Soccer League Organizer!", menuList);
		} while (choice != 1);
	}

}
