package app.whatsapp.common.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse {

    private ResponseStatus responseStatus;

    public BaseResponse(IResponseCode iResponseCode) {
        responseStatus = new ResponseStatus(iResponseCode);
    }
}
