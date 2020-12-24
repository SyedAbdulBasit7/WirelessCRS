package com.example.wirelesscrs.Wi_Lect.tasks.operation;

import android.os.Build;
import android.os.Looper;
import androidx.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;

import com.example.wirelesscrs.Wi_Lect.entities.FMFile;
import com.example.wirelesscrs.Wi_Lect.exceptions.BlockingStuffOnMainThreadException;

import static com.example.wirelesscrs.Wi_Lect.tasks.operation.CompatOperations.copyDirectory;
import static com.example.wirelesscrs.Wi_Lect.tasks.operation.CompatOperations.copyFile;
import static com.example.wirelesscrs.Wi_Lect.tasks.operation.CompatOperations.moveDirectory;
import static com.example.wirelesscrs.Wi_Lect.tasks.operation.CompatOperations.moveFile;

/**
 * Utility class for file operations.
 *
 * @author Lukas Fülling (lukas@k40s.net)
 * @see CompatOperations
 */
class OperationUtil {

    private static final String TAG = OperationUtil.class.getCanonicalName();

    static boolean doCopy(FMFile f, File destination) throws BlockingStuffOnMainThreadException {
        Log.i(TAG, "Starting copy...");
        return doCopyNoValidation(f, destination);
    }

    static boolean doMove(FMFile f, File destination) throws BlockingStuffOnMainThreadException {
        Log.i(TAG, "Starting move...");
        return doMoveNoValidation(f, destination);
    }

    static boolean doDeleteNoValidation(FMFile f) throws BlockingStuffOnMainThreadException {
        if(Looper.myLooper() == Looper.getMainLooper()) {
            throw new BlockingStuffOnMainThreadException();
        }
        if (f.isDirectory()) {
            try {
                for (File file : f.getFile().listFiles()) {
                    doDeleteNoValidation(new FMFile(file));
                }
            } catch (NullPointerException e) {
                Log.e(TAG, "No permission for this file.", e);
            }
        }
        return f.getFile().exists() && f.getFile().delete();
    }

    private static boolean doCopyNoValidation(FMFile f, File d) throws BlockingStuffOnMainThreadException {
        if(Looper.myLooper() == Looper.getMainLooper()) {
            throw new BlockingStuffOnMainThreadException();
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Files.copy(f.getFile().toPath(), d.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } else {
                if (f.getFile().isDirectory()) {
                    copyDirectory(f.getFile(), d);
                } else {
                    copyFile(f.getFile(), d);
                }
            }
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Unable to copy file!", e);
            return false;
        }
    }

    private static boolean doMoveNoValidation(FMFile f, File d) throws BlockingStuffOnMainThreadException {
        if(Looper.myLooper() == Looper.getMainLooper()) {
            throw new BlockingStuffOnMainThreadException();
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Files.move() seems to be broken :c
                Files.copy(f.getFile().toPath(), d.toPath(), StandardCopyOption.REPLACE_EXISTING);
                if(!f.getFile().delete()) {
                    Log.w(TAG, "Unable to remove source file!");
                }
            } else {
                if (f.getFile().isDirectory()) {
                    moveDirectory(f.getFile(), d);
                } else {
                    moveFile(f.getFile(), d);
                }
            }
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Unable to move file!", e);
            return false;
        }
    }

    @NonNull
    static String getFullPathForRename(FMFile f, String newName) {
        final String[] path = {""};
        ArrayList<String> pathSplit = new ArrayList<>(Arrays.asList(f.getFile().getAbsolutePath().split(File.separator)));
        pathSplit.remove(pathSplit.size() - 1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            pathSplit.forEach(s -> path[0] += s + File.separator);
        } else { // Ohooold version uuusers, uuupdate youuur phoooooone......
            for (String s : pathSplit) {
                path[0] += s + File.separator;
            }
        }
        return path[0] + newName;
    }
}
