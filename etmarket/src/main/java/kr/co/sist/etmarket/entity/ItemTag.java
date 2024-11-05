package kr.co.sist.etmarket.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class ItemTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemTagId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    @JsonIgnore
    private Item item;

    private String itemTag;

}
