package com.example.wirelesscrs.Wi_Lect.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.List;

import com.example.wirelesscrs.Wi_Lect.Handler;
import com.example.wirelesscrs.R;
import com.example.wirelesscrs.Wi_Lect.activities.file.FileActivity;
import com.example.wirelesscrs.Wi_Lect.entities.FMFile;

/**
 * @author Lukas Fülling (lukas@k40s.net)
 */
public abstract class BaseArrayAdapter extends ArrayAdapter<FMFile> {

    public static final String TAG = BaseArrayAdapter.class.getCanonicalName();

    FileActivity activity;

    BaseArrayAdapter(Context context, int resource, List<FMFile> items) {
        super(context, resource, items);
        if (context instanceof FileActivity) {
            this.activity = (FileActivity) context;
        } else {
            Log.d(TAG, "Context is no FileActivity: " + context.getClass().getName());
        }
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return initUI(getItem(position));
    }

    protected abstract View initUI(FMFile f);

    protected abstract void openFile(FMFile f);

    /**
     * Sets file name in the text field of a dialog.
     *
     * @param alertDialog     the dialog
     * @param destinationName the id of the EditText
     * @param name            the name
     */
    public void presetNameForDialog(AlertDialog alertDialog, @IdRes int destinationName, String name) {
        EditText editText = alertDialog.findViewById(destinationName);
        if (editText != null) {
            editText.setText(name);
        } else {
            Log.w(TAG, "Unable to find view, can not set file title.");
        }
    }

    /**
     * Adds the path of a file to a dialog.
     *
     * @param f           the file
     * @param alertDialog the dialog
     * @see #presetNameForDialog(AlertDialog, int, String)
     */
    @SuppressWarnings("unused")
    public void presetPathForDialog(FMFile f, AlertDialog alertDialog) {
        presetNameForDialog(alertDialog, R.id.lect_destinationPath, f.getFile().getAbsolutePath());
    }

    /**
     * Utility method to create an AlertDialog.
     *
     * @param positiveBtnText  the text of the positive button
     * @param title            the title
     * @param icon             the icon
     * @param view             the content view
     * @param positiveCallBack the positive callback
     * @param negativeCallBack the negative callback
     * @return the dialog
     */
    public AlertDialog getGenericFileOpDialog(
            @StringRes int positiveBtnText,
            @StringRes int title,
            @DrawableRes int icon,
            @LayoutRes int view,
            Handler<AlertDialog> positiveCallBack,
            Handler<AlertDialog> negativeCallBack) {

        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setView(view)
                .setTitle(title)
                .setCancelable(true).create();

        dialog.setButton(DialogInterface.BUTTON_POSITIVE, activity.getString(positiveBtnText), (d, i) -> positiveCallBack.handle(dialog));
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, activity.getString(R.string.cancel), (d, i) -> negativeCallBack.handle(dialog));
        dialog.setOnShowListener(dialog1 -> {

            ImageView dialogIcon = dialog.findViewById(R.id.lect_dialogIcon);
            dialogIcon.setImageDrawable(getContext().getDrawable(icon));

            EditText inputField;
            if (view == R.layout.lect_layout_name_prompt) {
                inputField = dialog.findViewById(R.id.lect_destinationName);
                if (inputField != null) {
                    String name = activity.getTitleFromPath(activity.getCurrentDirectory());
                    inputField.setText(name);
                    Log.d(TAG, "Destination set to: " + name);
                } else {
                    Log.w(TAG, "Unable to preset current name, text field is null!");
                }
            } else if (view == R.layout.lect_layout_path_prompt) {
                inputField = dialog.findViewById(R.id.lect_destinationPath);
                if (inputField != null) {
                    String directory = activity.getCurrentDirectory();
                    inputField.setText(directory);
                    Log.d(TAG, "Destination set to: " + directory);
                } else {
                    Log.w(TAG, "Unable to preset current path, text field is null!");
                }
            }
        });
        return dialog;
    }
}
