package by.issoft.entity;

import javax.persistence.*;

@Entity
@Table(name = "event_member_list", indexes = {
        @Index(name = "fk_event_event_user_idx", columnList = "fk_event"),
        @Index(name = "fk_member_user_data_idx", columnList = "fk_member"),
        @Index(name = "fk_member_fk_event_UNIQUE", columnList = "fk_event, fk_member", unique = true)
})
public class EventMemberList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fk_member", nullable = false)
    private UserData member;

    @ManyToOne
    @JoinColumn(name = "fk_event", nullable = false)
    private UserEvent event;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserData getMember() {
        return member;
    }

    public void setMember(UserData member) {
        this.member = member;
    }

    public UserEvent getEvent() {
        return event;
    }

    public void setEvent(UserEvent event) {
        this.event = event;
    }
}
