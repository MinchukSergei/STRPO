package by.issoft.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.*;

import java.util.Set;

@Entity
@Table(name = "user_data",
        indexes = {
                @Index(name = "user_login_UNIQUE", unique = true, columnList = "user_login"),
                @Index(name = "fk_user_icon_idx", columnList = "fk_user_icon")
        })
public class UserData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_id", nullable = false)
    private Long id;

    @Column(name = "user_login", nullable = false, unique = true)
    private String userLogin;

    @Column(name = "user_password", nullable = false)
    private String userPassword;

    @Column(name = "user_firstname", nullable = false)
    private String userFirstName;

    @Column(name = "user_lastname", nullable = false)
    private String userLastName;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "user_phone")
    private String userPhone;

    @Column(name = "user_address")
    private String userAddress;

    @OneToOne
    @JoinColumn(name = "fk_user_icon")
    private UserImage userIcon;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "friend_list",
            joinColumns = @JoinColumn(name = "fk_owner_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "fk_friend_id", nullable = false))
    private Set<UserData> friends;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "image_list",
            joinColumns = @JoinColumn(name = "fk_user", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "fk_image", nullable = false))
    private Set<UserImage> gallery;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userOwner", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserEvent> ownEvents;




    //region Getters And Setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    @JsonIgnore
    public String getUserPassword() {
        return userPassword;
    }

    @JsonProperty
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public UserImage getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(UserImage userIcon) {
        this.userIcon = userIcon;
    }

    public Set<UserData> getFriends() {
        return friends;
    }

    public void setFriends(Set<UserData> friends) {
        this.friends = friends;
    }

    public Set<UserImage> getGallery() {
        return gallery;
    }

    public void setGallery(Set<UserImage> gallery) {
        this.gallery = gallery;
    }

    public Set<UserEvent> getOwnEvents() {
        return ownEvents;
    }

    public void setOwnEvents(Set<UserEvent> ownEvents) {
        this.ownEvents = ownEvents;
    }

    //endregion

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserData userData = (UserData) o;

        if (id != null ? !id.equals(userData.id) : userData.id != null) return false;
        if (userLogin != null ? !userLogin.equals(userData.userLogin) : userData.userLogin != null) return false;
        if (userPassword != null ? !userPassword.equals(userData.userPassword) : userData.userPassword != null)
            return false;
        if (userFirstName != null ? !userFirstName.equals(userData.userFirstName) : userData.userFirstName != null)
            return false;
        if (userLastName != null ? !userLastName.equals(userData.userLastName) : userData.userLastName != null)
            return false;
        if (userEmail != null ? !userEmail.equals(userData.userEmail) : userData.userEmail != null) return false;
        if (userPhone != null ? !userPhone.equals(userData.userPhone) : userData.userPhone != null) return false;
        if (userAddress != null ? !userAddress.equals(userData.userAddress) : userData.userAddress != null)
            return false;
        return userIcon != null ? userIcon.equals(userData.userIcon) : userData.userIcon == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (userLogin != null ? userLogin.hashCode() : 0);
        result = 31 * result + (userPassword != null ? userPassword.hashCode() : 0);
        result = 31 * result + (userFirstName != null ? userFirstName.hashCode() : 0);
        result = 31 * result + (userLastName != null ? userLastName.hashCode() : 0);
        result = 31 * result + (userEmail != null ? userEmail.hashCode() : 0);
        result = 31 * result + (userPhone != null ? userPhone.hashCode() : 0);
        result = 31 * result + (userAddress != null ? userAddress.hashCode() : 0);
        result = 31 * result + (userIcon != null ? userIcon.hashCode() : 0);
        return result;
    }
}
