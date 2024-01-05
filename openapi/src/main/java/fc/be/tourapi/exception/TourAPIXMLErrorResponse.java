package fc.be.tourapi.exception;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;

@Getter
@XmlRootElement(name = "OpenAPI_ServiceResponse")
public class TourAPIXMLErrorResponse {

    @XmlElement(name = "cmmMsgHeader")
    private ErrorHeader errorHeader;

    @Getter
    public static class ErrorHeader {

        @XmlElement(name = "errMsg")
        private String errMsg;

        @XmlElement(name = "returnAuthMsg")
        private String returnAuthMsg;

        @XmlElement(name = "returnReasonCode")
        private String returnReasonCode;

    }
}