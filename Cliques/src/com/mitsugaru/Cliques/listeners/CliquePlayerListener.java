package com.mitsugaru.Cliques.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.mitsugaru.Cliques.CliquesManager;

public class CliquePlayerListener implements Listener
{
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		if (event.getPlayer() == null)
		{
			return;
		}
		if (CliquesManager.containsMember(event.getPlayer().getName()))
		{
			CliquesManager.removeMember(event.getPlayer().getName());
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		if (event.getPlayer() == null)
		{
			return;
		}
		CliquesManager.addMember(event.getPlayer().getName());
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerChat(PlayerChatEvent event)
	{
		if (event.getPlayer() == null)
		{
			return;
		}
		final String name = event.getPlayer().getName();
		if(!CliquesManager.containsMember(name))
		{
			return;
		}
		final String activeClique = CliquesManager.getMemberActiveClique(name);
		if(activeClique.equals(""))
		{
			return;
		}
		//TODO format chat
		//TODO send chat message to other listening members of that clique
		//TODO do a local echo of the chat as well
		
	}
}
