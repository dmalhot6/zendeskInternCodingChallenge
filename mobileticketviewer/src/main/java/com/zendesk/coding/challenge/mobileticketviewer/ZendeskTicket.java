package com.zendesk.coding.challenge.mobileticketviewer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ZendeskTicket {

    List<TicketPojo> ticketPojos ;
    String beforeCursor;
    String afterCursor;
}
