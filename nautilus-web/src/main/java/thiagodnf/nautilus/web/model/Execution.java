package thiagodnf.nautilus.web.model;

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
import thiagodnf.nautilus.core.util.Converter;

@Document
@Getter
@Setter
@NoArgsConstructor
public class Execution {

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
	
	private boolean showToAllUsers;
	
	private boolean showLines;
	
	private String colorizeId;
	
	private String normalizeId;
	
	private String correlationId;
	
	private String duplicatesRemoverId;
	
	private String reducerId;
	
	@CreatedDate
	private Date creationDate;
	
	@LastModifiedDate
	private Date lastChangeDate;
	
	public String toString() {
		return Converter.toJson(this);
	}
}
