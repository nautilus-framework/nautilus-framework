package org.nautilus.web.persistence.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.nautilus.plugin.extension.algorithm.NSGAIIAlgorithmExtension;
import org.nautilus.plugin.extension.correlation.PearsonCorrelationExtension;
import org.nautilus.plugin.extension.normalizer.ByParetoFrontValuesNormalizerExtension;
import org.nautilus.plugin.extension.remover.ObjectivesRemoverExtension;
import org.nautilus.web.annotation.ContainsNotBlank;
import org.nautilus.web.annotation.HTMLAutoComplete;
import org.nautilus.web.annotation.HTMLAutoFocus;
import org.nautilus.web.annotation.HTMLReadonly;
import org.nautilus.web.annotation.HTMLRequired;
import org.nautilus.web.annotation.HTMLSpellCheck;
import org.nautilus.web.annotation.HTMLStep;
import org.nautilus.web.persistence.model.Execution.Visibility;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ParametersDTO {

	@NotBlank
	@HTMLReadonly
	private String problemId;

	@NotBlank
	@HTMLReadonly
	private String instance;
	
	@Min(100)
    @Max(1000)
	@HTMLAutoFocus
    @HTMLStep(1)
    @HTMLRequired
    @HTMLAutoComplete("off")
	@HTMLSpellCheck("false")
	private int populationSize = 100;
	
	@Min(100)
	@Max(100000000)
	@HTMLStep(100)
	@HTMLRequired
    @HTMLAutoComplete("off")
	@HTMLSpellCheck("false")
	private int maxEvaluations = 100000;
	

    @Min(1)
    @Max(1000)
    @HTMLStep(1)
    @HTMLRequired
    @HTMLAutoComplete("off")
    @HTMLSpellCheck("false")
    private int numberOfRuns = 1;
	
	@NotBlank
	private String algorithmId = new NSGAIIAlgorithmExtension().getId();
	
	@NotBlank
	private String selectionId;
	
	@NotBlank
	private String crossoverId;
	
	@DecimalMin("0.0")
	@DecimalMax("1.0")
	@HTMLStep(0.01)
    @HTMLRequired
    @HTMLAutoComplete("off")
    @HTMLSpellCheck("false")
	private double crossoverProbability = 0.9;
	
	@DecimalMin("0.0")
	@DecimalMax("50.0")
	@HTMLStep(1.0)
	@HTMLRequired
    @HTMLAutoComplete("off")
    @HTMLSpellCheck("false")
	private double crossoverDistribution = 20.0;
	
	@NotBlank
	private String mutationId;
	
	@DecimalMin("0.0")
	@DecimalMax("1.0")
	@HTMLStep(0.001)
    @HTMLRequired
    @HTMLAutoComplete("off")
    @HTMLSpellCheck("false")
	private double mutationProbability = 0.005;
	
	@DecimalMin("0.0")
	@DecimalMax("50.0")
	@HTMLStep(1.0)
    @HTMLRequired
    @HTMLAutoComplete("off")
    @HTMLSpellCheck("false")
	private double mutationDistribution = 20.0;
	
	@DecimalMin("0.0")
	@DecimalMax("1.0")
	@HTMLStep(0.00001)
    @HTMLRequired
    @HTMLAutoComplete("off")
    @HTMLSpellCheck("false")
	private double epsilon = 0.001;
	
	@NotNull
	@Size(min = 1)
	@ContainsNotBlank
	private List<List<Double>> referencePoints = new ArrayList<>();
	
	@NotNull
	@Size(min = 1)
	private List<String> objectiveIds;
	
	private String lastExecutionId;
	
	private Visibility visibility = Visibility.PRIVATE;
    
    private boolean showLines = true;
    
    private String normalizeId = new ByParetoFrontValuesNormalizerExtension().getId();

    private String correlationId = new PearsonCorrelationExtension().getId();

    private String removerId = new ObjectivesRemoverExtension().getId();
    
    public String getTitle() {
        return problemId + "-" + instance + "-" + algorithmId;
    }
}
