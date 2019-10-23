package org.nautilus.web.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.nautilus.core.util.Converter;
import org.nautilus.web.exception.FileAlreadyExistsException;
import org.nautilus.web.exception.FileIsEmptyException;
import org.nautilus.web.exception.FileNotFoundException;
import org.nautilus.web.exception.FileNotReadableException;
import org.nautilus.web.exception.InstanceNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

	protected Path rootLocation = Paths.get("files");
	
	@PostConstruct
	private void initIt() {
		createDirectories(getRootLocation());
		createDirectories(getInstancesLocation());
		createDirectories(getPluginsLocation());
	}
	
	public Path getRootLocation() {
		return rootLocation;
	}
	
	public Path getInstancesLocation() {
		return getRootLocation().resolve("instances");
	}
	
	public Path getPluginsLocation() {
		return getRootLocation().resolve("plugins");
	}
	
	public List<String> getPlugins() {
		try {
			return Files.walk(getPluginsLocation(), 1)
				.filter(path -> !path.toFile().isDirectory())
				.filter(path -> path.toFile().getAbsolutePath().endsWith(".jar"))
				.filter(p -> !p.endsWith(".DS_Store"))
				.map(path -> path.toFile().getAbsolutePath())
				.collect(Collectors.toList());
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public List<Path> getInstances(String problemId){
		return findAll(getInstancesLocation().resolve(problemId))
			.sorted()
			.collect(Collectors.toList());
	}
	
	public void createInstancesDirectory(String pluginKey) {
		createDirectories(getInstancesLocation().resolve(pluginKey));
	}
	
	public void createPluginDirectory(String pluginId, String problemId) {
		createDirectories(getInstancesLocation().resolve(pluginId).resolve(problemId));
	}
	
	public void createDirectories(Path directory) {
		try {
			Files.createDirectories(directory);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
    public boolean containsInstance(String problemId, String filename) {
        return Files.exists(getInstance(problemId, filename));
    }
	
	public Path getInstance(String problemId, String filename) {
	    return getInstancesLocation().resolve(problemId).resolve(filename);
	}
	
	public Stream<Path> findAll(Path path) {
		
		try {
			return Files.walk(path, 1)
				.filter(p -> !p.equals(path))
				.filter(p -> !p.endsWith(".DS_Store")
			);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
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
	
    public Resource getInstanceAsResource(String problemId, String filename) {

        if (!containsInstance(problemId, filename)) {
            throw new InstanceNotFoundException();
        }

        return loadAsResource(getInstance(problemId, filename));
    }
	
	public Path load(Path root, String filename) {
		return root.resolve(filename);
	}

	public Path loadInstances(String pluginKey, String filename) {
		return load(getInstancesLocation().resolve(pluginKey), filename);
	}
	
	public void store(String problemId, MultipartFile file, String filename) {
		store(getInstancesLocation().resolve(problemId), filename, file);
	}
	
	public void storeInstance(String problemId, String filename, MultipartFile file) {
		store(getInstancesLocation().resolve(problemId), filename, file);
	}
	
	public void storePlugin(String filename, MultipartFile file) {
		store(getPluginsLocation(), filename, file);
	}
	
	public boolean exists(Path path, String filename) {
		return exists(path.resolve(filename));
	}

	public boolean exists(Path path) {
		return Files.exists(path);
	}
	
	public void deleteInstance(String problemId, String filename) {
		delete(getInstance(problemId, filename));
	}
	
	public void delete(Path path) {
		
		if (path.toFile().isDirectory()) {
			throw new RuntimeException("The path is not a file");
		}

		if (!exists(path)) {
			throw new RuntimeException("The file does not exist. Please choose a different one");
		}
		
		try {
			Files.delete(path);
		} catch (Exception ex) {
			throw new RuntimeException("Delete file is not possible", ex);
		}
	}
	
	public void store(Path path, String filename, MultipartFile file) {

		String name = FilenameUtils.removeExtension(filename);
		String extension = FilenameUtils.getExtension(filename);
		
		name = Converter.toKey(name);
		
		filename = String.format("%s.%s", name, extension);
		
		createDirectories(path);

		if (file.isEmpty()) {
			throw new FileIsEmptyException();
		}
		
		// This is a security check
		if (filename.contains("..")) {
			throw new RuntimeException("StoreFileWithRelativePathException");
		}
		
		if (exists(path, filename)) {
			throw new FileAlreadyExistsException();
		}
		
		try {
			Files.copy(file.getInputStream(), load(path, filename), StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception ex) {
			throw new RuntimeException("StoreFileIsNotPossibleException", ex);
		}
	}
	
	public String readFileToString(String problemId, String filename) {

		Path path = getInstance(problemId, filename);

		if (!exists(path)) {
			throw new RuntimeException("The file does not exist. Please choose a different one");
		}

		try {
			return FileUtils.readFileToString(path.toFile());
		} catch (Exception ex) {
			throw new RuntimeException("There was an I/O error", ex);
		}
	}
}
