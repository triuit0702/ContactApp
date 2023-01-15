package tri.chung.safetruttest.dto;

import java.util.Objects;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import tri.chung.safetruttest.validator.Phone;

public class ContactDto {

	@NotEmpty(message = "Name is required")
	private String name;
	@NotEmpty(message = "firstName is required")
	private String firstName;
	@NotEmpty(message = "lastName is required")
	private String lastName;
	@NotEmpty(message = "Email is required")
	@Email
	private String emailAdress;
	@Phone
	private String phoneNumber;
	private String poltalAdress;

	public ContactDto() {
	}

	public ContactDto(@NotEmpty(message = "Name is required") String name,
			@NotEmpty(message = "firstName is required") String firstName,
			@NotEmpty(message = "lastName is required") String lastName,
			@NotEmpty(message = "Email is required") @Email String emailAdress, String phoneNumber,
			String poltalAdress) {
		this.name = name;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAdress = emailAdress;
		this.phoneNumber = phoneNumber;
		this.poltalAdress = poltalAdress;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailAdress() {
		return emailAdress;
	}

	public void setEmailAdress(String emailAdress) {
		this.emailAdress = emailAdress;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPoltalAdress() {
		return poltalAdress;
	}

	public void setPoltalAdress(String poltalAdress) {
		this.poltalAdress = poltalAdress;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, emailAdress);
	}

	// Boolean function to check
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || this.getClass() != obj.getClass())
			return false;
		ContactDto contact = (ContactDto) obj;

		return this.name.equals(contact.name) && this.firstName.equals(contact.firstName)
				&& this.lastName.equals(contact.lastName) && this.emailAdress.equals(contact.emailAdress)
				&& this.phoneNumber.equals(contact.phoneNumber) && this.poltalAdress.equals(contact.poltalAdress);
	}
}
