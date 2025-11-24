package entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PdpAuthor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long tvcId;
    String PdpCode;
    String PdpName;
    String PdpDescription;
    String PdpImgUrl;
    String PdpEmail;
    String PdpPhone;
    String PdpAddress;
    Boolean PdpActive;

    // Tạo mỗi quan hệ với bảng tvcBook
    @ManyToMany(mappedBy = "PdpAuthor")
    List<PdpBook> PdpBooks = new ArrayList<>();
}
