package com.example.wirelesscrs.Wi_Lect.tasks;

import android.util.Log;

import java.util.ArrayList;

import com.example.wirelesscrs.Wi_Lect.Handler;
import com.example.wirelesscrs.Wi_Lect.activities.file.FileActivity;
import com.example.wirelesscrs.Wi_Lect.activities.file.FileLoader;
import com.example.wirelesscrs.Wi_Lect.entities.FMFile;
import com.example.wirelesscrs.Wi_Lect.exceptions.BlockingStuffOnMainThreadException;
import com.example.wirelesscrs.Wi_Lect.exceptions.EmptyDirectoryException;
import com.example.wirelesscrs.Wi_Lect.exceptions.NoAccessException;

/**
 * Task to load directory contents.
 *
 * @author Lukas Fülling (lukas@k40s.net)
 */
public class DirectoryLoaderTask extends ProgressDialogCallbackTask<ArrayList<FMFile>> {

    private static final String TAG = DirectoryLoaderTask.class.getCanonicalName();
    private final String path;

    /**
     * Constructor.
     *
     * @param context  the current {@link FileActivity} instance.
     * @param path     the path to load
     * @param callback the callback to use.
     */
    public DirectoryLoaderTask(FileActivity context, String path, Handler<ArrayList<FMFile>> callback) {
        super(context, callback);
        this.path = path;
    }

    @Override
    protected ArrayList<FMFile> doInBackground(Void... voids) {
        ArrayList<FMFile> files = null;
        FileLoader fileLoader = new FileLoader(path);

        try {
            files = fileLoader.loadLocationFiles();
        } catch (NoAccessException e) {
            Log.w(TAG, "Can't read '" + path + "': Permission denied!");
        } catch (EmptyDirectoryException e) {
            Log.w(TAG, "Can't read '" + path + "': Empty directory!");
        } catch (BlockingStuffOnMainThreadException e) {
            Log.wtf(TAG, "This should not happen!", e);
        }
        return files;
    }
}
