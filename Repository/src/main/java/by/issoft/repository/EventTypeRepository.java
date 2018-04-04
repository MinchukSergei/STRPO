package by.issoft.repository;


import by.issoft.entity.EventType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventTypeRepository extends CrudRepository<EventType, Byte> {
    EventType findByTypeName(String name);
}
