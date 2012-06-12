package com.mitsugaru.Cliques.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Commander implements CommandExecutor
{

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args)
	{
		if (args.length == 0)
		{
			// TODO show help
			return true;
		}
		try
		{
			final ValidCommands com = ValidCommands.valueOf(args[0]
					.toLowerCase());
			return parseCommand(com, sender, args);
		}
		catch (IllegalArgumentException ia)
		{
			// TODO tell sender invalid command
		}
		return true;
	}

	private boolean parseCommand(ValidCommands com, CommandSender sender,
			String[] args)
	{
		switch (com)
		{
			case CREATE:
			{
				// TODO create clique
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
				//TODO modify self pvp toggle if pvp is disabled for the clique
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
					final ValidCommands adminCom = ValidCommands.valueOf(args[1]
							.toLowerCase());
					return parseAdminCommand(adminCom, sender, args);
				}
				catch (IllegalArgumentException ia)
				{
					// TODO tell sender invalid admin command
				}
				catch(ArrayIndexOutOfBoundsException aioob)
				{
					//TODO show admin help
				}
				break;
			}
			default:
			{
				// TODO show help
				return false;
			}
		}
		return true;
	}

	private boolean parseAdminCommand(ValidCommands com, CommandSender sender,
			String[] args)
	{
		switch(com)
		{
			case RELOAD:
			{
				break;
			}
			default:
			{
				// TODO tell player invalid command
			}
		}
		return true;
	}
}
