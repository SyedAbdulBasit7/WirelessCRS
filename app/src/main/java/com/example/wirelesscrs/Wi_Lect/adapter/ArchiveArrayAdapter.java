package com.example.wirelesscrs.Wi_Lect.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import com.example.wirelesscrs.Wi_Lect.Pref;
import com.example.wirelesscrs.R;
import com.example.wirelesscrs.Wi_Lect.entities.FMArchive;
import com.example.wirelesscrs.Wi_Lect.entities.FMFile;
import com.example.wirelesscrs.Wi_Lect.activities.file.ContextMenuUtil;

import static com.example.wirelesscrs.Wi_Lect.consts.PreferenceEntity.FILENAME_LENGTH;

/**
 * @author Lukas Fülling (lukas@k40s.net)
 */
public class ArchiveArrayAdapter extends BaseArrayAdapter {

    private static final String TAG = ArchiveArrayAdapter.class.getCanonicalName();

    private final FMArchive archive;

    public ArchiveArrayAdapter(Context context, int resource, List<FMFile> items, FMArchive archive) {
        super(context, resource, items);
        this.archive = archive;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return initUI(getItem(position));
    }

    /**
     * Opens a file using the default app.
     *
     * @param f the file
     */
    @Override
    protected void openFile(FMFile f) {
        if(f.isDirectory()) {
            activity.loadArchivePath(f.getAbsolutePath(), archive);
        } else {
            Toast.makeText(activity, R.string.only_browsing_in_archives, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Initializes the UI for each file.
     *
     * @param f the file
     * @return the initialized UI
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected View initUI(FMFile f) {
        assert activity != null;

        @SuppressLint("InflateParams") View v = LayoutInflater.from(activity).inflate(R.layout.lect_layout_archive_file, null); // works.

        if (f != null) {
            TextView fileNameView = v.findViewById(R.id.lect_fileTitle);
            ImageView fileImage = v.findViewById(R.id.lect_fileIcon);

            final String fileName = f.getName();
            if (fileNameView != null) {
                int maxLength = Integer.parseInt(new Pref<String>(FILENAME_LENGTH).getValue());
                if (fileName.length() >= maxLength) {
                    @SuppressLint("SetTextI18n") String output = fileName.substring(0, maxLength - 3) + "...";
                    fileNameView.setText(output); //shorten long names
                } else {
                    fileNameView.setText(fileName);
                }
            } else {
                Log.e(TAG, "TextView fileName is null!");
            }
            if (fileImage != null) {
                if (!f.isDirectory()) {
                    if (f.isArchive()) {
                        fileImage.setImageDrawable(getContext().getDrawable(R.drawable.ic_perm_media_black_24dp));
                    } else {
                        fileImage.setImageDrawable(getContext().getDrawable(R.drawable.ic_insert_drive_file_black_24dp));
                    }
                }
            } else {
                Log.e(TAG, "ImageView fileImage is null!");
            }

            v.setOnClickListener(v1 -> openFile(f));
            v.setOnCreateContextMenuListener((menu, view, info) -> new ContextMenuUtil(activity, this).initializeContextMenu(f, fileName, menu));
            ImageButton contextButton = v.findViewById(R.id.lect_contextMenuButton);
            contextButton.setOnClickListener(v1 -> activity.getFileListView().showContextMenuForChild(v));

            for (FMFile contextFile : activity.getFileOpContext().getSecond()) {
                if (contextFile.getFile().getAbsolutePath().equals(f.getFile().getAbsolutePath())) {
                    v.setBackgroundColor(activity.getColor(R.color.default_primary));
                }
            }
        } else {
            Log.e(TAG, "File is null!");
        }
        return v;
    }
}
