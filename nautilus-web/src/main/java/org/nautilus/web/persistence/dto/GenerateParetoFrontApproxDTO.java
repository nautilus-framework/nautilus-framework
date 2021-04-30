package org.nautilus.web.persistence.dto;

import javax.validation.constraints.NotBlank;

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
public class GenerateParetoFrontApproxDTO {
    
    @NotBlank
    private String problemId;
    
    @NotBlank
    private String instanceId;
}

