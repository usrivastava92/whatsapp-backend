package app.whatsapp.commonweb.models.sessions.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddSessionRequest implements Serializable {

    private static final long serialVersionUID = 8850794019938454245L;

    @NotEmpty
    private String userId;
    @NotEmpty
    private String routingKey;

}
