package by.issoft.repository;


import by.issoft.entity.UserImageBlob;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserImageBlobRepository extends CrudRepository<UserImageBlob, Long> {
    UserImageBlob findByImageName(String name);
}
