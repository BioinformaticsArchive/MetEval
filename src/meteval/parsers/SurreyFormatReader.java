/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package meteval.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import meteval.types.Reaction;

/**
 *
 * @author gieku
 */
public class SurreyFormatReader extends BufferedReader {

    private LinkedList<String> allCompounds = new LinkedList<String>();

    private SurreyFormatReader(FileReader surreyFileReader) {
        super(surreyFileReader);
    }

    public static  SurreyFormatReader getInstance(File surreyFile) {
        FileReader reader = null;
        try {
            if(!surreyFile.exists())JOptionPane.showMessageDialog(null, "SurreyFormat error. File not found: "+surreyFile.getAbsolutePath());
            reader = new FileReader(surreyFile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SurreyFormatReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new SurreyFormatReader(reader);
    }
    private LinkedList<String> content = new LinkedList<String>();
    private boolean read = false;

    public LinkedList<String> getCompounds() {
        if (!read) {
            readlAll();
        }
        return (LinkedList<String>) allCompounds.clone();
    }
    private LinkedList<Reaction> allReactions = new LinkedList<Reaction>();

    public LinkedList<Reaction> getReactions() {
        if (!read) {
            readlAll();
        }
        return (LinkedList<Reaction>) allReactions.clone();
    }

    private void readlAll() {
        populateContent();
        parseContent();
        read = true;
    }

    private void parseContent() {
        for (String line : content) {
            parseLine(line);
        }
    }

    private void populateContent() {
        try {
            while (this.ready()) {
                String readLine = this.readLine();
                this.content.add(readLine);
            }
        } catch (IOException ex) {
            Logger.getLogger(SurreyFormatReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void parseLine(String line) {
        String[] splitage = line.split("\t");
        Reaction reaction = new Reaction(splitage[0]);
        boolean leftSide = true;
        Double number  = null;
        for(String token:splitage[1].split(" ")){
            if(isNumber(token) && !token.equals("+")&& !token.equals("=") && !token.equals("")  ){
              number = Double.parseDouble(token);
            }
            else if(!token.equals("=") && !token.equals("") && !token.equals("+") ){
                if(number == null)number = 1.0;
                if(leftSide){
                    reaction.addSubstrate(token, number);
                }else{
                    reaction.addProduct(token, number);
                }
                number = null;
            }
            else leftSide = false;
        }
        allReactions.add(reaction);
        for (String compound:reaction.getInvolved()){
            if(!allCompounds.contains(compound)){
                allCompounds.add(compound);
            }
            
        }
    }
    private boolean isNumber(String sp) {
        Pattern floatInString = Pattern.compile("\\d*\\p{Punct}\\d*");
        Matcher matcher = floatInString.matcher(sp);
        if(matcher.matches() || sp.equals("0"))return true;
        else return false;
    }
}
