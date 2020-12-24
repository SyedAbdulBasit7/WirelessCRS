package com.example.wirelesscrs.Wi_Lect.activities.file;

import java.io.File;

import com.example.wirelesscrs.Wi_Lect.entities.FMArchive;
import com.example.wirelesscrs.Wi_Lect.entities.FMFile;
import com.example.wirelesscrs.Wi_Lect.exceptions.BlockingStuffOnMainThreadException;

/**
 * Finds parent archives.
 *
 * @author Lukas Fülling (lukas@k40s.net)
 */
public class ArchiveParentFinder {
    private String path;
    private boolean archive;
    private FMArchive archiveFile;

    public ArchiveParentFinder(String path) {
        this.path = path;
    }

    public boolean isArchive() {
        return archive;
    }

    public FMArchive getArchiveFile() {
        return archiveFile;
    }

    public ArchiveParentFinder invoke() throws BlockingStuffOnMainThreadException {
        archive = false;
        archiveFile = null;
        String tPath = path;
        while (!"/".equals(tPath) && tPath != null) {
            FMFile f = new FMFile(new File(tPath));
            if (f.isArchive()) {
                archive = true;
                archiveFile = new FMArchive(new File(tPath));
            }
            tPath = new File(tPath).getParent();
            if(archive || tPath == null || tPath.isEmpty()) {
                break;
            }
        }
        return this;
    }
}
