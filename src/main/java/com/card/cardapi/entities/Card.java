package com.card.cardapi.entities;

import com.card.cardapi.utils.RecordStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name="card")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name",nullable = false) // Mark the column as not nullable
    @NotNull
    private String name;

    private String color;

    private String status;

    private String description;

    @JsonIgnore
    @Column(name = "created_by_user_id")
    private Long createdByUserId;


    @Column(name = "card_status")
    private String cardStatus= RecordStatus.ACTIVE.toString();

    private Date createdAt=new Date();
    private Date updatedAt=new Date();

    @JoinColumn(name = "created_by_user_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.EAGER  )
    private User user;

}
