package app.whatsapp.gateway.service;

import app.whatsapp.commonweb.models.sessions.response.AddSessionResponse;
import app.whatsapp.commonweb.models.sessions.response.EvictSessionResponse;
import app.whatsapp.gateway.models.StompPrincipal;

public interface SessionRegistryService {

    AddSessionResponse registerSession(StompPrincipal stompPrincipal);
    EvictSessionResponse evictSession(StompPrincipal stompPrincipal);

}
