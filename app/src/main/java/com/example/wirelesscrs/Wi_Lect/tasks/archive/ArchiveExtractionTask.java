package com.example.wirelesscrs.Wi_Lect.tasks.archive;

import android.util.Log;

import com.example.wirelesscrs.Wi_Lect.Handler;
import com.example.wirelesscrs.R;
import com.example.wirelesscrs.Wi_Lect.activities.file.FileActivity;
import com.example.wirelesscrs.Wi_Lect.entities.FMFile;
import com.example.wirelesscrs.Wi_Lect.exceptions.BlockingStuffOnMainThreadException;
import com.example.wirelesscrs.Wi_Lect.tasks.ProgressDialogCallbackTask;


/**
 * {@link android.os.AsyncTask} that is called when there's an archive to extract.
 *
 * @author Lukas Fülling (lukas@k40s.net)
 */
public class ArchiveExtractionTask extends ProgressDialogCallbackTask<Boolean> {

    private static final String TAG = ArchiveExtractionTask.class.getCanonicalName();
    /**
     * The destination path to extract to.
     */
    private final String destinationPath;

    /**
     * The archive to extract.
     */
    private final FMFile archive;

    /**
     * Constructor.
     * @param context the current {@link FileActivity} instance
     * @param destinationPath the destination path to extract to
     * @param archive the archive to extract
     * @param callback the callback {@link Handler}
     */
    public ArchiveExtractionTask(FileActivity context, String destinationPath, FMFile archive, Handler<Boolean> callback) {
        super(context, callback);
        this.dialog.setTitle(R.string.extracting);
        this.dialog.setMessage(context.getString(R.string.extracting_detail) + FileActivity.WHITESPACE + archive.getName());
        this.dialog.setCancelable(false);

        this.destinationPath = destinationPath;
        this.archive = archive;
    }

    /**
     * Extracts the archive.
     * @param args the void args (unused)
     * @return true if successful.
     * @see ArchiveUtil#doExtractArchive(String, FMFile)
     */
    @Override
    protected Boolean doInBackground(final Void... args) {
        try {
            return new ArchiveUtil().doExtractArchive(destinationPath, archive);
        } catch (BlockingStuffOnMainThreadException e) {
            Log.wtf(TAG, "This should not happen here!", e);
            return false;
        }
    }
}