/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package meteval.SurreyWrapper;

import MetEvalmain.OpenJDKBugOverrides;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import meteval.SurreyWrapper.ResultFileReader.ResultFileType;
import meteval.types.Medium;
import meteval.types.MetModel;
import meteval.types.Objective;
import meteval.types.Reaction;

/**
 *
 * @author gieku
 */
 public class SurreyFbaWrapper {
    public SurreyFbaWrapper(){
//        try {
//            URI toURI = getClass().getClassLoader().getResource("sfba").toURI();
//            JOptionPane.showMessageDialog(null, toURI);
//            this.sfbaDir = new File(getClass().getClassLoader().getResource("sfba").toURI());//OpenJDKBugOverrides.getCurrentDir();
//        
//        } catch (URISyntaxException ex) {
//            Logger.getLogger(SurreyFbaWrapper.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
    private File sfbaDir;
    private String getProblemFile(Medium medium,MetModel model){
        HashMap<String,Float> formulation = medium.getFormulation();
        String problem = "";
        for (String comp:formulation.keySet()){
            String reactionTag = getReactionTagContaining(comp,model);
            problem += reactionTag +"\t" +"-10000"+"\t"+ formulation.get(comp)+"\n";
        }
        return problem;
    }
    private String getReactionTagContaining(String compound,MetModel model){
        for(Reaction reaction:model.getReactions()){
            if(reaction.isInvolved(compound))return reaction.getName();
        }
        return compound;
    }
    private File mergeProblemFiles(File externals, Medium medium,MetModel model){
        BufferedReader externalsReader = null;
        File tempFile = new File(OpenJDKBugOverrides.getCurrentDir(),"mergedProblemfile.txt");
        {
            FileWriter out = null;
            try {
                out = new FileWriter(tempFile);
                String mediaContent = getProblemFile(medium,model);
                out.append(mediaContent);
                externalsReader = new BufferedReader(new FileReader(externals));
                while(externalsReader.ready()){
                    String line = externalsReader.readLine();
                     out.append(line);
                }
                return tempFile;
            } catch (IOException ex) {
                Logger.getLogger(SurreyFbaWrapper.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    externalsReader.close();
                } catch (IOException ex) {
                    Logger.getLogger(SurreyFbaWrapper.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    out.close();
                } catch (IOException ex) {
                    Logger.getLogger(SurreyFbaWrapper.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return tempFile;
    }
//    private File mergeProblemFiles(File externals, File medium){
//        FileWriter out = null;
//        File tempFile = new File("mergedProblemfile.txt");
//        try {
//            
//            FileReader in = new FileReader(medium);
//            out = new FileWriter(tempFile);
//            IOUtils.copy(in,out);
//            BufferedReader externalsReader = new BufferedReader(new FileReader(externals));
//            while(externalsReader.ready()){
//                String line = externalsReader.readLine();
//                 out.append(line);
//            }
//        } catch (IOException ex) {
//            Logger.getLogger(SurreyFbaWrapper.class.getName()).log(Level.SEVERE, null, ex);
//        } finally {
//            try {
//                out.close();
//            } catch (IOException ex) {
//                Logger.getLogger(SurreyFbaWrapper.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//        return tempFile;
//    }
    private void waitForResults(File resultFile) throws InterruptedException{
        Thread.sleep(250);
        while(!resultFile.exists()){
            ResultFileReader reader = ResultFileReader.getInstance(resultFile);
            if(reader.getResultType() == ResultFileType.MALFORMED)
            Thread.sleep(250);
            else break;
        }
    }
    public  FbaResult fba(MetModel model, Objective obj, Medium medium) {
                File input =  model.getAssociatedSurreyFormat();
                File externals = model.getAssociatedExternalsFile();
                File problemFile = mergeProblemFiles(externals, medium,model);
                String command = String.format("-i %s -o %s -b %s -f %s", passablePath(input),obj.getReaction(),passablePath(problemFile),"results.txt");
                executeCommand(command);
                File resultFile = null;
            resultFile = new File(OpenJDKBugOverrides.getCurrentDir(),"results.txt");
            //JOptionPane.showMessageDialog(null, "resultFile:"+resultFile.getAbsolutePath());
        try {
            
            waitForResults(resultFile);
        } catch (InterruptedException ex) {
            Logger.getLogger(SurreyFbaWrapper.class.getName()).log(Level.SEVERE, null, ex);
       }
                ResultFileReader reader = ResultFileReader.getInstance(resultFile);
                Result result = reader.getResult();
                if(resultFile == null || !resultFile.exists())JOptionPane.showMessageDialog(null, "No result file found in: "+ resultFile.getPath());
                resultFile.delete();
        return (FbaResult) result;
    }
    public TradeoffResult tradeoff(MetModel model, Objective firstObj, Objective secondObj, Medium medium){
        File input = model.getAssociatedSurreyFormat();
        File externals = model.getAssociatedExternalsFile();
        File problemFile = mergeProblemFiles(externals,medium,model);
        String command = String.format("-i %s -o %s -b %s -f %s -a %s -p troff", passablePath(input),firstObj.getReaction(),passablePath(problemFile),"results.txt",secondObj.getReaction());
        executeCommand(command);
        File resultFile = null;
            resultFile = new File(OpenJDKBugOverrides.getCurrentDir(),"results.txt");
        
        try {
            Thread.sleep(30000);
            waitForResults(resultFile);
            //Thread.sleep(60000);
        } catch (InterruptedException ex) {
            Logger.getLogger(SurreyFbaWrapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(resultFile == null || !resultFile.exists())JOptionPane.showMessageDialog(null, "No result file found in: "+ resultFile.getPath());
        ResultFileReader reader = ResultFileReader.getInstance(resultFile);
        Result result = reader.getResult();
        return (TradeoffResult) result;
        
    }
    private String passablePath(File file){
        String passablePath = String.format("'%s'", file.getAbsolutePath());
        return passablePath;
    }
    
    
    private LinkedList<String> sfbaOutput = new LinkedList<String>();
    private void executeCommand(String command){
            this.sfbaDir = new File(OpenJDKBugOverrides.getCurrentDir(),"sfba");//OpenJDKBugOverrides.getCurrentDir();
                command = String.format("%s %s",passablePath(sfbaDir), command);
        try {
            //JOptionPane.showMessageDialog(null, "command redirected to sfba: "+ command);
            redirectCommand(command);
        } catch (IOException ex) {
            Logger.getLogger(SurreyFbaWrapper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void redirectCommand(String command) throws IOException{
        //String extendedCommand = String.format("python %s/redirect.py %s", sfbaDir.getAbsolutePath(),command);
        Runtime runtime = Runtime.getRuntime();
        //runtime.exec(extendedCommand);
        String[] commands = {"python",OpenJDKBugOverrides.getCurrentDir().getAbsolutePath()+"/redirect.py" , command};
        runtime.exec(commands,null,OpenJDKBugOverrides.getCurrentDir());
    }

    private double parseFbaResults() {
        double result = -1;
        
        for(String line:this.sfbaOutput){
            if(line.contains(";")){
                line = line.replaceAll(";", "");
                result = Double.parseDouble(line);
            }
            
        }
        return result;
    }
    private  class resultReaderThread extends Thread{
        public resultReaderThread(File file){
            instance = ResultFileReader.getInstance(file);
        }
        private ResultFileReader instance;
        @Override
        public void run(){
            //this.wait(1000);
           result = instance.getResult();
        }
        private Result result;
        public Result getResult(){
            return result;
        }
    }
}
