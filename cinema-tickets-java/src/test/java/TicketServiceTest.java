import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import uk.gov.dwp.uc.pairtest.TicketService;
import uk.gov.dwp.uc.pairtest.TicketServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TicketServiceTest {
    private TicketService testService;
    private TicketTypeRequest typeRequest;

    @BeforeEach
    public void setUp(){
        this.testService = new TicketServiceImpl();
    }


    @ParameterizedTest
    @CsvSource({"-1000000", "-1","0"})
    @DisplayName("Test purchaseTickets when given invalid IDs which are less than zero, then an exception should be thrown")
    public void testPurchaseTicketsGivenInvalidIDsThrowException(long input){
        assertThrows(InvalidPurchaseException.class, () -> testService.purchaseTickets(input));
    }

    @Test
    @DisplayName("Test purchaseTicket given number of tickets above the maximum, then an exception should be thrown")
    public void testPurchaseTicketsGivenValidIDAndInvalidTicketTypeThrowException() {
        typeRequest = new TicketTypeRequest(TicketTypeRequest.Type.ADULT,21);
        assertThrows(InvalidPurchaseException.class, () -> testService.purchaseTickets(1L, typeRequest));
    }

}
