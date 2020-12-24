package com.example.wirelesscrs.Wi_Lect.activities.file;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import com.example.wirelesscrs.Wi_Lect.entities.FMFile;
import com.example.wirelesscrs.Wi_Lect.exceptions.BlockingStuffOnMainThreadException;
import com.example.wirelesscrs.Wi_Lect.exceptions.EmptyDirectoryException;
import com.example.wirelesscrs.Wi_Lect.exceptions.NoAccessException;

/**
 * Because I like to rant about <pre>ILoader</pre> 'nsuch.
 *
 * @author Lukas Fülling (lukas@k40s.net)
 */
public abstract class AbstractLoader {

    abstract ArrayList<FMFile> loadLocationFiles() throws NoAccessException, EmptyDirectoryException, BlockingStuffOnMainThreadException;

    abstract protected ArrayList<FMFile> loadLocationFilesForPath(@Nullable String parent) throws NoAccessException, EmptyDirectoryException, BlockingStuffOnMainThreadException;

}
