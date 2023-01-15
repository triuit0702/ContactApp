package tri.chung.safetruttest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tri.chung.safetruttest.entity.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long>{

	@Query("select u from Contact u where (:firstName is null or u.firstName = :firstName)"
		      +" and (:lastName is null or u.lastName = :lastName)")
	List<Contact> searchByFirstAndLastName(@Param("firstName") String firstName,
            @Param("lastName") String lastName);
}
