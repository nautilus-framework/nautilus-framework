package thiagodnf.nautilus.web.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

@Service
public class FileService {

	protected Path rootLocation = Paths.get("files");
	
	@PostConstruct
	private void initIt() {
		createDirectories(getRootLocation());
		createDirectories(getInstancesLocation());
	}
	
	public Path getRootLocation() {
		return rootLocation;
	}
	
	public Path getInstancesLocation() {
		return getRootLocation().resolve("instances");
	}
	
	public List<Path> getProblems(){
		return findAll(getInstancesLocation())
			.sorted()
			.collect(Collectors.toList());
	}
	
	public List<Path> getInstanceFiles(String problemKey){
		return findAll(getInstancesLocation().resolve(problemKey))
			.sorted()
			.collect(Collectors.toList());
	}
	
	
	
	public void createInstancesDirectory(String pluginKey) {
		createDirectories(getInstancesLocation().resolve(pluginKey));
	}
	
	private void createDirectories(Path directory) {
		try {
			Files.createDirectories(directory);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public List<Path> getInstanceFilesLocation() {
		
		List<Path> files = new ArrayList<>();
		
		for(Path problem : getProblems()) {
			files.addAll(findAll(problem).collect(Collectors.toList()));
		}
		
		return files;
	}
	
	public Path getInstancesFile(String problemKey, String filename) {
		return getInstancesLocation().resolve(problemKey).resolve(filename);
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
	
	public Resource loadAsResource(String filename) {
		try {
			Path file = load(filename);

			Resource resource = new UrlResource(file.toUri());

			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException();
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public Path load(Path root, String filename) {
		return root.resolve(filename);
	}

	public Path load(String filename) {
		return load(getPath(), filename);
	}
	
	public Path loadInstances(String pluginKey, String filename) {
		return load(getPath().resolve("instances").resolve(pluginKey), filename);
	}
	
	public Path getPath() {
		return rootLocation;
	}
}
