package app.whatsapp.gateway.service;

import app.whatsapp.commonweb.models.sessions.response.AddSessionResponse;
import app.whatsapp.commonweb.models.sessions.response.EvictSessionResponse;
import app.whatsapp.gateway.models.StompPrincipal;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
public interface SessionRegistryService {

    AddSessionResponse registerSession(@Valid @NotNull StompPrincipal stompPrincipal);
    EvictSessionResponse evictSession(@Valid @NotNull StompPrincipal stompPrincipal);

}
