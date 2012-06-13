package com.mitsugaru.Cliques;

import org.bukkit.plugin.java.JavaPlugin;

import com.mitsugaru.Cliques.api.CliquesAPI;
import com.mitsugaru.Cliques.api.CliquesManager;
import com.mitsugaru.Cliques.commands.Commander;
import com.mitsugaru.Cliques.config.RootConfig;
import com.mitsugaru.Cliques.database.DatabaseHandler;
import com.mitsugaru.Cliques.listeners.CliquePlayerListener;

public class Cliques extends JavaPlugin
{
	public static final String TAG = "[Cliques]";
	private RootConfig config;
	private DatabaseHandler database;

	@Override
	public void onDisable()
	{
		// Disconnect from sql database
		if (database.checkConnection())
		{
			// Close connection
			database.close();
		}
	}

	@Override
	public void onEnable()
	{
		// Grab RootConfig
		config = new RootConfig(this);
		// Grab database
		database = new DatabaseHandler(this, config);
		// TODO check update
		// Setup API
		CliquesAPI.init(this);
		CliquesManager.init();
		// Grab command
		getCommand("clique").setExecutor(new Commander(this));
		// Add listeners
		getServer().getPluginManager().registerEvents(
				new CliquePlayerListener(), this);
	}

	public RootConfig getRootConfig()
	{
		return config;
	}

	public DatabaseHandler getDatabaseHandler()
	{
		return database;
	}
}
