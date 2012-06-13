package com.mitsugaru.Cliques.api;

import java.util.HashSet;
import java.util.Set;

public class CliquesMember
{
	private String playerName = "", activeClique = "";
	private Set<String> cliqueListen = new HashSet<String>();

	public CliquesMember(String name)
	{
		this.playerName = name;
		this.activeClique = CliquesAPI.getActiveCliqueForPlayer(name);
	}

	public String getActiveClique()
	{
		return activeClique;
	}

	public void setActiveClique(String clique)
	{
		this.activeClique = clique;
	}

	public boolean listeningOnClique(String clique)
	{
		if (activeClique.equalsIgnoreCase(clique)
				|| cliqueListen.contains(clique))
		{
			return true;
		}
		return false;
	}

	public boolean listenToClique(String clique)
	{
		boolean added = false;
		if (activeClique.equalsIgnoreCase(clique)
				|| cliqueListen.contains(clique))
		{
			// They are already listening
			return true;
		}
		if (isMemberOfClique(clique))
		{
			cliqueListen.add(clique);
		}
		return added;
	}

	public boolean isMemberOfClique(String clique)
	{
		// Check if player is member of given clique
		if (activeClique.equals(clique))
		{
			return true;
		}
		else if (cliqueListen.contains(clique))
		{
			return true;
		}
		return CliquesAPI.playerIsMemberOfClique(playerName, clique);
	}
}
