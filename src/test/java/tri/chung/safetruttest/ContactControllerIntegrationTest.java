package tri.chung.safetruttest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.HttpClientErrorException;

import tri.chung.safetruttest.constant.ContactConstant;
import tri.chung.safetruttest.dto.ContactDto;
import tri.chung.safetruttest.entity.Contact;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ContactControllerIntegrationTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	private int port;
	@Autowired
	JdbcTemplate jdbcTemplate;

	private String getRootUrl() {
		return "http://localhost:" + port + "/api";
	}

	@BeforeEach
	void setup() {
		jdbcTemplate.execute("insert into Contact(id, name, FIRST_NAME, LAST_NAME, email, phone, potal) "
				+ "values(1, 'chungtri', 'tri', 'chung', 'tri@gmail.com', '0123456789','test')");
	}

	@AfterEach
	void emptyData() {
		jdbcTemplate.execute("DELETE FROM Contact");
	}

	@Test
	public void testGetContactById() {
		ResponseEntity<ContactDto> responseEntity = restTemplate.getForEntity(getRootUrl() + "/contact/1",
				ContactDto.class);
		
		assertNotNull(responseEntity.getBody());
		assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
	}

	@Test
	void testGetAllContact() {
		Map<String, Integer> params = new HashMap<>();
		params.put("page", Integer.parseInt(ContactConstant.DEFAULT_PAGE_START));
		params.put("size", Integer.parseInt(ContactConstant.DEFAULT_PAGE_SIZE));
		ResponseEntity<Map<String, Object>> responseEntity = restTemplate.getForEntity(getRootUrl() + "/contact",
				(Class<Map<String, Object>>) (Object) Map.class, params);
		assertNotNull(responseEntity.getBody());
		assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
	}

	@Test
	void testDeleteContact() {
		restTemplate.delete(getRootUrl() + "/contact/1");
		try {
			restTemplate.getForObject(getRootUrl() + "/contact/1", ContactDto.class);
		} catch (HttpClientErrorException  e) {
			assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
		}
	}
	
	@Test
	void testUpdateContact() {
		ContactDto contactDto = restTemplate.getForObject(getRootUrl() + "/contact/1", ContactDto.class);
		contactDto.setFirstName("ka");
		contactDto.setLastName("jonh");
		
		restTemplate.put(getRootUrl() + "/contact/1", contactDto);
		
		ContactDto contactDtoUpdated = restTemplate.getForObject(getRootUrl() + "/contact/1", ContactDto.class);
		
		assertNotNull(contactDtoUpdated);
		assertEquals("ka", contactDtoUpdated.getFirstName());
		assertEquals("jonh", contactDtoUpdated.getLastName());
	}
	
	@Test
	public void testCreateContact() {
		jdbcTemplate.execute("DELETE FROM Contact");
		ContactDto contactDto = new ContactDto("chunglam", "lam", "chung", "tri@gmail.com", "0333456785", "abc");
		ResponseEntity<Contact> responseEntity = restTemplate.postForEntity(getRootUrl() + "/contact", contactDto,
				Contact.class);
		assertNotNull(responseEntity.getBody());
		assertEquals(HttpStatus.CREATED.value(), responseEntity.getStatusCodeValue());

	}
}
