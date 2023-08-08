package com.card.cardapi.taskamo.dtos;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class WordDto {
    @NotNull
    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z ]*$", message = "Input should contain only characters from 'a' to 'z'")
    String firstWord;
    @NotNull
    String secondWord;
    @NotNull
    String relation;
}
