package by.issoft.entity;


import javax.persistence.*;
import java.util.Arrays;

@Entity
@Table(name = "nn_initial_weights", indexes = {
        @Index(name = "unique_user_id", columnList = "user_id", unique = true)
})
public class NNInitialWeights {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_id", nullable = false)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Lob
    @Column(name = "initial_weights", nullable = false, columnDefinition = "BLOB")
    private byte[] initialWeights;

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

    public byte[] getInitialWeights() {
        return initialWeights;
    }

    public void setInitialWeights(byte[] initialWeights) {
        this.initialWeights = initialWeights;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NNInitialWeights that = (NNInitialWeights) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        return Arrays.equals(initialWeights, that.initialWeights);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(initialWeights);
        return result;
    }
}
