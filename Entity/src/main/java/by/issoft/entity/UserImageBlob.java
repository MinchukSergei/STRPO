package by.issoft.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.*;

@Entity
@Table(name = "user_image", indexes = {
        @Index(name = "image_name_UNIQUE", unique = true, columnList = "image_name")
})
public class UserImageBlob {
    @Id
    @Column(name = "pk_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(name = "image_name", unique = true, nullable = false)
    private String imageName;

    @Lob @Basic(fetch = FetchType.LAZY)
    @Column(name = "image_blob", columnDefinition = "MEDIUMBLOB", nullable = false)
    private byte[] imageBlob;

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

    @JsonIgnore
    public byte[] getImageBlob() {
        return imageBlob;
    }

    @JsonProperty
    public void setImageBlob(byte[] imageBlob) {
        this.imageBlob = imageBlob;
    }
}
