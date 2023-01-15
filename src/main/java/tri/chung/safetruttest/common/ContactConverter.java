package tri.chung.safetruttest.common;

import tri.chung.safetruttest.dto.ContactDto;
import tri.chung.safetruttest.entity.Contact;

public class ContactConverter {

	public static Contact convertToContact(ContactDto contactDto) {
		Contact contact = new Contact();
		contact.setEmailAdress(contactDto.getEmailAdress());
		contact.setName(contactDto.getName());
		contact.setFirstName(contactDto.getFirstName());
		contact.setLastName(contactDto.getLastName());
		contact.setPhoneNumber(contactDto.getPhoneNumber());
		contact.setPoltalAdress(contactDto.getPoltalAdress());
		return contact;
	}
	
	public static ContactDto convertToContactDto(Contact contact) {
		ContactDto contactDto = new ContactDto();
		contactDto.setEmailAdress(contact.getEmailAdress());
		contactDto.setName(contact.getName());
		contactDto.setFirstName(contact.getFirstName());
		contactDto.setLastName(contact.getLastName());
		contactDto.setPhoneNumber(contact.getPhoneNumber());
		contactDto.setPoltalAdress(contact.getPoltalAdress());
		return contactDto;
	}
}
