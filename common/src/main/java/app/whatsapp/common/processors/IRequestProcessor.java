package app.whatsapp.common.processors;

import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;

@Validated
public interface IRequestProcessor<Request, ServiceRequest, ServiceResponse, Response> {

    default Response process(@Valid Request request) {
        ServiceRequest serviceRequest = preProcess(request);
        ServiceResponse serviceResponse = onProcess(request, serviceRequest);
        Response response = postProcess(request, serviceRequest, serviceResponse);
        return response;
    }

    ServiceRequest preProcess(Request request);

    ServiceResponse onProcess(Request request, ServiceRequest serviceRequest);

    Response postProcess(Request request, ServiceRequest serviceRequest, ServiceResponse serviceResponse);

}
