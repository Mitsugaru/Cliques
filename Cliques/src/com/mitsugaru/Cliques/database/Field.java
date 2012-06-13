package com.mitsugaru.Cliques.database;

public enum Field
{
	PLAYERNAME(Table.PLAYERS, "playername", Type.STRING), PLAYER_CLIQUES(
			Table.PLAYERS, "cliques", Type.STRING), PLAYER_ACTIVE(
			Table.PLAYERS, "active", Type.INTEGER), CLIQUE_NAME(Table.CLIQUES,
			"name", Type.STRING), CLIQUE_MEMBERS(Table.CLIQUES, "members",
			Type.STRING), CLIQUE_PREFIX(Table.CLIQUES, "chatprefix",
			Type.STRING), CLIQUE_INVITE(Table.CLIQUES, "invite", Type.INTEGER), CLIQUE_PVP(
			Table.CLIQUES, "pvp", Type.INTEGER), CLIQUES_BROADCAST(
			Table.CLIQUES, "broadcast", Type.INTEGER), NEWS_CLIQUE_ID(
			Table.NEWS, "cliqueID", Type.INTEGER), NEWS_AUTHOR(Table.NEWS,
			"author", Type.STRING), NEWS_MESSAGE(Table.NEWS, "message",
			Type.STRING), PLAYER_ID(Table.PLAYERS, "id", Type.INTEGER), CLIQUE_ID(
			Table.CLIQUES, "id", Type.INTEGER), NEWS_ID(Table.NEWS, "id",
			Type.INTEGER);

	private Table table;
	private Type type;
	private String field;

	private Field(Table table, String field, Type type)
	{
		this.table = table;
		this.field = field;
		this.type = type;
	}

	public Table getTable()
	{
		return table;
	}

	public String getColumnName()
	{
		return field;
	}

	public Type getType()
	{
		return type;
	}

	public enum Type
	{
		STRING, INTEGER;
	}
}
