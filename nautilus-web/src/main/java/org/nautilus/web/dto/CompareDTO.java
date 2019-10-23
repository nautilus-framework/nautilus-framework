package org.nautilus.web.dto;

import java.util.List;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CompareDTO {
    
    @NotNull
    @Size(min = 1)
    private List<String> objectiveIds;
    
    @NotNull
    @Size(min = 1)
    private List<String> executionIds;
    
    @DecimalMin("0.0")
    @DecimalMax("1.0")
    private double delta = 0.3;
    
    private boolean filterBySelectedSolutions = true;
    
    private boolean restrictedRP = true;
}

