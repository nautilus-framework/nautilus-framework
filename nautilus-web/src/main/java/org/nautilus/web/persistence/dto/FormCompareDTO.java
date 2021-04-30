package org.nautilus.web.persistence.dto;

import javax.validation.constraints.NotBlank;

import org.nautilus.plugin.toy.extension.problem.ToyProblemExtension;

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
public class FormCompareDTO {
    
    @NotBlank
    private String problemId = new ToyProblemExtension().getId();
    
    @NotBlank
    private String instanceId = "1-to-020-w-005.txt";
}

