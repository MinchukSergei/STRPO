package by.issoft.service;


import by.issoft.entity.EventType;

import java.util.Set;

public interface EventTypeService {
    EventType findByTypeName(String name);
    Set<EventType> findAll();
}
