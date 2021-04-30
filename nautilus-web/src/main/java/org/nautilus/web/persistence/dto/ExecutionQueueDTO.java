package org.nautilus.web.persistence.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ExecutionQueueDTO {

    private String id;
    
    private String userId;
    
    private String title;
    
    private Date creationDate;
    
    private double progress;
    
    private String status;
}
