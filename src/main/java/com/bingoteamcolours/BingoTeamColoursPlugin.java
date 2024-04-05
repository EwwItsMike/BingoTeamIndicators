package com.bingoteamcolours;

import com.google.inject.Provides;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.Text;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashMap;

@Slf4j
@PluginDescriptor(
        name = "Bingo Team Colours"
)
public class BingoTeamColoursPlugin extends Plugin {
    @Inject
    private Client client;

    @Inject
    private BingoTeamColoursConfig config;

    @Inject
    private ClientToolbar clientToolbar;

    @Inject
    private ClientThread clientThread;

    //Sidepanel
    private BingoTeamColoursPanel panel;
    private NavigationButton navButton;

    private final HashMap<ChatIcons, Integer> iconIds = new HashMap<>();
    private final HashMap<ChatIcons, String> iconImgs = new HashMap<>();
    private final HashMap<String, ChatIcons> nameColourCombo = new HashMap<>();

    @Provides
    BingoTeamColoursConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(BingoTeamColoursConfig.class);
    }

    private BufferedImage getIcon() {
        return ImageUtil.loadImageResource(BingoTeamColoursPlugin.class, "/trophy.png");
    }

    @Override
    protected void startUp() throws Exception {
        panel = new BingoTeamColoursPanel(this);

        navButton = NavigationButton.builder()
                .tooltip("Bingo Team Colours")
                .priority(10)
                .icon(getIcon())
                .panel(panel)
                .build();

        clientToolbar.addNavigation(navButton);

        clientThread.invoke(() -> {
            if (client.getModIcons() == null) return;
            loadIcons();
        });

        linkNamesToColours();
    }

    @Override
    protected void shutDown() throws Exception {
        clientToolbar.removeNavigation(navButton);
    }

    @Subscribe
    public void onChatMessage(ChatMessage event) {
        if (event.getType() != ChatMessageType.CLAN_CHAT && event.getType() != ChatMessageType.CLAN_GUEST_CHAT) {
            return;
        }
        if (nameColourCombo.containsKey(event.getName().toLowerCase())) {
            rebuildChat(event.getName());
        }
    }


    private void loadIcons() {
        final IndexedSprite[] modIcons = client.getModIcons();

        if (modIcons == null) return;

        iconIds.put(ChatIcons.RED, modIcons.length);
        iconIds.put(ChatIcons.CYAN, modIcons.length + 1);
        iconIds.put(ChatIcons.BLACK, modIcons.length + 2);
        iconIds.put(ChatIcons.BLUE, modIcons.length + 3);
        iconIds.put(ChatIcons.DARK_GREEN, modIcons.length + 4);
        iconIds.put(ChatIcons.LIGHT_GREEN, modIcons.length + 5);
        iconIds.put(ChatIcons.ORANGE, modIcons.length + 6);
        iconIds.put(ChatIcons.PINK, modIcons.length + 7);
        iconIds.put(ChatIcons.PURPLE, modIcons.length + 8);
        iconIds.put(ChatIcons.WHITE, modIcons.length + 9);
        iconIds.put(ChatIcons.YELLOW, modIcons.length + 10);

        final IndexedSprite[] newModIcons = Arrays.copyOf(modIcons, modIcons.length + iconIds.keySet().size());

        int index = 0;
        for (ChatIcons icon : iconIds.keySet()) {
            BufferedImage img = ImageUtil.loadImageResource(getClass(), icon.getIconPath());
            IndexedSprite sprite = ImageUtil.getImageIndexedSprite(img, client);
            newModIcons[modIcons.length + index] = sprite;

            iconImgs.put(icon, "<img=" + iconIds.get(icon) + ">");

            index++;
        }

        client.setModIcons(newModIcons);
    }

    private void rebuildChat(String rsn) {
        boolean needsRefresh = false;
        IterableHashTable<MessageNode> msgs = client.getMessages();

        for (MessageNode msg : msgs) {
            String cleanRsn = Text.standardize(Text.removeTags(msg.getName()));
            String rsnFromEvent = Text.standardize(rsn);
            ChatMessageType msgType = msg.getType();

            if (cleanRsn.equals(rsnFromEvent) && (msgType == ChatMessageType.CLAN_CHAT || msgType == ChatMessageType.CLAN_GUEST_CHAT)) {
                msg.setName(iconImgs.get(nameColourCombo.get(rsn)) + cleanRsn);
                needsRefresh = true;
            }
        }
        if (needsRefresh) {
            client.refreshChat();
        }
    }

    public void linkNamesToColours() {
        PersistantVariablesHandler handler = panel.getDataHandler();

        nameColourCombo.clear();

        for (int i = 0; i < handler.amountOfTeams; i++) {
            if (i >= handler.getNames().size() || i >= handler.getIcons().size()) return;

            String[] names = handler.getNames().get(i).split(",");
            ChatIcons icon = handler.getIcons().get(i);

            if (icon == ChatIcons.SELECT_A_COLOUR) continue;

            for (String s : names) {
                nameColourCombo.put(s.toLowerCase(), icon);
            }
        }
    }
}
