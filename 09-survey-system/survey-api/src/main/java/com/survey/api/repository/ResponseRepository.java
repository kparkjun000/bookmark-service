package com.survey.api.repository;

import com.survey.api.entity.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResponseRepository extends JpaRepository<Response, Long> {
    
    List<Response> findBySurveyId(Long surveyId);
    
    List<Response> findByRespondentId(Long respondentId);
    
    long countBySurveyId(Long surveyId);
}
