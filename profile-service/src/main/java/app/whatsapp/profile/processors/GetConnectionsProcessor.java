package app.whatsapp.profile.processors;

import app.whatsapp.common.enums.ECommonResponseCodes;
import app.whatsapp.common.models.ResponseStatus;
import app.whatsapp.common.processors.IRequestProcessor;
import app.whatsapp.commonweb.models.profile.response.ConnectionsResponse;
import app.whatsapp.profile.entities.User;
import app.whatsapp.profile.service.UserService;
import app.whatsapp.profile.utility.ModelMappingUtils;
import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GetConnectionsProcessor implements IRequestProcessor<User, User, List<User>, ConnectionsResponse> {

    private final UserService userService;

    public GetConnectionsProcessor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public User preProcess(User user) {
        return user;
    }

    @Override
    public List<User> onProcess(User user, User user2) {
        return userService.getConnections(user);
    }

    @Override
    public ConnectionsResponse postProcess(User request, User serviceRequest, List<User> serviceResponse) {
        ConnectionsResponse connectionsResponse = new ConnectionsResponse();
        connectionsResponse.setConnections(serviceResponse.stream().map(ModelMappingUtils::getUserProfile).collect(Collectors.toSet()));
        connectionsResponse.setResponseStatus(new ResponseStatus(ECommonResponseCodes.SUCCESS));
        return connectionsResponse;
    }
}
