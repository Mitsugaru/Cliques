package com.mitsugaru.Cliques.exception;

public class CliqueIDNotFoundException extends Exception
{
	private String cliqueName;
	/**
	 * Auto-generated serial version UID
	 */
	private static final long serialVersionUID = -267070363982290362L;

	public CliqueIDNotFoundException(String name)
	{
		super("Failed to retrieve CliqueID for '" + name + "'");
		this.cliqueName = name;
	}

	public String getCliqueName()
	{
		return cliqueName;
	}
}
