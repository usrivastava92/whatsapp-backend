package app.whatsapp.commonweb.models.gateway;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
public class Message implements Serializable {

    private final Long timestamp;
    @JsonProperty("from")
    private Long fromUserId;
    @JsonProperty("to")
    private Long toUserId;
    private String message;

    public Message() {
        this.timestamp = System.currentTimeMillis();
    }

}

