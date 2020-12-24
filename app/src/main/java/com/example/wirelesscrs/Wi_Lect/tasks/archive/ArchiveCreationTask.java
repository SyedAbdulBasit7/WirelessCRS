package com.example.wirelesscrs.Wi_Lect.tasks.archive;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import com.example.wirelesscrs.Wi_Lect.Handler;
import com.example.wirelesscrs.R;
import com.example.wirelesscrs.Wi_Lect.activities.file.FileActivity;
import com.example.wirelesscrs.Wi_Lect.entities.FMFile;
import com.example.wirelesscrs.Wi_Lect.exceptions.BlockingStuffOnMainThreadException;
import com.example.wirelesscrs.Wi_Lect.tasks.ProgressDialogCallbackTask;

/**
 * {@link android.os.AsyncTask} to be run when creating an archive.
 *
 * @author Lukas Fülling (lukas@k40s.net)
 */
public class ArchiveCreationTask extends ProgressDialogCallbackTask<Boolean> {

    private static final String TAG = ArchiveCreationTask.class.getCanonicalName();
    /**
     * The files to be added to the archive.
     */
    private final CopyOnWriteArrayList<FMFile> targets;

    /**
     * The destination archive file.
     */
    private final File destination;

    /**
     * Constructor.
     * @param context the current {@link FileActivity} instance.
     * @param targets the target files
     * @param destination the destination archive file
     * @param callback the callback {@link Handler}
     */
    public ArchiveCreationTask(FileActivity context, CopyOnWriteArrayList<FMFile> targets, File destination, Handler<Boolean> callback) {
        super(context, callback);
        this.dialog.setTitle(R.string.creating);
        this.dialog.setMessage(context.getString(R.string.creating_detail) + FileActivity.WHITESPACE + destination.getName());
        this.dialog.setCancelable(false);

        this.targets = targets;
        this.destination = destination;
    }

    /**
     * Creates the archive.
     * @param voids the unused params
     * @return true if successful
     */
    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            return new ArchiveUtil().doCreateZip(targets, destination);
        } catch (BlockingStuffOnMainThreadException e) {
            Log.wtf(TAG, "This should not happen here!", e);
            return false;
        }
    }
}