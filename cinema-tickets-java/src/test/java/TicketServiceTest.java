import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import thirdparty.paymentgateway.TicketPaymentService;
import uk.gov.dwp.uc.pairtest.TicketService;
import uk.gov.dwp.uc.pairtest.TicketServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TicketServiceTest {
    private TicketService testService;
    private TicketTypeRequest typeRequest;
    @Mock
    private TicketPaymentService paymentService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        this.testService = new TicketServiceImpl(paymentService);
    }

    @ParameterizedTest
    @CsvSource({"-1000000", "-1","0"})
    @DisplayName("Test purchaseTickets when given invalid IDs which are less than zero, then an exception should be thrown")
    public void testPurchaseTicketsGivenInvalidIDsThrowException(long input){
        assertThrows(InvalidPurchaseException.class, () -> testService.purchaseTickets(input));
    }

    @ParameterizedTest
    @CsvSource({"21", "-1"})
    @DisplayName("Test purchaseTicket given number of tickets above the maximum or below zero, then an exception should be thrown")
    public void testPurchaseTicketsGivenValidIDAndInvalidTicketTypeThrowException(int input) {
        typeRequest = new TicketTypeRequest(TicketTypeRequest.Type.ADULT,input);
        assertThrows(InvalidPurchaseException.class, () -> testService.purchaseTickets(1L, typeRequest));
    }

    @ParameterizedTest
    @CsvSource({"'INFANT',2", "'CHILD', 2"})
    @DisplayName("Test purchaseTicket given an infant or child ticket with no adult ticket, then an exception should be thrown")
    public void testPurchaseTicketGivenInfantTicketAndNoAdultTicketThrowAnException(TicketTypeRequest.Type type, int numOfTickets){
        typeRequest = new TicketTypeRequest(type, numOfTickets);
        assertThrows(InvalidPurchaseException.class, () -> testService.purchaseTickets(1L, typeRequest));
    }

    @Test
    @DisplayName("Test purchaseTicket given a valid ticket calls makePayment")
    public void testPurchaseTicketGivenValidTicketRequestCallsMakePayment(){
        typeRequest = new TicketTypeRequest(TicketTypeRequest.Type.ADULT,1);
        TicketTypeRequest typeRequest2 = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 2);
        TicketTypeRequest typeRequest3 = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1);
        testService.purchaseTickets(1L,typeRequest,typeRequest2);
        verify(paymentService, times(1)).makePayment(1L,40);
    }



}
