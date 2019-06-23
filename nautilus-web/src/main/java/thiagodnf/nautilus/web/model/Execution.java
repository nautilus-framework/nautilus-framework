package thiagodnf.nautilus.web.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import thiagodnf.nautilus.core.encoding.NSolution;
import thiagodnf.nautilus.core.reduction.AbstractReduction.ItemForEvaluation;
import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.plugin.extension.correlation.PearsonCorrelationExtension;
import thiagodnf.nautilus.plugin.extension.normalizer.ByParetoFrontValuesNormalizerExtension;
import thiagodnf.nautilus.plugin.extension.remover.ObjectivesRemoverExtension;

@Document
@Getter
@Setter
@NoArgsConstructor
public class Execution {

    public enum Visibility {
        PUBLIC, 
        PRIVATE
    }
    
	@Id
	private String id;
	
	private long executionTime;
	
	private List<NSolution<?>> solutions;
	
	private String lastExecutionId;
	
	private String title;
	
	private String userId;
	
	private String problemId;

	private String instance;
	
	private int populationSize;
	
	private int maxEvaluations;
	
	private String algorithmId;
	
	private String selectionId;
	
	private String crossoverId;
	
	private Double crossoverProbability;
	
	private Double crossoverDistribution;
	
	private String mutationId;
	
	private Double mutationProbability;
	
	private Double mutationDistribution;
	
	private Double epsilon;
	
	private List<List<Double>> referencePoints;
	
	private List<String> objectiveIds;
	
	private Visibility visibility = Visibility.PRIVATE;
	
	private boolean showLines = true;
	
	private String color = "#7cb5ec";
	
    private String normalizeId = new ByParetoFrontValuesNormalizerExtension().getId();

    private String correlationId = new PearsonCorrelationExtension().getId();

    private String removerId = new ObjectivesRemoverExtension().getId();

    private List<ItemForEvaluation> itemForEvaluations = new ArrayList<>();
    
	@CreatedDate
	private Date creationDate;
	
	@LastModifiedDate
	private Date lastChangeDate;
	
	public String toString() {
		return Converter.toJson(this);
	}
}
