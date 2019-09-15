package thiagodnf.nautilus.web.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import thiagodnf.nautilus.web.annotation.HTMLAutoFocus;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CompareDTO {

    @NotBlank
    @HTMLAutoFocus
    private String problemId;
    
    @NotNull
    @Size(min = 1)
    private List<String> executionIds;
}

