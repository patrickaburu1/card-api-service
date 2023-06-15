package com.card.cardapi.dtos;



import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CardRequestDto {

    @NotNull(message = "Card Name is mandatory")
    private String name;
    private String color;
    private String description;
}
