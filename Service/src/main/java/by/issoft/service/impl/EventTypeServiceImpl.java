package by.issoft.service.impl;



import by.issoft.entity.EventType;
import by.issoft.repository.EventTypeRepository;
import by.issoft.service.EventTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class EventTypeServiceImpl implements EventTypeService {
    private final EventTypeRepository eventTypeRepository;

    @Autowired
    public EventTypeServiceImpl(EventTypeRepository eventTypeRepository) {
        this.eventTypeRepository = eventTypeRepository;
    }

    @Override
    public EventType findByTypeName(String name) {
        return eventTypeRepository.findByTypeName(name);
    }

    @Override
    public Set<EventType> findAll() {
        Set<EventType> eventTypes = new HashSet<>();
        Iterable<EventType> eventTypeIterable = eventTypeRepository.findAll();
        for (EventType et: eventTypeIterable) {
            eventTypes.add(et);
        }
        return eventTypes;
    }
}
