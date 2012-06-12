package com.mitsugaru.Cliques.database;

import com.mitsugaru.Cliques.Cliques;
import com.mitsugaru.Cliques.config.RootConfig;
import com.mitsugaru.Cliques.database.SQLibrary.MySQL;
import com.mitsugaru.Cliques.database.SQLibrary.SQLite;

public class DatabaseHandler
{
	private Cliques plugin;
	private RootConfig config;
	private MySQL mysql;
	private SQLite sqlite;
	private boolean useMySQL;

	public DatabaseHandler(Cliques plugin, RootConfig config)
	{
		this.plugin = plugin;
		this.config = config;
		useMySQL = config.useMySQL;
		checkTables();
		if (config.importSQL)
		{
			if (useMySQL)
			{
				importSQL();
			}
			config.set("mysql.import", false);
		}
	}

	private void checkTables()
	{
		if (useMySQL)
		{
			mysql = new MySQL(plugin.getLogger(), Cliques.TAG, config.host,
					config.port, config.database, config.user, config.password);
			// Check if player table exists
			if (!mysql.checkTable(Table.PLAYERS.getName()))
			{
				plugin.getLogger().info("Created players table");
				mysql.createTable("CREATE TABLE "
						+ Table.PLAYERS.getName()
						+ " (id INT UNSIGNED NOT NULL AUTO_INCREMENT, playername varchar(32) NOT NULL, cliques TEXT, PRIMARY KEY(id), UNIQUE(playername));");
			}
			if (!mysql.checkTable(Table.CLIQUES.getName()))
			{
				plugin.getLogger().info("Created cliques table");
				mysql.createTable("CREATE TABLE "
						+ Table.CLIQUES.getName()
						+ " (id INT UNSIGNED NOT NULL AUTO_INCREMENT, name TEXT NOT NULL, members TEXT NOT NULL, chatprefix TEXT, invite INT NOT NULL, pvp INT NOT NULL, PRIMARY KEY(id), UNQIUE(name));");
			}
			if (!mysql.checkTable(Table.NEWS.getName()))
			{
				plugin.getLogger().info("Created news table");
				mysql.createTable("CREATE TABLE "
						+ Table.NEWS.getName()
						+ " (id INT UNSIGNED NOT NULL AUTO_INCREMENT, cliqueID INT NOT NULL, author TEXT NOT NULL, message TEXT NOT NULL, PRIMARY KEY(id));");
			}
		}
		else
		{
			sqlite = new SQLite(plugin.getLogger(), Cliques.TAG, "cliques",
					plugin.getDataFolder().getAbsolutePath());
			if (!sqlite.checkTable(Table.PLAYERS.getName()))
			{
				plugin.getLogger().info("Created players table");
				sqlite.createTable("CREATE TABLE "
						+ Table.PLAYERS.getName()
						+ " (id INTEGER PRIMARY KEY, playername varchar(32) NOT NULL, cliques TEXT, UNIQUE(playername));");
			}
			if (!sqlite.checkTable(Table.CLIQUES.getName()))
			{
				plugin.getLogger().info("Created cliques table");
				sqlite.createTable("CREATE TABLE "
						+ Table.CLIQUES.getName()
						+ " (id INTEGER PRIMARY KEY, name TEXT NOT NULL, members TEXT NOT NULL, chatprefix TEXT, invite INTEGER NOT NULL, pvp INTEGER NOT NULL, UNQIUE(name));");
			}
			if (!sqlite.checkTable(Table.NEWS.getName()))
			{
				plugin.getLogger().info("Created news table");
				sqlite.createTable("CREATE TABLE "
						+ Table.NEWS.getName()
						+ " (id INTEGER PRIMARY KEY, cliqueID INTEGER NOT NULL, author TEXT NOT NULL, message TEXT NOT NULL);");
			}
		}
	}

	private void importSQL()
	{
		// TODO import
	}
}
