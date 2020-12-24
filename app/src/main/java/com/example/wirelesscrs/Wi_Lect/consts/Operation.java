package com.example.wirelesscrs.Wi_Lect.consts;

import androidx.annotation.StringRes;

import com.example.wirelesscrs.R;
import com.example.wirelesscrs.Wi_Lect.activities.file.FileActivity;

/**
 * File Operations.
 *
 * @author Lukas Fülling (lukas@k40s.net)
 */
public enum Operation {
    COPY(R.string.copy),
    MOVE(R.string.move),
    EXTRACT(R.string.extract),
    CREATE_ZIP(R.string.new_zip_file),
    NONE(R.string.empty),
    DELETE(R.string.delete);

    private final int title;

    /**
     * File Operation.
     *
     * @param title May not contain <pre>(</pre>
     * @see FileActivity#onPrepareOptionsMenu
     */
    Operation(@StringRes int title) {
        this.title = title;
    }

    public int getTitle() {
        return title;
    }
}