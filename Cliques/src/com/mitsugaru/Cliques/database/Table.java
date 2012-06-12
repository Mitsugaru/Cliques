package com.mitsugaru.Cliques.database;

import com.mitsugaru.Cliques.config.RootConfig;

public enum Table
{
	PLAYERS(RootConfig.tablePrefix + "players"), CLIQUES(RootConfig.tablePrefix
			+ "clique"), NEWS(RootConfig.tablePrefix + "news");

	private String name;

	private Table(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}
}
