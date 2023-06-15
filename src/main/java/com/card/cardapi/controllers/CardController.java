package com.card.cardapi.controllers;

import com.card.cardapi.dtos.CardRequestDto;
import com.card.cardapi.dtos.CardDtoDelete;
import com.card.cardapi.dtos.CardDtoUpdate;
import com.card.cardapi.services.CardService;
import com.card.cardapi.services.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1")
public class CardController {

    private final CardService cardService;
    private final UserServiceImpl userService;

    public CardController(CardService cardService, UserServiceImpl userService) {
        this.cardService = cardService;
        this.userService = userService;
    }

    @PostMapping("/api/card/create")
    public ResponseEntity<?> createCard( @Valid @RequestBody CardRequestDto cardDto){

        return  cardService.createCard(cardDto);
    }


    @GetMapping("/api/card/all")
    public ResponseEntity<?> getCardAll(@RequestParam(value ="page", defaultValue = "0", required = false) Integer page,
                                        @RequestParam(value = "size", defaultValue = "10", required = false ) Integer size,
                                            @RequestParam(value = "sortField" , defaultValue = "" , required = false ) String sortField,
                                        @RequestParam(value = "sortOrder", defaultValue = "", required = false ) String sortOrder,
                                        @RequestParam(value = "searchKey",  required = false ) String searchKey){
        return  cardService.getCards(page,size,sortField,sortOrder,searchKey);
    }

    @GetMapping("/api/card/{cardId}")
    public ResponseEntity<?> getCard(@PathVariable Long cardId){
        return  cardService.getCard(cardId);
    }

    @PostMapping("/api/card/update")
    public ResponseEntity<?> updateCard(@Valid @RequestBody CardDtoUpdate cardDtoUpdate){
        return  cardService.updateCard(cardDtoUpdate);
    }

    @PostMapping("/api/card/delete")
    public ResponseEntity<?> deleteCard(@Valid @RequestBody CardDtoDelete cardDtoDelete){
        return  cardService.deleteCard(cardDtoDelete);
    }
}
