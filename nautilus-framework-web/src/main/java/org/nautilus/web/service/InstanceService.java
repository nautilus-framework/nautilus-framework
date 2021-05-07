package org.nautilus.web.service;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.nautilus.plugin.extension.ProblemExtension;
import org.nautilus.web.dto.UploadFileDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class InstanceService {

    @Autowired
    private FileService fileService;
	
    @Autowired
    private PluginService pluginService;

    public Map<ProblemExtension, List<Path>> getProblemAndInstances() {

        Map<ProblemExtension, List<Path>> instances = new HashMap<>();

        for (ProblemExtension problem : pluginService.getProblemsSorted()) {
            instances.put(problem, fileService.getInstances(problem.getId()));
        }

        return instances;
    }
    
    public Path save(String problemId, UploadFileDTO uploadFileDTO) {

        checkArgument(!StringUtils.isBlank(problemId), "problemId should not be black");
        checkNotNull(uploadFileDTO, "uploadFileDTO should not be null");

        ProblemExtension problem = pluginService.getProblemById(problemId);

        return fileService.saveInstance(problem.getId(), uploadFileDTO.getFile());
    }
    
    public Resource getFileAsResource(String problemId, String filename) {
        
        checkArgument(!StringUtils.isBlank(problemId), "problemId should not be black");
        checkArgument(!StringUtils.isBlank(filename), "filename should not be black");
        
        ProblemExtension problem = pluginService.getProblemById(problemId);

        return fileService.getInstanceAsResource(problem.getId(), filename);
    }

    public Path delete(String problemId, String filename) {

        checkArgument(!StringUtils.isBlank(problemId), "problemId should not be black");
        checkArgument(!StringUtils.isBlank(filename), "filename should not be black");

        ProblemExtension problem = pluginService.getProblemById(problemId);

        return fileService.deleteInstance(problem.getId(), filename);
    }
}
