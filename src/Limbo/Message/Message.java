package Limbo.Message;

import Limbo.Main;

public enum Message {
	MOD_MSG(Main.getIntance().getMessageConfig().getConfig().getString("message.mod_msg")),
	FARM_MSG(Main.getIntance().getMessageConfig().getConfig().getString("message.farm_msg")),
	RELOAD_MSG(Main.getIntance().getMessageConfig().getConfig().getString("message.reload_msg")),
	HASNT_PERM(Main.getIntance().getMessageConfig().getConfig().getString("message.hasnt_perm")),
	KICK_MSG(Main.getIntance().getMessageConfig().getConfig().getString("message.kick_msg"));
	
	public final String label;
	private Message(String label) {
		this.label = label;
	}
}
