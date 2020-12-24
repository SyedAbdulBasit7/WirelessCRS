package com.example.wirelesscrs.Wi_Lect.tasks.operation;

import android.util.Log;

import com.example.wirelesscrs.Wi_Lect.Handler;
import com.example.wirelesscrs.R;
import com.example.wirelesscrs.Wi_Lect.activities.file.FileActivity;
import com.example.wirelesscrs.Wi_Lect.entities.FMFile;
import com.example.wirelesscrs.Wi_Lect.exceptions.BlockingStuffOnMainThreadException;

/**
 * Task to delete a file.
 *
 * @author Lukas Fülling (lukas@k40s.net)
 */
public class FileDeleteTask extends FileOperationTask {

    private static final String TAG = FileDeleteTask.class.getCanonicalName();
    private final FMFile f;

    public FileDeleteTask(FileActivity context, Handler<Boolean> callback, FMFile f) {
        super(context, callback);
        this.f = f;
        this.dialog.setTitle(R.string.deleting);
        this.dialog.setMessage(context.getString(R.string.deleting_detail) + FileActivity.WHITESPACE + f.getName());
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            return OperationUtil.doDeleteNoValidation(f);
        } catch (BlockingStuffOnMainThreadException e) {
            Log.wtf(TAG, "This should not happen here!", e);
            return false;
        }
    }
}
