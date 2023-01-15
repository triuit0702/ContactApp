package tri.chung.safetruttest.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import tri.chung.safetruttest.dto.ContactDto;
import tri.chung.safetruttest.entity.Contact;
import tri.chung.safetruttest.exception.ResourceNotFoundException;

public interface ContactService {

	Contact createContact(ContactDto contactDto) throws Exception;
	ContactDto getContactById(long id) throws ResourceNotFoundException;
	Map<String, Object> getAllContact(Pageable page);
	Contact updateContact(long id, ContactDto contactDto) throws ResourceNotFoundException;
	void deleteContact(long id) throws ResourceNotFoundException;
	List<ContactDto> searchByFirstAndLastName(String firstName, String lastName) throws Exception;
}
