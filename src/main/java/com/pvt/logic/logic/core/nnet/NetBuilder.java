package com.pvt.logic.logic.core.nnet;


import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created by admin on 19.12.2017.
 */
public class NetBuilder  {

    static final double DECREASE_RATE_FOR_MAX_POSSIBLE_INPUT_VALUE = 0.93;

    public List<RealMatrix> build(int inputsNumber, int outputNumbers, int inermidiateLevelsNumber, int neuronsPerlayerNumber){

        List<RealMatrix> matricesW = new ArrayList();

        RealMatrix matrixDataWI =buildMatrix(3,1);
        initWITest(matrixDataWI);
        //initValues(matrixDataWI);
        matricesW.add(matrixDataWI);

        for(int i=0; i<inermidiateLevelsNumber-1;i++){
            RealMatrix matrixW = buildMatrix(neuronsPerlayerNumber,neuronsPerlayerNumber);
            initW32Test(matrixW);
            matricesW.add(matrixW);
        }

        RealMatrix matrixDataWO =buildMatrix(outputNumbers,neuronsPerlayerNumber);
        initWOTest(matrixDataWO);
        //initValues(matrixDataWI);
        matricesW.add(matrixDataWO);

        return matricesW;
    }

    public RealMatrix calcLevelOutput(RealMatrix inputMatrix, List<RealMatrix> matrixWList, int level){
        RealMatrix intermidiateOutputMatrix = inputMatrix;
        for(int currentLevel = 0; currentLevel<level; currentLevel++){
            RealMatrix intermidiateInputMatrix= matrixWList.get(currentLevel).multiply(intermidiateOutputMatrix);
            intermidiateOutputMatrix = sigmoid(intermidiateInputMatrix);
        }
        System.out.println(intermidiateOutputMatrix);
        return intermidiateOutputMatrix;

    }

    public RealMatrix adjustSingleWValue(List<RealMatrix> matrixWList, int level, int rownum, int columnNum, double deltaRate){
        RealMatrix matrixW = matrixWList.get(level);
        matrixW.setEntry(rownum,columnNum,matrixW.getEntry(rownum,columnNum)*deltaRate);
        return matrixW;
    }

    public RealMatrix getOutputDeltaRatio(RealMatrix actualResult, RealMatrix expectedResult){
        double[][] result = new double[actualResult.getRowDimension()][1];
        for(int i = 0;i<actualResult.getRowDimension();i++){
            result[i][0] = expectedResult.getEntry(i,0)/ actualResult.getEntry(i,0);
        }

        return MatrixUtils.createRealMatrix(result);
    }

    public RealMatrix calcAdjustmentsPerOutputNeuron(RealMatrix prevLevelOutput, RealMatrix incomingWforOutput, double expectedResult){
        double[][] kResults = new double[prevLevelOutput.getRowDimension()][1];

        for(int i=0; i<prevLevelOutput.getRowDimension();i++){
           kResults[i][0]=calculateSingleIncomingDeltaRate(prevLevelOutput, i, incomingWforOutput,expectedResult);
        }

        return  MatrixUtils.createRealMatrix(kResults);

    }

    public double calculateSingleIncomingDeltaRate(RealMatrix prevLevelOutput, int prevLevelNeuronIndex, RealMatrix incomingWforOutput, double expectedResult){
        RealMatrix iwSum = incomingWforOutput.multiply(prevLevelOutput);

        double c = Math.exp(prevLevelOutput.getEntry(prevLevelNeuronIndex,0)*incomingWforOutput.getEntry(0,prevLevelNeuronIndex));
        double a = Math.exp(iwSum.getEntry(0,0)-prevLevelOutput.getEntry(prevLevelNeuronIndex,0)*incomingWforOutput.getEntry(0,prevLevelNeuronIndex));
        double m = expectedResult/(a-expectedResult*a);
        return Math.log(m)/Math.log(c);

    }

    public RealMatrix getSprededOutputRates(RealMatrix standaloneAdjustmentsPerOutputNeuron, double outputDeltaRatio){
        double[][] spreadedOutputRates = new double[standaloneAdjustmentsPerOutputNeuron.getRowDimension()][1];
       double kMax=getMaxRate(standaloneAdjustmentsPerOutputNeuron);
       double sum = 0;
       for(double value:standaloneAdjustmentsPerOutputNeuron.getColumn(0)){
           sum += kMax/value;
       }
       double x = (outputDeltaRatio-1)/sum;
        double prevVal =1;
       for(int i =0;i<standaloneAdjustmentsPerOutputNeuron.getRowDimension();i++){
           double p = (kMax/standaloneAdjustmentsPerOutputNeuron.getEntry(i,0))*x;
           spreadedOutputRates[i][0]=(prevVal+p)/prevVal;
           prevVal=prevVal+p;
       }

       return MatrixUtils.createRealMatrix(spreadedOutputRates);

    }

    public void spreadTmpRatesBetweenInputsAndWeights(RealMatrix tmpInputRates, RealMatrix tmpWRates, List<RealMatrix> matrixWList,double[] levelDeltaRatesPerIncomeNeuron, int currentLevelIndex, int outputNeuronIndex, RealMatrix prevOutputsValues){

        int incomeLayerIndex = currentLevelIndex-1;
        if(incomeLayerIndex==0){
            applyAdjustmentsForW(matrixWList,incomeLayerIndex,outputNeuronIndex, levelDeltaRatesPerIncomeNeuron);
            return;
        }

        double[][] incomeOutputDeltaRates = new double[levelDeltaRatesPerIncomeNeuron.length][1];

        int amountOfIncomingSynapsPerIncomeNeuron = matrixWList.get(incomeLayerIndex-1).getColumnDimension();
        double maxPossibleIncomeValueForSigmoid = amountOfIncomingSynapsPerIncomeNeuron*DECREASE_RATE_FOR_MAX_POSSIBLE_INPUT_VALUE;
        double maxPossibleIncomeOutputValue = 1/(1+Math.exp(-maxPossibleIncomeValueForSigmoid));

        for(int i=0; i<levelDeltaRatesPerIncomeNeuron.length; i++){
            double maxPossibleIncomeOutputDeltaRate = maxPossibleIncomeOutputValue/prevOutputsValues.getEntry(i,0);
            double currentWvalue = matrixWList.get(incomeLayerIndex).getEntry(outputNeuronIndex,i);
            double maxPossibleWDeltaRate = 1/currentWvalue;

            double wk = 1;
            double ik = 1;

            if(maxPossibleIncomeOutputDeltaRate*maxPossibleWDeltaRate<=levelDeltaRatesPerIncomeNeuron[i]){
                wk=maxPossibleWDeltaRate;
                ik = maxPossibleIncomeOutputDeltaRate;

            }else{
                double n = maxPossibleIncomeOutputDeltaRate/maxPossibleWDeltaRate;
                double x = (levelDeltaRatesPerIncomeNeuron[i]-1)/(n+1);
                wk = 1+x;
                ik = (1+x+n*x)/(1+x);

                if(ik>=maxPossibleIncomeOutputDeltaRate){
                   ik=maxPossibleIncomeOutputDeltaRate;
                   wk = levelDeltaRatesPerIncomeNeuron[i]/ik;
                }else if(wk>=maxPossibleWDeltaRate){
                   wk = maxPossibleWDeltaRate;
                   ik=levelDeltaRatesPerIncomeNeuron[i]/wk;

                }
            }

            tmpInputRates.setEntry(outputNeuronIndex,i, ik);
            tmpWRates.setEntry(outputNeuronIndex,i,wk);

        }


    }


    public RealMatrix spreadRatesBetweenInputsandWeights(RealMatrix tmpInputRates, RealMatrix tmpWRates,List<RealMatrix> matrixWList, int currentLevelIndex){

        int incomeLayerIndex = currentLevelIndex-1;

        if(incomeLayerIndex==0){
           return null;
        }

        double[][] incomeOutputDeltaRates = new double[tmpInputRates.getColumnDimension()][1];

        double[] maxInputRate = new double[tmpInputRates.getColumnDimension()];
        for(int inputIndex=0; inputIndex<tmpInputRates.getColumnDimension(); inputIndex++){
            for(int outputIndex = 0; outputIndex<tmpInputRates.getRowDimension(); outputIndex++){
                if(tmpInputRates.getEntry(outputIndex,inputIndex)>maxInputRate[inputIndex]){
                    maxInputRate[inputIndex]=tmpInputRates.getEntry(outputIndex,inputIndex);

                }
            }
        }

        for(int i=0; i<incomeOutputDeltaRates.length; i++){
            incomeOutputDeltaRates[i][0] = maxInputRate[i];
        }


        for(int outputIndex =0; outputIndex<tmpWRates.getRowDimension(); outputIndex++){
            for(int inputIndex=0; inputIndex<tmpWRates.getColumnDimension(); inputIndex++){
                double wk =tmpWRates.getEntry(outputIndex,inputIndex)*tmpInputRates.getEntry(outputIndex,inputIndex)/maxInputRate[inputIndex];
                adjustSingleWValue(matrixWList,incomeLayerIndex,outputIndex,inputIndex,wk);
            }
        }

        return MatrixUtils.createRealMatrix(incomeOutputDeltaRates);
    }

    private void applyAdjustmentsForW(List<RealMatrix> matrixWList, int currentLevelIndex,int outputNeuronIndex, double[] levelDeltaRates) {
        for(int i=0; i<levelDeltaRates.length; i++){
            adjustSingleWValue(matrixWList,currentLevelIndex,outputNeuronIndex,i,levelDeltaRates[i]);
        }
    }

    public RealMatrix getExpectedResults(RealMatrix actualResults, RealMatrix inputDeltaRates){
        double[][] expectedReults = new double[actualResults.getRowDimension()][1];
        for(int i =0; i<actualResults.getRowDimension(); i++){
            double expectedReult = actualResults.getEntry(i,0)*inputDeltaRates.getEntry(i,0);
            expectedReults[i][0]= expectedReult;
        }
        return MatrixUtils.createRealMatrix(expectedReults);
    }

    private double getMaxRate(RealMatrix standaloneRates){
        double maxVal = standaloneRates.getEntry(0,0);
        for(double value:standaloneRates.getColumn(0)){
            if(Math.abs(value)>Math.abs(maxVal)){
                maxVal=value;
            }
        }
        return maxVal;
    }


    private RealMatrix sigmoid(RealMatrix intermidiateInputMatrix) {
        double[][] result = new double[intermidiateInputMatrix.getRowDimension()][1];
        int i = 0;
        for(double itemValue:intermidiateInputMatrix.getColumn(0)){
            result[i][0]= 1/(1+Math.exp(-itemValue));
            i++;
        }
        return MatrixUtils.createRealMatrix(result);
    }

    public RealMatrix buildMatrix(int rowNumber, int columnNumber){
      return  MatrixUtils.createRealMatrix(new double[rowNumber][columnNumber]);
    }

    private void initValues(double[][] matrixW21) {
        for(int rowIndex=0;rowIndex<matrixW21.length;rowIndex++){
            for(int columnIndex=0;columnIndex<matrixW21[rowIndex].length;columnIndex++ ){
                matrixW21[rowIndex][columnIndex]=Math.random();
            }
        }
    }

    private void initWITest(RealMatrix matrixDataW21) {
        matrixDataW21.setEntry(0,0,0.5);
        matrixDataW21.setEntry(1,0,0.34);
        matrixDataW21.setEntry(2,0,0.12);
    }
    private void initW32Test(RealMatrix matrixDataW32) {
        matrixDataW32.setEntry(0,0,0.5);
        matrixDataW32.setEntry(1,0,0.5);
        matrixDataW32.setEntry(2,0,0.5);

        matrixDataW32.setEntry(0,1,0.34);
        matrixDataW32.setEntry(1,1,0.34);
        matrixDataW32.setEntry(2,1,0.34);

        matrixDataW32.setEntry(0,2,0.12);
        matrixDataW32.setEntry(1,2,0.12);
        matrixDataW32.setEntry(2,2,0.12);
    }
    private void initWOTest(RealMatrix matrixDataW43) {
        matrixDataW43.setEntry(0,0,0.34);
        matrixDataW43.setEntry(0,1,0.5);
        matrixDataW43.setEntry(0,2,0.12);
    }


}
