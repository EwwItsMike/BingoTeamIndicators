package com.bingoteamcolours;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.FlatTextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;

@Slf4j
public class BingoTeamColoursPanel extends PluginPanel {

    private final ArrayList<JPanel> teamFields = new ArrayList<>();
    private final ArrayList<String> teamNames = new ArrayList<>();
    private final ArrayList<ChatIcons> selectedIcon = new ArrayList<>();

    private FlatTextField amountOfTeams;

    private final PersistantVariablesHandler dataHandler;
    private final BingoTeamColoursPlugin plugin;


    public BingoTeamColoursPanel(BingoTeamColoursPlugin plugin) {
        dataHandler = new PersistantVariablesHandler(true);
        this.plugin = plugin;


        createTitle();
        createTeamPanels();
    }

    public PersistantVariablesHandler getDataHandler(){
        return dataHandler;
    }

    private void createTitle() {
        JPanel titlePanel = new JPanel();
        titlePanel.setBorder(new EmptyBorder(10, 10, 20, 10));

        JLabel title = new JLabel();
        title.setText("Bingo Team Colours");
        title.setForeground(Color.WHITE);
        titlePanel.add(title);

        add(titlePanel);

        JPanel teamAmount = new JPanel();
        JLabel amountLabel = new JLabel();
        amountLabel.setText("Amount of teams");
        amountLabel.setForeground(Color.WHITE);
        teamAmount.add(amountLabel);

        amountOfTeams = new FlatTextField();
        if (dataHandler.getAmountOfTeams() > 0) {
            amountOfTeams.setText(String.valueOf(dataHandler.getAmountOfTeams()));
        } else {
            amountOfTeams.setText("1");
        }
        amountOfTeams.setBackground(ColorScheme.DARKER_GRAY_COLOR);

        teamAmount.add(amountOfTeams);
        amountOfTeams.getDocument().addDocumentListener(new DocumentListener() {

            final Runnable refresh = () -> refreshPanel();

            @Override
            public void insertUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(refresh);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(refresh);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(refresh);
            }
        });

        add(teamAmount);
    }

    public void createTeamPanels() {
        for (int i = 0; i < getAmountOfTeams(); i++) {
            addTeamField(i);
        }
    }

    private int getAmountOfTeams() {
        int n = 1;
        try {
            n = Integer.parseInt(amountOfTeams.getText());
            if (n < 1) {
                n = 1;
                amountOfTeams.setText("1");
            }
        } catch (NumberFormatException e) {
            amountOfTeams.setText("1");
        }
        return n;
    }

    public void refreshPanel() {
        int amount = getAmountOfTeams();

        //If the number got lowered, remove the latest ones in the list
        if (amount < teamFields.size()) {
            ArrayList<JPanel> toRemove = new ArrayList<>();
            for (int i = teamFields.size(); i > amount; i--) {
                toRemove.add(teamFields.get(i - 1));
            }
            for (int i = 0; i < toRemove.size(); i++) {
                remove(toRemove.get(i));
                teamFields.remove(toRemove.get(i));
            }
        }
        //If the number got raised, add new panels without removing the previous
        else if (amount > teamFields.size()) {
            for (int i = teamFields.size(); i < amount; i++) {
                addTeamField(i);
            }
        }

        dataHandler.setAmountOfTeams(getAmountOfTeams());
        revalidate();
    }

    private void addTeamField(int index) {
        JPanel teamPanel = new JPanel();
        teamPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel teamLabel = new JLabel();
        teamLabel.setText("Team " + (index + 1));
        teamLabel.setToolTipText("Separate names by commas");
        teamLabel.setForeground(Color.WHITE);
        teamPanel.add(teamLabel, BorderLayout.NORTH);

        JComboBox<ChatIcons> dropdown = makeNewDropdown(ChatIcons.values(), index);

        if (dataHandler.getIcons().size() > index){
            dropdown.setSelectedIndex(getIconColourIndex(dataHandler.getIcons().get(index)));
        }

        teamPanel.add(dropdown);

        JTextArea names = new JTextArea();
        names.setPreferredSize(new Dimension(PluginPanel.PANEL_WIDTH - 20, 100));
        names.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        names.setLineWrap(true);
        names.getDocument().addDocumentListener(new DocumentListener() {

            final Runnable refresh = new Runnable() {
                @Override
                public void run() {
                    if (teamNames.size() > index)
                        teamNames.set(index, names.getText());
                    else
                        teamNames.add(names.getText());
                    dataHandler.setNames(teamNames);
                    plugin.linkNamesToColours();
                }
            };

            @Override
            public void insertUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(refresh);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(refresh);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(refresh);
            }
        });

        if (dataHandler.getNames().size() > index) {
            String n = dataHandler.getNames().get(index);
            names.setText(n);
        }
        teamPanel.add(names);

        teamFields.add(teamPanel);
        teamPanel.setPreferredSize(new Dimension(PANEL_WIDTH, 150));
        add(teamPanel);
    }

    private JComboBox<ChatIcons> makeNewDropdown(ChatIcons[] values, int index){
        JComboBox<ChatIcons> dropdown = new JComboBox<>(values);
        dropdown.setFocusable(false);
        dropdown.setForeground(Color.WHITE);
        dropdown.setRenderer(new DropdownRenderer());
        dropdown.addItemListener( e -> {
            if (e.getStateChange() == ItemEvent.SELECTED){
                ChatIcons source = (ChatIcons) e.getItem();

                if (selectedIcon.size() > index){
                    selectedIcon.set(index, source);
                } else {
                    selectedIcon.add(source);
                }

                dataHandler.setIcon(source, index);
            }
        });
        return dropdown;
    }

    private int getIconColourIndex(ChatIcons icon) {
        switch (icon){
            case RED: return 1;
            case BLACK: return 2;
            case BLUE: return 3;
            case CYAN: return 4;
            case DARK_GREEN: return 5;
            case LIGHT_GREEN: return 6;
            case ORANGE: return 7;
            case PINK: return 8;
            case PURPLE: return 9;
            case WHITE: return 10;
            case YELLOW: return 11;

            default: return 0;
        }
    }
}
