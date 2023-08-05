package Limbo.AF;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.metadata.MetadataStoreBase;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.Metadatable;

import Limbo.Main;
import Limbo.Message.Message;

public class Anti implements Listener{
	static final long sec = 1000;
	Main main;
	Location oldP, newP;
	static HashMap<UUID, Fisher> fishers = new HashMap<UUID, Fisher>();
	static int rad, fish, maxSpam;
	static double timeSpam;
	static boolean isAntiMod, isAntiFarm, isAntiSpam;
	static List<String> blockList;
	HashMap<String, Boolean> visited;
	
	int dX[] = {1, -1, 0, 0, 0, 0};
	int dY[] = {0, 0, 1, -1, 0, 0};
	int dZ[] = {0, 0, 0, 0, 1, -1};
	
	public Anti() {
		main = Main.getIntance();
		visited = new HashMap<>();
		reload();
	}


	@EventHandler
	public void onFishing(PlayerFishEvent e) {
		Player p = e.getPlayer();
		if(p.hasPermission("af.bypass")) return;
		if(fishers.get(p.getUniqueId()) == null) {
			fishers.put(p.getUniqueId(), new Fisher(p));
		}
		AntiSpam(e);
		AntiMod(e, p);
		if(!e.getState().equals(State.REEL_IN) && !e.getState().equals(State.CAUGHT_FISH)) return;
		if(!BFS(e.getHook().getLocation())) {
			e.setCancelled(true);
			Main.send(p, Message.FARM_MSG);
		}
	}
	
	public void AntiMod(PlayerFishEvent e, Player p) {
		if(!isAntiMod) return;
		UUID id = p.getUniqueId();
		if(e.getState().equals(State.CAUGHT_FISH)) {
			
			newP = e.getHook().getLocation().clone();
			
			Fisher fisher = fishers.get(id);

			if(!inside(oldP, newP, rad)) {
				fisher.fishNum = 0;
			}

			if(fisher.fishNum == 0) {
				fisher.fishNum += 1;
				oldP = newP.clone();
			}
			else if(fisher.fishNum > 0 && fisher.fishNum < fish) {
				fisher.fishNum += 1;
			}
			else {
				e.setCancelled(true);
				Main.send(p, Message.MOD_MSG, rad);
			}
		}
	}

	public void AntiSpam(PlayerFishEvent e) {
		Player p = e.getPlayer();
		Fisher fisher = fishers.get(p.getUniqueId());
		if(!isAntiSpam || p.hasPermission("af.bypass")) return;
		
		if(e.getState().equals(State.FISHING)) {
			fisher.fishingTimes.add(System.currentTimeMillis());
		}
		if(fisher.fishingTimes.size() >= maxSpam) {
			long total = 0;
			int count = 0;
			
			for(int i = 0; (i + 1) < fisher.fishingTimes.size(); i++) {
				total += Math.abs(fisher.fishingTimes.get(i) - fisher.fishingTimes.get(i + 1));
				count++;
			}
			if((total/count) < (sec * timeSpam)) {
				p.kickPlayer(Main.format(Message.KICK_MSG));
			}
			fisher.fishingTimes.clear();
		}
	}
	
	boolean BFS(Location loc) {
		if(!isAntiFarm) return true;
		if(!checkBlock(loc)) {
			return false;
		}
		Queue<Limbo.AF.Location> queue = new ArrayDeque<>();
		visited.clear();
		int x, y, z;
		queue.add(new Limbo.AF.Location(loc));
		
		while(!queue.isEmpty()) {
			Limbo.AF.Location tmp = queue.poll();
			x = tmp.getX();
			y = tmp.getY();
			z = tmp.getZ();
			
			for(int i = 0; i < 6; i++) {
				int xn = x + dX[i];
				int yn = y + dY[i];
				int zn = z + dZ[i];
				Limbo.AF.Location simpleLoc = new Limbo.AF.Location(xn, yn, zn);
				
				if(simpleLoc.distance(loc) > rad) continue;
				if(!visited.containsKey(simpleLoc.getName())) visited.put(simpleLoc.getName(), false);
				
				if(!visited.get(simpleLoc.getName())) {
					visited.put(simpleLoc.getName(), true);
					queue.add(simpleLoc);
				}
				if(!checkBlock(getLoc(loc, simpleLoc))) {
					return false;
				}
			}
		}
		return true;
	}
	
	public Location getLoc(Location o, Limbo.AF.Location loc) {
		Location location;
		location = o.clone();
		location.setX(loc.getX());
		location.setY(loc.getY());
		location.setZ(loc.getZ());
		return location;
	}
	
	public boolean checkBlock(Location loc) {
		if(blockList == null) return true;
		for (String string : blockList) {
			if(loc.getBlock().getType().getKey().getKey().contains(string.toLowerCase())){
				return false;
			}
		}
		return true;
	}
	
	public static double distance(Location loc1, Location loc2) {
		double x = loc1.getX() - loc2.getX();
		double z = loc1.getZ() - loc2.getZ();
		return Math.sqrt(x*x + z*z);
	}
	
	public boolean inside(Location loc1, Location loc2, int radius) {
		if(loc1 == null || loc2 == null) return false;
		return distance(loc1, loc2) <= radius? true : false;
	}
	
	public static void reload() {
		rad = Main.getIntance().getConfig().getInt("radius");
		fish = Main.getIntance().getConfig().getInt("fish");
		isAntiMod  = Main.getIntance().getConfig().getBoolean("anti-mod");
		isAntiFarm  = Main.getIntance().getConfig().getBoolean("anti-farm");
		timeSpam  = Main.getIntance().getConfig().getDouble("time-spam");
		isAntiSpam  = Main.getIntance().getConfig().getBoolean("anti-spam-fish");
		maxSpam = Main.getIntance().getConfig().getInt("spam-check");
		blockList = Main.getIntance().getConfig().getStringList("check-list");
	}
}
