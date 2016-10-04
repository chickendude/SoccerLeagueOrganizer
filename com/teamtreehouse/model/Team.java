package com.teamtreehouse.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by chickendude on 04/10/16.
 */
public class Team {
	private String mName;
	private String mCoach;
	private Set<Player> mPlayers;

	public Team(String name, String coach) {
		mName = name;
		mCoach = coach;
		mPlayers = new HashSet<>();
	}

	@Override
	public String toString() {
		return mName;
	}

	public void addPlayer(Player player) {
		mPlayers.add(player);
	}

	public void removePlayer(Player player) {
		mPlayers.remove(player);
	}

	public boolean hasPlayer(Player player) {
		return mPlayers.contains(player);
	}

	public String getName() {
		return mName;
	}

	public String getCoach() {
		return mCoach;
	}

	public Set<Player> getPlayers() {
		return mPlayers;
	}
}
