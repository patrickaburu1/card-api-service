package com.card.cardapi.taskamo.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name="words_relation")
public class WordRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;

    @JsonIgnore
    @Column(name = "first_word_id")
    private Long firstWordId;

    @JsonIgnore
    @Column(name = "second_word_id")
    private Long secondWordId;

    @Column(name = "relation")
    private String relation;


    @JoinColumn(name = "first_word_id",referencedColumnName = "id", insertable = false,updatable = false)
    @ManyToOne(fetch = FetchType.EAGER  )
    private Words firstWordsLink;


    @JoinColumn(name = "second_word_id",referencedColumnName = "id", insertable = false,updatable = false)
    @ManyToOne(fetch = FetchType.EAGER  )
    private Words secondWordsLink;


}
