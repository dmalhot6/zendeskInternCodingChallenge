package com.zendesk.coding.challenge.mobileticketviewer;

public interface MobileTicketService {

    ZendeskTicket fetchAllTicketsFromZendeskAPI(String beforeCursor, String afterCursor) throws ResponseNotFoundException;

    TicketPojo fetchTicketByIdFromZendeskAPI(String ticketID) throws ResponseNotFoundException;
}
