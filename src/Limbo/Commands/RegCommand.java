package Limbo.Commands;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;

import Limbo.Main;
import Limbo.Message.Message;

public class RegCommand implements CommandExecutor{
	final Main main;
	
	public RegCommand() {
		main = Main.getIntance();
		main.getCommand("afreload").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] arg) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(!p.hasPermission("af.admin")){
				Main.send(p, Message.HASNT_PERM);
				return false;
			}
		}
		main.reload();
		Main.send(sender, Message.RELOAD_MSG);
		return false;
	}

}
