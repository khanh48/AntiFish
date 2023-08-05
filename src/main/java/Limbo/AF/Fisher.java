package Limbo.AF;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public class Fisher {
	public int fishNum;
	public Player player;
	public ArrayList<Long> fishingTimes;
	public Fisher(Player player) {
		this.player = player;
		fishNum = 0;
		fishingTimes = new ArrayList<>();
	}
}
