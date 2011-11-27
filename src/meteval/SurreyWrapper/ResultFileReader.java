/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package meteval.SurreyWrapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

/**
 *
 * @author gieku
 */
public class ResultFileReader extends BufferedReader{
    private LinkedList<String> resultFileContent = new LinkedList<String>();
    
    private  ResultFileReader(FileReader reader){
        super(reader);
        try {
            this.readAllFile();
        } catch (IOException ex) {
            Logger.getLogger(ResultFileReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        determineType();
    }
    public static ResultFileReader getInstance(File file){
        FileReader reader =initFileReader(file);
        ResultFileReader resultReader = new ResultFileReader(reader);
        return resultReader;
    }
    private static FileReader initFileReader(File file){
        FileReader reader = null;
        try {
            reader = new FileReader(file);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ResultFileReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return reader;
    }
    public Result getResult(){
//        try {
//          //  readAllFile();
//        } catch (IOException ex) {
//            Logger.getLogger(ResultFileReader.class.getName()).log(Level.SEVERE, null, ex);
//        }
        //determineType();
        Result result = createResultObj();
        return result;
    }
private ResultFileType resultType;

    public ResultFileType getResultType() {
        return resultType;
    }

    private void readAllFile() throws IOException {
        resultFileContent.clear();
        while(this.ready()){
            String line = this.readLine();
            resultFileContent.add(line);
        }
        
    }

    private void determineType() {
        int numberOfNumbersInTheFile = countNumbersOccurance();
        if(numberOfNumbersInTheFile == 1){
            this.resultType = ResultFileType.FBA;
        }
        else if(numberOfNumbersInTheFile > 1){
            this.resultType = ResultFileType.TRADEOFF;
        }
        else this.resultType = ResultFileType.MALFORMED; 
    }
    private String purgeNumberString(String numberString){
        String newString = numberString.replaceAll(";", "");
        newString = newString.replaceAll(" ", "");
        return newString;
    }
    private int countNumbersOccurance() {
        int number = 0;
        for(String line:this.resultFileContent){
            String[] splitage = line.split("\t");
            for(String sp:splitage){
                String purged = purgeNumberString(sp);
                if(isNumber(purged))number++;
            }
        }
        return number;
    }

    private boolean isNumber(String sp) {
        Pattern floatInString = Pattern.compile("\\d*\\p{Punct}\\d*");
        Matcher matcher = floatInString.matcher(sp);
        if(matcher.matches() || sp.equals("0"))return true;
        else return false;
    }

    private Result createResultObj() {
        Result result = null;
        switch(this.resultType){
            case MALFORMED:
                //System.out.print("Result file Malformed");
                //JOptionPane.showMessageDialog(null, "Result file Malformed");
                break;
            case FBA:
                result = getFbaResult();
                break;
            case TRADEOFF:
                result = getTradeoffResult();
                break;
        }
        return result;
    }
    private Result getFbaResult(){
        String resultLine = "";
        for(String line:this.resultFileContent){
            resultLine += line;
        }
        resultLine = this.purgeNumberString(resultLine);
        Double resultValue = Double.parseDouble(resultLine);
        Result result = new FbaResult(resultValue);
        return result;
    }

    private Result getTradeoffResult() {
        LinkedHashMap<Double,Double> resultTable = new LinkedHashMap<Double,Double>();
        for(String line:this.resultFileContent){
            String[] splitage = line.split("\t");
            if (splitage.length >= 2){
                Double first  = Double.parseDouble(splitage[0]);
                Double second = Double.parseDouble(splitage[1]);
                resultTable.put(first, second);
            }
        }
        Result result = new TradeoffResult(resultTable);
        return result;
    }
public enum ResultFileType{
    FBA,TRADEOFF,MALFORMED
}    
}
