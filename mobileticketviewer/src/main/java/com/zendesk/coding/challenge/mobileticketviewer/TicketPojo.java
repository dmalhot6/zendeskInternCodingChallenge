package com.zendesk.coding.challenge.mobileticketviewer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class TicketPojo {

    int id;
    String status;
    String subject;
    String description;
    Date dateTime;
}
