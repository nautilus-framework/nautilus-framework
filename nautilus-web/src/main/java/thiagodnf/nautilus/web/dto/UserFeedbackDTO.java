package thiagodnf.nautilus.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserFeedbackDTO {

    private int objectiveIndex;
    
    private double feedback;
    
    public UserFeedbackDTO(int objectiveIndex) {
        this.objectiveIndex = objectiveIndex;
    }
}
