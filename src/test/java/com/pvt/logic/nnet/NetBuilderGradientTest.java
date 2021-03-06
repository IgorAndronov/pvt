package com.pvt.logic.nnet;

import com.pvt.logic.logic.core.nnet.NetBuilder;
import com.pvt.logic.logic.core.nnet.NetBuilderGradient;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 20.01.2018.
 */
public class NetBuilderGradientTest {

    final static int COLUMN_SIZE_ONE = 1;
    final static int COLUMN_INDEX_ZERO = 0;
    @Test
    public void buildTest(){
        int inputNumber = 1;
        int outputNumber = 1;

        List<Integer> inermidiateLevelsNeuronsNumber = new ArrayList<>();
        inermidiateLevelsNeuronsNumber.add(inputNumber);
        inermidiateLevelsNeuronsNumber.add(5);
        inermidiateLevelsNeuronsNumber.add(5);
        inermidiateLevelsNeuronsNumber.add(outputNumber);




        NetBuilderGradient netBuilder = new NetBuilderGradient();
        List<RealMatrix> matrixWList =  netBuilder.build(inermidiateLevelsNeuronsNumber,false);
        List<RealMatrix> matrixBList =  netBuilder.build(inermidiateLevelsNeuronsNumber,true);

        RealMatrix matrixI=netBuilder.buildMatrix(inputNumber,COLUMN_SIZE_ONE);

        List<RealMatrix> levelsOutputs = new ArrayList<>();
        List<RealMatrix> levelsZ = new ArrayList<>();
        netBuilder.calcLevelsOutputs(matrixI,matrixWList,matrixBList,levelsOutputs,levelsZ);


        RealMatrix expectedResults = netBuilder.buildMatrix(outputNumber,COLUMN_SIZE_ONE);



        try (BufferedReader br = new BufferedReader(new FileReader("src/test/resources/trainingset.txt"))) {

            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                String dataLine[] = sCurrentLine.split(";");

                matrixI.setEntry(0,0, Double.parseDouble(dataLine[0]));
                expectedResults.setEntry(0,0,Double.parseDouble(dataLine[1]));

                netBuilder.trainNetwork(matrixWList,matrixI,expectedResults);

                RealMatrix receivedResult = netBuilder.calcLevelOutput(matrixI ,matrixWList, matrixWList.size());
                if(receivedResult.getEntry(0,0)!=receivedResult.getEntry(0,0)){
                    System.out.println("Error logarifm NaN!!!");
                }
                System.out.println("\nInput data = "+matrixI.getEntry(0,0)+
                        "\nExpected data = "+expectedResults.getEntry(0,0)+
                        "\nReceived result = "+ receivedResult.getEntry(0,0)+
                        "\nDelta="+100*(receivedResult.getEntry(0,0)-expectedResults.getEntry(0,0))/expectedResults.getEntry(0,0)+"%");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        double result=0;
        int maxLayerIndex = matrixWList.size();
        for(int i = 1;i<10; i++){
            matrixI.setEntry(0,0,(double)i/10);
            result = netBuilder.calcLevelOutput(matrixI ,matrixWList,maxLayerIndex).getEntry(0,0);
            System.out.println("\n!!! input = "+(double)i/10+" result = "+result);
        }


    }

}
