package ch.wisv.payments.util;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LocalDateTimeAttributeConverter implements AttributeConverter<LocalDateTime, Object> {

    @Override
    public Object convertToDatabaseColumn(LocalDateTime locDateTime) {
        return locDateTime == null ? null : Timestamp.valueOf(locDateTime);
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Object sqlTimestamp) {
        if (sqlTimestamp != null) {
            try {
                byte[] dbBytes = (byte[]) sqlTimestamp;
                ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(dbBytes));
                LocalDateTime covertedDateFromBytes = (LocalDateTime) ois.readObject();
                ois.close();
                return covertedDateFromBytes;
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } catch (ClassNotFoundException cnfe) {
                cnfe.printStackTrace();
            }
        }
        return null;
    }
}