package app.whtsapp.clientapp.model;

import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class ValidateTokenRequest {

    @NotNull
    private String accessToken;

}
