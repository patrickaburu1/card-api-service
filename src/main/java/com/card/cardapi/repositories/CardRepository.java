package com.card.cardapi.repositories;

import com.card.cardapi.entities.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {


    Page<Card> findCardByCreatedByUserIdAndCardStatus(Long userId, String status, Pageable pageable);


    Page<Card> findAllByCardStatus(String status, Pageable pageable);


    Page<Card> findByCardStatusAndNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrColorContainingIgnoreCaseOrStatusOrCreatedAtContains(String cardStatus,
                                                                                                                                                  String searchKeyName,
                                                                                                                                                  String searchKeyDescription,
                                                                                                                                                  String searchKeyColor,
                                                                                                                                                  String searchKeyStatus,
                                                                                                                                                  String searchKeyDate,
                                                                                                                                                  Pageable pageable);


    @Query(nativeQuery = true, value = "SELECT * FROM Card  WHERE card_status = :cardStatus AND " +
            "(LOWER(name) LIKE %:searchKeyName% OR LOWER(description) LIKE %:searchKeyDescription% " +
            "OR LOWER(color) LIKE %:searchKeyColor% OR LOWER(status) LIKE %:searchKeyStatus% " +
            "OR LOWER(created_at) LIKE %:searchKeyDate%)")
    Page<Card> getAlCardStatusAndNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrColorContainingIgnoreCaseOrStatusContainsIgnoreCaseOrCreatedAt(String cardStatus,
                                                                                                                                                           String searchKeyName,
                                                                                                                                                           String searchKeyDescription,
                                                                                                                                                           String searchKeyColor,
                                                                                                                                                           String searchKeyStatus,
                                                                                                                                                           String searchKeyDate,
                                                                                                                                                           Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM Card  WHERE created_by_user_id = :createdByUserId AND card_status = :cardStatus AND " +
            "(LOWER(name) LIKE %:searchKeyName% OR LOWER(description) LIKE %:searchKeyDescription% " +
            "OR LOWER(color) LIKE %:searchKeyColor% OR LOWER(status) LIKE %:searchKeyStatus% " +
            "OR LOWER(created_at) LIKE %:searchKeyDate%)")
    Page<Card> getAlCreatedByUserIdAndCardStatusAndNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrColorContainingIgnoreCaseOrStatusContainsIgnoreCaseOrCreatedAt(Long createdByUserId,
                                                                                                                                                                             String cardStatus,
                                                                                                                                                                             String searchKeyName,
                                                                                                                                                                             String searchKeyDescription,
                                                                                                                                                                             String searchKeyColor,
                                                                                                                                                                             String searchKeyStatus,
                                                                                                                                                                             String searchKeyDate,
                                                                                                                                                                             Pageable pageable);

    Optional<Card> findByIdAndCardStatus(Long id, String card_status);

}
