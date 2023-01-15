package tri.chung.safetruttest.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import tri.chung.safetruttest.common.ContactConverter;
import tri.chung.safetruttest.dto.ContactDto;
import tri.chung.safetruttest.entity.Contact;
import tri.chung.safetruttest.exception.ResourceNotFoundException;
import tri.chung.safetruttest.repository.ContactRepository;

@Service
public class ContactServiceImpl implements ContactService {

	@Autowired
	private ContactRepository contactRepository;

	@Override
	public Contact createContact(ContactDto contactDto) throws Exception {
		Contact contact = ContactConverter.convertToContact(contactDto);
		Contact contactCreated = contactRepository.save(contact);
		return contactCreated;
	}

	@Override
	public ContactDto getContactById(long id) throws ResourceNotFoundException {
		Optional<Contact> contact = contactRepository.findById(id);
		if (contact.isPresent()) {
			return ContactConverter.convertToContactDto(contact.get());
		} else {
			throw new ResourceNotFoundException("Contact not found for this id: " + id);
		}
	}

	@Override
	public Map<String, Object> getAllContact(Pageable page) {
		Page<Contact> pageContact = contactRepository.findAll(page);
		List<Contact> listContact = pageContact.getContent();

		List<ContactDto> contactDtos = listContact.stream().map(x -> ContactConverter.convertToContactDto(x))
		.collect(Collectors.toList());
		Map<String, Object> response = new HashMap<>();
		response.put("contacts", contactDtos);
		response.put("currentPage", pageContact.getNumber());
		response.put("totalItems", pageContact.getTotalElements());
		response.put("totalPages", pageContact.getTotalPages());


		return response;
	}

	@Override
	public Contact updateContact(long id, ContactDto contactDto) throws ResourceNotFoundException {
		Contact contact = contactRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Contact not found for this id : " + id));

		contact.setEmailAdress(contactDto.getEmailAdress());
		contact.setName(contactDto.getName());
		contact.setFirstName(contactDto.getFirstName());
		contact.setLastName(contactDto.getLastName());
		contact.setPhoneNumber(contactDto.getPhoneNumber());
		contact.setPoltalAdress(contactDto.getPoltalAdress());
		Contact updatedContact = contactRepository.save(contact);
		return updatedContact;
	}

	@Override
	public void deleteContact(long id) throws ResourceNotFoundException {
		Optional<Contact> contact = contactRepository.findById(id);
		if (!contact.isPresent()) {
			throw new ResourceNotFoundException("Contact not found for this id: " + id);
		}
		contactRepository.delete(contact.get());
	}

	@Override
	public List<ContactDto> searchByFirstAndLastName(String firstName, String lastName) throws Exception {
		List<Contact> contactList = contactRepository.searchByFirstAndLastName(firstName, lastName);
		if (CollectionUtils.isEmpty(contactList)) {
			throw new ResourceNotFoundException("Search Contact not found");
		}
		List<ContactDto> contactDtoList = contactList.stream().map(x -> ContactConverter.convertToContactDto(x))
				.collect(Collectors.toList());
		return contactDtoList;
	}

}
