package com.customdiscordpresence;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class CustomDiscordPresencePluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(CustomDiscordPresencePlugin.class);
		RuneLite.main(args);
	}
}