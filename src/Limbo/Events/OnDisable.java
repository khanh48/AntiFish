package Limbo.Events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

import Limbo.Main;

public class OnDisable implements Listener{

	@EventHandler
	public void playerCmd(PlayerCommandPreprocessEvent e) {
		if(e.getMessage().equalsIgnoreCase("/stop") && e.getPlayer().hasPermission("minecraft.command.stop")) {
			e.setCancelled(true);
			Main.getIntance().saveOnDisable();
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
		}
	}
	
	@EventHandler
	public void consoleCmd(ServerCommandEvent e) {
		if(e.getCommand().equalsIgnoreCase("stop")) {
			e.setCancelled(true);
			Main.getIntance().saveOnDisable();
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
		}
	}
}
