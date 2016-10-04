import com.teamtreehouse.model.Player;
import com.teamtreehouse.model.Players;
import com.teamtreehouse.model.Prompter;

public class LeagueManager {

	public static void main(String[] args) {
		Prompter prompt = new Prompter();
		Player[] players = Players.load();
		System.out.printf("There are currently %d registered players.%n", players.length);
		// Your code here!
		String[] menuList = {"Create new team","Quit"};
		prompt.drawMenu("Welcome to Soccer League Organizer!",menuList);
	}

}
