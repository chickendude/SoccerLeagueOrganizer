package com.teamtreehouse.model;

import java.io.BufferedReader;
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
        int i = 0;
        for (String choice : choices) {
            System.out.printf("%d. %s%n",++i,choice);
        }
        return 0;
    }
}
