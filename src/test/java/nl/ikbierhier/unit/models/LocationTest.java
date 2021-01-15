package nl.ikbierhier.unit.models;

import nl.ikbierhier.models.Location;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LocationTest {
    @Test
    public void test_isExpired_expiresAtBeforeCurrentTime_returnsTrue() {
        Calendar pastCalendar = Calendar.getInstance();
        pastCalendar.add(Calendar.MINUTE, -10);
        Timestamp past = new Timestamp(pastCalendar.getTime().getTime());

        Location location = new Location(null, null, 12, 12, past);

        boolean result = location.isExpired();

        assertTrue(result);
    }
    @Test
    public void test_isExpired_expiresAtAfterCurrentTime_returnsFalse() {
        Calendar pastCalendar = Calendar.getInstance();
        pastCalendar.add(Calendar.MINUTE, 10);
        Timestamp future = new Timestamp(pastCalendar.getTime().getTime());

        Location location = new Location(null, null, 12, 12, future);

        boolean result = location.isExpired();

        assertFalse(result);
    }
}
