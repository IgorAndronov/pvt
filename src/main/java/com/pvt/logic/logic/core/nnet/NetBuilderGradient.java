package com.pvt.logic.logic.core.nnet;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.*;

/**
 * Created by admin on 20.01.2018.
 */
public class NetBuilderGradient {

    final static int COLUMN_SIZE_ONE = 1;
    final static int COLUMN_INDEX_ZERO = 0;
    final static int ROW_INDEX_ZERO = 0;
    final static double ZERO_VALUE = 0.000001;
    final static double LEARNING_SPEED_RATE =0.1;
    final static RealMatrix ZERO_LAYER_EMPTY_STUB =null;

    static final double DECREASE_RATE_FOR_MAX_POSSIBLE_INPUT_VALUE = 0.93;

    public void trainNetwork(List<RealMatrix> matrixWList, RealMatrix matrixI, RealMatrix expectedResults){
        int maxLayerIndex = matrixWList.size();

        RealMatrix acualFinalResults = calclayerOutput(matrixI ,matrixWList,maxLayerIndex);
        RealMatrix inputDeltaRates= getOutputDeltaRatio(acualFinalResults,expectedResults, LEARNING_SPEED_RATE);

        for(int currentLayerIndex=maxLayerIndex; currentLayerIndex>0; currentLayerIndex--){
            int neuronsPerLayer = matrixWList.get(currentLayerIndex-1).getRowDimension();

            RealMatrix acualResultsMatrix = calclayerOutput(matrixI ,matrixWList,currentLayerIndex);
            expectedResults = getExpectedResults(acualResultsMatrix, inputDeltaRates);

            RealMatrix outputDeltaRates = inputDeltaRates;

            RealMatrix tmpInputDeltaRates = buildMatrix(neuronsPerLayer,matrixWList.get(currentLayerIndex-1).getColumnDimension());
            RealMatrix tmpWDeltaRates = buildMatrix(neuronsPerLayer,matrixWList.get(currentLayerIndex-1).getColumnDimension());

            for(int outputNeuronIndex=0; outputNeuronIndex<neuronsPerLayer; outputNeuronIndex++){
                RealMatrix prevlayerOutput = calclayerOutput(matrixI ,matrixWList,currentLayerIndex-1);
                RealMatrix incomingW = matrixWList.get(currentLayerIndex-1);
                if(expectedResults.getEntry(outputNeuronIndex,COLUMN_INDEX_ZERO)!=expectedResults.getEntry(outputNeuronIndex,COLUMN_INDEX_ZERO)){
                    System.out.println("Error NaN!!!");
                }
                RealMatrix independentAdjustments = calcAdjustmentsPerOutputNeuron(prevlayerOutput,
                        incomingW,
                        expectedResults.getEntry(outputNeuronIndex,COLUMN_INDEX_ZERO),
                        outputNeuronIndex);
                RealMatrix spreadedOutputRates = getSprededOutputRates(independentAdjustments,outputDeltaRates.getEntry(outputNeuronIndex,COLUMN_INDEX_ZERO));

                double[] kArray = new double[spreadedOutputRates.getRowDimension()];
                double k = 1;

                RealMatrix savedLayerIndexes = matrixWList.get(currentLayerIndex-1).copy();
                RealMatrix prevOutputsValues =   calclayerOutput(matrixI ,matrixWList,currentLayerIndex-1);
                for(int j = 0; j<spreadedOutputRates.getRowDimension(); j++){
                    int columnToAjustIndex= j==0? 0:j-1;
                    RealMatrix prevOutputsW = adjustSingleWValue (matrixWList,currentLayerIndex-1,outputNeuronIndex,columnToAjustIndex, k);
                    double newExpectedResult = calclayerOutput(matrixI ,matrixWList,currentLayerIndex).getEntry(outputNeuronIndex,COLUMN_INDEX_ZERO)*spreadedOutputRates.getEntry(j,COLUMN_INDEX_ZERO);
                    if(newExpectedResult!=newExpectedResult){
                        System.out.println("Error NaN!!!");
                    }
                    newExpectedResult = calclayerOutput(matrixI ,matrixWList,currentLayerIndex).getEntry(outputNeuronIndex,COLUMN_INDEX_ZERO)*spreadedOutputRates.getEntry(j,COLUMN_INDEX_ZERO);

                    k=  calculateSingleIncomingDeltaRate(prevOutputsValues, j,prevOutputsW,newExpectedResult,outputNeuronIndex);
                    if(k!=k){
                        System.out.println("Error NaN!!!");
                    }
                    kArray[j]=k;
                }

                matrixWList.set(currentLayerIndex-1,savedLayerIndexes);
                spreadTmpRatesBetweenInputsAndWeights(tmpInputDeltaRates,tmpWDeltaRates,matrixWList,kArray,currentLayerIndex,outputNeuronIndex, prevOutputsValues);

            }

            inputDeltaRates = spreadRatesBetweenInputsandWeights(tmpInputDeltaRates,tmpWDeltaRates,matrixWList,currentLayerIndex, matrixI, currentLayerIndex, LEARNING_SPEED_RATE);


        }


    }

    public List<RealMatrix> build(List<Integer> inermidiateLayersNeuronsNumber, boolean isBiasMatrix){

        List<RealMatrix> matricesW = new ArrayList();
        matricesW.add(ZERO_LAYER_EMPTY_STUB);

        for(int i = 0; i< inermidiateLayersNeuronsNumber.size()-1; i++){
            int neuronsPerCurrentlayerNumber=isBiasMatrix? COLUMN_SIZE_ONE:inermidiateLayersNeuronsNumber.get(i);
            int neuronsPerNextlayerNumber=inermidiateLayersNeuronsNumber.get(i+1);
            RealMatrix matrixW = buildMatrix(neuronsPerNextlayerNumber,neuronsPerCurrentlayerNumber);
            initValues(matrixW,false, null);
            matricesW.add(matrixW);
        }


        return matricesW;
    }


    //Z=W*I+B;  I=A of the previous layer; A=sigmoid(Z); A- actiavation function of the layer (layer output)
    public void calcLayersOutputs(RealMatrix inputMatrix, List<RealMatrix> matrixWList, List<RealMatrix> matrixBList, List<RealMatrix> layersOutputs, List<RealMatrix> layersZ ){
        layersOutputs.add(ZERO_LAYER_EMPTY_STUB);
        layersZ.add(ZERO_LAYER_EMPTY_STUB);

        RealMatrix intermidiateOutputMatrix = inputMatrix;
        int outputLayerIndex = matrixWList.size()-1;
        for(int currentLayer = 1; currentLayer<=outputLayerIndex; currentLayer++){
            RealMatrix intermidiateInputMatrix= matrixWList.get(currentLayer).multiply(intermidiateOutputMatrix).add(matrixBList.get(currentLayer));
            layersZ.add(intermidiateInputMatrix);
            intermidiateOutputMatrix = sigmoid(intermidiateInputMatrix);
            layersOutputs.add(intermidiateOutputMatrix);
        }
        return;

    }

    //activation values of the level are sigmoids of the z
    public List<RealMatrix> calcLayersDeltas(List<RealMatrix> layersOutputs, List<RealMatrix> layersZ , RealMatrix expectedResult, List<RealMatrix> matrixWList){
        List<RealMatrix> layersDeltasList= new ArrayList<>(layersOutputs.size());

        for(int currentLayerIndex =layersOutputs.size()-1; currentLayerIndex>0; currentLayerIndex--){
            int neuronsInTheLayer = layersOutputs.get(currentLayerIndex).getRowDimension();

            RealMatrix layerActivations = sigmoid(layersZ.get(currentLayerIndex));
            double[][] unitValues = new double[neuronsInTheLayer][COLUMN_SIZE_ONE];
            RealMatrix unitVector = MatrixUtils.createRealMatrix(unitValues);
            RealMatrix activationLayerDerivative= getHadamardMultiplication(layerActivations,unitVector.subtract(layerActivations));

            RealMatrix layerDeltas= null;
            if(currentLayerIndex==(layersOutputs.size()-1)){
                layerDeltas = getHadamardMultiplication(layersOutputs.get(currentLayerIndex).subtract(expectedResult),activationLayerDerivative);

            }else{
                layerDeltas=getHadamardMultiplication(matrixWList.get(currentLayerIndex+1).transpose().multiply(layerDeltas),activationLayerDerivative);
            }

            layersDeltasList.add(currentLayerIndex,layerDeltas);

        }


        return layersDeltasList;


    }

    public RealMatrix getHadamardMultiplication(RealMatrix v1, RealMatrix v2){
        RealMatrix result = MatrixUtils.createRealMatrix(new double[v1.getRowDimension()][COLUMN_SIZE_ONE]);
        for(int i =0; i<v1.getRowDimension();i++){
            result.setEntry(i,COLUMN_INDEX_ZERO,v1.getEntry(i,COLUMN_INDEX_ZERO)*v2.getEntry(i,COLUMN_INDEX_ZERO));
        }

        return result;
    }

    public RealMatrix adjustSingleWValue(List<RealMatrix> matrixWList, int layer, int rownum, int columnNum, double deltaRate){
        RealMatrix matrixW = matrixWList.get(layer);
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

    public RealMatrix calcAdjustmentsPerOutputNeuron(RealMatrix prevlayerOutput, RealMatrix incomingW, double expectedResult, int outputNeuronIndex){
        double[][] kResults = new double[prevlayerOutput.getRowDimension()][1];

        for(int i=0; i<prevlayerOutput.getRowDimension();i++){
            kResults[i][0]=calculateSingleIncomingDeltaRate(prevlayerOutput, i, incomingW,expectedResult,outputNeuronIndex);
            if( kResults[i][0]!=kResults[i][0] || Double.isInfinite(kResults[i][0]) || !Double.isFinite(kResults[i][0])){
                System.out.println("!!!Error NaN");
            }
        }

        return  MatrixUtils.createRealMatrix(kResults);

    }

    public double calculateSingleIncomingDeltaRate(RealMatrix prevlayerOutput, int prevlayerNeuronIndex, RealMatrix incomingW, double expectedResult,int outputNeuronIndex){
        RealMatrix iwSum = incomingW.getRowMatrix(outputNeuronIndex).multiply(prevlayerOutput);

        double c = Math.exp(prevlayerOutput.getEntry(prevlayerNeuronIndex,COLUMN_INDEX_ZERO)*incomingW.getEntry(outputNeuronIndex,prevlayerNeuronIndex));
        double a = Math.exp(iwSum.getEntry(ROW_INDEX_ZERO,COLUMN_INDEX_ZERO)-prevlayerOutput.getEntry(prevlayerNeuronIndex,COLUMN_INDEX_ZERO)*incomingW.getEntry(outputNeuronIndex,prevlayerNeuronIndex));
        double m = expectedResult/(a*(1-expectedResult));

        double result =  Math.log(m)/Math.log(c);

        if(!Double.isFinite(result)){
            result=1;
        }


        return result;

    }

    public RealMatrix getSprededOutputRates(RealMatrix standaloneAdjustmentsPerOutputNeuron, double outputDeltaRatio){
        double[][] spreadedOutputRates = new double[standaloneAdjustmentsPerOutputNeuron.getRowDimension()][1];
        double kMax=getMaxRate(standaloneAdjustmentsPerOutputNeuron);
        double sum = 0;
        boolean zeroExists = false;
        for(double value:standaloneAdjustmentsPerOutputNeuron.getColumn(COLUMN_INDEX_ZERO)){
            sum += Math.abs(kMax/value);
            zeroExists = (Math.abs(value)<ZERO_VALUE)?true:zeroExists;
        }
        double x = (outputDeltaRatio-1)/sum;
        double prevVal =1;
        for(int i =0;i<standaloneAdjustmentsPerOutputNeuron.getRowDimension();i++){
            if(outputDeltaRatio==1){
                spreadedOutputRates[i][0]=1;

            }else{
                if(zeroExists){
                    spreadedOutputRates[i][0]= (Math.abs(standaloneAdjustmentsPerOutputNeuron.getEntry(i,COLUMN_INDEX_ZERO))<ZERO_VALUE)?0:1;
                }else{
                    double p = Math.abs((kMax/standaloneAdjustmentsPerOutputNeuron.getEntry(i,COLUMN_INDEX_ZERO))*x);
                    p = outputDeltaRatio>1?p:-p;
                    spreadedOutputRates[i][0]=(prevVal+p)/prevVal;
                    if(spreadedOutputRates[i][0]!=spreadedOutputRates[i][0]){
                        System.out.println("Error!!! NaN");
                    }
                    prevVal=prevVal+p;
                }

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

    public void spreadTmpRatesBetweenInputsAndWeights(RealMatrix tmpInputRates, RealMatrix tmpWRates, List<RealMatrix> matrixWList,double[] layerDeltaRatesPerIncomeNeuron, int currentlayerIndex, int outputNeuronIndex, RealMatrix prevOutputsValues){

        int incomeLayerIndex = currentlayerIndex-1;
        if(incomeLayerIndex==0){
            applyAdjustmentsForW(matrixWList,incomeLayerIndex,outputNeuronIndex, layerDeltaRatesPerIncomeNeuron);
            return;
        }


        int amountOfIncomingSynapsPerIncomeNeuron = matrixWList.get(incomeLayerIndex-1).getColumnDimension();
        double maxPossibleIncomeValueForSigmoid = amountOfIncomingSynapsPerIncomeNeuron*DECREASE_RATE_FOR_MAX_POSSIBLE_INPUT_VALUE;
        double maxPossibleIncomeOutputValue = 1/(1+Math.exp(-maxPossibleIncomeValueForSigmoid));

        for(int i=0; i<layerDeltaRatesPerIncomeNeuron.length; i++){
            double maxPossibleIncomeOutputDeltaRate = maxPossibleIncomeOutputValue/prevOutputsValues.getEntry(i,COLUMN_INDEX_ZERO);
            double currentWvalue = matrixWList.get(incomeLayerIndex).getEntry(outputNeuronIndex,i);
            double maxPossibleWDeltaRate = Math.abs(1/currentWvalue);

            double wk = 1;
            double ik = 1;

            if(Math.abs(maxPossibleIncomeOutputDeltaRate*maxPossibleWDeltaRate)<=Math.abs(layerDeltaRatesPerIncomeNeuron[i])){
                wk=layerDeltaRatesPerIncomeNeuron[i]>0? Math.abs(maxPossibleWDeltaRate):-Math.abs(maxPossibleWDeltaRate);
                ik = maxPossibleIncomeOutputDeltaRate;

            }else{
                double n = Math.abs(maxPossibleIncomeOutputDeltaRate/maxPossibleWDeltaRate);
                double x = (Math.abs(layerDeltaRatesPerIncomeNeuron[i])-1)/(n+1);
                wk=layerDeltaRatesPerIncomeNeuron[i]>0? Math.abs(1+x):-Math.abs(1+x);
                ik = Math.abs((1+x+n*x)/(1+x));

                if(ik>=maxPossibleIncomeOutputDeltaRate){
                    ik=maxPossibleIncomeOutputDeltaRate;
                    wk = layerDeltaRatesPerIncomeNeuron[i]/ik;
                }else if(Math.abs(wk)>=maxPossibleWDeltaRate){
                    wk=layerDeltaRatesPerIncomeNeuron[i]>0? Math.abs(maxPossibleWDeltaRate):-Math.abs(maxPossibleWDeltaRate);
                    ik=layerDeltaRatesPerIncomeNeuron[i]/wk;

                }
            }

            tmpInputRates.setEntry(outputNeuronIndex,i, ik);
            tmpWRates.setEntry(outputNeuronIndex,i,wk);

            if(maxPossibleWDeltaRate<Math.abs(wk)){
                System.out.println("!!!Error wk");
            }

        }


    }


    public RealMatrix spreadRatesBetweenInputsandWeights(RealMatrix tmpWinputRates, RealMatrix tmpWwRates,List<RealMatrix> matrixWList, int currentlayerIndex, RealMatrix matrixI, int currentLayerIndex, double lerningSpeedRate){

        int incomeLayerIndex = currentlayerIndex-1;

        if(incomeLayerIndex==0){
            return null;
        }

        double[][] incomeOutputDeltaRates = new double[tmpWinputRates.getColumnDimension()][COLUMN_SIZE_ONE];

        double[] minWInputRate = new double[tmpWinputRates.getColumnDimension()];
        Arrays.fill(minWInputRate,Double.MAX_VALUE);
        for(int inputIndex=0; inputIndex<tmpWinputRates.getColumnDimension(); inputIndex++){
            for(int outputIndex = 0; outputIndex<tmpWinputRates.getRowDimension(); outputIndex++){
                if(tmpWinputRates.getEntry(outputIndex,inputIndex)<minWInputRate[inputIndex]){
                    minWInputRate[inputIndex]=tmpWinputRates.getEntry(outputIndex,inputIndex);

                }
            }
        }

        for(int i=0; i<minWInputRate.length;i++){
            incomeOutputDeltaRates[i][COLUMN_INDEX_ZERO] = minWInputRate[i];
        }


        for(int outputIndex =0; outputIndex<tmpWwRates.getRowDimension(); outputIndex++){
            for(int inputIndex=0; inputIndex<tmpWwRates.getColumnDimension(); inputIndex++){
                double wk =tmpWwRates.getEntry(outputIndex,inputIndex);
                double tmpWk =(Math.abs(wk)-1)*lerningSpeedRate+1;
                wk = wk>0? tmpWk:-tmpWk;
                adjustSingleWValue(matrixWList,incomeLayerIndex,outputIndex,inputIndex,wk);
            }
        }

        for(int i=0; i<minWInputRate.length;i++){
            if(minWInputRate[i]!=minWInputRate[i]){
                System.out.println("!!!Error NaN");
            }
            incomeOutputDeltaRates[i][COLUMN_INDEX_ZERO] = (minWInputRate[i]-1)*lerningSpeedRate+1;
        }

        ///////////////////////////////
        RealMatrix acualResultsMatrix = calclayerOutput(matrixI ,matrixWList,incomeLayerIndex);
        RealMatrix expectedResults = getExpectedResults(acualResultsMatrix, MatrixUtils.createRealMatrix(incomeOutputDeltaRates));
        for(int i = 0; i< expectedResults.getRowDimension(); i++){
            if(expectedResults.getEntry(i,0)>1){
                System.out.println("Error!!!");
            }
        }


        //////////////////////////////

        return MatrixUtils.createRealMatrix(incomeOutputDeltaRates);
    }

    private void applyAdjustmentsForW(List<RealMatrix> matrixWList, int currentlayerIndex,int outputNeuronIndex, double[] layerDeltaRates) {
        for(int i=0; i<layerDeltaRates.length; i++){
            adjustSingleWValue(matrixWList,currentlayerIndex,outputNeuronIndex,i,layerDeltaRates[i]);
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


    private RealMatrix sigmoid(RealMatrix zMatrix) {
        double[][] result = new double[zMatrix.getRowDimension()][COLUMN_SIZE_ONE];
        int i = 0;
        for(double itemValue:zMatrix.getColumn(COLUMN_INDEX_ZERO)){
            result[i][0]= 1/(1+Math.exp(-itemValue));
            i++;
        }
        return MatrixUtils.createRealMatrix(result);
    }

    public RealMatrix buildMatrix(int rowOutputNumber, int columnInputNumber){
        return  MatrixUtils.createRealMatrix(new double[rowOutputNumber][columnInputNumber]);
    }

    private void initValues(RealMatrix matrixW, boolean isForseValue, Integer forseValue ) {
        for(int rowIndex=0;rowIndex<matrixW.getRowDimension();rowIndex++){
            for(int columnIndex=0;columnIndex<matrixW.getColumnDimension();columnIndex++ ){
                if(isForseValue){
                    matrixW.setEntry(rowIndex,columnIndex,forseValue);
                }else{
                    matrixW.setEntry(rowIndex,columnIndex,Math.random()-0.5);  //initial range from -0.5 to 0.5
                }

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
