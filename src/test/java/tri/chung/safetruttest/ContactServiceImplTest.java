package tri.chung.safetruttest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import tri.chung.safetruttest.common.ContactConverter;
import tri.chung.safetruttest.constant.ContactConstant;
import tri.chung.safetruttest.dto.ContactDto;
import tri.chung.safetruttest.entity.Contact;
import tri.chung.safetruttest.exception.ResourceNotFoundException;
import tri.chung.safetruttest.repository.ContactRepository;
import tri.chung.safetruttest.service.ContactServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ContactServiceImplTest {

	@Mock
	private ContactRepository contactRepository;

	@InjectMocks
	private ContactServiceImpl contactService;

	private Contact contact;

	@BeforeEach
	public void setup() {
		contact = new Contact(1, "nam", "tri", "chung", "tri@gmail.com", "0123456789", "test");
	}

	@Test
	public void createContact_Sucess() throws Exception {
		ContactDto contactDto = ContactConverter.convertToContactDto(contact);
		when(contactRepository.save(any())).thenReturn(contact);
		Contact result = contactService.createContact(contactDto);
		assertEquals(contact, result);
	}

	@Test
	public void createContact_ThrowException() throws Exception {
		ContactDto contactDto = ContactConverter.convertToContactDto(contact);
		when(contactRepository.save(any())).thenThrow(new RuntimeException("Server Error"));
		assertThrows(Exception.class, () -> contactService.createContact(contactDto));
	}

	@Test
	public void getContactById_Sucess() throws Exception {
		List<Contact> contacts = new ArrayList<>();
		contacts.add(contact);
		Optional<Contact> optContact = contacts.stream().findFirst();
		ContactDto contactDto = ContactConverter.convertToContactDto(contact);
		when(contactRepository.findById(anyLong())).thenReturn(optContact);
		ContactDto result = contactService.getContactById(1);
		assertEquals(contactDto, result);
	}

	@Test
	public void getContactById_NotFound() throws Exception {
		List<Contact> contacts = new ArrayList<>();
		Optional<Contact> optContact = contacts.stream().findFirst();

		when(contactRepository.findById(anyLong())).thenReturn(optContact);
		assertThrows(ResourceNotFoundException.class, () -> contactService.getContactById(contact.getId()));
	}

	@Test
	public void getAllContact_Success() {
		List<Contact> contactList = new ArrayList<>(
				Arrays.asList(new Contact(1, "nam", "tri", "chung", "tri@gmail.com", "0123456789", "test"),
						new Contact(2, "lam", "lam", "chung", "lam@gmail.com", "0123456789", "test"),
						new Contact(3, "hai", "hai", "chung", "hai@gmail.com", "0123456789", "test")));
		Pageable pageable = PageRequest.of(Integer.parseInt(ContactConstant.DEFAULT_PAGE_START),
				Integer.parseInt(ContactConstant.DEFAULT_PAGE_SIZE));
		Page<Contact> pageContact = new PageImpl<>(contactList, pageable, contactList.size());

		when(contactRepository.findAll(pageable)).thenReturn(pageContact);
		Map<String, Object> result = contactService.getAllContact(pageable);

		List<ContactDto> contactDtoList = contactList.stream().map(x -> ContactConverter.convertToContactDto(x))
				.collect(Collectors.toList());
		Map<String, Object> expected = new HashMap<>();
		expected.put("contacts", contactDtoList);
		expected.put("currentPage", pageContact.getNumber());
		expected.put("totalItems", pageContact.getTotalElements());
		expected.put("totalPages", pageContact.getTotalPages());

		assertEquals(expected.get("totalItems"), result.get("totalItems"));
		assertEquals(expected.get("currentPage"), result.get("currentPage"));
		assertEquals(expected.get("totalPages"), result.get("totalPages"));
		assertEquals(expected.get("contacts"), result.get("contacts"));
	}

	@Test
	public void updateContact_Success() throws Exception {
		ContactDto contactDto = ContactConverter.convertToContactDto(contact);
		List<Contact> contacts = new ArrayList<>();
		contacts.add(contact);
		Optional<Contact> optContact = contacts.stream().findFirst();
		when(contactRepository.findById(anyLong())).thenReturn(optContact);
		when(contactRepository.save(any())).thenReturn(contact);

		Contact resultActual = contactService.updateContact(contact.getId(), contactDto);
		assertEquals(contact, resultActual);
	}

	@Test
	public void deleteContact_Success() throws Exception {
		List<Contact> contacts = new ArrayList<>();
		contacts.add(contact);
		Optional<Contact> optContact = contacts.stream().findFirst();
		when(contactRepository.findById(anyLong())).thenReturn(optContact);

		doNothing().when(contactRepository).delete(any());
		contactService.deleteContact(anyLong());
		verify(contactRepository, times(1)).delete(any());
	}

	@Test
	public void searchByFirstAndLastName_Success() throws Exception {
		List<Contact> contactList = new ArrayList<>(
				Arrays.asList(new Contact(1, "nam", "tri", "chung", "tri@gmail.com", "0123456789", "test"),
						new Contact(2, "lam", "lam", "chung", "lam@gmail.com", "0123456789", "test"),
						new Contact(3, "hai", "hai", "chung", "hai@gmail.com", "0123456789", "test")));
		when(contactRepository.searchByFirstAndLastName(anyString(), anyString())).thenReturn(contactList);
		List<ContactDto> listContactExpected = contactList.stream().map(x -> ContactConverter.convertToContactDto(x))
				.collect(Collectors.toList());

		List<ContactDto> listContactActual = contactService.searchByFirstAndLastName(contact.getFirstName(),
				contact.getLastName());
		assertEquals(listContactExpected, listContactActual);

	}
}
