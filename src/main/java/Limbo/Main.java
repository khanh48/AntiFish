package Limbo;

import java.util.concurrent.Callable;

import Limbo.Metrics.Metrics;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import Limbo.AF.Anti;
import Limbo.Commands.RegCommand;
import Limbo.CustomConfig.Config;
import Limbo.Events.RegEvents;
import Limbo.Message.Message;
import Limbo.Updater.UpdateChecker;

public class Main extends JavaPlugin{
	static Main intance;
	RegEvents events;
	RegCommand cm;
	UpdateChecker updateChecker;
	Config message;

	@Override
	public void onEnable() {
		intance = this;
		this.saveDefaultConfig();
		this.message = new Config("message");
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
	@Override
	public void onDisable() {
		this.saveOnDisable();
		super.onDisable();
	}
	
	public static Main getIntance() {
		return intance;
	}
	
	public static void send(CommandSender to, String cnt) {
		to.sendMessage(format(cnt));
	}
	
	public static void send(CommandSender to, String msg, Object val) {
		String tmp = "";
		tmp = msg.replace("%radius%", String.valueOf(val));
		send(to, tmp);
	}
	
	public static String format(String cnt) {
		return ChatColor.translateAlternateColorCodes('&', "&l&3[AntiAutoFish]&r " + cnt);
	}
	
	public void reload() {
		reloadConfig();
		getMessageConfig().reloadConfig();
		Message.reload();
		Anti.reload();
	}
	
	public void saveOnDisable() {
		saveConfig();
		UpdateChecker.checker.cancel();
		send(getServer().getConsoleSender(), "Goodbye...");
	}
	
	public Config getMessageConfig(){
		return this.message;
	}
}
