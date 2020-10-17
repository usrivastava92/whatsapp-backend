package app.whatsapp.sessions.processors;

import app.whatsapp.common.enums.ECommonResponseCodes;
import app.whatsapp.common.models.ResponseStatus;
import app.whatsapp.common.processors.IRequestProcessor;
import app.whatsapp.commonweb.models.sessions.request.EvictSessionRequest;
import app.whatsapp.commonweb.models.sessions.response.EvictSessionResponse;
import app.whatsapp.commonweb.services.CacheService;
import app.whatsapp.sessions.constants.SessionServiceConstants;
import org.springframework.stereotype.Component;

@Component
public class EvictSessionProcessor implements IRequestProcessor<EvictSessionRequest, EvictSessionRequest, EvictSessionResponse, EvictSessionResponse> {

    private CacheService cacheService;

    public EvictSessionProcessor(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Override
    public EvictSessionRequest preProcess(EvictSessionRequest addSessionRequest) {
        return addSessionRequest;
    }

    @Override
    public EvictSessionResponse onProcess(EvictSessionRequest request, EvictSessionRequest serviceRequest) {
        cacheService.hDel(SessionServiceConstants.USER_GATEWAY_MAPPING_KEY, request.getUserId());
        return new EvictSessionResponse();
    }

    @Override
    public EvictSessionResponse postProcess(EvictSessionRequest request, EvictSessionRequest serviceRequest, EvictSessionResponse serviceResponse) {
        serviceResponse.setResponseStatus(new ResponseStatus(ECommonResponseCodes.SUCCESS));
        return serviceResponse;
    }
}
