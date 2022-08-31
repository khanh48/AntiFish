package Limbo.Events;

import org.bukkit.plugin.PluginManager;

import Limbo.Main;

public class RegEvents {
	public RegEvents() {
		
		Main m = Main.getIntance();
		PluginManager p = m.getServer().getPluginManager();
		
		p.registerEvents(m.getAnti(), m);
		p.registerEvents(m.getSpam(), m);
		p.registerEvents(new OnDisable(), m);
	}
}
