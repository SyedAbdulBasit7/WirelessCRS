package com.example.wirelesscrs.Wi_Lect.tasks.archive;

import com.example.wirelesscrs.Wi_Lect.Handler;
import com.example.wirelesscrs.Wi_Lect.entities.FMArchive;
import com.example.wirelesscrs.Wi_Lect.entities.FMFile;
import com.example.wirelesscrs.Wi_Lect.exceptions.BlockingStuffOnMainThreadException;
import com.example.wirelesscrs.Wi_Lect.tasks.CallbackTask;

/**
 * Task that loads an archive.
 *
 * @author Lukas Fülling (lukas@k40s.net)
 */
public class ArchiveLoaderTask extends CallbackTask<FMArchive> {

    private final FMFile file;

    /**
     * Constructor.
     *
     * @param callback the callback to use.
     */
    public ArchiveLoaderTask(FMFile file, Handler<FMArchive> callback) {
        super(callback);
        this.file = file;
    }

    @Override
    protected FMArchive doInBackground(Void... voids) {
        try {
            return new FMArchive(file.getFile());
        } catch (BlockingStuffOnMainThreadException e) {
            throw new RuntimeException("This should not happen!", e);
        }
    }
}
