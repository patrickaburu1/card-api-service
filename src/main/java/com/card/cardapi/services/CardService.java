package com.card.cardapi.services;

import com.card.cardapi.dtos.CardRequestDto;
import com.card.cardapi.dtos.CardDtoDelete;
import com.card.cardapi.dtos.CardDtoUpdate;
import com.card.cardapi.entities.Card;
import com.card.cardapi.entities.User;
import com.card.cardapi.exceptions.CustomExceptionNotFound;
import com.card.cardapi.exceptions.UnAuthorizedExceptionNotFound;
import com.card.cardapi.repositories.CardRepository;
import com.card.cardapi.security.SecurityUtils;
import com.card.cardapi.utils.AppFunctions;
import com.card.cardapi.utils.CardStatus;
import com.card.cardapi.utils.RecordStatus;
import com.card.cardapi.utils.ResponseModel;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Log4j2
public class CardService {


    private final CardRepository cardRepository;
    private final UserServiceImpl userService;
    private final AppFunctions appFunctions;

    public CardService(CardRepository cardRepository, UserServiceImpl userService, AppFunctions appFunctions) {
        this.cardRepository = cardRepository;
        this.userService = userService;
        this.appFunctions = appFunctions;
    }

    public ResponseEntity<?> createCard(CardRequestDto cardDto) {
        User user = userService.getUser(SecurityUtils.getCurrentUserLogin());
        Card card = new Card();
        card.setCreatedByUserId(user.getId());
        card.setName(cardDto.getName());
        card.setStatus(CardStatus.TODO.toString());
        if (null != cardDto.getColor() && !appFunctions.isValidColor(cardDto.getColor()))
            return ResponseEntity.badRequest().body(new ResponseModel("error", "Color validation failed"));
        card.setDescription(cardDto.getDescription() != null ? cardDto.getDescription() : "");
        card.setColor(cardDto.getColor() != null ? cardDto.getColor() : "");
        cardRepository.save(card);
        return ResponseEntity.ok(new ResponseModel("success", "Card created successfully"));
    }


    public ResponseEntity<?> updateCard(CardDtoUpdate cardDtoUpdate) {

        User user = userService.getUser(SecurityUtils.getCurrentUserLogin());
        Optional<Card> cardOptional = cardRepository.findByIdAndCardStatus(cardDtoUpdate.getId(), RecordStatus.ACTIVE.toString());
        if (cardOptional.isEmpty())
            throw new CustomExceptionNotFound("Invalid card");
        //return ResponseEntity.ok().body(new ResponseModel("error", "Invalid card"));
        Card card = cardOptional.get();
        card.setCreatedByUserId(user.getId());
        card.setName(cardDtoUpdate.getName());
        if (Arrays.stream(CardStatus.values()).noneMatch(status -> status.toString().equals(cardDtoUpdate.getStatus().toUpperCase())))
            return ResponseEntity.ok().body(new ResponseModel("error", "Status not supported"));

        if (user.getRoles().stream().noneMatch(node -> node.getName().equalsIgnoreCase("ROLE_SUPER_ADMIN")) && !card.getCreatedByUserId().equals(user.getId()))
            throw new UnAuthorizedExceptionNotFound("User not authorized");

        card.setStatus(cardDtoUpdate.getStatus().toUpperCase());
        if (null != cardDtoUpdate.getColor() && !appFunctions.isValidColor(cardDtoUpdate.getColor()))
            return ResponseEntity.badRequest().body(new ResponseModel("error", "Color validation failed"));
        card.setDescription(cardDtoUpdate.getDescription() != null ? cardDtoUpdate.getDescription() : "");
        card.setColor(cardDtoUpdate.getColor() != null ? cardDtoUpdate.getColor() : "");
        cardRepository.save(card);
        return ResponseEntity.ok(new ResponseModel("success", "Card updated successfully"));


    }

    public ResponseEntity<?> deleteCard(CardDtoDelete cardDtoDelete) {

        User user = userService.getUser(SecurityUtils.getCurrentUserLogin());
        Optional<Card> cardOptional = cardRepository.findByIdAndCardStatus(cardDtoDelete.getId(), RecordStatus.ACTIVE.toString());

        if (cardOptional.isEmpty())
            throw new CustomExceptionNotFound("Invalid card");
        Card card = cardOptional.get();
        card.setCardStatus(RecordStatus.DELETED.toString());

        if (user.getRoles().stream().noneMatch(node -> node.getName().equalsIgnoreCase("ROLE_SUPER_ADMIN")) && !card.getCreatedByUserId().equals(user.getId()))
            throw new UnAuthorizedExceptionNotFound("User not authorized");

        cardRepository.save(card);
        return ResponseEntity.ok(new ResponseModel("success", "Card deleted successfully"));


    }

    public ResponseEntity<?> getCard(Long cardId) {
        User user = userService.getUser(SecurityUtils.getCurrentUserLogin());
        Optional<Card> cardOptional = cardRepository.findByIdAndCardStatus(cardId, RecordStatus.ACTIVE.toString());
        if (cardOptional.isEmpty())
            throw new CustomExceptionNotFound("Invalid card");
        Card card = cardOptional.get();

        if (user.getRoles().stream().noneMatch(node -> node.getName().equalsIgnoreCase("ROLE_SUPER_ADMIN")) && !card.getCreatedByUserId().equals(user.getId()))
            throw new UnAuthorizedExceptionNotFound("User not authorized");
        Map<String, Object> map = new HashMap<>();
        map.put("cards", card);

        return ResponseEntity.ok().body(new ResponseModel("success", "success", map));
    }

    public ResponseEntity<?> getCards(Integer pageNo, Integer pageSize, String sortField, String sortOrder, String searchKey) {
        User user = userService.getUser(SecurityUtils.getCurrentUserLogin());
        Page<Card> card;
        Pageable pageable;

        //add pageable and sortable and validate inputs
        if (appFunctions.isSortValid(sortField, sortOrder)) {
            Sort.Direction direction = appFunctions.getSortDirection(sortOrder);
            Sort sort = Sort.by(direction, sortField);
            pageable = PageRequest.of(pageNo, pageSize, sort);
        } else {
            pageable = PageRequest.of(pageNo, pageSize);
        }

        Boolean shouldSearch = appFunctions.shouldSearch(searchKey);

        Date date=new Date();

        if (user.getRoles().stream().anyMatch(node -> node.getName().equalsIgnoreCase("ROLE_ADMIN")) && shouldSearch)

            card = cardRepository.findAllByCardStatus(RecordStatus.ACTIVE.toString(), pageable);

        else if (user.getRoles().stream().anyMatch(node -> node.getName().equalsIgnoreCase("ROLE_ADMIN")) && !shouldSearch)
            card = cardRepository.getAlCardStatusAndNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrColorContainingIgnoreCaseOrStatusContainsIgnoreCaseOrCreatedAt(RecordStatus.ACTIVE.toString(), searchKey, searchKey, searchKey,searchKey,searchKey, pageable);
        else if (!shouldSearch)
            card = cardRepository.findCardByCreatedByUserIdAndCardStatus(user.getId(), RecordStatus.ACTIVE.toString(), pageable);
        else
            card = cardRepository.getAlCreatedByUserIdAndCardStatusAndNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrColorContainingIgnoreCaseOrStatusContainsIgnoreCaseOrCreatedAt(user.getId(), RecordStatus.ACTIVE.toString(), searchKey, searchKey, searchKey,searchKey, searchKey, pageable);
        Map<String, Object> map = new HashMap<>();
        map.put("cards", card.getContent());
        return ResponseEntity.ok(new ResponseModel("success", " successful", map));
    }
}
