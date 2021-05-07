package org.nautilus.core.util;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkArgument;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for managing files
 * 
 * @author Thiago Ferreira
 * @since 2021-05-07
 */
public class FileUtils {
    
    /**
     * Private Constructor will prevent the instantiation of this class directly
     */
    private FileUtils() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    /**
     * Create a folder on the hard drive if it does not exist
     * 
     * @param folder should not be null
     * @return the folder created
     * @throws IllegalArgumentException if an I/O error occurs 
     */
    public static Path createIfNotExists(Path folder) {

        checkNotNull(folder, "folder should not be null");

        if (!Files.exists(folder)) {

            try {
                Files.createDirectories(folder);
            } catch (IOException ex) {
                throw new IllegalArgumentException(ex);
            }
        }
        
        return folder;
    }
    
    /**
     * A valid path means that the path is not null and exists
     * 
     * @param path to be validated
     * @return True if the path is valid. Otherwise, false
     */
    public static boolean isValid(Path path) {

        if (path == null || !Files.exists(path)) {
            return false;
        }

        return true;
    }
    
    /**
     * It retrieves all files sorted from a given folder
     * 
     * @param folder should be valid
     * @return the list of filtered files
     * @throws IOException if an I/O error occurs 
     */
    public static List<Path> getFilesFromFolder(Path folder) throws IOException {
        
        checkArgument(FileUtils.isValid(folder), "folder is not valid");
        
        return Files.walk(folder)
                .filter(Files::isRegularFile)
                .filter(p -> !p.endsWith(".DS_Store"))
                .sorted()
                .collect(Collectors.toList());
    }
    
    public static Path deleteFile(Path path) {

        checkArgument(FileUtils.isValid(path), "path is not valid");

        if (path.toFile().isDirectory()) {
            throw new RuntimeException("The path is not a file");
        }

        try {
            Files.delete(path);
        } catch (Exception ex) {
            throw new RuntimeException("It was not possible to delete the file", ex);
        }

        return path;
    }
}
