package org.nautilus.web.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nautilus.core.encoding.NSolution;
import org.nautilus.core.model.SelectedSolution;
import org.nautilus.core.reduction.AbstractReduction.ItemForEvaluation;
import org.nautilus.core.util.Converter;
import org.nautilus.plugin.extension.correlation.PearsonCorrelationExtension;
import org.nautilus.plugin.extension.normalizer.ByMaxAndMinValuesNormalizerExtension;
import org.nautilus.plugin.extension.remover.ObjectivesRemoverExtension;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
	
	private boolean showOriginalObjectiveValues = false;
	
	private String color = "#7cb5ec";
	
    private String normalizeId = new ByMaxAndMinValuesNormalizerExtension().getId();

    private String correlationId = new PearsonCorrelationExtension().getId();

    private String removerId = new ObjectivesRemoverExtension().getId();

    private List<ItemForEvaluation> itemForEvaluations = new ArrayList<>();
    
    private List<SelectedSolution> selectedSolutions = new ArrayList<>();
    
    private Map<String, Object> attributes = new HashMap<>();
    
    @CreatedDate
	private Date creationDate;
	
	@LastModifiedDate
	private Date lastChangeDate;
	
	public String toString() {
		return Converter.toJson(this);
	}
}
