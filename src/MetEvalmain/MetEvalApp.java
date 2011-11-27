package MetEvalmain;

import GUI.MainFrame;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import meteval.SurreyWrapper.SurreyFbaWrapper;
import meteval.parsers.SBMLParserWrapper;
import meteval.types.Medium;
import meteval.types.MetModel;
import meteval.types.MetModelCollection;
import org.jdesktop.application.Application;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author gieku
 */
public class MetEvalApp extends Application {

    private File collectionDir = new File(".");
    public SurreyFbaWrapper sfba = new SurreyFbaWrapper();

    public static void main(String args[]) {
        MetEvalApp.launch(MetEvalApp.class, args);
    }

    @Override
    protected void startup() {
        loadAll();
        initMainView();
    }

    private void initMainView() {
        mainFrame = new MainFrame(this);
        mainFrame.setVisible(true);
    }
    private MainFrame mainFrame;
    private MetModelCollection masterCollection = new MetModelCollection();
    private LinkedList<Medium> availableMedia = new LinkedList<Medium>();
    private HashMap<String, MetModelCollection> availableCollections = new HashMap<String, MetModelCollection>();
    private SBMLParserWrapper sbmlParser = new SBMLParserWrapper(new File("parseSbml.py"));

    public void addNewModelsToRepo(File[] sbmlFiles) {
        for (File sbml : sbmlFiles) {
            MetModel newModel = sbmlParser.parse(sbml, collectionDir);//TODO:check for redundant models
            masterCollection.addModel(newModel);
        }
    }

    public void createNewCollection(MetModel[] models, String name) {
        MetModelCollection newCollection = new MetModelCollection(models);
        newCollection.setName(name);
        this.availableCollections.put(name, newCollection);
    }

    public void addCollection(MetModelCollection collection) {
        availableCollections.put(collection.getName(), collection);
    }

    public MetModelCollection getCollectionByName(String name) {
        return availableCollections.get(name);
    }

    public void saveAll() {
        save(masterCollection, "repo");
        save(availableCollections, "collections");
    }
    public void addNewMedium(Medium medium){
        availableMedia.add(medium);
    }
    public LinkedList<Medium> getAllMedia(){
        return (LinkedList<Medium>) availableMedia.clone();
    }
    public Medium AddFullMedium(MetModel model ){
        Medium fullMedium = new Medium();
        fullMedium.setName("All possible medium ingridients unconstrained");
        LinkedList<String> externals = CompoundManager.getExternals(model);
        for(String comp:externals)
        fullMedium.addIngridient(comp, 10000);
        availableMedia.add(fullMedium);
        return fullMedium;
    }
    
    public void removeMedium(Medium medium){
        availableMedia.remove(medium);
        this.saveAll();
    }
    public void loadAll() {
        masterCollection = (MetModelCollection) load(new File(OpenJDKBugOverrides.getCurrentDir(),"repo"));
        availableCollections = (HashMap<String, MetModelCollection>) load(new File(OpenJDKBugOverrides.getCurrentDir(),"collections"));
        availableMedia = (LinkedList<Medium>) load(new File(OpenJDKBugOverrides.getCurrentDir(),"mediaForumulations"));
        if (masterCollection == null || availableCollections == null) {
            firstUseScenario();
        }
        if(availableMedia == null)availableMedia = new LinkedList<Medium>();
    }
    public  void saveMedia(){
        save(availableMedia,"mediaForumulations");
    }
    private void firstUseScenario() {
        masterCollection = new MetModelCollection();
        availableCollections = new HashMap<String, MetModelCollection>();
    }

    private Object load(File source) {
        Object loaded = null;
        if (!source.exists()) {
            return null;
        }
        ObjectInputStream ois = null;
        try {
            FileInputStream fis = new FileInputStream(source);
            ois = new ObjectInputStream(fis);
            loaded = ois.readObject();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MetEvalApp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MetEvalApp.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                ois.close();
            } catch (IOException ex) {
                Logger.getLogger(MetEvalApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return loaded;
    }

    private void save(Object o, String name) {

        ObjectOutputStream oos = null;
        try {
            FileOutputStream fos = new FileOutputStream(new File(OpenJDKBugOverrides.getCurrentDir(),name));
            oos = new ObjectOutputStream(fos);
            oos.writeObject(o);
            oos.close();
        } catch (IOException ex) {
            Logger.getLogger(MetEvalApp.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                oos.close();
            } catch (IOException ex) {
                Logger.getLogger(MetEvalApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public MetModelCollection getRepo() {
        return this.masterCollection;
    }

    public HashMap<String, MetModelCollection> getAvailableCollections() {
        return this.availableCollections;
    }

    public void addNewModelsToRepo(File sbml) {
        MetModel newModel = sbmlParser.parse(sbml, OpenJDKBugOverrides.getCurrentDir());//TODO:check for redundant models
        masterCollection.addModel(newModel);
    }

    public void removeCollection(MetModelCollection selected) {
        this.availableCollections.remove(selected.getName());
        this.saveAll();
    }
}
