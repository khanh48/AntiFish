package Limbo;

import java.util.concurrent.Callable;

import Limbo.Metrics.Metrics;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import Limbo.AF.Anti;
import Limbo.AF.SpamFish;
import Limbo.Commands.RegCommand;
import Limbo.CustomConfig.Config;
import Limbo.Events.RegEvents;
import Limbo.Message.Message;
import Limbo.Updater.UpdateChecker;

public class Main extends JavaPlugin{
	static Main intance;
	Anti anti;
	SpamFish spam;
	RegEvents events;
	RegCommand cm;
	UpdateChecker updateChecker;
	Config message;

	@Override
	public void onEnable() {
		intance = this;
		this.saveDefaultConfig();
		this.message = new Config("message");
		this.anti = new Anti();
		this.spam = new SpamFish();
		this.events = new RegEvents();
		this.cm = new RegCommand();
		updateChecker = new UpdateChecker(104939);
		Metrics metrics = new Metrics(this, 16307);
		metrics.addCustomChart(new Metrics.SimplePie("anti_mod", new Callable<String>() {
			@Override
			public String call() throws Exception {
				return getConfig().getString("anti-mod");
			}
		}));
		metrics.addCustomChart(new Metrics.SimplePie("anti_farm", new Callable<String>() {
			@Override
			public String call() throws Exception {
				return getConfig().getString("anti-farm");
			}
		}));
		metrics.addCustomChart(new Metrics.SimplePie("anti_spam_fish", new Callable<String>() {
			@Override
			public String call() throws Exception {
				return getConfig().getString("anti-spam-fish");
			}
		}));
	}
	
	public static Main getIntance() {
		return intance;
	}
	
	public Anti getAnti() {
		return this.anti;
	}
	public SpamFish getSpam() {
		return this.spam;
	}
	
	public static void send(CommandSender to, String cnt) {
		to.sendMessage(format(cnt));
	}
	
	public static void send(CommandSender to, Message msg, Object val) {
		String tmp = "";
		tmp = msg.label.replace("%radius%", String.valueOf(val));
		send(to, tmp);
	}
	
	public static String format(String cnt) {
		return ChatColor.translateAlternateColorCodes('&', "&l&3[AntiAutoFish]&r " + cnt);
	}
	
	public static void send(CommandSender to, Message msg) {
		send(to, msg, "Limbo48");
	}
	
	public void reload() {
		reloadConfig();
		getMessageConfig().reloadConfig();
	}
	
	public void saveOnDisable() {
		send(getServer().getConsoleSender(), "Goodbye...");
		saveConfig();
		UpdateChecker.checker.cancel();
	}
	
	public Config getMessageConfig(){
		return this.message;
	}
}
