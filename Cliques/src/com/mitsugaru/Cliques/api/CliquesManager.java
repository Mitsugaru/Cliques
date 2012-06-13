package com.mitsugaru.Cliques.api;

import java.util.HashMap;
import java.util.Map;

public class CliquesManager
{
	private static Map<String, CliquesMember> members;

	public static void init()
	{
		members = new HashMap<String, CliquesMember>();
	}
	
	/**
	 * Check if member is known.
	 * 
	 * @param Member
	 *            name
	 * @return True if member is known, else false.
	 */
	public static boolean containsMember(String name)
	{
		return members.containsKey(name);
	}

	/**
	 * Removes a member
	 * 
	 * @param Name
	 *            of member
	 */
	public static void removeMember(String name)
	{
		members.remove(name);
	}

	/**
	 * Adds a member
	 * 
	 * @param Name
	 *            of member
	 */
	public static void addMember(String name)
	{
		members.put(name, new CliquesMember(name));
	}

	/**
	 * Grabs the member's active clique, if they have one. If they don't, then
	 * it defaults to an empty string.
	 * 
	 * @param Member
	 *            name.
	 * @return Name of active clique. Empty string if they do not have one.
	 */
	public static String getMemberActiveClique(String name)
	{
		String active = "";
		if (members.containsKey(name))
		{
			active = members.get(name).getActiveClique();
			if (active == null)
			{
				active = "";
			}
		}
		return active;
	}

	/**
	 * Sets a member's active clique to given clique name
	 * 
	 * @param Name
	 *            of member
	 * @param Clique
	 *            name
	 */
	public static void setMemberActiveClique(String name, String clique)
	{
		// Add member if missing
		if (!members.containsKey(name))
		{
			addMember(name);
		}
		// set clique
		members.get(name).setActiveClique(clique);
	}

}
