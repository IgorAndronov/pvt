package com.pvt.logic.service;


import com.pvt.AbstractDbTest;
import com.pvt.dao.entity.Question;
import com.pvt.dao.repository.GenericRepository;
import com.pvt.dao.repository.MeasurementUnitRepository;
import com.pvt.dao.repository.QuestionRepository;
import com.pvt.init.impl.QuestionnariesDataCreator;
import com.pvt.logic.logic.core.CentralAI;
import com.pvt.logic.logic.core.DialogState;
import com.pvt.logic.model.SearchRoutineContext;
import com.pvt.logic.model.SearchRoutineResult;
import com.pvt.web.controller.ChatController;
import lombok.extern.log4j.Log4j;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;


@Log4j
public class FindRoutineTest extends AbstractDbTest {



    @Autowired
    private GenericRepository genericRepository;

    @Autowired
    private MeasurementUnitRepository measurementUnitRepository;


    @Autowired
    private QuestionRepository questionRepository;


    private QuestionnariesDataCreator dataCreator;


    @Autowired
    private RoutineSearchService routineSearchService;


    private ChatController chatController = new ChatController();
    @Before
    public void setUp(){
        this.dataCreator = new QuestionnariesDataCreator(genericRepository,measurementUnitRepository,questionRepository);
        this.dataCreator.create();
    }


    @Test
    public void testFindProgramBasedOnUserAnswer(){
        final Iterable<Question> questions = questionRepository.findAll();
        final DialogState state = CentralAI.StateHolder.INSTANCE.getState("tester");

        questions.forEach(question->{
            log.info("Question :"+question.getQuestion());
            if(question.isAnswerRequired()){
                state.setCurrentQuestion(question);
                String answer = null;
                switch (question.getMeasurementUnit().getName()){
                    case "HEIGHT":
                        answer = "188";
                        break;

                    case "WEIGHT":
                        answer = "96";
                        break;

                    case "TRAIN_TARGET":
                        answer = "Набрать Вес";
                        break;

                    default:
                        fail("Can't be here");
                }
                state.addAnswer(answer);
            }
        });

        assertEquals(3, state.getUserSurvey().getAnswers().size());

        final SearchRoutineResult searchRoutineResult = routineSearchService.search(SearchRoutineContext.of(state.getUserSurvey()));

        assertNotNull(searchRoutineResult.getRoutines());
        assertEquals(2,searchRoutineResult.getRoutines().size());
        assertEquals(searchRoutineResult.getRoutines().iterator().next().getName(),"Программа набора мышечной массы");

    }
}
