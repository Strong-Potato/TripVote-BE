package fc.be.tourapi.exception;

import jakarta.xml.bind.JAXBException;

@Deprecated(forRemoval = true)
public class WrongXMLFormatException extends RuntimeException {
    public WrongXMLFormatException(JAXBException e) {
        super(e);
    }
}
