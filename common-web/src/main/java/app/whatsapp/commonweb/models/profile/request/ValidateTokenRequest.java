package app.whatsapp.commonweb.models.profile.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ValidateTokenRequest {

    @NotNull
    private String accessToken;

}
