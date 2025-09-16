package com.vijay.question_service.service;

import com.vijay.question_service.model.Question;
import com.vijay.question_service.model.QuestionWrapper;
import com.vijay.question_service.model.Response;
import com.vijay.question_service.dao.QuestionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    QuestionDao questionDao;

    public ResponseEntity<List<Question>> getAllQuestions() {
        try {
            return new ResponseEntity<>(questionDao.findAll(), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<Question>> getQuestionsByCategory(String category) {
        try {
            return new ResponseEntity<>(questionDao.findByCategory(category),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);

    }

    public ResponseEntity<String> addQuestion(Question question) {
        questionDao.save(question);
        return new ResponseEntity<>("success",HttpStatus.CREATED);
    }

    public ResponseEntity<List<Integer>> getQuestionsForQuiz(String category, Integer numberOfQuestions) {
        try{
            List<Integer> questions = questionDao.findRandomQuestionsByCategory(category, numberOfQuestions);
            return new ResponseEntity<>(questions,HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
    }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuestions(List<Integer> qIds) {
        try{
            List<QuestionWrapper> questionsWrappers = new ArrayList<>();
            List<Question> questions = questionDao.findAllById(qIds);
            QuestionWrapper qWrapper= new QuestionWrapper();
            for (Question question : questions) {
                qWrapper.setId(question.getId());
                qWrapper.setQuestionTitle(question.getQuestionTitle());
                qWrapper.setOption1(question.getOption1());
                qWrapper.setOption2(question.getOption2());
                qWrapper.setOption3(question.getOption3());
                qWrapper.setOption4(question.getOption4());
                questionsWrappers.add(qWrapper);
            }
            return new ResponseEntity<>(questionsWrappers,HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Integer> getScore(List<Response> responses) {
        try{
            int score=0;
            for(Response r: responses){
                Question question = questionDao.findById(r.getId()).get();
                if(question.getRightAnswer().equals(r.getResponse())){
                    score++;
                }
            }
            return ResponseEntity.ok(score);
        }catch (Exception e){
            e.printStackTrace();    
    }
        return new ResponseEntity<>(0, HttpStatus.BAD_REQUEST);
    }
}
