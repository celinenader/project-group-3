package com.ecse321.group3.tutorME.controller;

import com.ecse321.group3.tutorME.domain.Lesson;
import com.ecse321.group3.tutorME.service.LessonServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;

@RestController
public class LessonController {

    @Autowired
    private LessonServiceIF lessonService;

    //request mapping makes this method link to tutorme-heroku.com/api/lesson
    //the request body just says that take in a lesson object (json)
    //the method could be GET instead of POST where appropriate.
    @RequestMapping(value = "/api/lesson", method = POST)
    public ResponseEntity<Lesson> createLesson(@RequestBody Lesson lesson){

        //validate the input first.
        if(lesson == null || lesson.getLessonId() < 0){
            //invalid subject entered, return a bad request.
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        //tell the lesson service to create the lesson.
        Lesson createdLesson = null;

        try{
        	createdLesson = lessonService.createLesson(lesson);
        } catch(Exception e){
            //If we get any exceptions while creating a subject, we will return a server error
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //if no errors, we're going to return the created subject with a ok status
        return new ResponseEntity<>(createdLesson, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/api/lesson", method = GET)
    public ResponseEntity<Lesson> getLesson(@RequestParam int lessonId){

        //validate the input first.
        if(lessonId < 0){
            //invalid lesson name entered, return a bad request.
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        //tell the lesson service to create the lesson.
        Lesson lesson = null;

        try{
            lesson = lessonService.getLesson(lessonId);
        } catch(Exception e){
            //If we get any exceptions while getting a lesson, we will return a server error
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //if no errors, we're going to return the lesson with an ok status
        return new ResponseEntity<>(lesson, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/api/lesson/getall", method = GET)
    public ResponseEntity<List<Lesson>> getLessons(){
        List<Lesson> lessons = null;
        
        //tell the lesson service to list all lessons.
        try{
            lessons = lessonService.getLessons();
        } catch(Exception e){
            //If we get any exceptions while getting a lesson, we will return a server error
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //if no errors, we're going to return the lesson with an ok status
        return new ResponseEntity<>(lessons, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/lesson/delete", method = DELETE)
    public ResponseEntity<Lesson> deleteLesson(@RequestParam int lessonId,LocalDate startTime){
    	//validate the input first.
        if(lessonId < 0 || startTime.isBefore(LocalDate.now()) || startTime.isEqual(LocalDate.now()) ){
            //invalid lesson id entered, return a bad request.
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        try{
            lessonService.deleteLesson(lessonId);
        } catch(Exception e){
            //If we get any exceptions while getting a lesson, we will return a server error
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //if no errors, we're going to return the lesson with an ok status
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}
