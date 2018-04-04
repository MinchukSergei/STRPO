package by.issoft.repository;

import by.issoft.entity.UserImage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserImageRepository extends CrudRepository<UserImage, Long> {
    UserImage findByImageName(String name);
}
