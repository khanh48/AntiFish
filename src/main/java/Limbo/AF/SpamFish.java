package Limbo.AF;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.event.player.PlayerJoinEvent;

import Limbo.Main;
import Limbo.Message.Message;

public class SpamFish implements Listener{
	static final long sec = 1000;
	static HashMap<Player, ArrayList<Long>> spam;
	Main main;
	public SpamFish() {
		main = Main.getIntance();
		spam = new HashMap<>();
	}
	
	@EventHandler
	public void onjoin(PlayerJoinEvent e) {
		spam.put(e.getPlayer(), new ArrayList<>());
	}
	
	@EventHandler
	public void onSpam(PlayerFishEvent e) {
		Player p = e.getPlayer();
		if(!main.getConfig().getBoolean("anti-spam-fish") || p.hasPermission("af.bypass")) return;
		if(e.getState().equals(State.FISHING)) {
			spam.get(p).add(System.currentTimeMillis());
		}
		if(spam.get(p).size() >= main.getConfig().getInt("spam-check")) {
			long total = 0;
			int count = 0;
			
			for(int i = spam.get(p).size() - 2; i >= 0; i--) {
				total += Math.abs(spam.get(p).get(i) - spam.get(p).get(i + 1));
				count++;
			}
			if((total/count) < (sec * main.getConfig().getDouble("time-spam"))) {
				p.kickPlayer(Main.format(Message.KICK_MSG));
			}
			spam.get(p).clear();
		}
	}
}
