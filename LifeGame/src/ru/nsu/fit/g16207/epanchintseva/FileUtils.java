package ru.nsu.fit.g16207.epanchintseva;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;

public class FileUtils {
    private static File dataDirectory = null;
    /**
     * Returns File pointing to Data directory of current project. If Data directory is not found, returns project directory.
     * @return File object.
     */
    public static File getDataDirectory()
    {
        if(dataDirectory == null)
        {
            try
            {
                String path = URLDecoder.decode(MainFrame.class.getProtectionDomain().getCodeSource().getLocation().getFile(), Charset.defaultCharset().toString());
                dataDirectory = new File(path).getParentFile();
            }
            catch (UnsupportedEncodingException e)
            {
                dataDirectory = new File(".");
            }
            if(dataDirectory == null || !dataDirectory.exists()) dataDirectory = new File(".");
            for(File f: dataDirectory.listFiles())
            {
                if(f.isDirectory() && f.getName().endsWith("_Data"))
                {
                    dataDirectory = f;
                    break;
                }
            }
        }
        return dataDirectory;
    }

    public static File getSaveFileName(JFrame parent, String extension, String description)
    {
        JFileChooser fileChooser = new JFileChooser();
        FileFilter filter = new ExtensionFileFilter(extension, description);
        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setCurrentDirectory(getDataDirectory());
        if(fileChooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION)
        {
            File f = fileChooser.getSelectedFile();
            if(!f.getName().contains("."))
                f = new File(f.getParent(), f.getName()+"."+extension);
            return f;
        }
        return null;
    }

    public static File getOpenFileName(JFrame parent, String extension, String description)
    {
        JFileChooser fileChooser = new JFileChooser();
        FileFilter filter = new ExtensionFileFilter(extension, description);
        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setCurrentDirectory(getDataDirectory());
        if(fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION)
        {
            File f = fileChooser.getSelectedFile();
            if(!f.getName().contains("."))
                f = new File(f.getParent(), f.getName()+"."+extension);
            return f;
        }
        return null;
    }
}


/**
 * File filter which leaves only directories and files with specific extension
 * @author Tagir F. Valeev
 */
 class ExtensionFileFilter extends FileFilter
{
    String extension, description;

    /**
     * Constructs filter
     * @param extension - extension (without point), for example, "txt"
     * @param description - file type description, for example, "Text files"
     */
    public ExtensionFileFilter(String extension, String description)
    {
        this.extension = extension;
        this.description = description;
    }

    @Override
    public boolean accept(File f) {
        return f.isDirectory() || f.getName().toLowerCase().endsWith("."+extension.toLowerCase());
    }

    @Override
    public String getDescription() {
        return description+" (*."+extension+")";
    }
}
