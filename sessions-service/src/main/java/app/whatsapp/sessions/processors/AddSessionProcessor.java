package app.whatsapp.sessions.processors;

import app.whatsapp.common.enums.ECommonResponseCodes;
import app.whatsapp.common.models.ResponseStatus;
import app.whatsapp.common.processors.IRequestProcessor;
import app.whatsapp.commonweb.models.sessions.request.AddSessionRequest;
import app.whatsapp.commonweb.models.sessions.response.AddSessionResponse;
import app.whatsapp.commonweb.services.CacheService;
import app.whatsapp.sessions.constants.SessionServiceConstants;
import org.springframework.stereotype.Component;

@Component
public class AddSessionProcessor implements IRequestProcessor<AddSessionRequest, AddSessionRequest, AddSessionResponse, AddSessionResponse> {

    private CacheService cacheService;

    public AddSessionProcessor(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Override
    public AddSessionRequest preProcess(AddSessionRequest addSessionRequest) {
        return addSessionRequest;
    }

    @Override
    public AddSessionResponse onProcess(AddSessionRequest request, AddSessionRequest serviceRequest) {
        cacheService.hSet(SessionServiceConstants.USER_GATEWAY_MAPPING_KEY, request.getUserId(), request.getRoutingKey());
        return new AddSessionResponse();
    }

    @Override
    public AddSessionResponse postProcess(AddSessionRequest request, AddSessionRequest serviceRequest, AddSessionResponse serviceResponse) {
        serviceResponse.setResponseStatus(new ResponseStatus(ECommonResponseCodes.SUCCESS));
        return serviceResponse;
    }
}
