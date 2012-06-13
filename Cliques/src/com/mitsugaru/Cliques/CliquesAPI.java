package com.mitsugaru.Cliques;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mitsugaru.Cliques.config.RootConfig;
import com.mitsugaru.Cliques.database.DatabaseHandler;
import com.mitsugaru.Cliques.database.Field;
import com.mitsugaru.Cliques.database.Table;
import com.mitsugaru.Cliques.database.SQLibrary.Database.Query;
import com.mitsugaru.Cliques.exception.CliqueIDNotFoundException;
import com.mitsugaru.Cliques.exception.PlayerIDNotFoundException;

public class CliquesAPI
{
	private static Cliques plugin;
	private static DatabaseHandler database;

	public static void init(Cliques cliques)
	{
		plugin = cliques;
		database = plugin.getDatabaseHandler();
	}

	/**
	 * Creates a new clique with the creator as the first member. Checks that
	 * the clique does not already exist.
	 * 
	 * @param Clique
	 *            name
	 * @param Creator
	 *            of new clique
	 * @return true if successful, else false
	 */
	public static boolean createClique(String clique, String creator)
	{
		boolean created = false;
		// Check if it already exists
		if (cliqueExists(clique))
		{
			return created;
		}
		try
		{
			// Create clique
			final PreparedStatement statement = database.prepare("INSERT INTO "
					+ Table.CLIQUES.getName() + " ("
					+ Field.CLIQUE_NAME.getColumnName() + ","
					+ Field.CLIQUE_MEMBERS.getColumnName() + ","
					+ Field.CLIQUE_INVITE.getColumnName() + ","
					+ Field.CLIQUE_PVP.getColumnName() + ","
					+ Field.CLIQUES_BROADCAST.getColumnName()
					+ ") VALUES(?,?,?,?,?);");
			statement.setString(1, clique);
			statement.setString(2, "" + getPlayerID(creator));
			statement.setInt(3,
					booleanToInt(!plugin.getRootConfig().cliquePublicOnCreate));
			statement.setInt(4,
					booleanToInt(plugin.getRootConfig().cliquePVPGuard));
			statement.setInt(5,
					booleanToInt(plugin.getRootConfig().cliqueNewsOnEnter));
			// Execute statement
			statement.execute();
			statement.close();
			// Add player to newly made clique
			final int cliqueID = getCliqueID(clique);
			final int playerID = getPlayerID(creator);
			String playerCliques = getCliquesOfPlayer(creator);
			if (playerCliques.equals(""))
			{
				playerCliques = "" + cliqueID;
			}
			else
			{
				playerCliques += "," + cliqueID;
			}
			final PreparedStatement update = database.prepare("UPDATE "
					+ Table.PLAYERS.getName() + " SET "
					+ Field.PLAYER_CLIQUES.getColumnName() + "=? WHERE "
					+ Field.PLAYER_ID.getColumnName() + "=" + playerID + ";");
			update.setString(1, playerCliques);
			update.execute();
			update.close();
			created = true;
		}
		catch (SQLException sql)
		{
			if (RootConfig.debugDatabase)
			{
				plugin.getLogger().warning(
						"SQL Exception on createClique(" + clique + ","
								+ creator + ")");
				sql.printStackTrace();
			}
		}
		catch (CliqueIDNotFoundException cid)
		{
			if (RootConfig.debugDatabase)
			{
				plugin.getLogger().warning(
						"CliqueID not found on createClique(" + clique + ","
								+ creator + ")");
				cid.printStackTrace();
			}
		}
		catch (PlayerIDNotFoundException pid)
		{
			if (RootConfig.debugDatabase)
			{
				plugin.getLogger().warning(
						"PlayerID not found on createClique(" + clique + ","
								+ creator + ")");
				pid.printStackTrace();
			}
		}
		return created;
	}

	/**
	 * Check if the clique name given already exists. Case insensitive.
	 * 
	 * @param Name
	 *            to search.
	 * @return true if it exists in database already. Else, false.
	 */
	public static boolean cliqueExists(String clique)
	{
		boolean has = false;
		if (clique == null)
		{
			return has;
		}
		try
		{
			// Create query
			final PreparedStatement statement = database.prepare("SELECT "
					+ Field.CLIQUE_NAME.getColumnName() + " FROM "
					+ Table.CLIQUES.getName() + " WHERE ucase("
					+ Field.CLIQUE_NAME.getColumnName() + ")=?;");
			statement.setString(1, clique.toUpperCase());
			// Get result set
			final ResultSet rs = statement.getResultSet();
			if (rs.next())
			{
				// We have entries
				has = true;
			}
			// Close database objects
			rs.close();
			statement.close();
		}
		catch (SQLException sql)
		{
			if (RootConfig.debugDatabase)
			{
				plugin.getLogger().warning(
						"SQL Exception on cliqueExists(" + clique + ")");
				sql.printStackTrace();
			}
		}
		return has;
	}

	public static boolean cliqueIsPublic(String clique)
	{
		boolean invite = !plugin.getRootConfig().cliquePublicOnCreate;
		// Grab id
		try
		{
			final int id = getCliqueID(clique);
			final Query query = database.select("SELECT "
					+ Field.CLIQUE_INVITE.getColumnName() + " FROM "
					+ Table.CLIQUES.getName() + " WHERE "
					+ Field.CLIQUE_ID.getColumnName() + "=" + id + ";");
			if (query.getResult().next())
			{
				// We have entries
				invite = intToBoolean(query.getResult().getInt(
						Field.CLIQUE_INVITE.getColumnName()));
			}
			query.closeQuery();
		}
		catch (SQLException sql)
		{
			if (RootConfig.debugDatabase)
			{
				plugin.getLogger().warning(
						"SQL Exception on cliqueIsPublic(" + clique + ")");
				sql.printStackTrace();
			}
		}
		catch (CliqueIDNotFoundException cid)
		{
			if (RootConfig.debugDatabase)
			{
				plugin.getLogger().warning(
						"CliqueID not found on cliqueIsPublic(" + clique + ")");
				cid.printStackTrace();
			}
		}
		return invite;
	}

	public static boolean cliqueBroadcastOnEnter(String clique)
	{
		boolean broadcast = !plugin.getRootConfig().cliqueNewsOnEnter;
		// Grab id
		try
		{
			final int id = getCliqueID(clique);
			final Query query = database.select("SELECT "
					+ Field.CLIQUES_BROADCAST.getColumnName() + " FROM "
					+ Table.CLIQUES.getName() + " WHERE "
					+ Field.CLIQUE_ID.getColumnName() + "=" + id + ";");
			if (query.getResult().next())
			{
				// We have entries
				broadcast = intToBoolean(query.getResult().getInt(
						Field.CLIQUES_BROADCAST.getColumnName()));
			}
			query.closeQuery();
		}
		catch (SQLException sql)
		{
			if (RootConfig.debugDatabase)
			{
				plugin.getLogger().warning(
						"SQL Exception on cliqueBroadcastOnEnter(" + clique
								+ ")");
				sql.printStackTrace();
			}
		}
		catch (CliqueIDNotFoundException cid)
		{
			if (RootConfig.debugDatabase)
			{
				plugin.getLogger().warning(
						"CliqueID not found on cliqueBroadcastOnEnter("
								+ clique + ")");
				cid.printStackTrace();
			}
		}
		return broadcast;
	}

	public static int getCliqueID(String clique)
			throws CliqueIDNotFoundException
	{
		int id = -1;
		if (clique == null)
		{
			throw new CliqueIDNotFoundException(clique);
		}
		try
		{
			// Create query
			final PreparedStatement statement = database
					.prepare("SELECT * FROM " + Table.CLIQUES.getName()
							+ " WHERE ucase("
							+ Field.CLIQUE_NAME.getColumnName() + ")=?;");
			statement.setString(1, clique.toUpperCase());
			// Get result set
			final ResultSet rs = statement.getResultSet();
			if (rs.next())
			{
				// We have entries
				do
				{
					// Compare
					if (clique.equalsIgnoreCase(rs.getString(Field.CLIQUE_NAME
							.getColumnName())))
					{
						id = rs.getInt(Field.CLIQUE_ID.getColumnName());
					}
				} while (rs.next());
			}
			// Close database objects
			rs.close();
			statement.close();
		}
		catch (SQLException sql)
		{
			if (RootConfig.debugDatabase)
			{
				plugin.getLogger().warning(
						"SQL Exception on getCliqueID(" + clique + ")");
				sql.printStackTrace();
			}
		}
		if (id == -1)
		{
			throw new CliqueIDNotFoundException(clique);
		}
		return id;
	}

	public static int getPlayerID(String name) throws PlayerIDNotFoundException
	{
		int id = -1;
		if (name == null)
		{
			throw new PlayerIDNotFoundException(name);
		}
		try
		{
			// Create query
			final PreparedStatement statement = database
					.prepare("SELECT * FROM " + Table.PLAYERS.getName()
							+ " WHERE ucase("
							+ Field.PLAYERNAME.getColumnName() + ")=?;");
			statement.setString(1, name.toUpperCase());
			// Get result set
			final ResultSet rs = statement.getResultSet();
			if (rs.next())
			{
				// We have entries
				do
				{
					// Compare
					if (name.equalsIgnoreCase(rs.getString(Field.PLAYERNAME
							.getColumnName())))
					{
						id = rs.getInt(Field.PLAYER_ID.getColumnName());
					}
				} while (rs.next());
			}
			// Close database objects
			rs.close();
			statement.close();
		}
		catch (SQLException sql)
		{
			if (RootConfig.debugDatabase)
			{
				plugin.getLogger().warning(
						"SQL Exception on getPlayerID(" + name + ")");
				sql.printStackTrace();
			}
		}
		if (id == -1)
		{
			throw new PlayerIDNotFoundException(name);
		}
		return id;
	}

	public static String getCliquesOfPlayer(String name)
	{
		String playerCliques = "";
		try
		{
			final int playerID = getPlayerID(name);
			final Query query = database.select("SELECT "
					+ Field.PLAYER_CLIQUES.getColumnName() + " FROM "
					+ Table.PLAYERS.getName() + " WHERE "
					+ Field.PLAYER_ID.getColumnName() + "=" + playerID + ";");
			if (query.getResult().next())
			{
				playerCliques = query.getResult().getString(
						Field.PLAYER_CLIQUES.getColumnName());
				if (query.getResult().wasNull())
				{
					playerCliques = "";
				}
			}
			query.closeQuery();
		}
		catch (SQLException sql)
		{
			if (RootConfig.debugDatabase)
			{
				plugin.getLogger().warning(
						"SQL Exception on getPlayerID(" + name + ")");
				sql.printStackTrace();
			}
		}
		catch (PlayerIDNotFoundException pid)
		{
			if (RootConfig.debugDatabase)
			{
				plugin.getLogger().warning(
						"PlayerID not found on getCliquesOfPlayer(" + name
								+ ")");
				pid.printStackTrace();
			}
		}
		return playerCliques;
	}

	public static String getActiveCliqueForPlayer(String name)
	{
		String active = "";
		try
		{
			final int playerID = getPlayerID(name);
			final Query query = database.select("SELECT "
					+ Field.PLAYER_ACTIVE.getColumnName() + " FROM "
					+ Table.PLAYERS.getName() + " WHERE "
					+ Field.PLAYER_ID.getColumnName() + "=" + playerID + ";");
			if (query.getResult().next())
			{
				active = query.getResult().getString(
						Field.PLAYER_ACTIVE.getColumnName());
				if (query.getResult().wasNull())
				{
					active = "";
				}
			}
			query.closeQuery();
		}
		catch (SQLException sql)
		{
			if (RootConfig.debugDatabase)
			{
				plugin.getLogger().warning(
						"SQL Exception on getPlayerID(" + name + ")");
				sql.printStackTrace();
			}
		}
		catch (PlayerIDNotFoundException pid)
		{
			if (RootConfig.debugDatabase)
			{
				plugin.getLogger().warning(
						"PlayerID not found on getCliquesOfPlayer(" + name
								+ ")");
				pid.printStackTrace();
			}
		}
		return active;
	}

	/**
	 * Convert boolean to integer
	 * 
	 * @param Boolean
	 *            to convert
	 * @return 1 if true, 0 if false;
	 */
	private static int booleanToInt(boolean b)
	{
		if (b)
		{
			return 1;
		}
		return 0;
	}

	/**
	 * Convert integer to boolean
	 * 
	 * @param Integer
	 *            to convert
	 * @return False if 0, else true
	 */
	private static boolean intToBoolean(int i)
	{
		if (i != 0)
		{
			return true;
		}
		return false;
	}
}
