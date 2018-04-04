package by.issoft.entity;

import javax.persistence.*;

@Entity
@Table(name = "learning_set", indexes = {
        @Index(name = "fk_learning_set_event_id", columnList = "fk_event_id"),
        @Index(name = "fk_learning_set_event_type", columnList = "fk_event_type"),
        @Index(name = "fk_learning_set_user_owner", columnList = "fk_user_owner"),
        @Index(name = "fk_learning_set_user_participant", columnList = "fk_user_participant"),
        @Index(name = "user_id", columnList = "user_id, fk_event_id, fk_user_participant", unique = true)
})
public class LearningSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_id", nullable = false)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "fk_event_id", nullable = false)
    private Long eventId;

    @Column(name = "event_weekday", nullable = false)
    private Byte eventWeekDay;

    @Column(name = "event_time", nullable = false)
    private Integer eventTime;

    @Column(name = "fk_event_type", nullable = false)
    private Byte eventType;

    @Column(name = "fk_user_owner", nullable = false)
    private Long userOwner;

    @Column(name = "fk_user_participant")
    private Long userParticipant;

    @Column(name = "accepted", nullable = false)
    private Boolean accepted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Byte getEventWeekDay() {
        return eventWeekDay;
    }

    public void setEventWeekDay(Byte eventWeekDay) {
        this.eventWeekDay = eventWeekDay;
    }

    public Integer getEventTime() {
        return eventTime;
    }

    public void setEventTime(Integer eventTime) {
        this.eventTime = eventTime;
    }

    public Byte getEventType() {
        return eventType;
    }

    public void setEventType(Byte eventType) {
        this.eventType = eventType;
    }

    public Long getUserOwner() {
        return userOwner;
    }

    public void setUserOwner(Long userOwner) {
        this.userOwner = userOwner;
    }

    public Long getUserParticipant() {
        return userParticipant;
    }

    public void setUserParticipant(Long userParticipant) {
        this.userParticipant = userParticipant;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LearningSet that = (LearningSet) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (eventId != null ? !eventId.equals(that.eventId) : that.eventId != null) return false;
        if (eventWeekDay != null ? !eventWeekDay.equals(that.eventWeekDay) : that.eventWeekDay != null) return false;
        if (eventTime != null ? !eventTime.equals(that.eventTime) : that.eventTime != null) return false;
        if (eventType != null ? !eventType.equals(that.eventType) : that.eventType != null) return false;
        if (userOwner != null ? !userOwner.equals(that.userOwner) : that.userOwner != null) return false;
        if (userParticipant != null ? !userParticipant.equals(that.userParticipant) : that.userParticipant != null)
            return false;
        return accepted != null ? accepted.equals(that.accepted) : that.accepted == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (eventId != null ? eventId.hashCode() : 0);
        result = 31 * result + (eventWeekDay != null ? eventWeekDay.hashCode() : 0);
        result = 31 * result + (eventTime != null ? eventTime.hashCode() : 0);
        result = 31 * result + (eventType != null ? eventType.hashCode() : 0);
        result = 31 * result + (userOwner != null ? userOwner.hashCode() : 0);
        result = 31 * result + (userParticipant != null ? userParticipant.hashCode() : 0);
        result = 31 * result + (accepted != null ? accepted.hashCode() : 0);
        return result;
    }
}
