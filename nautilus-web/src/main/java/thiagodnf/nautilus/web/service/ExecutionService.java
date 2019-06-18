package thiagodnf.nautilus.web.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import thiagodnf.nautilus.web.dto.ExecutionSettingsDTO;
import thiagodnf.nautilus.web.dto.ExecutionSimplifiedDTO;
import thiagodnf.nautilus.web.exception.ExecutionNotFoundException;
import thiagodnf.nautilus.web.model.Execution;
import thiagodnf.nautilus.web.repository.ExecutionRepository;

@Service
public class ExecutionService {
	
	@Autowired
	private ExecutionRepository executionRepository;
	
	public ExecutionSimplifiedDTO duplicate(String userId, String executionId) {

		Execution execution = findExecutionById(executionId);

		execution.setId(null);
		execution.setUserId(userId);
		execution.setCreationDate(null);
		execution.setLastChangeDate(null);
		execution.setShowToAllUsers(false);
		
		Execution saved = save(execution);

		return convertToExecutionSimplifiedDTO(saved);
	}
	
	public void updateSettings(String executionId, ExecutionSettingsDTO executionSettingsDTO) {

		Execution execution = findExecutionById(executionId);

		execution.setTitle(executionSettingsDTO.getTitle());
		execution.setShowToAllUsers(executionSettingsDTO.isShowToAllUsers());
		execution.setShowLines(executionSettingsDTO.isShowLines());
		execution.setColorizeId(executionSettingsDTO.getColorizeId());
		execution.setNormalizeId(executionSettingsDTO.getNormalizeId());
		execution.setCorrelationId(executionSettingsDTO.getCorrelationId());
		execution.setDuplicatesRemoverId(executionSettingsDTO.getDuplicatesRemoverId());
		execution.setReducerId(executionSettingsDTO.getReducerId());

		save(execution);
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

	public List<ExecutionSimplifiedDTO> findByUserId(String userId) {

		List<ExecutionSimplifiedDTO> allExecutions = new ArrayList<>();

		allExecutions.addAll(executionRepository.findByUserId(userId));
		allExecutions.addAll(executionRepository.findByShowToAllUsers(true));

		Map<String, ExecutionSimplifiedDTO> executions = new HashMap<>();

		for (ExecutionSimplifiedDTO execution : allExecutions) {
			executions.put(execution.getId(), execution);
		}

		return executions.values().stream().collect(Collectors.toList());
	}
	
    public void deleteById(String executionId) {

        ExecutionSimplifiedDTO found = findExecutionSimplifiedDTOById(executionId);
        
        executionRepository.deleteById(found.getId());
    }
	
	public boolean existsById(String executionId) {
		return executionRepository.existsById(executionId);
	}

	public ExecutionSimplifiedDTO convertToExecutionSimplifiedDTO(Execution execution) {
		
		if(execution == null) return null;
		
		return new ExecutionSimplifiedDTO(
			execution.getId(),
			execution.getTitle(),
			execution.getProblemId(),
			execution.getInstance(),
			execution.isShowToAllUsers(),
			execution.getCreationDate()
		);
	}
	
	public ExecutionSettingsDTO convertToExecutionSettingsDTO(Execution execution) {
		
		if(execution == null) return null;
		
		return new ExecutionSettingsDTO(
				execution.getTitle(), 
				execution.isShowToAllUsers(), 
				execution.isShowLines(), 
				execution.getColorizeId(), 
				execution.getNormalizeId(), 
				execution.getCorrelationId(), 
				execution.getDuplicatesRemoverId(), 
				execution.getReducerId()
		);
	}
}
