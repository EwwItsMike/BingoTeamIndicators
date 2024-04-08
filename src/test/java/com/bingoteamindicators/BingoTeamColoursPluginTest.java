package com.bingoteamindicators;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class BingoTeamColoursPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(BingoTeamIndicatorsPlugin.class);
		RuneLite.main(args);
	}
}