package by.issoft.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;

@Entity
@Table(name = "event_type", indexes = {
        @Index(name = "type_name_UNIQUE", columnList = "type_name", unique = true),
})
public class EventType {
    public static final String DEFAULT_TYPE = "other";

    @Id
    @Column(name = "pk_id", nullable = false)
    @JsonIgnore
    private Byte id;

    @Column(name = "type_name", nullable = false, unique = true)
    private String typeName;

    public Byte getId() {
        return id;
    }

    public void setId(Byte id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventType eventType = (EventType) o;

        if (id != null ? !id.equals(eventType.id) : eventType.id != null) return false;
        return typeName != null ? typeName.equals(eventType.typeName) : eventType.typeName == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (typeName != null ? typeName.hashCode() : 0);
        return result;
    }
}
