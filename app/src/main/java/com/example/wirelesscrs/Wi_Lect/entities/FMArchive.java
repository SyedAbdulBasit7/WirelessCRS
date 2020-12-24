package com.example.wirelesscrs.Wi_Lect.entities;

import android.os.Looper;
import android.util.Log;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.zip.ZipEntry;

import com.example.wirelesscrs.Wi_Lect.consts.FileType;
import com.example.wirelesscrs.Wi_Lect.exceptions.BlockingStuffOnMainThreadException;

/**
 * @author Lukas Fülling (lukas@k40s.net)
 */
public class FMArchive extends FMFile {

    public final static String TAG = FMArchive.class.getCanonicalName();

    private final HashMap<String, ArrayList<FMFile>> contents;
    private static final String ROOT_DIR = "/";

    /**
     * Constructor.
     *
     * @param f the file
     */
    public FMArchive(File f) throws BlockingStuffOnMainThreadException {
        super(f);
        if (!this.isArchive()) {
            this.contents = null;
            throw new ClassCastException("Error creating " + f.getName() + "as archive:" + FMFile.class.getName() + " cannot be cast to " + FMArchive.class.getName());
        } else {
            this.contents = calculateArchiveContents();
        }
    }

    /**
     * Gets archive content for path.
     *
     * @param path the relative path inside the archive
     * @return the contents
     */
    public ArrayList<FMFile> getContentForPath(String path) {
        String rPath;
        if (!path.startsWith(ROOT_DIR)) {
            rPath = ROOT_DIR + path;
        } else {
            try {
                rPath = path.split(getName())[1];
            } catch (ArrayIndexOutOfBoundsException e) {
                rPath = ROOT_DIR;
            }
        }
        if (!rPath.equals(ROOT_DIR) && rPath.endsWith("/")) {
            rPath = rPath.substring(0, rPath.length() - 1);
        }
        ArrayList<FMFile> pathContents = contents.get(rPath);
        if (pathContents == null && rPath.equals(ROOT_DIR)) {
            pathContents = new ArrayList<>();
            for (String s : contents.keySet()) {
                String[] split = s.split(File.separator);
                if (split.length == 2) {
                    pathContents.add(new FMFile(new File(split[1])));
                }
            }
        }

        return new ArrayList<>(Objects.requireNonNull(pathContents));
    }

    /**
     * Calculates the archive contents.
     *
     * @return a {@link HashMap} containing the relative path of the file in the archive and the file.
     */
    private HashMap<String, ArrayList<FMFile>> calculateArchiveContents() throws BlockingStuffOnMainThreadException {
        if(Looper.myLooper() == Looper.getMainLooper()) {
            throw new BlockingStuffOnMainThreadException();
        }
        HashMap<String, ArrayList<FMFile>> res = new HashMap<>();
        if(getFileType() != FileType.ARCHIVE_P7Z) {
            try(InputStream is = new FileInputStream(getFile())) {

                ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(getFileType().getExtension(), is);
                ZipEntry entry;
                while ((entry = (ZipArchiveEntry) ais.getNextEntry()) != null) {
                    String filePath;

                    filePath = entry.getName();

                    FMArchiveFile outFile = new FMArchiveFile(new File(entry.getName()));
                    outFile.setDirectory(entry.isDirectory());
                    outFile.setAbsolutePath(entry.getName());

                    String fileParent = new File(filePath).getParent();
                    String parent = ROOT_DIR + ((fileParent != null) ? fileParent : "");
                    ArrayList<FMFile> pathContents = res.get(parent);
                    if (pathContents == null) {
                        pathContents = new ArrayList<>();
                    }

                    pathContents.add(outFile);
                    res.put(parent, pathContents);
                }
                ais.close();
            } catch (IOException | ArchiveException e) {
                Log.e(TAG, "Error reading " + getFileType().getExtension());
            }
        } else {
            try(SevenZFile sevenZFile = new SevenZFile(getFile())) {
                SevenZArchiveEntry entry;
                while ((entry = sevenZFile.getNextEntry()) != null) {
                    String filePath;

                    filePath = entry.getName();

                    FMArchiveFile outFile = new FMArchiveFile(new File(entry.getName()));
                    outFile.setDirectory(entry.isDirectory());
                    outFile.setAbsolutePath(entry.getName());

                    String fileParent = new File(filePath).getParent();
                    String parent = ROOT_DIR + ((fileParent != null) ? fileParent : "");
                    ArrayList<FMFile> pathContents = res.get(parent);
                    if (pathContents == null) {
                        pathContents = new ArrayList<>();
                    }

                    pathContents.add(outFile);
                    res.put(parent, pathContents);
                }
            } catch (IOException e) {
                Log.e(TAG, "Error reading " + getFileType().getExtension());
            }
        }
        return res;
    }
}
