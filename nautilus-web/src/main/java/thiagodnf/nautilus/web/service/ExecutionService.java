package thiagodnf.nautilus.web.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        
        if (!isOwner(execution)) {
            throw new UserNotOwnerException();
        }
        
        execution.setTitle(executionSettingsDTO.getTitle());
        execution.setVisibility(executionSettingsDTO.getVisibility());
        execution.setShowLines(executionSettingsDTO.isShowLines());
        execution.setNormalizeId(executionSettingsDTO.getNormalizeId());
        execution.setCorrelationId(executionSettingsDTO.getCorrelationId());
        execution.setRemoverId(executionSettingsDTO.getRemoverId());
        execution.setColor(executionSettingsDTO.getColor());

        save(execution);
    }

    public boolean contains(String executionId) {
        return this.executionRepository.existsById(executionId);
    }

    public Execution save(Execution execution) {
        return this.executionRepository.save(execution);
    }

    public ExecutionSimplifiedDTO findExecutionSimplifiedDTOById(String executionId) {
        return this.executionRepository.findExecutionSimplifiedDTOById(executionId)
                .orElseThrow(ExecutionNotFoundException::new);
    }

    public Execution findExecutionById(String executionId) {
        return this.executionRepository.findById(executionId).orElseThrow(ExecutionNotFoundException::new);
    }

    public List<ExecutionSimplifiedDTO> findExecutionSimplifiedDTOByUserId(String userId) {
        return executionRepository.findByUserIdAndSolutionsNotNull(userId);
    }

    public void deleteById(String executionId) {

        if (!contains(executionId)) {
            throw new ExecutionNotFoundException();
        }

        this.executionRepository.deleteById(executionId);
    }

    public ExecutionSimplifiedDTO convertToExecutionSimplifiedDTO(Execution execution) {

        if (execution == null)
            return null;

        return new ExecutionSimplifiedDTO(
            execution.getId(), 
            execution.getTitle(), 
            execution.getProblemId(),
            execution.getInstance(),
            execution.getCreationDate()
        );
    }

    public ExecutionSettingsDTO convertToExecutionSettingsDTO(Execution execution) {

        if (execution == null)
            return null;

        ExecutionSettingsDTO dto = new ExecutionSettingsDTO();

        dto.setTitle(execution.getTitle());
        dto.setVisibility(execution.getVisibility());
        dto.setShowLines(execution.isShowLines());
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
        
        if (isOwner(execution)) {
            return false;
        }
        
        return !isOwner(execution) && execution.getVisibility() == Visibility.PUBLIC;
    }
}
