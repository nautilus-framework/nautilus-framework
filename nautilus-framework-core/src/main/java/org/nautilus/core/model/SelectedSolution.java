package org.nautilus.core.model;

import java.util.Date;

import com.google.common.base.Preconditions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SelectedSolution {

    private int solutionIndex;

    private Date selectionDate;

    public SelectedSolution(int solutionIndex, Date selectionDate) {

        Preconditions.checkArgument(solutionIndex >= 0, "The solution index must be >= 0");
        Preconditions.checkNotNull(selectionDate, "The selection date must not be null");

        this.solutionIndex = solutionIndex;
        this.selectionDate = selectionDate;
    }

    public SelectedSolution(int solutionIndex) {
        this(solutionIndex, new Date());
    }
}
