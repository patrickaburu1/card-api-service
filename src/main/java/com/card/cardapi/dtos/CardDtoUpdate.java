package com.card.cardapi.dtos;



import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CardDtoUpdate {

    @NotNull(message = "Card Name is mandatory")
    private String name;

    @NotNull(message = "Card Id  is mandatory")
    private Long id;

    private String color;

    @NotNull(message  = "Card status  is mandatory")
    private String status;

    private String description;
}
