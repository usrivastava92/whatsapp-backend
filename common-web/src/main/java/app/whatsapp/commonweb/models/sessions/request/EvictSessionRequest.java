package app.whatsapp.commonweb.models.sessions.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class EvictSessionRequest {

    @NotNull
    private Long userId;

}
