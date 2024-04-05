package com.bingoteamcolours;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class BingoTeamColoursPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(BingoTeamColoursPlugin.class);
		RuneLite.main(args);
	}
}