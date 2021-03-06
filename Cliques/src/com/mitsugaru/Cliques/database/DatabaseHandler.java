package com.mitsugaru.Cliques.database;

import java.sql.PreparedStatement;

import com.mitsugaru.Cliques.Cliques;
import com.mitsugaru.Cliques.config.RootConfig;
import com.mitsugaru.Cliques.database.SQLibrary.MySQL;
import com.mitsugaru.Cliques.database.SQLibrary.SQLite;
import com.mitsugaru.Cliques.database.SQLibrary.Database.Query;

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
						+ " (id INT UNSIGNED NOT NULL AUTO_INCREMENT, playername varchar(32) NOT NULL, cliques TEXT, active INT, PRIMARY KEY(id), UNIQUE(playername));");
			}
			if (!mysql.checkTable(Table.CLIQUES.getName()))
			{
				plugin.getLogger().info("Created cliques table");
				mysql.createTable("CREATE TABLE "
						+ Table.CLIQUES.getName()
						+ " (id INT UNSIGNED NOT NULL AUTO_INCREMENT, cliquename TEXT NOT NULL, cliquemembers TEXT NOT NULL, chatprefix TEXT, invite INT NOT NULL, pvp INT NOT NULL, broadcast INT NOT NULL, PRIMARY KEY(id), UNIQUE(UNIQUE(cliquename)));");
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
						+ " (id INTEGER PRIMARY KEY, playername varchar(32) NOT NULL, cliques TEXT, active INTEGER, UNIQUE(playername));");
			}
			if (!sqlite.checkTable(Table.CLIQUES.getName()))
			{
				plugin.getLogger().info("Created cliques table");
				sqlite.createTable("CREATE TABLE "
						+ Table.CLIQUES.getName()
						+ " (id INTEGER PRIMARY KEY, cliquename TEXT NOT NULL, cliquemembers TEXT NOT NULL, chatprefix TEXT, invite INTEGER NOT NULL, pvp INTEGER NOT NULL, broadcast INTEGER NOT NULL, UNIQUE(cliquename));");
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

	public boolean checkConnection()
	{
		boolean connected = false;
		if (useMySQL)
		{
			connected = mysql.checkConnection();
		}
		else
		{
			connected = sqlite.checkConnection();
		}
		return connected;
	}

	public void close()
	{
		if (useMySQL)
		{
			mysql.close();
		}
		else
		{
			sqlite.close();
		}
	}

	public Query select(String query)
	{
		if (useMySQL)
		{
			return mysql.select(query);
		}
		else
		{
			return sqlite.select(query);
		}
	}

	public void standardQuery(String query)
	{
		if (useMySQL)
		{
			mysql.standardQuery(query);
		}
		else
		{
			sqlite.standardQuery(query);
		}
	}

	public void createTable(String query)
	{
		if (useMySQL)
		{
			mysql.createTable(query);
		}
		else
		{
			sqlite.createTable(query);
		}
	}

	public PreparedStatement prepare(String statement)
	{
		if (useMySQL)
		{
			return mysql.prepare(statement);
		}
		else
		{
			return sqlite.prepare(statement);
		}
	}
}
