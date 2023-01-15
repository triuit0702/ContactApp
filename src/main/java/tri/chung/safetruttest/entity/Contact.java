package tri.chung.safetruttest.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "contact")
public class Contact {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(name = "name")
	private String name;
	@Column(name = "firstName")
	private String firstName;
	@Column(name = "lastName")
	private String lastName;
	@Column(name = "email")
	private String emailAdress;
	@Column(name = "phone")
	private String phoneNumber;
	@Column(name = "potal")
	private String poltalAdress;

	
	public Contact() {
	}

	public Contact(long id, String name, String firstName, String lastName, String emailAdress, String phoneNumber,
			String poltalAdress) {
		this.id = id;
		this.name = name;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAdress = emailAdress;
		this.phoneNumber = phoneNumber;
		this.poltalAdress = poltalAdress;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
}
