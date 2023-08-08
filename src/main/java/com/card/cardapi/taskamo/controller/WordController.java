package com.card.cardapi.taskamo.controller;

import com.card.cardapi.taskamo.dtos.WordDto;
import com.card.cardapi.taskamo.services.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/word")
public class WordController {

    @Autowired
    private WordService wordService;

    @PostMapping("/create-relation")
    public ResponseEntity<?> createWordAndRelationShip(@Valid @RequestBody WordDto wordDto){
       return wordService.createWordAndRelation(wordDto);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getGetAll(){
        return wordService.getAllWordsWithRelationShip();
    }

    @GetMapping("/filter/{relation}")
    public ResponseEntity<?> getWordByRelationShip(@PathVariable String relation ){
        return wordService.getWordByRelationShip(relation);
    }

    @GetMapping("/all-with-inverse")
    public ResponseEntity<?> getGetAllWithInverser(){
        return wordService.getAllWordsWithRelationShip1();
    }



    @GetMapping("/searchPath")
    public String searchPath(@RequestParam String source, @RequestParam String target) {
        return wordService.findPath(source, target);
    }
}
