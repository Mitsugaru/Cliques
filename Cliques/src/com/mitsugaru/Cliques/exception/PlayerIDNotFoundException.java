package com.mitsugaru.Cliques.exception;

public class PlayerIDNotFoundException extends Exception
{
	private String playerName;
	/**
	 * Auto-generated serial version UID
	 */
	private static final long serialVersionUID = 5412173881544765834L;

	public PlayerIDNotFoundException(String name)
	{
		super("Failed to retrieve PlayerID for '" + name + "'");
		this.playerName = name;
	}
	
	public String getPlayerName()
	{
		return playerName;
	}
}
