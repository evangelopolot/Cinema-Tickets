package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import java.util.HashMap;

public class TicketServiceImpl implements TicketService {
    /**
     * Should only have private methods other than the one below.
     */

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        if(accountId <= 0){
            throw new InvalidPurchaseException();
        }

        boolean adultTicket = false;
        for (TicketTypeRequest request : ticketTypeRequests) {
            int quantity = request.getNoOfTickets();
            if(quantity > 20 || quantity < 0){
                throw new InvalidPurchaseException();
            }
            if (request.getTicketType() == TicketTypeRequest.Type.ADULT){
                adultTicket = true;
            }
        }

        if(!adultTicket){
            throw new InvalidPurchaseException();
        }

    }

}
