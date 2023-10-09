package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.paymentgateway.TicketPaymentServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import java.util.HashMap;

public class TicketServiceImpl implements TicketService {
    public static final int MAXIMUM_NUMBER_OF_TICKETS = 20;
    public static final int CHILD_TICKET_PRICE = 10;
    public static final int ADULT_TICKET_PRICE = 20;
    /**
     * Should only have private methods other than the one below.
     */
    TicketPaymentService paymentService;
    public TicketServiceImpl(TicketPaymentService paymentService){
        this.paymentService = paymentService;
    }

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        int totalCost = 0;
        if(accountId <= 0){
            throw new InvalidPurchaseException();
        }

        boolean adultTicket = false;
        for (TicketTypeRequest request : ticketTypeRequests) {
            int quantity = request.getNoOfTickets();
            if(quantity > MAXIMUM_NUMBER_OF_TICKETS || quantity < 0){
                throw new InvalidPurchaseException();
            }
            if (request.getTicketType() == TicketTypeRequest.Type.ADULT){
                adultTicket = true;
            }
            totalCost += calculateTicketCost(request.getTicketType(),quantity);
        }

        if(!adultTicket){
            throw new InvalidPurchaseException();
        }  else {
            paymentService.makePayment(accountId,totalCost);
        }
    }

    private int calculateTicketCost(TicketTypeRequest.Type type, int numOfTickets){
        switch (type){
            case INFANT:
                return 0;
            case CHILD:
                return numOfTickets * CHILD_TICKET_PRICE;
            case ADULT:
                return numOfTickets * ADULT_TICKET_PRICE;
            default:
                throw new IllegalArgumentException();
        }
    }

}
