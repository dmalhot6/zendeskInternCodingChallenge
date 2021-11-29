package com.zendesk.coding.challenge.mobileticketviewer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@PropertySource("file:src/main/resources/application.properties")
@Service("MobileTicketService")
public class MobileTicketServiceImpl implements MobileTicketService {

    public static final String zendeskTicketsAPIUrl = "https://zccasu.zendesk.com/api/v2/tickets";
    public static final String beforeIdentifier = "page[before]=";
    public static final String afterIdentifier = "page[after]=";
    public static final String pageSizeSpecifier = "&page[size]=25";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Environment env;

    @Override
    public ZendeskTicket fetchAllTicketsFromZendeskAPI(String beforeCursor, String afterCursor) {
        String fooResourceUrl;
        if (beforeCursor != null) {
            beforeCursor = beforeCursor.substring(1);
            beforeCursor = beforeCursor.substring(0, beforeCursor.length() - 1);
            fooResourceUrl = zendeskTicketsAPIUrl + "?" + beforeIdentifier +
                    beforeCursor + pageSizeSpecifier;
        } else if (afterCursor != null) {
            afterCursor = afterCursor.substring(1);
            afterCursor = afterCursor.substring(0, afterCursor.length() - 1);
            System.out.println(afterCursor);
            fooResourceUrl = zendeskTicketsAPIUrl + "?" + afterIdentifier +
                    afterCursor + pageSizeSpecifier;
        } else {
            fooResourceUrl
                    = zendeskTicketsAPIUrl + "?" + pageSizeSpecifier;
        }
        HttpEntity<String> request = createHttpEntityRequest();
        String account = null;
        ResponseEntity<String> response = restTemplate.exchange(fooResourceUrl, HttpMethod.GET, request, String.class);
        account = response.getBody();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(account);
            List<TicketPojo> tickets = new ArrayList<>();
            for (int i = 0; i < jsonNode.get("tickets").size(); i++) {
                String finalResponse = jsonNode.get("tickets").get(i).toString();
                TicketPojo ticketPojo = mapper.readValue(finalResponse, TicketPojo.class);
                String dateTimeZ = jsonNode.get("tickets").get(i).get("created_at").toString();
                ticketPojo.setDateTime(convertStringToDate(dateTimeZ));
                tickets.add(ticketPojo);
            }
            String beforeApiResponse = jsonNode.get("meta").get("before_cursor").toString();
            String afterApiResponse = jsonNode.get("meta").get("after_cursor").toString();
            ZendeskTicket zd = new ZendeskTicket();
            zd.afterCursor = afterApiResponse;
            zd.beforeCursor = beforeApiResponse;
            zd.ticketPojos = tickets;

            return zd;
        } catch (JsonProcessingException e) {
            log.info(e.getMessage());
        }

        return null;
    }

    @Override
    public TicketPojo fetchTicketByIdFromZendeskAPI(String ticketID) {

        HttpEntity<String> request = createHttpEntityRequest();
        String fooResourceUrl
                = zendeskTicketsAPIUrl + "/" + Integer.valueOf(ticketID);
        ResponseEntity<String> response = restTemplate.exchange(fooResourceUrl, HttpMethod.GET, request, String.class);
        String account = response.getBody();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(account);
            String finalResponse = jsonNode.get("ticket").toString();
            TicketPojo ticket = mapper.readValue(finalResponse, TicketPojo.class);
            String dateTimeZ = jsonNode.get("ticket").get("created_at").toString();
            ticket.setDateTime(convertStringToDate(dateTimeZ));
            return ticket;
        } catch (JsonProcessingException e) {
            log.info(e.getMessage());
        }

        return null;
    }

    private HttpEntity<String> createHttpEntityRequest() {

        String plainCreds = env.getProperty("zendesk.coding.challenge.username") + ":"
                + env.getProperty("zendesk.coding.challenge.password");
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);
        HttpEntity<String> request = new HttpEntity<>(headers);

        return request;
    }

    private Date convertStringToDate(String dateReceived) {

        LocalDateTime dateTime = LocalDateTime.parse(dateReceived.substring(1, dateReceived.length()-2));
        Date date = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());

        return date;
    }
}
