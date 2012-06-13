package com.mitsugaru.Cliques.commands;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.ChatPaginator;

import com.mitsugaru.Cliques.Cliques;
import com.mitsugaru.Cliques.api.CliquesAPI;
import com.mitsugaru.Cliques.api.CliquesManager;
import com.mitsugaru.Cliques.permissions.PermissionHandler;
import com.mitsugaru.Cliques.permissions.PermissionNode;

public class Commander implements CommandExecutor
{
	private Cliques plugin;
	private final static String BAR = "======================";

	public Commander(Cliques cliques)
	{
		this.plugin = cliques;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args)
	{
		if (args.length == 0)
		{
			// show player's information
			if (sender instanceof Player)
			{
				showPlayerInfo(sender);
			}
			else
			{
				showHelp(sender);
			}
			return true;
		}
		try
		{
			final ValidCommands com = ValidCommands.valueOf(args[0]
					.toUpperCase());
			return parseCommand(com, sender, args);
		}
		catch (IllegalArgumentException ia)
		{
			if (args[0].equals("?"))
			{
				showHelp(sender);
				return true;
			}
			// TODO tell sender invalid command
		}
		catch (ArrayIndexOutOfBoundsException aioob)
		{
			// Ignore?
		}
		return true;
	}

	private boolean parseCommand(ValidCommands com, CommandSender sender,
			String[] args)
	{
		switch (com)
		{
			case HELP:
			{
				// show help
				showHelp(sender);
				break;
			}
			case CREATE:
			{
				// TODO create clique
				createClique(sender, args);
				break;
			}
			case JOIN:
			{
				// TODO attempt to join public clique
				break;
			}
			case INVITE:
			{
				// TODO invite player to join clique
				// TODO use conversation prompt
				break;
			}
			case LEAVE:
			{
				// TODO leave clique
				break;
			}
			case POST:
			{
				// TODO post message to clique board
				break;
			}
			case WALL:
			{
				// TODO show / page clique board wall
				break;
			}
			case PVP:
			{
				// TODO modify self pvp toggle if pvp is disabled for the clique
				break;
			}
			case EDIT:
			{
				// TODO parse edit command
				break;
			}
			case ADMIN:
			{
				// TODO parse admin command
				try
				{
					final ValidCommands adminCom = ValidCommands
							.valueOf(args[1].toUpperCase());
					return parseAdminCommand(adminCom, sender, args);
				}
				catch (IllegalArgumentException ia)
				{
					if (args[1].equals("?"))
					{
						// TODO show admin help
						return true;
					}
					// TODO tell sender invalid admin command
				}
				catch (ArrayIndexOutOfBoundsException aioob)
				{
					// TODO show admin help
				}
				break;
			}
			case VER:
			{
				// Rollover to the version section
			}
			case VERSION:
			{
				showVersion(sender);
				break;
			}
			default:
			{
				// TODO bad command
				return false;
			}
		}
		return true;
	}

	private void createClique(CommandSender sender, String[] args)
	{
		if (args.length < 2)
		{
			sender.sendMessage(ChatColor.RED + Cliques.TAG
					+ " Missing name of clique");
			return;
		}
		if (!(sender instanceof Player))
		{
			sender.sendMessage(ChatColor.YELLOW + Cliques.TAG
					+ " Cannot create cliques as console");
			return;
		}
		if (!PermissionHandler.has(sender, PermissionNode.CLIQUE_CREATE))
		{
			sender.sendMessage(ChatColor.RED + Cliques.TAG
					+ " Lack permission: "
					+ PermissionNode.CLIQUE_CREATE.getNode());
			return;
		}
		final String clique = args[1];
		if (CliquesAPI.cliqueExists(clique))
		{
			sender.sendMessage(ChatColor.YELLOW + Cliques.TAG + " Clique '"
					+ ChatColor.AQUA + clique + ChatColor.YELLOW
					+ "' already exists");
			return;
		}
		if (CliquesAPI.createClique(clique, sender.getName()))
		{
			sender.sendMessage(ChatColor.GREEN + Cliques.TAG + " Clique '"
					+ ChatColor.AQUA + clique + ChatColor.GREEN + "' created");
			// TODO set player's current clique to new clique
		}
		else
		{
			sender.sendMessage(ChatColor.RED + Cliques.TAG + " Clique '"
					+ ChatColor.AQUA + clique + ChatColor.RED
					+ "' failed to be created");
		}
	}

	private boolean parseAdminCommand(ValidCommands com, CommandSender sender,
			String[] args)
	{
		switch (com)
		{
			case RELOAD:
			{
				if (PermissionHandler.has(sender, PermissionNode.ADMIN_RELOAD))
				{
					plugin.getRootConfig().reloadConfig();
					sender.sendMessage(ChatColor.GREEN + Cliques.TAG
							+ " Config reloaded");
				}
				else
				{
					sender.sendMessage(ChatColor.RED + Cliques.TAG
							+ " Lack permssion: "
							+ PermissionNode.ADMIN_RELOAD.getNode());
				}
				break;
			}
			case DELETE:
			{
				// TODO delete clique
				break;
			}
			case KICK:
			{
				// TODO force remove specified player from clique
				break;
			}
			case HELP:
			{
				// TODO show admin help
				break;
			}
			default:
			{
				// TODO tell player invalid command
			}
		}
		return true;
	}

	private void showVersion(CommandSender sender)
	{
		sender.sendMessage(ChatColor.BLUE + BAR + "=====");
		sender.sendMessage(ChatColor.GREEN + Cliques.TAG + " v"
				+ plugin.getDescription().getVersion());
		sender.sendMessage(ChatColor.GREEN + "Coded by Mitsugaru");
		sender.sendMessage(ChatColor.BLUE + "===========" + ChatColor.GRAY
				+ "Config" + ChatColor.BLUE + "===========");
		sender.sendMessage(ChatColor.GRAY + "Clique invite only on create: "
				+ !plugin.getRootConfig().cliquePublicOnCreate);
		sender.sendMessage(ChatColor.GRAY + "Clique pvp disabled on create: "
				+ plugin.getRootConfig().cliquePVPGuard);
	}

	private void showHelp(CommandSender sender)
	{
		sender.sendMessage(ChatColor.WHITE + "=====" + ChatColor.GREEN
				+ "Cliques" + ChatColor.WHITE + "=====");
		// TODO show help based on permissions
		sender.sendMessage(ChatColor.AQUA + "/clique help" + ChatColor.GOLD
				+ " : Show help menu");
	}

	private void showPlayerInfo(CommandSender sender)
	{
		showPlayerInfo(sender, sender.getName());
	}

	private void showPlayerInfo(CommandSender sender, String target)
	{
		sender.sendMessage(ChatColor.WHITE + "=====" + ChatColor.AQUA
				+ "Cliques" + ChatColor.WHITE + "=====");
		sender.sendMessage(ChatColor.GOLD + "Active: " + ChatColor.WHITE
				+ CliquesManager.getMemberActiveClique(target));
		final Set<String> cliqueSet = CliquesAPI.getCliquesOfPlayer(target);
		StringBuilder sb = new StringBuilder();
		sb.append(ChatColor.GOLD + "Member: " + ChatColor.WHITE);
		for (String clique : cliqueSet)
		{
			sb.append(clique + " ");
		}
		for (String cliqueLine : ChatPaginator.wordWrap(sb.toString(),
				ChatPaginator.AVERAGE_CHAT_PAGE_WIDTH))
		{
			sender.sendMessage(cliqueLine);
		}
	}
}
