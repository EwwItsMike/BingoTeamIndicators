package com.bingoteamcolours;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static net.runelite.client.RuneLite.RUNELITE_DIR;

public class PersistantVariablesHandler implements Serializable {

    int amountOfTeams = 1;
    ArrayList<String> names = new ArrayList<>();

    transient File file;


    public PersistantVariablesHandler() {}

    public PersistantVariablesHandler(boolean firstSetup) {
        createDirectory();
        load();
    }

    private void createDirectory() {
        File mainFolder = new File(RUNELITE_DIR, "bingo-team-colours");
        file = new File(mainFolder, "bingo.json");

        if (!file.exists()) {
            mainFolder.mkdirs();
            try {
                file.createNewFile();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    private void save() {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(this);

            Files.write(file.toPath(), json.getBytes(StandardCharsets.UTF_8));

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void load() {
        try {
            String json = Files.readString(file.toPath());
            PersistantVariablesHandler handler = new Gson().fromJson(json, PersistantVariablesHandler.class);

            if (handler != null) {
                this.amountOfTeams = handler.getAmountOfTeams();
                this.names = handler.getNames();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public int getAmountOfTeams() {
        return amountOfTeams;
    }

    public ArrayList<String> getNames() {
        return names;
    }

    public void setAmountOfTeams(int amount) {
        this.amountOfTeams = amount;
        save();
    }

    public void setNames(ArrayList<String> names) {
        this.names = names;
        save();
    }
}
