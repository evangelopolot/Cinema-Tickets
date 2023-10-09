import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.TicketService;
import uk.gov.dwp.uc.pairtest.TicketServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TicketServiceTest {
    private TicketService testService;
    @Mock
    private TicketPaymentService paymentService;
    @Mock
    private SeatReservationService seatReservationService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        this.testService = new TicketServiceImpl(paymentService, seatReservationService);
    }

    @ParameterizedTest
    @CsvSource({"-1000000", "-1","0"})
    @DisplayName("Test purchaseTickets when given invalid IDs which are less than one, then an exception should be thrown")
    public void testPurchaseTicketsGivenInvalidIDsThrowException(long input){
        assertThrows(InvalidPurchaseException.class, () -> testService.purchaseTickets(input));
    }
    @Test
    @DisplayName("test purchaseTicket when given an invalid ticket type, throws exception")
    public void testPurchaseTicketThrowsAnExceptionWhenGivenInvalidTicketType(){
        assertThrows(InvalidPurchaseException.class, ()-> testService.purchaseTickets(2L,
                createTicketRequest(null, 1)));
    }

    @ParameterizedTest
    @CsvSource({"21", "-1", "0"})
    @DisplayName("Test purchaseTicket given number of tickets above the maximum or below 1, then an exception should be thrown")
    public void testPurchaseTicketsGivenValidIDAndInvalidTicketTypeThrowException(int input) {
        assertThrows(InvalidPurchaseException.class, () -> testService.purchaseTickets(1L,
                createTicketRequest(TicketTypeRequest.Type.ADULT,input)));
    }

    @ParameterizedTest
    @CsvSource({"'INFANT',2", "'CHILD', 2"})
    @DisplayName("Test purchaseTicket given an infant or child ticket with no adult ticket, then an exception should be thrown")
    public void testPurchaseTicketGivenInfantTicketAndNoAdultTicketThrowAnException(TicketTypeRequest.Type type, int numOfTickets){
        assertThrows(InvalidPurchaseException.class, () -> testService.purchaseTickets(1L,
                createTicketRequest(type, numOfTickets)));
    }

    @Test
    @DisplayName("Test purchaseTicket given a valid ticket calls makePayment with correct total cost")
    public void testPurchaseTicketGivenValidTicketRequestCallsMakePayment(){
        testService.purchaseTickets(1L,
                createTicketRequest(TicketTypeRequest.Type.ADULT,1),
                createTicketRequest(TicketTypeRequest.Type.CHILD, 2),
                createTicketRequest(TicketTypeRequest.Type.INFANT, 1));
        verify(paymentService, times(1)).makePayment(1L,40);
    }

    @Test
    @DisplayName("Test purchaseTicket when given valid ticket requests, it correctly reserves seats for only adult and child tickets")
    public void testPurchaseTicketOnlyReservesSeatsForAdultAndChildTickets(){
        testService.purchaseTickets(1L,
                createTicketRequest(TicketTypeRequest.Type.ADULT,1),
                createTicketRequest(TicketTypeRequest.Type.CHILD, 2),
                createTicketRequest(TicketTypeRequest.Type.INFANT, 1));
        verify(seatReservationService, times(1)).reserveSeat(1L,3);
    }

    private TicketTypeRequest createTicketRequest(TicketTypeRequest.Type type, int quantity) {
        return new TicketTypeRequest(type, quantity);
    }
}
