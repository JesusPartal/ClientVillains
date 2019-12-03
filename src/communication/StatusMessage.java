package communication;

import java.io.Serializable;

public class StatusMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    private StatusMessageType message;

    public StatusMessage(StatusMessageType message) {
        this.message = message;
    }

    public StatusMessageType getMessage() {
        return message;
    }

    public void setMessage(StatusMessageType message) {
        this.message = message;
    }
}