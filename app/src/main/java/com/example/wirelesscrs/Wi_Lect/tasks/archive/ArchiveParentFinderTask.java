package com.example.wirelesscrs.Wi_Lect.tasks.archive;

import com.example.wirelesscrs.Wi_Lect.Handler;
import com.example.wirelesscrs.Wi_Lect.activities.file.ArchiveParentFinder;
import com.example.wirelesscrs.Wi_Lect.entities.FMFile;
import com.example.wirelesscrs.Wi_Lect.exceptions.BlockingStuffOnMainThreadException;
import com.example.wirelesscrs.Wi_Lect.tasks.CallbackTask;

/**
 * Task to find the parent archive of a file.
 *
 * @author Lukas Fülling (lukas@k40s.net)
 */
public class ArchiveParentFinderTask extends CallbackTask<ArchiveParentFinder> {

    private final FMFile fmFile;

    /**
     * Constructor.
     *
     * @param callback the callback to use.
     */
    public ArchiveParentFinderTask(FMFile fmFile, Handler<ArchiveParentFinder> callback) {
        super(callback);
        this.fmFile = fmFile;
    }

    @Override
    protected ArchiveParentFinder doInBackground(Void... voids) {
        try {
            return new ArchiveParentFinder(fmFile.getAbsolutePath()).invoke();
        } catch (BlockingStuffOnMainThreadException e) {
            throw new RuntimeException("This should not happen!", e);
        }
    }
}
