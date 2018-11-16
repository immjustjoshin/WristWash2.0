package com.teamwishwash.wristwash;

/**
 * Created by Joshua on 10/6/2018.
 */

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


/**
 * This class handles file input/output operations, such as saving the accelerometer/gyroscope
 * data and labels, opening/closing file readers/writers, and deleting the data storage location.
 */
public class FileUtil {

    /** tag used for debugging purposes */
    private static final String TAG = FileUtil.class.getName();

    /** default name of the application's directory */
    private static final String DEFAULT_MOTION_DATA_DIRECTORY = "motion-data";

    /** CSV extenstion */
    private static final String CSV_EXTENSION = ".csv";

    /** Storage permissions */
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     * If the app does not have permission then the user will be
     * prompted to grant permissions.
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    /**
     * Returns a motion data directory where the logging takes place
     * @return File of the motion data directory
     */
    public static File getMotionDataFile(){
        File mdDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), DEFAULT_MOTION_DATA_DIRECTORY);

        if(!mdDirectory.exists()) {
            if (!mdDirectory.mkdir()) {
                Log.w(TAG, "Failed to create motion data directory! It may already exist");
            }
        }
        return mdDirectory;
    }

    /**
     * Returns a file writer for a device
     * @param filename file name (without extension!)
     * @return the file writer for the particular filename
     */
    public static BufferedWriter getFileWriter(String filename, int fileNumber) {
        File rootDir = getMotionDataFile();
        String fullFileName = filename + fileNumber + CSV_EXTENSION;
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(new File(rootDir, fullFileName)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }

    /**
     * Write the log to the specified file writer
     * @param s log to write
     * @param out file writer
     */
    public static void writeToFile(String s, final BufferedWriter out) {
        try {
            out.write(s + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Close and flush the given log writer. Flushing ensures that the data in the buffer is first save to the file
     * @param out file writer
     */
    public static void closeWriter(final BufferedWriter out) {
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes all the data from the motion-data directory
     * @return true if successfully deleted
     */
    public static boolean deleteData() {
        boolean motionDeleted = false;
        File motionFile = getMotionDataFile();

        // deletes motion data directory
        if (motionFile != null) {
            File files[] = motionFile.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (!file.delete()) {
                        Log.e(TAG, "Deleting file failed: " + file.getName());
                    }
                }
            }
            motionDeleted = motionFile.delete();
        }

        return motionDeleted;
    }

    /**
     * This method will take the accelerometer data collected
     * from the user and segment them into 6 different files.
     * These 6 different files represent each of the 6 hand washing
     * techniques. These 6 files will then be sent through preprocessing
     * through a HTTP request call. Then the 6 preprocessed files will be sent
     * to the confusion matrix through another HTTP request call that will
     * return a score for each hand washing technique.
     */
    public static ArrayList<ArrayList<ArrayList<Double>>> extractMotionData() {
        File motionDataDirectory = getMotionDataFile();
        File files[] = motionDataDirectory.listFiles();
        BufferedReader br;
        ArrayList<ArrayList<ArrayList<Double>>> accelFilesList = new ArrayList();
        try {
            String line;
            for (File accelFile : files) {
                br = new BufferedReader(new FileReader(accelFile));
                ArrayList<Double> dataLine;
                ArrayList<ArrayList<Double>> accelDataOfOneFile = new ArrayList<>();
                while ((line = br.readLine()) != null) {
                    dataLine = splitCSVLine(line);
                    accelDataOfOneFile.add(dataLine);
                }
                accelFilesList.add(accelDataOfOneFile);
                br.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accelFilesList;
    }

    /**
     * Splits a line in a csv file by the commas and
     * extracts the x, y, z values.
     * @param line csv line that is being passed in
     * @return An array that holds the x, y, z values
     */
    public static ArrayList<Double> splitCSVLine(String line) {
        ArrayList<Double> result = new ArrayList<>();
        if (line != null) {
            String[] splitData = line.split(",");
            for (int i = 1; i < splitData.length; i++) {
                if (splitData[i] != null) {
                    result.add(Double.valueOf(splitData[i]));
                }
            }
        }
        return result;
    }
}
