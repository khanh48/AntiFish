package Limbo.Updater;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import Limbo.Main;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

import javax.net.ssl.HttpsURLConnection;

public class UpdateChecker {
	public static BukkitRunnable checker;
    private final Main plugin;
    private final int resourceId;

    public UpdateChecker(int resourceId) {
        this.plugin = Main.getIntance();
        this.resourceId = resourceId;
        this.checkUpdate();
    }

    public void getVersion(final Consumer<String> consumer) {
    	try {
    		HttpsURLConnection url = (HttpsURLConnection) new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId).openConnection();
    		url.setUseCaches(false);
    		url.setReadTimeout(5000);
    		InputStream in = url.getInputStream();
    		Scanner scanner = new Scanner(in);
    		if(scanner.hasNext()) {
    			consumer.accept(scanner.next());
    		}
    		scanner.close();
    		url.disconnect();
		} catch (IOException ex) {
			Main.send(Bukkit.getConsoleSender(), "Unable to check for updates: " + ex.getMessage());
		}
    }
    
    public void checkUpdate() {
    	checker = new BukkitRunnable() {
			@Override
			public void run() {
				getVersion(ver -> {
					if(plugin.getDescription().getVersion().equals(ver)) {
						Main.send(Bukkit.getConsoleSender(), "&aThere is not a new update available.");
					}
					else
					{
						for (Player player : plugin.getServer().getOnlinePlayers())
							if(player.hasPermission("af.admin")) {
								Main.send(player, "&6&lThere is a new update available.");
								Main.send(player, "&6Dowload it here: https://www.spigotmc.org/resources/advanced-anti-auto-fishing.104939");
							}
						Main.send(Bukkit.getConsoleSender(), "&6There is a new update available.");
						Main.send(Bukkit.getConsoleSender(), "&6Dowload it here: https://www.spigotmc.org/resources/advanced-anti-auto-fishing.104939");
					}
				});
			}
		};
		checker.runTaskTimerAsynchronously(plugin, 100, 288000);
    }
}