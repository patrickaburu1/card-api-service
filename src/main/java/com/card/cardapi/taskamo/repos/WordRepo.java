package com.card.cardapi.taskamo.repos;

import com.card.cardapi.taskamo.entities.Words;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordRepo extends CrudRepository<Words,Long> {

    Words findFirstByWord(String word);

}
