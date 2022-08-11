package recipes.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Recipe {

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(min = 1)
    private String description;

    @NotNull
    @Size(min = 1)
    private List<String> ingredients;

    @NotNull
    @Size(min = 1)
    private List<String> directions;

    @NotBlank
    @Size(max = 100)
    private String category;

    @NotBlank
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime updateTime;
}
