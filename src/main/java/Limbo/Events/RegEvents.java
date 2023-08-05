package Limbo.Events;

import org.bukkit.plugin.PluginManager;

import Limbo.Main;
import Limbo.AF.Anti;

public class RegEvents {
	public RegEvents() {
		
		Main m = Main.getIntance();
		PluginManager p = m.getServer().getPluginManager();
		
		p.registerEvents(new Anti(), m);
	}
}
