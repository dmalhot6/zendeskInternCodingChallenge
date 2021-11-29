package com.zendesk.coding.challenge.mobileticketviewer;

public interface MobileTicketService {

    ZendeskTicket fetchAllTicketsFromZendeskAPI(String beforeCursor, String afterCursor);

    TicketPojo fetchTicketByIdFromZendeskAPI(String ticketID);
}
