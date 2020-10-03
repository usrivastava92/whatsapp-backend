package app.whtsapp.clientapp.processors;

import app.whtsapp.clientapp.model.LoginRequest;
import app.whtsapp.clientapp.service.ProfileServiceClient;
import app.whatsapp.common.processors.IRequestProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StartChattingProcessor implements IRequestProcessor<LoginRequest, LoginRequest, Boolean, Boolean> {

    @Autowired
    private ProfileServiceClient profileServiceClient;

    @Override
    public LoginRequest preProcess(LoginRequest request) {
        return request;
    }

    @Override
    public Boolean onProcess(LoginRequest request, LoginRequest serviceRequest) {
        profileServiceClient.login(serviceRequest);
        return true;
    }

    @Override
    public Boolean postProcess(LoginRequest LoginRequestPayload, LoginRequest LoginRequestPayload2, Boolean aBoolean) {
        return true;
    }
}
