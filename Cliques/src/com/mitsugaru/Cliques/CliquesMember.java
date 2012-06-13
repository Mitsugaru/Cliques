package com.mitsugaru.Cliques;

import java.util.HashSet;
import java.util.Set;

import com.mitsugaru.Cliques.exception.CliqueIDNotFoundException;

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
		if (cliqueListen.contains(clique))
		{
			return true;
		}
		return false;
	}

	public boolean isMemberOfClique(String clique)
	{
		// Check if player is member of given clique
		boolean member = false;
		if (activeClique.equals(clique))
		{
			return true;
		}
		else if (cliqueListen.contains(clique))
		{
			return true;
		}
		try
		{
			final int cid = CliquesAPI.getCliqueID(clique);
			final String cliques = CliquesAPI.getCliquesOfPlayer(playerName);
			for (String c : cliques.split(","))
			{
				if (c.equals("" + cid))
				{
					member = true;
					break;
				}
			}
		}
		catch (CliqueIDNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return member;
	}
}
