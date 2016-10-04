package com.teamtreehouse.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Class that handles user input for things like menus.
 */
public class Prompter {
    private BufferedReader reader;

    public Prompter() {
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public int drawMenu(String title, String[] choices) {
		printTitle(title);
        int i = 0;
        for (String choice : choices) {
			System.out.printf("%d. %s%n",++i,choice);
        }
		System.out.print("Your choice? ");
        int number = 0;
		while (number <= 0 || number > choices.length) {
			number = getInt();
			if (number <= 0 || number > choices.length)
				System.out.print("Invalid option. Try again: ");
		}
		return number - 1;
    }

	public int drawMenu(String title, List<String> choices) {
		String[] choicesArray = choices.toArray(new String[choices.size()]);
		return drawMenu(title, choicesArray);
	}

	private int getInt() {
		String line = getLine();
		// check if it is a valid number
		int number = 0;
		try {
			number = Integer.parseInt(line);
		} catch (NumberFormatException nfe) {
			System.out.printf("%s is not a valid number",line);
			nfe.printStackTrace();
		}
		return number;
	}

	private String getLine() {
		// get input from user
		String choice = "";
		try {
			choice = reader.readLine();
		} catch(IOException ioe) {
			System.out.println("Error getting input");
			ioe.printStackTrace();
		}
		return choice;
	}

	public String getLine(String message) {
		System.out.printf("%s: ",message);
		return getLine();
	}

    public void printTitle(String title) {
		System.out.printf("%n%s%n",title);
	    for (int i = 0; i < title.length(); i++)
	    	System.out.print("=");
	    System.out.println();
    }
}
