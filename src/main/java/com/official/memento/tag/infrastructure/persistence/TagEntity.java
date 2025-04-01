package com.official.memento.tag.infrastructure.persistence;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

import com.official.memento.tag.domain.Tag;
import com.official.memento.tag.domain.enums.TagColor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tag")
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
@Getter
public class TagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(name = "color")
    private TagColor color;
    private Long memberId;

    private TagEntity(String name, TagColor color, Long memberId) {
        this.name = name;
        this.color = color;
        this.memberId = memberId;
    }


    public static TagEntity of(String name, TagColor color, Long memberId) {
        return new TagEntity(
                name,
                color,
                memberId
        );
    }

    public static TagEntity withId(final Tag tag) {
        return new TagEntity(
                tag.getId(),
                tag.getName(),
                tag.getColor(),
                tag.getMemberId()
        );
    }
}
