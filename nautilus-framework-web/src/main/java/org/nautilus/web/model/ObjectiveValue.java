package org.nautilus.web.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObjectiveValue {

    private String problemId;

    private String instance;
    
    private List<String> objectiveIds = new ArrayList<>();

    private List<String> variables;

    private List<Double> objectiveValues = new ArrayList<>();

    private List<Double> normalizedObjectiveValues = new ArrayList<>();
}
