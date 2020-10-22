package app.whatsapp.commonweb.models.gateway;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Message implements Serializable {

    private String timestamp;
    @JsonProperty("from")
    private String fromUserId;
    @JsonProperty("to")
    private String toUserId;
    private String message;

}

