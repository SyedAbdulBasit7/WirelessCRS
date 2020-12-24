package com.example.wirelesscrs.Wi_Lect.activities.file;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import com.example.wirelesscrs.Wi_Lect.entities.FMArchive;
import com.example.wirelesscrs.Wi_Lect.entities.FMFile;

/**
 * Class used to load archives.
 *
 * @author Lukas Fülling (lukas@k40s.net)
 */
public class ArchiveLoader extends AbstractLoader {

    /**
     * The archive-
     */
    private final FMArchive archive;

    /**
     * The path to the archive.
     */
    private String path;

    /**
     * Constructor.
     * @param archiveFile the file to load
     * @param path the path to load
     */
    ArchiveLoader(@NonNull FMArchive archiveFile, String path) {
        this.archive = archiveFile;
        this.path = path;
    }

    @Override
    public ArrayList<FMFile> loadLocationFiles() {
        return this.loadLocationFilesForPath(path);
    }

    @Override
    public ArrayList<FMFile> loadLocationFilesForPath(@Nullable String parent) {
        return archive.getContentForPath((parent != null) ? parent : "/");
    }
}
