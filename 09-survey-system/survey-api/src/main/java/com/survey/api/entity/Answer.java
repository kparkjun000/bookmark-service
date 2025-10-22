package com.survey.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "answers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "response_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Response response;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Question question;

    @ElementCollection
    @CollectionTable(name = "answer_values", joinColumns = @JoinColumn(name = "answer_id"))
    @Column(name = "value")
    @Builder.Default
    private List<String> values = new ArrayList<>();
}
