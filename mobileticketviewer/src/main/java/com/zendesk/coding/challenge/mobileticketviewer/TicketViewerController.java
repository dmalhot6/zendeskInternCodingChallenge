package com.zendesk.coding.challenge.mobileticketviewer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
public class TicketViewerController {

    @Autowired
    private MobileTicketService mobileTicketService;

    @RequestMapping(value = "/getAllTickets", method = RequestMethod.GET)
    public void getAllTickets(@RequestParam(required = false) String beforeCursor, @RequestParam(required = false)
            String afterCursor, Model model) throws JsonProcessingException {
        try {
            ZendeskTicket zendeskTicket = mobileTicketService.fetchAllTicketsFromZendeskAPI(beforeCursor, afterCursor);
            model.addAttribute("zendesktickets", zendeskTicket);
        } catch (HttpClientErrorException e) {

            applicationError error = new applicationError();
            error.setErrorMsg("The credentials of the user trying to access are INVALID");
            model.addAttribute("errorApp", error);
        }
    }

    @RequestMapping(value = "/getTicketByID", method = RequestMethod.GET)
    public void getTicketByID(@RequestParam(value = "ticketID") String ticketID, Model model)
            throws JsonProcessingException {
        try {
            TicketPojo ticketPojo = mobileTicketService.fetchTicketByIdFromZendeskAPI(ticketID);
            model.addAttribute("ticketname", ticketPojo);
        }catch (HttpClientErrorException e) {

            applicationError error = new applicationError();
            error.setErrorMsg("The credentials of the user trying to access are INVALID");
            model.addAttribute("errorApp", error);
        }
    }
}


