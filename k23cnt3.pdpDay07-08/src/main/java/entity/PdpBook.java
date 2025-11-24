package entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.ArrayList;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE)
public class PdpBook {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long tvcId;
    String tvcCode;
    String tvcName;
    String tvcDescription;
    String tvcImgUrl;
    Integer tvcQuantity;
    Double tvcPrice;
    Boolean tvcActive;

    // Thiết kế mỗi quan hệ với bảng tvcAuthor
    @ManyToMany
    @JoinTable(
            name = "tvc_book_author",
            joinColumns = @JoinColumn(name = "tvcBookId"),
            inverseJoinColumns = @JoinColumn (name = "tvcAuthorId")
    )
    List<tvcAuthor> tvcAuthor = new ArrayList<>();
}
