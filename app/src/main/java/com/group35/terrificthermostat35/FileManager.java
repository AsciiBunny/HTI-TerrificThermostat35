package com.group35.terrificthermostat35;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.projectapi.thermometerapi.WeekProgram;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by AsciiBunny on 23-6-2017.
 */

public class FileManager {

    private TerrificApplication app;

    private Map<String, WeekProgram> weekPrograms;

    public FileManager(TerrificApplication app) {
        this.app = app;
        initialize();
    }

    public void initialize() {
        weekPrograms = new HashMap<>();
        File directory = app.getFilesDir();
        File[] files = directory.listFiles();
        if (files != null) {
            Log.d("Files", "Size: " + files.length);
            for (int i = 0; i < files.length; i++) {
                Log.d("Files", "FileName:" + files[i].getName());
                WeekProgram wp = loadWeekProgram(files[i]);
                if (wp != null) {
                    weekPrograms.put(wp.getName(), wp);
                }
            }
        }
    }

    public String[] getAllWeekProgramNames() {
        return weekPrograms.keySet().toArray(new String[weekPrograms.keySet().size()]);
    }

    public void save(WeekProgram wp) {
        if (weekPrograms == null) {
            initialize();
        }
        if (weekPrograms != null) {
            weekPrograms.put(wp.getName(), wp);
            if (wp != null) {
                saveWeekProgram(wp);
            }
        }
    }

    public WeekProgram get(String name) {
        if (weekPrograms == null) {
            initialize();
        }
        if (weekPrograms != null) {
            WeekProgram wp = weekPrograms.get(name);
            return wp;
        }
        return null;
    }

    public void delete(String name) {
        if (weekPrograms == null) {
            initialize();
        }
        if (weekPrograms != null) {
            weekPrograms.remove(name);
            File wp = new File(app.getFilesDir(), getFileName(name));
            deleteWeekProgram(wp);
        }
    }

    private void saveWeekProgram(WeekProgram weekProgram) {
        try {
            FileOutputStream outputStream = app.openFileOutput(getFileName(weekProgram.name), Context.MODE_PRIVATE);
            ObjectOutputStream objectStream = new ObjectOutputStream(outputStream);
            objectStream.writeObject(weekProgram);
            objectStream.flush();
            objectStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private WeekProgram loadWeekProgram(File file) {
        try {
            ObjectInputStream objectStream = new ObjectInputStream(new FileInputStream(file));
            WeekProgram wp = (WeekProgram) objectStream.readObject();
            return wp;
        } catch (FileNotFoundException e) {

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean deleteWeekProgram(File file) {
        return file.delete();
    }

    private String getFileName(String name) {
        return name + ".wprg";
    }
}
