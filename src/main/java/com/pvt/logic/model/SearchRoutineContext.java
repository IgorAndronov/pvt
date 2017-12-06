package com.pvt.logic.model;


import com.pvt.dao.entity.Answer;
import com.pvt.dao.entity.ExternalUser;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class SearchRoutineContext {

    private ExternalUser user;

    private Collection<Answer> answers = new ArrayList<>();

}
