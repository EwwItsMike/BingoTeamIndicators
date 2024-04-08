package com.bingoteamindicators;

import com.google.gson.Gson;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;

import static net.runelite.client.RuneLite.RUNELITE_DIR;

public class PersistentVariablesHandler implements Serializable {

    int amountOfTeams = 1;
    ArrayList<String> names = new ArrayList<>();
    transient File file;

    transient Gson gson;

    public PersistentVariablesHandler() {
    }

    public PersistentVariablesHandler(Gson gson) {
        this.gson = gson;
    }

    public void init() {
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
            String json = gson.toJson(this);

            Files.write(file.toPath(), json.getBytes(StandardCharsets.UTF_8));

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void load() {
        try {
            String json = Files.readString(file.toPath());
            PersistentVariablesHandler handler = gson.fromJson(json, PersistentVariablesHandler.class);

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
