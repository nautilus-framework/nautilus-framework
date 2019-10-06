package thiagodnf.nautilus.web.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import thiagodnf.nautilus.plugin.extension.algorithm.ParetoFrontApproxExtension;
import thiagodnf.nautilus.web.dto.ExecutionQueueDTO;
import thiagodnf.nautilus.web.dto.ExecutionSettingsDTO;
import thiagodnf.nautilus.web.dto.ExecutionSimplifiedDTO;
import thiagodnf.nautilus.web.exception.ExecutionNotFoundException;
import thiagodnf.nautilus.web.exception.UserNotOwnerException;
import thiagodnf.nautilus.web.model.Execution;
import thiagodnf.nautilus.web.model.User;
import thiagodnf.nautilus.web.model.Execution.Visibility;
import thiagodnf.nautilus.web.repository.ExecutionRepository;

@Service
public class ExecutionService {

    @Autowired
    private ExecutionRepository executionRepository;

    @Autowired
    private List<ExecutionQueueDTO> runningExecutions;

    @Autowired
    private SecurityService securityService;

    public List<ExecutionQueueDTO> findRunningExecutions() {

        List<ExecutionQueueDTO> selected = new ArrayList<>();

        for (ExecutionQueueDTO execution : runningExecutions) {

            if (isOwner(execution.getUserId())) {
                selected.add(execution);
            }
        }

        return selected;
    }

    public ExecutionSimplifiedDTO duplicate(String userId, String executionId) {

        Execution execution = findExecutionById(executionId);

        execution.setId(null);
        execution.setUserId(userId);
        execution.setCreationDate(null);
        execution.setLastChangeDate(null);
        execution.setVisibility(Visibility.PRIVATE);

        Execution saved = save(execution);

        return convertToExecutionSimplifiedDTO(saved);
    }

    public void updateSettings(String executionId, ExecutionSettingsDTO executionSettingsDTO) {

        Execution execution = findExecutionById(executionId);
        
        if (!isOwner(execution))
            throw new UserNotOwnerException();
        
        execution.setTitle(executionSettingsDTO.getTitle());
        execution.setVisibility(executionSettingsDTO.getVisibility());
        execution.setShowLines(executionSettingsDTO.isShowLines());
        execution.setShowOriginalObjectiveValues(executionSettingsDTO.isShowOriginalObjectiveValues());
        execution.setNormalizeId(executionSettingsDTO.getNormalizeId());
        execution.setCorrelationId(executionSettingsDTO.getCorrelationId());
        execution.setRemoverId(executionSettingsDTO.getRemoverId());
        execution.setColor(executionSettingsDTO.getColor());

        save(execution);
    }
    
    public Execution findParetoFrontApprox(String problemId, String instance){
        return this.executionRepository.findByProblemIdAndInstanceAndAlgorithmId(problemId, instance, new ParetoFrontApproxExtension().getId());
    }
    
    public List<Execution> findByProblemIdAndInstance(String problemId, String instance){
        return this.executionRepository.findByProblemIdAndInstance(problemId, instance);
    }

    public Execution save(Execution execution) {
        return this.executionRepository.save(execution);
    }

    public ExecutionSimplifiedDTO findExecutionSimplifiedDTOById(String executionId) {
        return this.executionRepository.findExecutionSimplifiedDTOById(executionId).orElseThrow(ExecutionNotFoundException::new);
    }

    public Execution findExecutionById(String executionId) {
        return this.executionRepository.findById(executionId).orElseThrow(ExecutionNotFoundException::new);
    }
    
    public List<Execution> findAll() {
        return this.executionRepository.findAll();
    }

    public List<ExecutionSimplifiedDTO> findExecutionSimplifiedDTOByUserId(String userId) {
        return executionRepository.findByUserIdAndSolutionsNotNull(userId);
    }

    public void deleteById(String executionId) {

        Execution execution = findExecutionById(executionId);

        if (!isOwner(execution))
            throw new UserNotOwnerException();
        
        this.executionRepository.delete(execution);
    }

    public ExecutionSimplifiedDTO convertToExecutionSimplifiedDTO(Execution execution) {

        if (execution == null)
            return null;

        return new ExecutionSimplifiedDTO(
            execution.getId(), 
            execution.getTitle(), 
            execution.getProblemId(),
            execution.getInstance(),
            execution.getAlgorithmId(),
            execution.getCreationDate(),
            execution.getSelectedSolutions(),
            execution.getUserId()
        );
    }

    public ExecutionSettingsDTO convertToExecutionSettingsDTO(Execution execution) {

        if (execution == null)
            return null;

        ExecutionSettingsDTO dto = new ExecutionSettingsDTO();

        dto.setTitle(execution.getTitle());
        dto.setVisibility(execution.getVisibility());
        dto.setShowLines(execution.isShowLines());
        dto.setShowOriginalObjectiveValues(execution.isShowOriginalObjectiveValues());
        dto.setNormalizeId(execution.getNormalizeId());
        dto.setCorrelationId(execution.getCorrelationId());
        dto.setRemoverId(execution.getRemoverId());
        dto.setColor(execution.getColor());

        return dto;
    }

    public List<ExecutionSimplifiedDTO> findExecutionSimplifiedDTOByVisibility(Visibility visibility) {
        return executionRepository.findByVisibility(visibility);
    }

    public boolean isOwner(Execution execution) {

        if (execution == null || execution.getUserId() == null)
            return false;

        return isOwner(execution.getUserId());
    }

    public boolean isOwner(String userId) {

        User user = securityService.getLoggedUser().getUser();

        return userId.equalsIgnoreCase(user.getId());
    }
    
    public boolean isReadOnly(Execution execution) {
        
        if (isOwner(execution))
            return false;
        
        return !isOwner(execution) && execution.getVisibility() == Visibility.PUBLIC;
    }
    
    public Execution getParent(String executionId) {

        if (executionId == null) {
            return null;
        }

        Execution execution = executionRepository.findById(executionId).orElse(null);

        if (execution != null) {

            Execution parent = getParent(execution.getLastExecutionId());

            if (parent != null) {
                return parent;
            }
            
            return execution;
        }

        return null;
    }

    public Execution findById(String id) {
        return executionRepository.findById(id).orElse(null);
    }
}
