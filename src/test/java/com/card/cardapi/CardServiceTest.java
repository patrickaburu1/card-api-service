package com.card.cardapi;

import com.card.cardapi.dtos.CardDtoUpdate;
import com.card.cardapi.dtos.CardRequestDto;
import com.card.cardapi.entities.Card;
import com.card.cardapi.entities.User;
import com.card.cardapi.exceptions.CustomExceptionNotFound;
import com.card.cardapi.repositories.CardRepository;
import com.card.cardapi.services.CardService;
import com.card.cardapi.services.UserServiceImpl;
import com.card.cardapi.utils.AppFunctions;
import com.card.cardapi.utils.RecordStatus;
import com.card.cardapi.utils.ResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private AppFunctions appFunctions;

    @InjectMocks
    private CardService cardService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @BeforeEach
    public void setup() {
        // Create a mock UserDetails object
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        Mockito.when(userDetails.getUsername()).thenReturn("amos@gmail.com");
        // Create an authentication object
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        // Create a mock SecurityContext and set the authentication
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        // Set the mock SecurityContext to SecurityContextHolder
        SecurityContextHolder.setContext(securityContext);
    }


    @Test
    public void testCreateCard() {
        // Prepare test data
        User user = new User();
        user.setId(1L);

        CardRequestDto cardDto = new CardRequestDto();
        cardDto.setName("Test Card");

        when(userService.getUser(anyString())).thenReturn(user);

        // Perform the test
        ResponseEntity<?> response = cardService.createCard(cardDto);

        // Verify the results
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ResponseModel responseModel = (ResponseModel) response.getBody();
        assertEquals("success", responseModel.getStatus());
        assertEquals("Card created successfully", responseModel.getMessage());
        verify(cardRepository, times(1)).save(any(Card.class));
    }

    @Test
    public void testUpdateCard() {
        // Prepare test data
        User user = new User();
        user.setId(1L);

        CardDtoUpdate cardDtoUpdate = new CardDtoUpdate();
        cardDtoUpdate.setId(1L);
        cardDtoUpdate.setName("Updated Card");
        cardDtoUpdate.setStatus("TODO");

        Card card = new Card();
        card.setId(1L);
        card.setCreatedByUserId(user.getId());
        card.setCardStatus(RecordStatus.ACTIVE.toString());
        when(userService.getUser(anyString())).thenReturn(user);
        when(cardRepository.findByIdAndCardStatus(eq(cardDtoUpdate.getId()), eq(RecordStatus.ACTIVE.toString()))).thenReturn(Optional.of(card));
        when(cardRepository.save(any(Card.class))).thenReturn(card);

        // Perform the test
        ResponseEntity<?> response = cardService.updateCard(cardDtoUpdate);

        // Verify the results
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ResponseModel responseModel = (ResponseModel) response.getBody();
        assertEquals("success", responseModel.getStatus());
        assertEquals("Card updated successfully", responseModel.getMessage());
        verify(cardRepository, times(1)).save(any(Card.class));
    }

    @Test
    public void testUpdateCard_InvalidCard() {
        // Prepare test data
        CardDtoUpdate cardDtoUpdate = new CardDtoUpdate();
        cardDtoUpdate.setId(1L);
        cardDtoUpdate.setName("Updated Card");
        when(cardRepository.findByIdAndCardStatus(eq(cardDtoUpdate.getId()), eq(RecordStatus.ACTIVE.toString()))).thenReturn(Optional.empty());
        // Perform the test and verify the exception
        assertThrows(CustomExceptionNotFound.class, () -> cardService.updateCard(cardDtoUpdate));
        verify(cardRepository, never()).save(any(Card.class));
    }

//    @Test
//    public void testDeleteCard() {
//        // Mock user service
//        User user = new User();
//        when(userService.getUser(anyString())).thenReturn(user);
//
//        // Mock card repository
//        CardDtoDelete cardDtoDelete = new CardDtoDelete();
//        cardDtoDelete.setId(1L);
//        Card card = new Card();
//        Optional<Card> cardOptional = Optional.of(card);
//        when(cardRepository.findByIdAndCardStatus(anyLong(), anyString())).thenReturn(cardOptional);
//        when(cardRepository.save(any(Card.class))).thenReturn(card);
//
//
//        // Verify the user service and card repository interactions
//        verify(userService).getUser(anyString());
//        verify(cardRepository).findByIdAndCardStatus(anyLong(), anyString());
//        verify(cardRepository).save(any(Card.class));
//
//
//    }
//
//
//    @Test
//    public void testGetCard() {
//        // Mock user service
//        User user = new User();
//        when(userService.getUser(anyString())).thenReturn(user);
//
//        // Mock card repository
//        Long cardId = 1L;
//        Card card = new Card();
//        testUpdateCard();
//
//        Optional<Card> cardOptional = Optional.of(card);
//        when(cardRepository.findByIdAndCardStatus(anyLong(), anyString())).thenReturn(cardOptional);
//
//        // Call the getCard method
//       // ResponseEntity<?> response = cardService.getCard(cardId);
//
//        // Verify the user service and card repository interactions
//        verify(userService).getUser(anyString());
//        verify(cardRepository).findByIdAndCardStatus(anyLong(), anyString());
//
//        // Assert the response
//       // assertEquals(ResponseEntity.ok().body(new ResponseModel("success", "success", Map.of("cards", card))), response);
//    }
}
