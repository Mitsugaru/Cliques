package com.mitsugaru.Cliques;

import java.util.HashMap;
import java.util.Map;

public class CliquesManager
{
	private static Map<String, CliquesMember> members = new HashMap<String, CliquesMember>();

	public static boolean containsMember(String name)
	{
		return members.containsKey(name);
	}
	
	public static void removeMember(String name)
	{
		members.remove(name);
	}
	
	public static void addMember(String name)
	{
		members.put(name, new CliquesMember(name));
	}
	
	public static String getMemberActiveClique(String name)
	{
		String active = "";
		if(members.containsKey(name))
		{
			active = members.get(name).getActiveClique();
			if(active == null)
			{
				active = "";
			}
		}
		return active;
	}

}
