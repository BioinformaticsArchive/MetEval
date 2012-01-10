/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package meteval.parsers;

import MetEvalmain.OpenJDKBugOverrides;
import java.net.URISyntaxException;
import java.util.LinkedList;
import meteval.types.MetModel;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import meteval.types.Reaction;

/**
 *
 * @author gieku
 */
 public class SBMLParserWrapper {
    private final String PYTHON_INTERPRETER_CALL = "python3.2";
    public SBMLParserWrapper(File parserFile){
        //parser = OpenJDKBugOverrides.getCurrentDir();
        //parser = new File(parser,parserFile.getName());
//        String resource = parserFile.getName();
//        try {
//            parser = new File(getClass().getClassLoader().getResource(resource).toURI());
//        } catch (URISyntaxException ex) {
//            Logger.getLogger(SBMLParserWrapper.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
    public MetModel parse(File sbmlFile,File destination){
            parser = new File(OpenJDKBugOverrides.getCurrentDir(),"parseSbml.py");
        MetModel newModel = new MetModel();
        String baseName = sbmlFile.getName();
        baseName = stripExtension(baseName);
        destination = new File(OpenJDKBugOverrides.getCurrentDir(),baseName);
        execParser(sbmlFile,destination);
        newModel.setAssociatedSBML(sbmlFile);
        newModel.setAssociatedSurreyFormat(destination);
        File externalsFile = getExternalsFile(destination);
        newModel.setAssociatedExternalsFile(externalsFile);
        newModel.setName(baseName);
        waitFor(newModel.getAssociatedSurreyFormat());
        parseSurreyFormat(newModel.getAssociatedSurreyFormat().getAbsoluteFile(),newModel);
        return newModel;
    }
    private void parseSurreyFormat(File surreyFormat, MetModel model){
        SurreyFormatReader sfbaReader = SurreyFormatReader.getInstance(surreyFormat);
        LinkedList<String> compounds = sfbaReader.getCompounds();
        LinkedList<Reaction> reactions = sfbaReader.getReactions();
        model.setReactions(reactions);
        model.setCompounds(compounds);
        if(reactions.size() == 0 || compounds.size() == 0){
        }
    }
    private File getExternalsFile(File destination){
       String name =  destination.getName()+"_ext";
       File dir = destination.getParentFile();
       return new File(dir,name);
    }
    private String stripExtension(String fileName){
        if(fileName != null && fileName.contains(".")){
            int dotPos = fileName.lastIndexOf(".");
            fileName =  fileName.substring(0,dotPos);
        }
        return fileName;
    }
    private String passablePath(String path){
        return "\""+path+"\"";
    }
    private void redirectCommand(String command) throws IOException{
        //String extendedCommand = String.format("python %s/redirect.py %s", sfbaDir.getAbsolutePath(),command);
        Runtime runtime = Runtime.getRuntime();
        //runtime.exec(extendedCommand);
        File currentDir = MetEvalmain.OpenJDKBugOverrides.getCurrentDir();
        String[] commands = {"python",currentDir.getAbsolutePath()+"/redirect.py" , command};
        JOptionPane.showMessageDialog(null, commands);
        runtime.exec(commands,null,null);
    }
    private void execParser(File inputFile,File outputFile){
        Runtime runtime = Runtime.getRuntime();
        String interpreterCall = PYTHON_INTERPRETER_CALL;
        String parserCall = parser.getAbsolutePath();
        String inputParam = inputFile.getAbsolutePath();
        String outputParam = outputFile.getAbsolutePath();
        String command = String.format("%s %s %s %s",interpreterCall, parserCall, passablePath(inputParam),passablePath(outputParam) );
        //JOptionPane.showMessageDialog(null,"SBML Parser call:"+ command+"\n parserCall: " + parserCall);
        try {
            redirectCommand(command);
            //runtime.exec(command);
        } catch (IOException ex) {
            Logger.getLogger(SBMLParserWrapper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private File parser;

    private void waitFor(File file) {
         try {
                Thread.sleep(3500);
            } catch (InterruptedException ex) {
                Logger.getLogger(SBMLParserWrapper.class.getName()).log(Level.SEVERE, null, ex);
            }
        while(!file.exists()){
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(SBMLParserWrapper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
