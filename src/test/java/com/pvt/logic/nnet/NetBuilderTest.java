package com.pvt.logic.nnet;

import com.pvt.logic.logic.core.nnet.NetBuilder;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 19.12.2017.
 */


public class NetBuilderTest {
 final static int COLUMN_SIZE_ONE = 1;
 final static int COLUMN_INDEX_ZERO = 0;
    @Test
    public void buildTest(){
        int inputNumber = 1;
        int outputNumber = 1;
        int intermidiateLevelsNumber = 2;
        int neuronsPerlayer = 3;
        int maxLayerIndex = inputNumber+outputNumber+intermidiateLevelsNumber-1; //layers start from 0,1...maxLayerIndex

        NetBuilder netBuilder = new NetBuilder();
        List<RealMatrix> matrixWList =  netBuilder.build(inputNumber, outputNumber,intermidiateLevelsNumber,neuronsPerlayer);

        RealMatrix matrixI=netBuilder.buildMatrix(inputNumber,COLUMN_SIZE_ONE);
        matrixI.setEntry(0,0,0.91);

        RealMatrix expectedResults = netBuilder.buildMatrix(outputNumber,COLUMN_SIZE_ONE);
        expectedResults.setEntry(0,0,0.0995);
        RealMatrix acualFinalResults = netBuilder.calcLevelOutput(matrixI ,matrixWList,maxLayerIndex);
        RealMatrix inputDeltaRates = netBuilder.buildMatrix(outputNumber,COLUMN_SIZE_ONE);
        inputDeltaRates.setEntry(0,0, expectedResults.getEntry(0,0)/acualFinalResults.getEntry(0,0));

        for(int currentLayerIndex=maxLayerIndex; currentLayerIndex>0; currentLayerIndex--){
            int neuronsPerLayer = matrixWList.get(currentLayerIndex-1).getRowDimension();

            RealMatrix acualResultsMatrix = netBuilder.calcLevelOutput(matrixI ,matrixWList,currentLayerIndex);
            expectedResults = netBuilder.getExpectedResults(acualResultsMatrix, inputDeltaRates);

            RealMatrix deltaRatesPerOutput = netBuilder.getOutputDeltaRatio(acualResultsMatrix,expectedResults );

            RealMatrix tmpInputDeltaRates = netBuilder.buildMatrix(neuronsPerLayer,matrixWList.get(currentLayerIndex-1).getColumnDimension());
            RealMatrix tmpWDeltaRates = netBuilder.buildMatrix(neuronsPerLayer,matrixWList.get(currentLayerIndex-1).getColumnDimension());

            for(int outputNeuronIndex=0; outputNeuronIndex<neuronsPerLayer; outputNeuronIndex++){
                RealMatrix prevLevelOutput = netBuilder.calcLevelOutput(matrixI ,matrixWList,currentLayerIndex-1);
                RealMatrix incomingWForOutput = matrixWList.get(currentLayerIndex-1);
                RealMatrix independentAdjustments = netBuilder.calcAdjustmentsPerOutputNeuron(prevLevelOutput,
                                                                                   incomingWForOutput.getRowMatrix(outputNeuronIndex),
                                                                                   expectedResults.getEntry(outputNeuronIndex,COLUMN_INDEX_ZERO));
                RealMatrix spreadedOutputRates = netBuilder.getSprededOutputRates(independentAdjustments,deltaRatesPerOutput.getEntry(outputNeuronIndex,COLUMN_INDEX_ZERO));

                double[] kArray = new double[spreadedOutputRates.getRowDimension()];
                double k = 1;

                RealMatrix savedLayerIndexes = matrixWList.get(currentLayerIndex-1).copy();
                RealMatrix prevOutputsValues =   netBuilder.calcLevelOutput(matrixI ,matrixWList,currentLayerIndex-1);
                for(int j = 0; j<spreadedOutputRates.getRowDimension(); j++){
                    int columnToAjustIndex= j==0? 0:j-1;
                    RealMatrix prevOutputsW = netBuilder.adjustSingleWValue (matrixWList,currentLayerIndex-1,outputNeuronIndex,columnToAjustIndex, k);
                    double newExpectedResult = netBuilder.calcLevelOutput(matrixI ,matrixWList,currentLayerIndex).getEntry(outputNeuronIndex,COLUMN_INDEX_ZERO)*spreadedOutputRates.getEntry(j,COLUMN_INDEX_ZERO);
                    k=  netBuilder.calculateSingleIncomingDeltaRate(prevOutputsValues, j,prevOutputsW,newExpectedResult);
                    kArray[j]=k;
                }

                matrixWList.set(currentLayerIndex-1,savedLayerIndexes);
                netBuilder.spreadTmpRatesBetweenInputsAndWeights(tmpInputDeltaRates,tmpWDeltaRates,matrixWList,kArray,currentLayerIndex,outputNeuronIndex, prevOutputsValues);
                System.out.println();
            }

            inputDeltaRates = netBuilder.spreadRatesBetweenInputsandWeights(tmpInputDeltaRates,tmpWDeltaRates,matrixWList,currentLayerIndex);


        }
        RealMatrix checkedResult = netBuilder.calcLevelOutput(matrixI ,matrixWList,3);

    }





}
