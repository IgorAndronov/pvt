package com.pvt.logic.model;


import com.pvt.dao.entity.Answer;
import com.pvt.dao.entity.ExternalUser;
import com.pvt.dao.entity.Survey;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;

@Data(staticConstructor = "of")
public class SearchRoutineContext {

    private final Survey survey;

}
