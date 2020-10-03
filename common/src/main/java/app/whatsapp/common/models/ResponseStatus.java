package app.whatsapp.common.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseStatus {

    private String code;
    private String message;
    private String status;

    public ResponseStatus(IResponseCode responseCodes){
        this.code = responseCodes.getCode();
        this.message = responseCodes.getMessage();
        this.status = responseCodes.getStatus();
    }
}
