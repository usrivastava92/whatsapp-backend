package app.whatsapp.commonweb.models.sessions.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddSessionRequest {

    @NotNull
    private Long userId;
    @NotEmpty
    private String host;

}
