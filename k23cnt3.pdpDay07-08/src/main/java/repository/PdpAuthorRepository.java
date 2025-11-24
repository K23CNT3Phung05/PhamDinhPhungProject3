package repository;
import k23cnt3.pdpDay07-08.entity.PdpAuthor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PdpAuthorRepository extends JpaRepository<PdpAuthor, Long> {
}
