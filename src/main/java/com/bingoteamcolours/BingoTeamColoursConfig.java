package com.bingoteamcolours;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("BingoTeamColours")
public interface BingoTeamColoursConfig extends Config
{
    @ConfigItem(
            keyName = "teams",
            name = "Amount of teams",
            description = "Amount of teams in the bingo"
    )
    default int amountOfTeams() {return 1;}

}
