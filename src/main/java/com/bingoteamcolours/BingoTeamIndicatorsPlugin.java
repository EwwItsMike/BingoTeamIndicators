package com.bingoteamcolours;

import com.google.inject.Provides;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
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
        name = "Bingo Team Indicators"
)
public class BingoTeamIndicatorsPlugin extends Plugin {
    @Inject
    private Client client;

    @Inject
    private BingoTeamIndicatorsConfig config;

    @Inject
    private ClientToolbar clientToolbar;

    @Inject
    private ClientThread clientThread;

    //Sidepanel
    private BingoTeamIndicatorsPanel panel;
    private NavigationButton navButton;

    //Icons stuff
    private final HashMap<ChatIcons, Integer> iconIds = new HashMap<>();
    private final HashMap<Integer, String> iconTags = new HashMap<>();
    private final HashMap<String, Integer> nameNumberCombo = new HashMap<>();
    private boolean hasLoaded = false;

    @Provides
    BingoTeamIndicatorsConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(BingoTeamIndicatorsConfig.class);
    }

    private BufferedImage getIcon() {
        return ImageUtil.loadImageResource(BingoTeamIndicatorsPlugin.class, "/panelIcon.png");
    }

    @Override
    protected void startUp() throws Exception {
        panel = new BingoTeamIndicatorsPanel(this);

        navButton = NavigationButton.builder()
                .tooltip("Bingo Team Indicators")
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

    @Subscribe
    public void onGameStateChanged(GameStateChanged state) {
        if (!hasLoaded && state.getGameState() == GameState.LOGGED_IN) {
            clientThread.invoke(() -> {
                if (client.getModIcons() == null) return;
                loadIcons();
                hasLoaded = true;
            });
        }
    }

    @Subscribe
    public void onChatMessage(ChatMessage event) {
        if (event.getType() != ChatMessageType.CLAN_CHAT && event.getType() != ChatMessageType.CLAN_GUEST_CHAT && event.getType() != ChatMessageType.FRIENDSCHAT) {
            return;
        }
        String name = Text.standardize(Text.removeTags(event.getName().toLowerCase()));
        if (nameNumberCombo.containsKey(name)) {
            rebuildChat(Text.removeTags(event.getName()));
        }
    }

    private void loadIcons() {
        final IndexedSprite[] modIcons = client.getModIcons();

        if (modIcons == null) return;

        int index = 0;
        for (ChatIcons icon : ChatIcons.values()) {
            iconIds.put(icon, modIcons.length + index);
            index++;
        }

        final IndexedSprite[] newModIcons = Arrays.copyOf(modIcons, modIcons.length + 15);

        for (ChatIcons icon : iconIds.keySet()) {
            BufferedImage img = ImageUtil.loadImageResource(getClass(), icon.getIconPath());
            IndexedSprite sprite = ImageUtil.getImageIndexedSprite(img, client);
            newModIcons[iconIds.get(icon)] = sprite;

            iconTags.put((iconIds.get(icon) - modIcons.length), "<img=" + iconIds.get(icon) + ">");
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

            if (cleanRsn.equals(rsnFromEvent) && (msgType == ChatMessageType.CLAN_CHAT || msgType == ChatMessageType.CLAN_GUEST_CHAT || msgType == ChatMessageType.FRIENDSCHAT)) {
                msg.setName(iconTags.get(nameNumberCombo.get(cleanRsn.toLowerCase())) + rsn);
                needsRefresh = true;
            }
        }
        if (needsRefresh) {
            client.refreshChat();
        }
    }

    public void linkNamesToIcons() {
        PersistentVariablesHandler handler = panel.getDataHandler();

        nameNumberCombo.clear();

        for (int i = 0; i < handler.getNames().size(); i++) {
            String[] names = handler.getNames().get(i).split(",");
            for (int j = 0; j < names.length; j++) {
                names[j] = names[j].strip();
                nameNumberCombo.put(names[j].toLowerCase(), i);
            }
        }
    }
}
