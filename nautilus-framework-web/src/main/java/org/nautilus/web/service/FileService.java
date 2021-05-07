package org.nautilus.web.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FilenameUtils;
import org.nautilus.core.util.Converter;
import org.nautilus.core.util.FileUtils;
import org.nautilus.web.exception.FileIsEmptyException;
import org.nautilus.web.exception.FileNotFoundException;
import org.nautilus.web.exception.FileNotReadableException;
import org.nautilus.web.exception.InstanceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;

@Getter
@Service
public class FileService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(FileService.class);

	protected Path rootLocation = Paths.get("data");
	
	protected Path instancesLocation = rootLocation.resolve("instances");
	
	@PostConstruct
	private void setUp() {
		FileUtils.createIfNotExists(getRootLocation());
		FileUtils.createIfNotExists(getInstancesLocation());
	}
	
	public Path getInstanceLocation(String problemId, String filename) {
        return getInstancesLocation().resolve(problemId).resolve(filename);
    }
	
	public void createProblemLocation(String problemId) {
	    FileUtils.createIfNotExists(getInstancesLocation().resolve(problemId));
	}
	
    public boolean problemExists(String problemId) {
        return Files.exists(getInstancesLocation().resolve(problemId));
    }

    public boolean instanceExists(String problemId, String filename) {
        return Files.exists(getInstancesLocation().resolve(problemId).resolve(filename));
    }
	
    public List<Path> getInstances(String problemId) {

        if (!problemExists(problemId)) {
            return new ArrayList<>();
        }

        try {
            return FileUtils.getFilesFromFolder(getInstancesLocation().resolve(problemId));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    
    
	public Resource getInstanceAsResource(String problemId, String filename) {

        if (!instanceExists(problemId, filename)) {
            throw new InstanceNotFoundException();
        }

        return loadAsResource(getInstanceLocation(problemId, filename));
    }
	
	public Resource loadAsResource(Path file) {
		
		try {
			
			Resource resource = new UrlResource(file.toUri());

			if (!resource.exists()) {
				throw new FileNotFoundException();
			}
			
			if (!resource.isReadable()) {
				throw new FileNotReadableException();
			}
					
			return resource;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
    public Path load(Path root, String filename) {
		return root.resolve(filename);
	}

	public Path loadInstances(String pluginKey, String filename) {
		return load(getInstancesLocation().resolve(pluginKey), filename);
	}
	
    public Path deleteInstance(String problemId, String filename) {
        return FileUtils.deleteFile(getInstanceLocation(problemId, filename));
    }
	
	
    public Path saveInstance(String problemId, MultipartFile file) {
        return save(getInstancesLocation().resolve(problemId), file);
    }
    
	public Path save(Path path, MultipartFile file) {

	    String filename = file.getOriginalFilename();
	    
	    LOGGER.info("Saving file {}", filename);
	    
		String name = FilenameUtils.removeExtension(filename);
		String extension = FilenameUtils.getExtension(filename);
		
		name = Converter.toKey(name);
		filename = String.format("%s.%s", name, extension);
		
		if (file.isEmpty()) {
			throw new FileIsEmptyException();
		}
		
		// This is a security check
		if (filename.contains("..")) {
			throw new RuntimeException("StoreFileWithRelativePathException");
		}
		
//		if (Files.exists(path, filename)) {
//			throw new FileAlreadyExistsException();
//		}
		
		try {
			Files.copy(file.getInputStream(), load(path, filename), StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception ex) {
			throw new RuntimeException("StoreFileIsNotPossibleException", ex);
		}
		
		return path.resolve(filename);
	}
	
	public String readFileToString(String problemId, String filename) {

		Path path = getInstanceLocation(problemId, filename);

		if (!Files.exists(path)) {
			throw new RuntimeException("The file does not exist. Please choose a different one");
		}

//		try {
//			return FileUtils.readFileToString(path.toFile());
//		} catch (Exception ex) {
//			throw new RuntimeException("There was an I/O error", ex);
//		}
		return null;
	}
}
