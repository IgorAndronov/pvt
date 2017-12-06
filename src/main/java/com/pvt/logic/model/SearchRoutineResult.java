package com.pvt.logic.model;


import com.pvt.dao.entity.Routine;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class SearchRoutineResult {

    private Collection<Routine> routines = new ArrayList<>();

}
