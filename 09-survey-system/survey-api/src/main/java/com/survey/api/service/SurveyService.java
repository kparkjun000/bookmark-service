package com.survey.api.service;

import com.survey.api.dto.QuestionDto;
import com.survey.api.dto.SurveyDto;
import com.survey.api.entity.Question;
import com.survey.api.entity.Survey;
import com.survey.api.entity.User;
import com.survey.api.repository.QuestionRepository;
import com.survey.api.repository.ResponseRepository;
import com.survey.api.repository.SurveyRepository;
import com.survey.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository surveyRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final ResponseRepository responseRepository;


    @Transactional
    public SurveyDto.Response createSurvey(Long userId, SurveyDto.CreateRequest request) {
        User creator = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Survey survey = Survey.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .creator(creator)
                .active(true)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .questions(new ArrayList<>())
                .build();

        survey = surveyRepository.save(survey);

        if (request.getQuestions() != null && !request.getQuestions().isEmpty()) {
            Survey finalSurvey = survey;
            List<Question> questions = request.getQuestions().stream()
                    .map(q -> Question.builder()
                            .survey(finalSurvey)
                            .questionText(q.getQuestionText())
                            .type(q.getType())
                            .orderIndex(q.getOrderIndex())
                            .required(q.getRequired() != null && q.getRequired())
                            .options(q.getOptions() != null ? q.getOptions() : new ArrayList<>())
                            .build())
                    .collect(Collectors.toList());

            questionRepository.saveAll(questions);
            survey.getQuestions().addAll(questions);
        }

        return convertToDto(survey);
    }

    @Transactional(readOnly = true)
    public SurveyDto.Response getSurvey(Long surveyId) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new RuntimeException("Survey not found"));
        
        return convertToDto(survey);
    }

    @Transactional(readOnly = true)
    public SurveyDto.Response getSurveyWithQuestions(Long surveyId) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new RuntimeException("Survey not found"));
        
        survey.getQuestions().size(); // Force lazy loading
        return convertToDto(survey);
    }

    @Transactional(readOnly = true)
    public List<SurveyDto.Response> getAllSurveys() {
        return surveyRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SurveyDto.Response> getMySurveys(Long userId) {
        return surveyRepository.findByCreatorId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SurveyDto.Response> getActiveSurveys() {
        return surveyRepository.findByActiveTrue().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public SurveyDto.Response updateSurvey(Long surveyId, Long userId, SurveyDto.UpdateRequest request) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new RuntimeException("Survey not found"));

        if (!survey.getCreator().getId().equals(userId)) {
            throw new RuntimeException("Not authorized to update this survey");
        }

        if (request.getTitle() != null) {
            survey.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            survey.setDescription(request.getDescription());
        }
        if (request.getActive() != null) {
            survey.setActive(request.getActive());
        }
        if (request.getStartDate() != null) {
            survey.setStartDate(request.getStartDate());
        }
        if (request.getEndDate() != null) {
            survey.setEndDate(request.getEndDate());
        }

        survey = surveyRepository.save(survey);
        return convertToDto(survey);
    }

    @Transactional
    public void deleteSurvey(Long surveyId, Long userId) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new RuntimeException("Survey not found"));

        if (!survey.getCreator().getId().equals(userId)) {
            throw new RuntimeException("Not authorized to delete this survey");
        }

        surveyRepository.delete(survey);
    }

    @Transactional
    public QuestionDto.Response addQuestion(Long surveyId, Long userId, QuestionDto.CreateRequest request) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new RuntimeException("Survey not found"));

        if (!survey.getCreator().getId().equals(userId)) {
            throw new RuntimeException("Not authorized to add questions to this survey");
        }

        Question question = Question.builder()
                .survey(survey)
                .questionText(request.getQuestionText())
                .type(request.getType())
                .orderIndex(request.getOrderIndex())
                .required(request.getRequired() != null && request.getRequired())
                .options(request.getOptions() != null ? request.getOptions() : new ArrayList<>())
                .build();

        question = questionRepository.save(question);
        return convertQuestionToDto(question);
    }

    @Transactional(readOnly = true)
    public List<QuestionDto.Response> getQuestions(Long surveyId) {
        List<Question> questions = questionRepository.findBySurveyIdOrderByOrderIndexAsc(surveyId);
        return questions.stream()
                .map(this::convertQuestionToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public QuestionDto.Response updateQuestion(Long questionId, Long userId, QuestionDto.UpdateRequest request) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        if (!question.getSurvey().getCreator().getId().equals(userId)) {
            throw new RuntimeException("Not authorized to update this question");
        }

        if (request.getQuestionText() != null) {
            question.setQuestionText(request.getQuestionText());
        }
        if (request.getType() != null) {
            question.setType(request.getType());
        }
        if (request.getOrderIndex() != null) {
            question.setOrderIndex(request.getOrderIndex());
        }
        if (request.getRequired() != null) {
            question.setRequired(request.getRequired());
        }
        if (request.getOptions() != null) {
            question.setOptions(request.getOptions());
        }

        question = questionRepository.save(question);
        return convertQuestionToDto(question);
    }

    @Transactional
    public void deleteQuestion(Long questionId, Long userId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        if (!question.getSurvey().getCreator().getId().equals(userId)) {
            throw new RuntimeException("Not authorized to delete this question");
        }

        questionRepository.delete(question);
    }

    private SurveyDto.Response convertToDto(Survey survey) {
        return SurveyDto.Response.builder()
                .id(survey.getId())
                .title(survey.getTitle())
                .description(survey.getDescription())
                .creatorId(survey.getCreator().getId())
                .creatorUsername(survey.getCreator().getUsername())
                .active(survey.isActive())
                .startDate(survey.getStartDate())
                .endDate(survey.getEndDate())
                .questionCount(survey.getQuestions().size())
                .responseCount((int) responseRepository.countBySurveyId(survey.getId()))
                .createdAt(survey.getCreatedAt())
                .updatedAt(survey.getUpdatedAt())
                .build();
    }

    private QuestionDto.Response convertQuestionToDto(Question question) {
        return QuestionDto.Response.builder()
                .id(question.getId())
                .surveyId(question.getSurvey().getId())
                .questionText(question.getQuestionText())
                .type(question.getType())
                .orderIndex(question.getOrderIndex())
                .required(question.isRequired())
                .options(question.getOptions())
                .build();
    }
}
