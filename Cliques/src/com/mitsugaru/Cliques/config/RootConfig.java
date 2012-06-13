package com.mitsugaru.Cliques.config;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.configuration.ConfigurationSection;

import com.mitsugaru.Cliques.Cliques;

public class RootConfig
{
	private Cliques plugin;
	public String host, port, database, user, password;
	public static String tablePrefix;
	public boolean useMySQL, importSQL, cliquePublicOnCreate, cliquePVPGuard, cliqueNewsOnEnter;
	public static boolean debugDatabase, debugAPI;

	public RootConfig(Cliques plugin)
	{
		this.plugin = plugin;
		final ConfigurationSection config = plugin.getConfig();
		// LinkedHashmap of defaults
		final Map<String, Object> defaults = new LinkedHashMap<String, Object>();
		// defaults
		defaults.put("clique.publicOnCreate", false);
		defaults.put("clique.pvpGuardOnCreate", true);
		defaults.put("clique.newsOnEnter", true);
		defaults.put("mysql.use", false);
		defaults.put("mysql.host", "localhost");
		defaults.put("mysql.port", 3306);
		defaults.put("mysql.database", "minecraft");
		defaults.put("mysql.user", "username");
		defaults.put("mysql.password", "pass");
		defaults.put("mysql.tablePrefix", "clique_");
		defaults.put("mysql.import", false);
		defaults.put("debug.api", false);
		defaults.put("debug.database", false);
		defaults.put("version", plugin.getDescription().getVersion());
		// Insert defaults into config file if they're not present
		for (final Entry<String, Object> e : defaults.entrySet())
		{
			if (!config.contains(e.getKey()))
			{
				config.set(e.getKey(), e.getValue());
			}
		}
		// Save config
		plugin.saveConfig();
		/**
		 * Database info
		 */
		useMySQL = config.getBoolean("mysql.use", false);
		host = config.getString("mysql.host", "localhost");
		port = config.getString("mysql.port", "3306");
		database = config.getString("mysql.database", "minecraft");
		user = config.getString("mysql.user", "user");
		password = config.getString("mysql.password", "password");
		tablePrefix = config.getString("mysql.prefix", "ks_");
		importSQL = config.getBoolean("mysql.import", false);
		loadSettings();
	}
	
	public void set(String path, Object o)
	{
		final ConfigurationSection config = plugin.getConfig();
		config.set(path, o);
		plugin.saveConfig();
	}
	
	private void loadSettings()
	{
		final ConfigurationSection config = plugin.getConfig();
		/**
		 * Clique
		 */
		cliquePublicOnCreate = config.getBoolean("clique.publicOnCreate", false);
		cliquePVPGuard = config.getBoolean("clique.pvpGuardOnCreate", true);
		cliqueNewsOnEnter = config.getBoolean("clique.newsOnEnter", true);
		/**
		 * Debug
		 */
		debugDatabase = config.getBoolean("debug.database", false);
		debugAPI = config.getBoolean("debug.api", false);
	}
	
	public void reloadConfig()
	{
		plugin.reloadConfig();
		loadSettings();
	}
}
