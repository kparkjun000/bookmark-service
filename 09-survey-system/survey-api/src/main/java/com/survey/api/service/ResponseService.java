package com.survey.api.service;

import com.survey.api.dto.AnswerDto;
import com.survey.api.dto.ResponseDto;
import com.survey.api.entity.Answer;
import com.survey.api.entity.Question;
import com.survey.api.entity.Response;
import com.survey.api.entity.Survey;
import com.survey.api.entity.User;
import com.survey.api.repository.AnswerRepository;
import com.survey.api.repository.QuestionRepository;
import com.survey.api.repository.ResponseRepository;
import com.survey.api.repository.SurveyRepository;
import com.survey.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResponseService {

    private final ResponseRepository responseRepository;
    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;

    @Transactional
    public ResponseDto.Response submitResponse(Long userId, ResponseDto.SubmitRequest request) {
        Survey survey = surveyRepository.findById(request.getSurveyId())
                .orElseThrow(() -> new RuntimeException("Survey not found"));

        if (!survey.isActive()) {
            throw new RuntimeException("Survey is not active");
        }

        if (survey.getStartDate() != null && LocalDateTime.now().isBefore(survey.getStartDate())) {
            throw new RuntimeException("Survey has not started yet");
        }

        if (survey.getEndDate() != null && LocalDateTime.now().isAfter(survey.getEndDate())) {
            throw new RuntimeException("Survey has ended");
        }

        User respondent = userId != null ? userRepository.findById(userId).orElse(null) : null;

        Response response = Response.builder()
                .survey(survey)
                .respondent(respondent)
                .answers(new ArrayList<>())
                .build();

        response = responseRepository.save(response);

        if (request.getAnswers() != null && !request.getAnswers().isEmpty()) {
            Response finalResponse = response;
            List<Answer> answers = request.getAnswers().stream()
                    .map(a -> {
                        Question question = questionRepository.findById(a.getQuestionId())
                                .orElseThrow(() -> new RuntimeException("Question not found: " + a.getQuestionId()));

                        if (!question.getSurvey().getId().equals(request.getSurveyId())) {
                            throw new RuntimeException("Question does not belong to this survey");
                        }

                        if (question.isRequired() && (a.getValues() == null || a.getValues().isEmpty())) {
                            throw new RuntimeException("Required question must be answered: " + question.getQuestionText());
                        }

                        return Answer.builder()
                                .response(finalResponse)
                                .question(question)
                                .values(a.getValues() != null ? a.getValues() : new ArrayList<>())
                                .build();
                    })
                    .collect(Collectors.toList());

            answerRepository.saveAll(answers);
            response.getAnswers().addAll(answers);
        }

        return convertToDto(response);
    }

    @Transactional(readOnly = true)
    public ResponseDto.Response getResponse(Long responseId) {
        Response response = responseRepository.findById(responseId)
                .orElseThrow(() -> new RuntimeException("Response not found"));

        return convertToDto(response);
    }

    @Transactional(readOnly = true)
    public List<ResponseDto.Response> getSurveyResponses(Long surveyId, Long userId) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new RuntimeException("Survey not found"));

        if (!survey.getCreator().getId().equals(userId)) {
            throw new RuntimeException("Not authorized to view responses for this survey");
        }

        return responseRepository.findBySurveyId(surveyId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ResponseDto.Response> getMyResponses(Long userId) {
        return responseRepository.findByRespondentId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteResponse(Long responseId, Long userId) {
        Response response = responseRepository.findById(responseId)
                .orElseThrow(() -> new RuntimeException("Response not found"));

        boolean isCreator = response.getSurvey().getCreator().getId().equals(userId);
        boolean isRespondent = response.getRespondent() != null && 
                               response.getRespondent().getId().equals(userId);

        if (!isCreator && !isRespondent) {
            throw new RuntimeException("Not authorized to delete this response");
        }

        responseRepository.delete(response);
    }

    private ResponseDto.Response convertToDto(Response response) {
        List<AnswerDto.Response> answerDtos = response.getAnswers().stream()
                .map(this::convertAnswerToDto)
                .collect(Collectors.toList());

        return ResponseDto.Response.builder()
                .id(response.getId())
                .surveyId(response.getSurvey().getId())
                .surveyTitle(response.getSurvey().getTitle())
                .respondentId(response.getRespondent() != null ? response.getRespondent().getId() : null)
                .respondentUsername(response.getRespondent() != null ? response.getRespondent().getUsername() : "Anonymous")
                .submittedAt(response.getSubmittedAt())
                .answers(answerDtos)
                .build();
    }

    private AnswerDto.Response convertAnswerToDto(Answer answer) {
        return AnswerDto.Response.builder()
                .id(answer.getId())
                .questionId(answer.getQuestion().getId())
                .questionText(answer.getQuestion().getQuestionText())
                .values(answer.getValues())
                .build();
    }
}

