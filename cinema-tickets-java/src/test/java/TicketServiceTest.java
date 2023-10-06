import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.gov.dwp.uc.pairtest.TicketService;
import uk.gov.dwp.uc.pairtest.TicketServiceImpl;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TicketServiceTest {
    private TicketService testService;

    @BeforeEach
    public void setUp(){
        this.testService = new TicketServiceImpl();
    }


    @Test
    @DisplayName("Test given invalid ID the purchase request should be rejected by throwing an exception")
    public void testGivenInvalidIDThrowException(){
        assertThrows(InvalidPurchaseException.class, () -> testService.purchaseTickets(-1L));
    }
}
