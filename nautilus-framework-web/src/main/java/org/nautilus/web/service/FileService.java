package org.nautilus.web.service;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.nautilus.core.util.FileUtils;
import org.nautilus.web.exception.FileIsEmptyException;
import org.nautilus.web.exception.FileNotFoundException;
import org.nautilus.web.exception.FileNotReadableException;
import org.nautilus.web.exception.InstanceNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;

@Getter
@Service
public class FileService {
    
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
	
	public Path deleteInstance(String problemId, String filename) {
        return FileUtils.deleteFile(getInstanceLocation(problemId, filename));
    }
	
	public Path saveInstance(String problemId, MultipartFile file) {
        return save(getInstancesLocation().resolve(problemId), file);
    }
    
    public Path save(Path path, MultipartFile multipartFile) {
        
        checkArgument(!multipartFile.isEmpty(), new FileIsEmptyException().toString());

        String filename = FileUtils.format(multipartFile.getOriginalFilename());
        Path file = path.resolve(filename);

        try {
            multipartFile.transferTo(file);
        } catch (Exception ex) {
            throw new RuntimeException("StoreFileIsNotPossibleException", ex);
        }

        return file;
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
