package com.bingoteamcolours;

import com.google.inject.Provides;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;

import java.awt.image.BufferedImage;

@Slf4j
@PluginDescriptor(
		name = "Bingo Team Colours"
)
public class BingoTeamColoursPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private BingoTeamColoursConfig config;

	@Inject
	private ClientToolbar clientToolbar;

	//Sidepanel
	private BingoTeamColoursPanel panel;
	private NavigationButton navButton;

	@Provides
	BingoTeamColoursConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(BingoTeamColoursConfig.class);
	}

	public int getAmountOfTeams(){
		return config.amountOfTeams();
	}

	private BufferedImage getIcon(){
		return ImageUtil.loadImageResource(BingoTeamColoursPlugin.class, "/trophy.png");
	}

	@Override
	protected void startUp() throws Exception {
		panel = new BingoTeamColoursPanel();


		navButton = NavigationButton.builder()
				.tooltip("Bingo Team Colours")
				.priority(10)
				.icon(getIcon())
				.panel(panel)
				.build();

		clientToolbar.addNavigation(navButton);
	}

	@Override
	protected void shutDown() throws Exception {
		clientToolbar.removeNavigation(navButton);
	}
}
