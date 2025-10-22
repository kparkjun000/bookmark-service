package com.survey.api.repository;

import com.survey.api.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {
    
    List<Survey> findByCreatorId(Long creatorId);
    
    List<Survey> findByActiveTrue();
    
    List<Survey> findByCreatorIdAndActiveTrue(Long creatorId);
}
