package org.nautilus.web.dto;

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
public class FormCompareDTO {
    
    @NotBlank
    private String problemId;
    
    @NotBlank
    private String instanceId = "1-to-020-w-005.txt";
}

