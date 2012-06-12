package com.mitsugaru.Cliques.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import com.mitsugaru.Cliques.Cliques;

@SuppressWarnings("unused")
public class CliqueChatListener implements Listener
{
	private Cliques plugin;

	public CliqueChatListener(Cliques plugin)
	{
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerChat(PlayerChatEvent event)
	{
		if (event.isCancelled())
		{
			return;
		}
		else if(event.getPlayer() == null)
		{
			return;
		}
		final Player player = event.getPlayer();
		//TODO handle player if they're in clique
	}
}
