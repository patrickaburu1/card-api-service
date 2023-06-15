package com.card.cardapi.dtos;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardDtoDelete {

    @NotNull(message = "card id  is mandatory and should be Long data type")
    private Long id;
}
