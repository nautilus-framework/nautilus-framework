package thiagodnf.nautilus.web.dto;

import java.util.List;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import thiagodnf.nautilus.core.colorize.DontColorize;
import thiagodnf.nautilus.core.correlation.DontCorrelation;
import thiagodnf.nautilus.core.duplicated.ByObjectivesDuplicatesRemover;
import thiagodnf.nautilus.core.normalize.ByParetoFrontValuesNormalize;
import thiagodnf.nautilus.core.reduction.ConfidenceBasedReduction;
import thiagodnf.nautilus.plugin.extension.algorithm.NSGAIIAlgorithmExtension;

@Getter
@Setter
@NoArgsConstructor
public class ParametersDTO {

	@NotBlank
	private String userId;
	
	@NotBlank
	private String problemId;

	@NotBlank
	private String instance;
	
	@DecimalMin("100")
	@DecimalMax("1000")
	private int populationSize = 100;
	
	@DecimalMin("100")
	@DecimalMax("10000000")
	private int maxEvaluations = 100000;
	
	@NotBlank
	private String algorithmId = new NSGAIIAlgorithmExtension().getId();
	
	@NotBlank
	private String selectionId;
	
	@NotBlank
	private String crossoverId;
	
	@DecimalMin("0.0")
	@DecimalMax("1.0")
	private Double crossoverProbability = 0.9;
	
	@DecimalMin("0.0")
	@DecimalMax("50.0")
	private Double crossoverDistribution = 20.0;
	
	@NotBlank
	private String mutationId;
	
	@DecimalMin("0.0")
	@DecimalMax("1.0")
	private Double mutationProbability = 0.005;
	
	@DecimalMin("0.0")
	@DecimalMax("50.0")
	private Double mutationDistribution = 20.0;
	
	@DecimalMin("0.0")
	@DecimalMax("1.0")
	private Double epsilon = 0.001;
	
	private List<List<Double>> referencePoints;
	
	@NotNull
	@Size(min = 1)
	private List<String> objectiveIds;
	
	private String lastExecutionId;
	
	private boolean showToAllUsers = false;
    
    private boolean showLines = true;
    
    private String colorizeId = new DontColorize().getId();

    private String normalizeId = new ByParetoFrontValuesNormalize().getId();

    private String correlationId = new DontCorrelation().getId();

    private String duplicatesRemoverId = new ByObjectivesDuplicatesRemover().getId();

    private String reducerId = new ConfidenceBasedReduction().getId();
	
	public ParametersDTO(String userId, String problemId, String instance) {
		this.userId = userId;
		this.problemId = problemId;
		this.instance = instance;
	}
}
