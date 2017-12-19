package com.pvt.logic.logic.core.nnet;


import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created by admin on 19.12.2017.
 */
public class NetBuilder  {

    public void build(int inputsNumber){
        int columnNumber =1;
        int rowNumber=3;
        double[][] matrixDataW21 = new double[rowNumber][columnNumber];
        initValues(matrixDataW21);
        RealMatrix matrixW21 = MatrixUtils.createRealMatrix(matrixDataW21);


        double[][] matrixDataI1 = new double[columnNumber][rowNumber];
        RealMatrix matrixI1 = MatrixUtils.createRealMatrix(matrixDataI1);

        RealMatrix matrixI2 = matrixW21.multiply(matrixI1);

    }

    private void initValues(double[][] matrixW21) {
       for(int rowIndex=0;rowIndex<matrixW21.length;rowIndex++){
           for(int columnIndex=0;columnIndex<matrixW21[rowIndex].length;columnIndex++ ){
               matrixW21[rowIndex][columnIndex]=Math.random();
           }
       }
    }
}
