package com.javarush.nikolenko.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import static com.javarush.nikolenko.utils.Key.*;
import static com.javarush.nikolenko.utils.Key.BUTTON_DELETE_QUESTION;

@Slf4j
@Transactional
public class ButtonService {
    private final Map<String, QuestEditOperation> operationMap = new HashMap<>();;
    private final QuestEditService questEditService;

    public ButtonService(QuestEditService questEditService) {
        this.questEditService = questEditService;

        operationMap.put(BUTTON_LOAD_QUEST_IMAGE, questEditService::uploadQuestImage);
        operationMap.put(BUTTON_LOAD_QUESTION_IMAGE, questEditService::uploadQuestionImage);
        operationMap.put(BUTTON_LOAD_ANSWER_IMAGE, questEditService::uploadAnswerImage);

        operationMap.put(BUTTON_DELETE_QUESTION, questEditService::deleteQuestion);
        operationMap.put(BUTTON_DELETE_ANSWER, questEditService::deleteAnswer);

        operationMap.put(BUTTON_EDIT_QUEST, questEditService::updateQuest);
        operationMap.put(BUTTON_EDIT_QUESTION, questEditService::updateQuestion);
        operationMap.put(BUTTON_EDIT_ANSWER, questEditService::updateAnswer);
    }

    public QuestEditOperation getOperation(String key) {
        return operationMap.get(key);
    }

}
