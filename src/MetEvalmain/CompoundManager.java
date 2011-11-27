/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetEvalmain;

import java.util.Collection;
import java.util.LinkedList;
import meteval.types.MetModel;
import meteval.types.MetModelCollection;
import meteval.types.Reaction;

/**
 *
 * @author gieku
 */
public class CompoundManager {
    public CompoundManager(){
        
    }
    LinkedList<String> compounds = new LinkedList<String>();
    public void addCompounds(Collection<String> compounds){
        for(String comp:compounds){
            this.addCompound(comp);
        }
    }
    public void addMetabolitesFromCollection(MetModelCollection collection){
        for(MetModel model: collection.getModels()){
            for(String comp:model.getCompounds()){
                addCompound(comp);
            }
        }
    }
    public static LinkedList<String> getExternals(MetModel model){
        LinkedList<String> externals = new LinkedList<String>();
        for(String comp:model.getCompounds()){
            if(comp.endsWith("_ext"))externals.add(comp);
        }
        return externals;
    }
    public void addCommon(MetModelCollection collection){
        addMetabolitesFromCollection(collection);
        LinkedList<String> toBeRemoved = new LinkedList<String>();
        for(String comp:compounds){
            for(MetModel model:collection.getModels()){
                if(!model.getCompounds().contains(comp)){
                    toBeRemoved.add(comp);
                    break;
                }
            }
        }
        compounds.removeAll(toBeRemoved);
    }
    public LinkedList<String> getCompounds(){
        return (LinkedList<String>) compounds.clone();
    }
    public void addCompound(String compound){
        if(!compounds.contains(compound))compounds.add(compound);
    }
    public LinkedList<String> getExternals(){
        LinkedList<String> externals = new LinkedList<String>();
        for (String compound:compounds){
            if(isExternal(compound))externals.add(compound);
        }
        return externals;
    }
    public void addCompounds(LinkedList<Reaction> reactions){
        for(Reaction reaction: reactions){
            this.addCompounds(reaction.getInvolved());
        }
    }
    private boolean isExternal(String metabolite){
        String externalityTag = "_b";
        boolean match = metabolite.endsWith(externalityTag);
        return match;
    }
}
