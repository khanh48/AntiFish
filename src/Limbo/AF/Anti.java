package Limbo.AF;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.event.player.PlayerJoinEvent;

import Limbo.Main;
import Limbo.Message.Message;

public class Anti implements Listener{
	Main main;
	static HashMap<Player, Integer> fisher;
	Location oldP, newP;
	static int rad, fish;
	HashMap<String, Boolean> visited;
	
	int dX[] = {1, -1, 0, 0, 0, 0};
	int dY[] = {0, 0, 1, -1, 0, 0};
	int dZ[] = {0, 0, 0, 0, 1, -1};
	
	public Anti() {
		main = Main.getIntance();
		fisher = new HashMap<Player, Integer>();
		visited = new HashMap<>();
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		fisher.put(e.getPlayer(), 0);
	}
	
	@EventHandler
	public void onFishing(PlayerFishEvent e) {
		Player p = e.getPlayer();
		if(p.hasPermission("af.bypass")) return;
		antiMod(e, p);
		if(!e.getState().equals(State.REEL_IN) && !e.getState().equals(State.CAUGHT_FISH)) return;
		if(!BFS(e.getHook().getLocation())) {
			e.setCancelled(true);
			Main.send(p, Message.FARM_MSG);
		}
	}
	
	public void antiMod(PlayerFishEvent e, Player p) {
		if(!main.getConfig().getBoolean("anti-mod")) return;
		if(e.getState().equals(State.CAUGHT_FISH)) {
			rad = main.getConfig().getInt("radius");
			fish = main.getConfig().getInt("fish");
			
			newP = e.getHook().getLocation();
			if(!fisher.containsKey(p)) return;
			int tmp = fisher.get(p);
			if(tmp == 0) {
				fisher.put(p, tmp + 1);
				oldP = newP;
			}
			else if(tmp > 0 && tmp <= fish) {
				fisher.put(p, tmp + 1);
				if(!inside(oldP, newP, rad)) {
					fisher.put(p, 0);
				}
			}
			else {
				if(!inside(oldP, newP, rad)) {
					fisher.put(p, 0);
					return;
				}
				e.setCancelled(true);
				Main.send(p, Message.MOD_MSG, rad);
			}
		}
	}
	
	boolean BFS(Location loc) {
		if(!main.getConfig().getBoolean("anti-farm")) return true;
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
		List<String> blockList = main.getConfig().getStringList("check-list");
		if(blockList == null) return true;
		for (String string : blockList) {
			if(loc.getBlock().getType().getKey().getKey().contains(string.toLowerCase())){
				return false;
			}
		}
		return true;
	}
	
	public static Double distance(Location loc1, Location loc2) {
		double x = loc1.getX() - loc2.getX();
		double y = loc1.getY() - loc2.getY();
		double z = loc1.getZ() - loc2.getZ();
		return Math.sqrt(x*x + y*y + z*z);
	}
	
	public Boolean inside(Location loc1, Location loc2, int radius) {
		return loc1.distance(loc2) <= radius? true : false;
	}
}
