package app.whatsapp.common.models;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class BaseResponse {

    @NonNull
    private ResponseStatus responseStatus;

    public BaseResponse(IResponseCode iResponseCode) {
        responseStatus = new ResponseStatus(iResponseCode);
    }

}
