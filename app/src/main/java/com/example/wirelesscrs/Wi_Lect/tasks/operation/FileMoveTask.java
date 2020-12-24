package com.example.wirelesscrs.Wi_Lect.tasks.operation;

import android.app.AlertDialog;
import androidx.annotation.Nullable;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.lang.ref.WeakReference;

import com.example.wirelesscrs.Wi_Lect.Handler;
import com.example.wirelesscrs.R;
import com.example.wirelesscrs.Wi_Lect.activities.file.FileActivity;
import com.example.wirelesscrs.Wi_Lect.entities.FMFile;
import com.example.wirelesscrs.Wi_Lect.exceptions.BlockingStuffOnMainThreadException;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Task to move a file.
 *
 * @author Lukas Fülling (lukas@k40s.net)
 */
public class FileMoveTask extends FileOperationTask {
    private static final String TAG = FileMoveTask.class.getCanonicalName();
    private final WeakReference<FileActivity> contextRef;
    private final FMFile f;
    private final File destination;


    public FileMoveTask(FileActivity context, Handler<Boolean> callback, FMFile f, @Nullable AlertDialog d) {
        this(context, callback, f, (d != null) ? ((EditText) d.findViewById(R.id.lect_destinationName)).getText().toString() : context.getCurrentDirectory());
    }

    private FileMoveTask(FileActivity context, Handler<Boolean> callback, FMFile f, String newName) {
        super(context, callback);
        this.contextRef = new WeakReference<>(context);
        this.f = f;
        if (newName.isEmpty()) {
            Toast.makeText(context, R.string.err_empty_input, LENGTH_SHORT).show();
            this.cancel(true);
        }
        destination = new File(newName + "/" + f.getFile().getName());
        if (destination.exists()) {
            getFileExistsDialogBuilder(context)
                    .setOnDismissListener(d -> this.execute())
                    .setOnCancelListener(d -> cancel(true)) //cancel task on "no"
                    .create().show();
        }
        this.dialog.setTitle(R.string.moving);
        this.dialog.setMessage(context.getString(R.string.moving_detail) + FileActivity.WHITESPACE + f.getName());
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            return OperationUtil.doMove(f, destination);
        } catch (BlockingStuffOnMainThreadException e) {
            Log.wtf(TAG, "This should not happen here!", e);
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean success) {
        super.onPostExecute(success);
        contextRef.get().clearFileOpCache();
        contextRef.get().reloadCurrentDirectory();
    }
}
