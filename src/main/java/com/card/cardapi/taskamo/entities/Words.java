package com.card.cardapi.taskamo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name="words")
public class Words {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;

    @Column(name = "word")
    private String word;

//    @JoinColumn(name = "id",referencedColumnName = "second_word_id", insertable = false,updatable = false)
//    @OneToMany(fetch = FetchType.EAGER  )
//    private List<WordRelation> wordRelationList;
}
