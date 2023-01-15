package tri.chung.safetruttest.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tri.chung.safetruttest.constant.ContactConstant;
import tri.chung.safetruttest.dto.ContactDto;
import tri.chung.safetruttest.entity.Contact;
import tri.chung.safetruttest.service.ContactService;

@RestController
@RequestMapping("/api")
public class ContactController {

	@Autowired
	ContactService contactService;

	@GetMapping("/home")
	public String getAllContacts() {
		return "<h1>welcome!!!</h1>";
	}

	@PostMapping("/contact")
	public ResponseEntity<Contact> createContact(@Valid @RequestBody ContactDto contactDto) throws Exception {
		Contact contactCreated = contactService.createContact(contactDto);
		return new ResponseEntity<Contact>(contactCreated, HttpStatus.CREATED);

	}

	@GetMapping("/contact")
	public ResponseEntity<Map<String, Object>> getAllContact(
			@RequestParam(defaultValue = ContactConstant.DEFAULT_PAGE_START) int page,
			@RequestParam(defaultValue = ContactConstant.DEFAULT_PAGE_SIZE) int size) throws Exception {
		Pageable paging = PageRequest.of(page, size);
		Map<String, Object> response = contactService.getAllContact(paging);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

	}

	@GetMapping("/contact/{id}")
	public ResponseEntity<ContactDto> getContactById(@PathVariable(value = "id") long id) throws Exception {
		ContactDto contactDto = contactService.getContactById(id);
		return new ResponseEntity<ContactDto>(contactDto, HttpStatus.OK);
	}

	@DeleteMapping("/contact/{id}")
	public ResponseEntity<?> deleteContact(@PathVariable(value = "id") long id) throws Exception {
		contactService.deleteContact(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);

	}

	@PutMapping("/contact/{id}")
	public ResponseEntity<Contact> updateContact(@PathVariable(value = "id") long id,
			@RequestBody ContactDto contactDto) throws Exception {
		Contact contact = contactService.updateContact(id, contactDto);
		return new ResponseEntity<Contact>(contact, HttpStatus.OK);
	}

	@GetMapping("/contact/search")
	public ResponseEntity<List<ContactDto>> searchContact(
			@RequestParam(value = "firstName", required = false) String firstName,
			@RequestParam(value = "lastName", required = false) String lastName) throws Exception {
		List<ContactDto> contactDtoList = contactService.searchByFirstAndLastName(firstName, lastName);
		return new ResponseEntity<List<ContactDto>>(contactDtoList, HttpStatus.OK);
	}

}
