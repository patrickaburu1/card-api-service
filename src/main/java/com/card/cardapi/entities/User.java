package com.card.cardapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

import static javax.persistence.FetchType.EAGER;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users" ,uniqueConstraints = { @UniqueConstraint(name = "UniqueUsername", columnNames = { "username" }) })
@Data
@Getter
@Setter
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;


    @Column(length = 100, unique = true)
    private String username;

    @JsonIgnore
    private String password;


    @ManyToMany(fetch =EAGER)
    @JsonIgnore
    private Collection<Role> roles= new ArrayList<>();


}
