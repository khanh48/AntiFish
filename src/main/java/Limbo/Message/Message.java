package Limbo.Message;

import Limbo.Main;

public class Message {
	public static String MOD_MSG = message("mod_msg");
	public static String FARM_MSG = message("farm_msg");
	public static String RELOAD_MSG = message("reload_msg");
	public static String HASNT_PERM = message("hasnt_perm");
	public static String KICK_MSG = message("kick_msg");
	
	private static String message(String label) {
		return Main.getIntance().getMessageConfig().getConfig().getString("message." + label);
	}
	
	public static void reload() {
		MOD_MSG = message("mod_msg");
		FARM_MSG = message("farm_msg");
		RELOAD_MSG = message("reload_msg");
		HASNT_PERM = message("hasnt_perm");
		KICK_MSG = message("kick_msg");
	}
}
