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

        for (TicketTypeRequest request : ticketTypeRequests) {
            int quantity = request.getNoOfTickets();
            if(quantity > 20){
                throw new InvalidPurchaseException();
            }
        }
    }

}
