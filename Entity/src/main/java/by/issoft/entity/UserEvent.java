package by.issoft.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Set;

@Entity
@Table(name = "user_event",
        indexes = {
                @Index(name = "fk_owner_user_data_idx", columnList = "fk_user_owner"),
                @Index(name = "fk_user_event_event_type_idx", columnList = "fk_event_type")
        }
)
public class UserEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_id", nullable = false)
    private Long id;

    @Column(name = "event_timestamp", nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Calendar eventTimestamp;

    @Column(name = "event_name", nullable = false)
    private String eventName;

    @ManyToOne
    @JoinColumn(name = "fk_event_type", nullable = false)
    private EventType eventType;

    @ManyToOne
    @JoinColumn(name = "fk_user_owner", nullable = false)
    private UserData userOwner;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "event_member_list",
            joinColumns = {@JoinColumn(name = "fk_event", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "fk_member", nullable = false)}
    )
    private Set<UserData> eventMembers;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "event")
    private Set<EventMemberList> memberList;

    @Column(name = "is_public", nullable = false)
    private Boolean isPublic;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Calendar getEventTimestamp() {
        return eventTimestamp;
    }

    public void setEventTimestamp(Calendar eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public UserData getUserOwner() {
        return userOwner;
    }

    public void setUserOwner(UserData userOwner) {
        this.userOwner = userOwner;
    }

    public Set<UserData> getEventMembers() {
        return eventMembers;
    }

    public void setEventMembers(Set<UserData> eventMembers) {
        this.eventMembers = eventMembers;
    }

    public Set<EventMemberList> getMemberList() {
        return memberList;
    }

    public void setMemberList(Set<EventMemberList> memberList) {
        this.memberList = memberList;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEvent userEvent = (UserEvent) o;

        if (id != null ? !id.equals(userEvent.id) : userEvent.id != null) return false;
        if (eventTimestamp != null ? !eventTimestamp.equals(userEvent.eventTimestamp) : userEvent.eventTimestamp != null)
            return false;
        if (eventName != null ? !eventName.equals(userEvent.eventName) : userEvent.eventName != null) return false;
        if (eventType != null ? !eventType.equals(userEvent.eventType) : userEvent.eventType != null) return false;
        if (userOwner != null ? !userOwner.equals(userEvent.userOwner) : userEvent.userOwner != null) return false;
        return isPublic != null ? isPublic.equals(userEvent.isPublic) : userEvent.isPublic == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (eventTimestamp != null ? eventTimestamp.hashCode() : 0);
        result = 31 * result + (eventName != null ? eventName.hashCode() : 0);
        result = 31 * result + (eventType != null ? eventType.hashCode() : 0);
        result = 31 * result + (userOwner != null ? userOwner.hashCode() : 0);
        result = 31 * result + (isPublic != null ? isPublic.hashCode() : 0);
        return result;
    }
}
