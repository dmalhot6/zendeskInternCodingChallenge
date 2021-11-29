package com.zendesk.coding.challenge.mobileticketviewer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
class MobileTicketViewerApplicationTests {

	@Autowired
	private MobileTicketService mobileTicketService;

	@MockBean
	private RestTemplate restTemplate;

	String mockResponse;

	@BeforeEach
	public void setup(){

		mockResponse = "{\n" +
				"    \"tickets\": [\n" +
				"        {\n" +
				"            \"url\": \"https://zccasu.zendesk.com/api/v2/tickets/1.json\",\n" +
				"            \"id\": 1,\n" +
				"            \"external_id\": null,\n" +
				"            \"via\": {\n" +
				"                \"channel\": \"sample_ticket\",\n" +
				"                \"source\": {\n" +
				"                    \"from\": {},\n" +
				"                    \"to\": {},\n" +
				"                    \"rel\": null\n" +
				"                }\n" +
				"            },\n" +
				"            \"created_at\": \"2021-11-19T22:43:16Z\",\n" +
				"            \"updated_at\": \"2021-11-19T22:43:17Z\",\n" +
				"            \"type\": \"incident\",\n" +
				"            \"subject\": \"Sample ticket: Meet the ticket\",\n" +
				"            \"raw_subject\": \"Sample ticket: Meet the ticket\",\n" +
				"            \"description\": \"Hi there,\\n\\nI’m sending an email because I’m having a problem setting up your new product. Can you help me troubleshoot?\\n\\nThanks,\\n The Customer\\n\\n\",\n" +
				"            \"priority\": \"normal\",\n" +
				"            \"status\": \"open\",\n" +
				"            \"recipient\": null,\n" +
				"            \"requester_id\": 421866369072,\n" +
				"            \"submitter_id\": 422050815811,\n" +
				"            \"assignee_id\": 422050815811,\n" +
				"            \"organization_id\": null,\n" +
				"            \"group_id\": 360022259292,\n" +
				"            \"collaborator_ids\": [],\n" +
				"            \"follower_ids\": [],\n" +
				"            \"email_cc_ids\": [],\n" +
				"            \"forum_topic_id\": null,\n" +
				"            \"problem_id\": null,\n" +
				"            \"has_incidents\": false,\n" +
				"            \"is_public\": true,\n" +
				"            \"due_at\": null,\n" +
				"            \"tags\": [\n" +
				"                \"sample\",\n" +
				"                \"support\",\n" +
				"                \"zendesk\"\n" +
				"            ],\n" +
				"            \"custom_fields\": [],\n" +
				"            \"satisfaction_rating\": null,\n" +
				"            \"sharing_agreement_ids\": [],\n" +
				"            \"followup_ids\": [],\n" +
				"            \"ticket_form_id\": 360003505752,\n" +
				"            \"brand_id\": 360007060792,\n" +
				"            \"allow_channelback\": false,\n" +
				"            \"allow_attachments\": true\n" +
				"        }\n" +
				"    ],\n" +
				"    \"meta\": {\n" +
				"        \"has_more\": true,\n" +
				"        \"after_cursor\": \"eyJvIjoibmljZV9pZCIsInYiOiJhUUVBQUFBQUFBQUEifQ==\",\n" +
				"        \"before_cursor\": \"eyJvIjoibmljZV9pZCIsInYiOiJhUUVBQUFBQUFBQUEifQ==\"\n" +
				"    },\n" +
				"    \"links\": {\n" +
				"        \"prev\": \"https://zccasu.zendesk.com/api/v2/tickets?page%5Bbefore%5D=eyJvIjoibmljZV9pZCIsInYiOiJhUUVBQUFBQUFBQUEifQ%3D%3D&page%5Bsize%5D=1\",\n" +
				"        \"next\": \"https://zccasu.zendesk.com/api/v2/tickets?page%5Bafter%5D=eyJvIjoibmljZV9pZCIsInYiOiJhUUVBQUFBQUFBQUEifQ%3D%3D&page%5Bsize%5D=1\"\n" +
				"    }\n" +
				"}";

		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		ResponseEntity<?> responseEntity = new ResponseEntity<>(mockResponse, header, HttpStatus.OK);
//		System.out.println("this is:"+responseEntity.getBody());
		Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.any(),
				Mockito.any(), (Class<Object>) Mockito.any())).thenReturn((ResponseEntity<Object>) responseEntity);
//		responseEntity = new ResponseEntity(mockResponse, HttpStatus.OK);
	}
	@Test
	void contextLoads() {
	}

	@Test
	public void fetchAllTicketsFromZendeskAPI() {

//				.thenReturn(responseEntity);
		ZendeskTicket zendeskTicket = mobileTicketService.fetchAllTicketsFromZendeskAPI(null,
				null);


		Assert.notNull(zendeskTicket, "Zendesk complete details object present");
		Assert.notNull(zendeskTicket.ticketPojos, "Ticket details present");
	}

	@Test
	public void fetchAllTicketsFromPreviousPage() {

		ZendeskTicket zendeskTicket = mobileTicketService.fetchAllTicketsFromZendeskAPI("faskjdw23423==",
				null);
		Assert.notNull(zendeskTicket, "Zendesk complete details object present");
		Assert.notNull(zendeskTicket.ticketPojos, "Ticket details present");
	}

	@Test
	public void fetchAllTicketsFromNextPage() {

		ZendeskTicket zendeskTicket = mobileTicketService.fetchAllTicketsFromZendeskAPI(null,
				"sfddskf34kmsdf==");
		Assert.notNull(zendeskTicket, "Zendesk complete details object present");
		Assert.notNull(zendeskTicket.ticketPojos, "Ticket details present");
	}

	@Test
	public void fetchTicketByIdFromZendeskAPI() {

		String singleTicketMockResponse = "{\n" +
				"    \"ticket\": {\n" +
				"        \"url\": \"https://zccasu.zendesk.com/api/v2/tickets/1.json\",\n" +
				"        \"id\": 1,\n" +
				"        \"external_id\": null,\n" +
				"        \"via\": {\n" +
				"            \"channel\": \"sample_ticket\",\n" +
				"            \"source\": {\n" +
				"                \"from\": {},\n" +
				"                \"to\": {},\n" +
				"                \"rel\": null\n" +
				"            }\n" +
				"        },\n" +
				"        \"created_at\": \"2021-11-19T22:43:16Z\",\n" +
				"        \"updated_at\": \"2021-11-19T22:43:17Z\",\n" +
				"        \"type\": \"incident\",\n" +
				"        \"subject\": \"Sample ticket: Meet the ticket\",\n" +
				"        \"raw_subject\": \"Sample ticket: Meet the ticket\",\n" +
				"        \"description\": \"Hi there,\\n\\nI’m sending an email because I’m having a problem setting up your new product. Can you help me troubleshoot?\\n\\nThanks,\\n The Customer\\n\\n\",\n" +
				"        \"priority\": \"normal\",\n" +
				"        \"status\": \"open\",\n" +
				"        \"recipient\": null,\n" +
				"        \"requester_id\": 421866369072,\n" +
				"        \"submitter_id\": 422050815811,\n" +
				"        \"assignee_id\": 422050815811,\n" +
				"        \"organization_id\": null,\n" +
				"        \"group_id\": 360022259292,\n" +
				"        \"collaborator_ids\": [],\n" +
				"        \"follower_ids\": [],\n" +
				"        \"email_cc_ids\": [],\n" +
				"        \"forum_topic_id\": null,\n" +
				"        \"problem_id\": null,\n" +
				"        \"has_incidents\": false,\n" +
				"        \"is_public\": true,\n" +
				"        \"due_at\": null,\n" +
				"        \"tags\": [\n" +
				"            \"sample\",\n" +
				"            \"support\",\n" +
				"            \"zendesk\"\n" +
				"        ],\n" +
				"        \"custom_fields\": [],\n" +
				"        \"satisfaction_rating\": null,\n" +
				"        \"sharing_agreement_ids\": [],\n" +
				"        \"fields\": [],\n" +
				"        \"followup_ids\": [],\n" +
				"        \"ticket_form_id\": 360003505752,\n" +
				"        \"brand_id\": 360007060792,\n" +
				"        \"allow_channelback\": false,\n" +
				"        \"allow_attachments\": true\n" +
				"    }\n" +
				"}";
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		ResponseEntity<?> responseEntity = new ResponseEntity<>(singleTicketMockResponse, header, HttpStatus.OK);
		Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.any(),
				Mockito.any(), (Class<Object>) Mockito.any())).thenReturn((ResponseEntity<Object>) responseEntity);
		String ticketID = "1";
		TicketPojo ticketPojo = mobileTicketService.fetchTicketByIdFromZendeskAPI(ticketID);
		Assert.notNull(ticketPojo, "Ticket details present");
	}
}
