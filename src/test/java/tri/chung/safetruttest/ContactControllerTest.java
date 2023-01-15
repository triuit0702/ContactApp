package tri.chung.safetruttest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;

import tri.chung.safetruttest.common.ContactConverter;
import tri.chung.safetruttest.constant.ContactConstant;
import tri.chung.safetruttest.controller.ContactController;
import tri.chung.safetruttest.dto.ContactDto;
import tri.chung.safetruttest.entity.Contact;
import tri.chung.safetruttest.exception.ResourceNotFoundException;
import tri.chung.safetruttest.service.ContactService;

@WebMvcTest(ContactController.class)
public class ContactControllerTest {

	@MockBean
	private ContactService contactService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void shouldCreateContact() throws Exception {
		Contact contact = new Contact(1, "nam", "tri", "chung", "tri@gmail.com", "0123456789", "test");
		ContactDto contactDto = ContactConverter.convertToContactDto(contact);

		when(contactService.createContact(any())).thenReturn(contact);
		MvcResult mvcResult = mockMvc.perform(post("/api/contact").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(contactDto))).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(HttpStatus.CREATED.value(), status);
		String content = mvcResult.getResponse().getContentAsString();
		String resultExpected = objectMapper.writeValueAsString(contact);
		assertEquals(resultExpected, content);
	}

	@Test
	void shouldReturnContact() throws Exception {
		ContactDto contactDto = new ContactDto("nam", "tri", "chung", "tri@gmail.com", "0123456789", "test");
		when(contactService.getContactById(anyLong())).thenReturn(contactDto);

		MvcResult mvcResult = mockMvc.perform(get("/api/contact/{id}", anyLong())).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(HttpStatus.OK.value(), status);
		String content = mvcResult.getResponse().getContentAsString();
		assertEquals(objectMapper.writeValueAsString(contactDto), content);

	}

	@Test
	void shouldReturnNotFoundContact() throws Exception {
		long id = 9;

		when(contactService.getContactById(anyLong()))
				.thenThrow(new ResourceNotFoundException("Contact not found for this id: " + id));
		MvcResult mvcResult = mockMvc.perform(get("/api/contact/{id}", id)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(HttpStatus.NOT_FOUND.value(), status);
	}

	@Test
	void shouldReturnInternalServerErrorWhenGetContactByID() throws Exception {
		long id = 9;

		when(contactService.getContactById(anyLong())).thenThrow(new RuntimeException("Server error"));
		MvcResult mvcResult = mockMvc.perform(get("/api/contact/{id}", id)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), status);
	}

	@Test
	void shouldReturnListContact() throws Exception {
		List<ContactDto> contactDtos = new ArrayList<>(
				Arrays.asList(new ContactDto("nam", "tri", "chung", "tri@gmail.com", "0123456789", "test"),
						new ContactDto("lam", "lam", "chung", "lam@gmail.com", "0123456789", "test"),
						new ContactDto("hai", "hai", "chung", "hai@gmail.com", "0123456789", "test")));

		Map<String, Object> response = new HashMap<>();
		double sizePage = Double.parseDouble(ContactConstant.DEFAULT_PAGE_SIZE);
		int totalPage = contactDtos.size() == 0 ? 1 : (int) Math.ceil((double) contactDtos.size() / sizePage);

		response.put("contacts", contactDtos);
		response.put("currentPage", ContactConstant.DEFAULT_PAGE_START);
		response.put("totalItems", contactDtos.size());
		response.put("totalPages", totalPage);
		when(contactService.getAllContact(any())).thenReturn(response);
		MvcResult mvcResult = mockMvc.perform(get("/api/contact")).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(HttpStatus.OK.value(), status);
		String content = mvcResult.getResponse().getContentAsString();
		assertEquals(objectMapper.writeValueAsString(response), content);
	}

	@Test
	void shouldReturnInternalServerErrorWhenGetListContact() throws Exception {
		when(contactService.getAllContact(any())).thenThrow(new RuntimeException("Server error"));
		MvcResult mvcResult = mockMvc.perform(get("/api/contact")).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), status);
	}

	@Test
	void shouldUpdateContact() throws Exception {
		Contact contact = new Contact(1, "nam", "tri", "chung", "tri@gmail.com", "0123456789", "test");
		ContactDto contactDto = ContactConverter.convertToContactDto(contact);

		when(contactService.updateContact(anyLong(), any())).thenReturn(contact);
		MvcResult mvcResult = mockMvc.perform(put("/api/contact/{id}", 1).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(contactDto))).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(HttpStatus.OK.value(), status);
		String content = mvcResult.getResponse().getContentAsString();
		String resultExpected = objectMapper.writeValueAsString(contact);
		assertEquals(resultExpected, content);
	}

	@Test
	void shouldReturnNotFoundUpdateContact() throws Exception {
		long id = 9;
		Contact contact = new Contact(1, "nam", "tri", "chung", "tri@gmail.com", "0123456789", "test");
		ContactDto contactDto = ContactConverter.convertToContactDto(contact);

		when(contactService.updateContact(anyLong(), any()))
				.thenThrow(new ResourceNotFoundException("Contact not found for this id: " + id));
		MvcResult mvcResult = mockMvc.perform(put("/api/contact/{id}", 1).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(contactDto))).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(HttpStatus.NOT_FOUND.value(), status);
	}

	@Test
	void shouldReturnInternalServerErrorWhenUpdateContact() throws Exception {
		Contact contact = new Contact(1, "nam", "tri", "chung", "tri@gmail.com", "0123456789", "test");
		ContactDto contactDto = ContactConverter.convertToContactDto(contact);

		when(contactService.updateContact(anyLong(), any())).thenThrow(new RuntimeException("Server error"));

		MvcResult mvcResult = mockMvc.perform(put("/api/contact/{id}", 1).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(contactDto))).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), status);
	}

	@Test
	void shouldReturnListContactWhenSearch() throws Exception {
		ContactDto contactDto1 = new ContactDto("nam", "tri", "chung", "tri@gmail.com", "0123456789", "test");
		List<ContactDto> contactDtos = new ArrayList<>();
		contactDtos.add(contactDto1);

		when(contactService.searchByFirstAndLastName(any(), any())).thenReturn(contactDtos);
		String firstName = "tri";
		MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
		paramsMap.add("firstName", firstName);
		MvcResult mvcResult = mockMvc.perform(get("/api/contact/search").params(paramsMap)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(HttpStatus.OK.value(), status);
		String content = mvcResult.getResponse().getContentAsString();
		assertEquals(objectMapper.writeValueAsString(contactDtos), content);
	}

	@Test
	void shouldReturnNotFoundContactWhenSearch() throws Exception {
		ContactDto contactDto1 = new ContactDto("nam", "tri", "chung", "tri@gmail.com", "0123456789", "test");
		List<ContactDto> contactDtos = new ArrayList<>();
		contactDtos.add(contactDto1);

		when(contactService.searchByFirstAndLastName(any(), any()))
				.thenThrow(new ResourceNotFoundException("Search Contact not found "));
		String firstName = "abc";
		MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
		paramsMap.add("firstName", firstName);
		MvcResult mvcResult = mockMvc.perform(get("/api/contact/search").params(paramsMap)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(HttpStatus.NOT_FOUND.value(), status);
	}

	@Test
	void shouldReturnInternalErrorServerContactWhenSearch() throws Exception {
		ContactDto contactDto1 = new ContactDto("nam", "tri", "chung", "tri@gmail.com", "0123456789", "test");
		List<ContactDto> contactDtos = new ArrayList<>();
		contactDtos.add(contactDto1);

		when(contactService.searchByFirstAndLastName(any(), any())).thenThrow(new RuntimeException("Server error"));
		String firstName = "abc";
		MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
		paramsMap.add("firstName", firstName);
		MvcResult mvcResult = mockMvc.perform(get("/api/contact/search").params(paramsMap)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), status);
	}

	@Test
	void shouldDeleteTutorial() throws Exception {
		long id = 1L;
		doNothing().when(contactService).deleteContact(id);
		mockMvc.perform(delete("/api/contact/{id}", id)).andExpect(status().isNoContent());
	}
	
	@Test
	void shouldReturnNotFoundWhenDeleteContact() throws Exception {
		long id = 1L;
		Mockito.doThrow(new ResourceNotFoundException("Contact not found for this id: " + id))
				.when(contactService).deleteContact(id);

		MvcResult mvcResult = mockMvc.perform(delete("/api/contact/{id}", id)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(HttpStatus.NOT_FOUND.value(), status);
	}

}
