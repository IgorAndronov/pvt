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

    final static int COLUMN_SIZE_ONE = 1;
    final static int COLUMN_INDEX_ZERO = 0;
    final static int ROW_INDEX_ZERO = 0;
    final static double LEARNING_SPEED_RATE =0.5;

    static final double DECREASE_RATE_FOR_MAX_POSSIBLE_INPUT_VALUE = 0.93;

    public void trainNetwork(List<RealMatrix> matrixWList, RealMatrix matrixI, RealMatrix expectedResults){
        int maxLayerIndex = matrixWList.size();

        RealMatrix acualFinalResults = calcLevelOutput(matrixI ,matrixWList,maxLayerIndex);
        RealMatrix inputDeltaRates= getOutputDeltaRatio(acualFinalResults,expectedResults, LEARNING_SPEED_RATE);

        for(int currentLayerIndex=maxLayerIndex; currentLayerIndex>0; currentLayerIndex--){
            int neuronsPerLayer = matrixWList.get(currentLayerIndex-1).getRowDimension();

            RealMatrix acualResultsMatrix = calcLevelOutput(matrixI ,matrixWList,currentLayerIndex);
            expectedResults = getExpectedResults(acualResultsMatrix, inputDeltaRates);

            RealMatrix outputDeltaRates = inputDeltaRates;

            RealMatrix tmpInputDeltaRates = buildMatrix(neuronsPerLayer,matrixWList.get(currentLayerIndex-1).getColumnDimension());
            RealMatrix tmpWDeltaRates = buildMatrix(neuronsPerLayer,matrixWList.get(currentLayerIndex-1).getColumnDimension());

            for(int outputNeuronIndex=0; outputNeuronIndex<neuronsPerLayer; outputNeuronIndex++){
                RealMatrix prevLevelOutput = calcLevelOutput(matrixI ,matrixWList,currentLayerIndex-1);
                RealMatrix incomingW = matrixWList.get(currentLayerIndex-1);
                RealMatrix independentAdjustments = calcAdjustmentsPerOutputNeuron(prevLevelOutput,
                        incomingW,
                        expectedResults.getEntry(outputNeuronIndex,COLUMN_INDEX_ZERO),
                        outputNeuronIndex);
                RealMatrix spreadedOutputRates = getSprededOutputRates(independentAdjustments,outputDeltaRates.getEntry(outputNeuronIndex,COLUMN_INDEX_ZERO));

                double[] kArray = new double[spreadedOutputRates.getRowDimension()];
                double k = 1;

                RealMatrix savedLayerIndexes = matrixWList.get(currentLayerIndex-1).copy();
                RealMatrix prevOutputsValues =   calcLevelOutput(matrixI ,matrixWList,currentLayerIndex-1);
                for(int j = 0; j<spreadedOutputRates.getRowDimension(); j++){
                    int columnToAjustIndex= j==0? 0:j-1;
                    RealMatrix prevOutputsW = adjustSingleWValue (matrixWList,currentLayerIndex-1,outputNeuronIndex,columnToAjustIndex, k);
                    double newExpectedResult = calcLevelOutput(matrixI ,matrixWList,currentLayerIndex).getEntry(outputNeuronIndex,COLUMN_INDEX_ZERO)*spreadedOutputRates.getEntry(j,COLUMN_INDEX_ZERO);
                    k=  calculateSingleIncomingDeltaRate(prevOutputsValues, j,prevOutputsW,newExpectedResult,outputNeuronIndex);
                    kArray[j]=k;
                }

                matrixWList.set(currentLayerIndex-1,savedLayerIndexes);
                spreadTmpRatesBetweenInputsAndWeights(tmpInputDeltaRates,tmpWDeltaRates,matrixWList,kArray,currentLayerIndex,outputNeuronIndex, prevOutputsValues);

            }

            inputDeltaRates = spreadRatesBetweenInputsandWeights(tmpInputDeltaRates,tmpWDeltaRates,matrixWList,currentLayerIndex, matrixI, currentLayerIndex);


        }


    }

    public List<RealMatrix> build(int inputsNumber, int outputNumbers, int inermidiateLevelsNumber, int neuronsPerlayerNumber){

        List<RealMatrix> matricesW = new ArrayList();

        RealMatrix matrixDataWI =buildMatrix(neuronsPerlayerNumber,inputsNumber);
        //initWITest(matrixDataWI);
        initValues(matrixDataWI);
        matricesW.add(matrixDataWI);

        for(int i=0; i<inermidiateLevelsNumber-1;i++){
            RealMatrix matrixW = buildMatrix(neuronsPerlayerNumber,neuronsPerlayerNumber);
           // initW32Test(matrixW);
            initValues(matrixW);
            matricesW.add(matrixW);
        }

        RealMatrix matrixDataWO =buildMatrix(outputNumbers,neuronsPerlayerNumber);
       // initWOTest(matrixDataWO);
        initValues(matrixDataWO);
        matricesW.add(matrixDataWO);

        return matricesW;
    }

    public RealMatrix calcLevelOutput(RealMatrix inputMatrix, List<RealMatrix> matrixWList, int level){
        RealMatrix intermidiateOutputMatrix = inputMatrix;
        for(int currentLevel = 0; currentLevel<level; currentLevel++){
            RealMatrix intermidiateInputMatrix= matrixWList.get(currentLevel).multiply(intermidiateOutputMatrix);
            intermidiateOutputMatrix = sigmoid(intermidiateInputMatrix);
        }
        return intermidiateOutputMatrix;

    }

    public RealMatrix adjustSingleWValue(List<RealMatrix> matrixWList, int level, int rownum, int columnNum, double deltaRate){
        RealMatrix matrixW = matrixWList.get(level);
        matrixW.setEntry(rownum,columnNum,matrixW.getEntry(rownum,columnNum)*deltaRate);
        return matrixW;
    }

    public RealMatrix getOutputDeltaRatio(RealMatrix actualResult, RealMatrix expectedResult, double lerningSpeedRate){
        double[][] result = new double[actualResult.getRowDimension()][1];
        for(int i = 0;i<actualResult.getRowDimension();i++){
            double deltaRate = expectedResult.getEntry(i,COLUMN_INDEX_ZERO)/ actualResult.getEntry(i,COLUMN_INDEX_ZERO);

            result[i][COLUMN_INDEX_ZERO] = (deltaRate-1)*lerningSpeedRate+1;
        }

        return MatrixUtils.createRealMatrix(result);
    }

    public RealMatrix calcAdjustmentsPerOutputNeuron(RealMatrix prevLevelOutput, RealMatrix incomingW, double expectedResult, int outputNeuronIndex){
        double[][] kResults = new double[prevLevelOutput.getRowDimension()][1];

        for(int i=0; i<prevLevelOutput.getRowDimension();i++){
           kResults[i][0]=calculateSingleIncomingDeltaRate(prevLevelOutput, i, incomingW,expectedResult,outputNeuronIndex);
        }

        return  MatrixUtils.createRealMatrix(kResults);

    }

    public double calculateSingleIncomingDeltaRate(RealMatrix prevLevelOutput, int prevLevelNeuronIndex, RealMatrix incomingW, double expectedResult,int outputNeuronIndex){
        RealMatrix iwSum = incomingW.getRowMatrix(outputNeuronIndex).multiply(prevLevelOutput);

        double c = Math.exp(prevLevelOutput.getEntry(prevLevelNeuronIndex,COLUMN_INDEX_ZERO)*incomingW.getEntry(outputNeuronIndex,prevLevelNeuronIndex));
        double a = Math.exp(iwSum.getEntry(ROW_INDEX_ZERO,COLUMN_INDEX_ZERO)-prevLevelOutput.getEntry(prevLevelNeuronIndex,COLUMN_INDEX_ZERO)*incomingW.getEntry(outputNeuronIndex,prevLevelNeuronIndex));
        double m = expectedResult/(a*(1-expectedResult));
        if(m<0 || c<0 ||a<0){
            System.out.println("Error logarifm!!!");
        }
        return Math.log(m)/Math.log(c);

    }

    public RealMatrix getSprededOutputRates(RealMatrix standaloneAdjustmentsPerOutputNeuron, double outputDeltaRatio){
        double[][] spreadedOutputRates = new double[standaloneAdjustmentsPerOutputNeuron.getRowDimension()][1];
       double kMax=getMaxRate(standaloneAdjustmentsPerOutputNeuron);
       double sum = 0;
       for(double value:standaloneAdjustmentsPerOutputNeuron.getColumn(COLUMN_INDEX_ZERO)){
           sum += Math.abs(kMax/value);
       }
       double x = (outputDeltaRatio-1)/sum;
       double prevVal =1;
       for(int i =0;i<standaloneAdjustmentsPerOutputNeuron.getRowDimension();i++){
           if(outputDeltaRatio==1){
               spreadedOutputRates[i][0]=1;

           }else{
               double p = Math.abs((kMax/standaloneAdjustmentsPerOutputNeuron.getEntry(i,COLUMN_INDEX_ZERO))*x);
               p = outputDeltaRatio>1?p:-p;
               spreadedOutputRates[i][0]=(prevVal+p)/prevVal;
               prevVal=prevVal+p;
           }

       }
       //////////////////////////////////////////
       double result = 1;
        for(int i =0;i<standaloneAdjustmentsPerOutputNeuron.getRowDimension();i++){
            result=result*spreadedOutputRates[i][0];
            if(spreadedOutputRates[i][0]<0){
                System.out.println("Error!!!");
            }

       }
       if((result-outputDeltaRatio)/outputDeltaRatio>0.01){
           System.out.println("Error!!! result="+result+" outputDeltaRatio="+outputDeltaRatio);
       }

       /////////////////////////////////
       return MatrixUtils.createRealMatrix(spreadedOutputRates);

    }

    public void spreadTmpRatesBetweenInputsAndWeights(RealMatrix tmpInputRates, RealMatrix tmpWRates, List<RealMatrix> matrixWList,double[] levelDeltaRatesPerIncomeNeuron, int currentLevelIndex, int outputNeuronIndex, RealMatrix prevOutputsValues){

        int incomeLayerIndex = currentLevelIndex-1;
        if(incomeLayerIndex==0){
            applyAdjustmentsForW(matrixWList,incomeLayerIndex,outputNeuronIndex, levelDeltaRatesPerIncomeNeuron);
            return;
        }


        int amountOfIncomingSynapsPerIncomeNeuron = matrixWList.get(incomeLayerIndex-1).getColumnDimension();
        double maxPossibleIncomeValueForSigmoid = amountOfIncomingSynapsPerIncomeNeuron*DECREASE_RATE_FOR_MAX_POSSIBLE_INPUT_VALUE;
        double maxPossibleIncomeOutputValue = 1/(1+Math.exp(-maxPossibleIncomeValueForSigmoid));

        for(int i=0; i<levelDeltaRatesPerIncomeNeuron.length; i++){
            double maxPossibleIncomeOutputDeltaRate = maxPossibleIncomeOutputValue/prevOutputsValues.getEntry(i,COLUMN_INDEX_ZERO);
            double currentWvalue = matrixWList.get(incomeLayerIndex).getEntry(outputNeuronIndex,i);
            double maxPossibleWDeltaRate = Math.abs(1/currentWvalue);

            double wk = 1;
            double ik = 1;

            if(Math.abs(maxPossibleIncomeOutputDeltaRate*maxPossibleWDeltaRate)<=Math.abs(levelDeltaRatesPerIncomeNeuron[i])){
                wk=levelDeltaRatesPerIncomeNeuron[i]>0? Math.abs(maxPossibleWDeltaRate):-Math.abs(maxPossibleWDeltaRate);
                ik = maxPossibleIncomeOutputDeltaRate;

            }else{
                double n = Math.abs(maxPossibleIncomeOutputDeltaRate/maxPossibleWDeltaRate);
                double x = (Math.abs(levelDeltaRatesPerIncomeNeuron[i])-1)/(n+1);
                wk=levelDeltaRatesPerIncomeNeuron[i]>0? Math.abs(1+x):-Math.abs(1+x);
                ik = Math.abs((1+x+n*x)/(1+x));

                if(ik>=maxPossibleIncomeOutputDeltaRate){
                   ik=maxPossibleIncomeOutputDeltaRate;
                   wk = levelDeltaRatesPerIncomeNeuron[i]/ik;
                }else if(wk>=maxPossibleWDeltaRate){
                    wk=levelDeltaRatesPerIncomeNeuron[i]>0? Math.abs(maxPossibleWDeltaRate):-Math.abs(maxPossibleWDeltaRate);
                   ik=levelDeltaRatesPerIncomeNeuron[i]/wk;

                }
            }

            tmpInputRates.setEntry(outputNeuronIndex,i, ik);
            tmpWRates.setEntry(outputNeuronIndex,i,wk);

        }


    }


    public RealMatrix spreadRatesBetweenInputsandWeights(RealMatrix tmpWinputRates, RealMatrix tmpWwRates,List<RealMatrix> matrixWList, int currentLevelIndex, RealMatrix matrixI, int currentLayerIndex){

        int incomeLayerIndex = currentLevelIndex-1;

        if(incomeLayerIndex==0){
           return null;
        }

        double[][] incomeOutputDeltaRates = new double[tmpWinputRates.getColumnDimension()][COLUMN_SIZE_ONE];

        double[] maxWInputRate = new double[tmpWinputRates.getColumnDimension()];
        for(int inputIndex=0; inputIndex<tmpWinputRates.getColumnDimension(); inputIndex++){
            for(int outputIndex = 0; outputIndex<tmpWinputRates.getRowDimension(); outputIndex++){
                if(tmpWinputRates.getEntry(outputIndex,inputIndex)>maxWInputRate[inputIndex]){
                    maxWInputRate[inputIndex]=tmpWinputRates.getEntry(outputIndex,inputIndex);

                }
            }
        }

        for(int i=0; i<maxWInputRate.length;i++){
            incomeOutputDeltaRates[i][COLUMN_INDEX_ZERO] = maxWInputRate[i];
        }


        for(int outputIndex =0; outputIndex<tmpWwRates.getRowDimension(); outputIndex++){
            for(int inputIndex=0; inputIndex<tmpWwRates.getColumnDimension(); inputIndex++){
                double wk =tmpWwRates.getEntry(outputIndex,inputIndex)*tmpWinputRates.getEntry(outputIndex,inputIndex)/maxWInputRate[inputIndex];
                adjustSingleWValue(matrixWList,incomeLayerIndex,outputIndex,inputIndex,wk);
            }
        }

        ///////////////////////////////
        RealMatrix acualResultsMatrix = calcLevelOutput(matrixI ,matrixWList,incomeLayerIndex);
        RealMatrix expectedResults = getExpectedResults(acualResultsMatrix, MatrixUtils.createRealMatrix(incomeOutputDeltaRates));
        for(int i = 0; i< expectedResults.getRowDimension(); i++){
            if(expectedResults.getEntry(i,0)>1){
                System.out.println("Error!!!");
            }
        }
        //////////////////////////////

        return MatrixUtils.createRealMatrix(incomeOutputDeltaRates);
    }

    private void applyAdjustmentsForW(List<RealMatrix> matrixWList, int currentLevelIndex,int outputNeuronIndex, double[] levelDeltaRates) {
        for(int i=0; i<levelDeltaRates.length; i++){
            adjustSingleWValue(matrixWList,currentLevelIndex,outputNeuronIndex,i,levelDeltaRates[i]);
        }
    }

    public RealMatrix getExpectedResults(RealMatrix actualResults, RealMatrix inputDeltaRates){
        double[][] expectedReults = new double[actualResults.getRowDimension()][COLUMN_SIZE_ONE];
        for(int i =0; i<actualResults.getRowDimension(); i++){
            double expectedReult = actualResults.getEntry(i,COLUMN_INDEX_ZERO)*inputDeltaRates.getEntry(i,COLUMN_INDEX_ZERO);
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

    private void initValues(RealMatrix matrixW) {
        for(int rowIndex=0;rowIndex<matrixW.getRowDimension();rowIndex++){
            for(int columnIndex=0;columnIndex<matrixW.getColumnDimension();columnIndex++ ){
                matrixW.setEntry(rowIndex,columnIndex,0.4*Math.random()+0.3);  //initial range from 0.3 to 0.7
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
