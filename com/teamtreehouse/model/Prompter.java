package com.teamtreehouse.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by crater on 04/10/16.
 */
public class Prompter {
    private BufferedReader reader;

    public Prompter() {
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public int drawMenu(String title, String[] choices) {
		System.out.println(title);
		drawTitleBar(title);
        int i = 0;
        for (String choice : choices) {
			System.out.printf("%d. %s%n",++i,choice);
        }
        System.out.print("Your choice? ");
	    String choice = "";
	    try {
			choice = reader.readLine();
	    } catch(IOException ioe) {
			System.out.println("Error getting input");
			ioe.printStackTrace();
	    }
	    int number = 0;
	    try {
		    number = Integer.parseInt(choice);
	    } catch (NumberFormatException nfe) {
		    System.out.printf("%s is not a valid number",choice);
			nfe.printStackTrace();
	    }
        return number - 1;
    }

    private void drawTitleBar(String title) {
	    for (int i = 0; i < title.length(); i++)
	    	System.out.print("=");
	    System.out.println();
    }
}
