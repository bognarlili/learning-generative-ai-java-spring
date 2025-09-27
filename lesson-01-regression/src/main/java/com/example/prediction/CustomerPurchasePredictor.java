package com.example.prediction;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

//A regressziós modell egy statisztikai eszköz, amely a független változók
// és a függő változó közötti kapcsolatot modellezi, lehetővé téve a függő változó előrejelzését
// a független változók alapján

public class CustomerPurchasePredictor {
    public static void main(String[] args) {
        try{
            //Load the data
            List<String[]> data = loadCsv("C:\\Users\\bogna\\IdeaProjects\\regression-prediction\\src\\main\\resources\\customer_purchases.csv");

            //Prepare the regression model
            SimpleRegression regression = new SimpleRegression();

            //Skip the header, adding data points
            for(int i = 1; i<data.size(); i++){
                String[] row = data.get(i);
                double income = Double.parseDouble(row[2]); //Independent varable -> X
                double purchaseAmount = Double.parseDouble(row[3]); // Dependent variable -> Y
                regression.addData(income, purchaseAmount);
            }

            //Printing statistics
            System.out.println("*** Model Sum ***");
            System.out.printf("R-squared: %.4f\n", regression.getRSquare());
            System.out.printf("Intercept: %.2f\n", regression.getIntercept());
            System.out.printf("Slope: %.4f\n", regression.getSlope());
            System.out.printf("Standard Error: %.4f\n\n", regression.getRegressionSumSquares());

            // 4. Make predictions for new customers
            System.out.println("=== Predictions ===");
            predictPurchase(regression, 40000);  // $40,000 income
            predictPurchase(regression, 55000);  // $55,000 income
            predictPurchase(regression, 80000);  // $80,000 income


        } catch (RuntimeException | IOException | CsvException e) {
            throw new RuntimeException(e);
        }
    }

        private static List<String[]> loadCsv(String filePath) throws IOException, CsvException {
            try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
                return reader.readAll();
            }
        }

        private static void predictPurchase(SimpleRegression regression, double income){
        double predictedAmount =regression.predict(income);
            System.out.printf("Predicted purchase for $%,.2f income: $%,.2f\n", income, predictedAmount);
        }


}

