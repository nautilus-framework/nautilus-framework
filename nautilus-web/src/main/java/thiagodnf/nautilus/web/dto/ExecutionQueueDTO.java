package thiagodnf.nautilus.web.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import thiagodnf.nautilus.web.model.Execution;

@Setter
@Getter
@AllArgsConstructor
public class ExecutionQueueDTO {

    private String id;
    
    private String userId;
    
    private Date creationDate;
    
    private double progress;
    
    private String status;
    
    public ExecutionQueueDTO(Execution execution) {
        this.id = execution.getId();
        this.userId = execution.getUserId();
        this.creationDate = execution.getCreationDate();
        this.progress = 0.0;
    }
}
