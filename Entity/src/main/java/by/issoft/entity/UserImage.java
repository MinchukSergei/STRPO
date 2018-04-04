package by.issoft.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "user_image", indexes = {
        @Index(name = "image_name_UNIQUE", unique = true, columnList = "image_name")
})
public class UserImage {
    public final static String DEFAULT_ICON_NAME = "default_icon";

    @Id
    @Column(name = "pk_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(name = "image_name", unique = true, nullable = false)
    private String imageName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserImage image = (UserImage) o;

        if (id != null ? !id.equals(image.id) : image.id != null) return false;
        return imageName != null ? imageName.equals(image.imageName) : image.imageName == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (imageName != null ? imageName.hashCode() : 0);
        return result;
    }
}
