/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package meteval.types;

import java.io.File;
import java.io.Serializable;

import java.util.LinkedList;
import meteval.types.Reaction;

/**
 *
 * @author gieku
 */
public class MetModel implements Serializable{
    public MetModel(){
        
    }
    private String name;
    private File associatedSBML;
    private File associatedSurreyFormat;
    private LinkedList<String> compounds = new LinkedList<String>();
    private LinkedList<Reaction> reactions = new LinkedList<Reaction>();
    private String description;

    public LinkedList<String> getCompounds() {
        return compounds;
    }

    public void setCompounds(LinkedList<String> compounds) {
        this.compounds = compounds;
    }

    public LinkedList<Reaction> getReactions() {
        return reactions;
    }

    public void setReactions(LinkedList<Reaction> reactions) {
        this.reactions = reactions;
    }
    
    @Override
    public MetModel clone(){
        MetModel copy = new MetModel();
        copy.setAssociatedSBML(this.getAssociatedSBML());
        copy.setAssociatedSurreyFormat(this.getAssociatedSurreyFormat());
        copy.setAssociatedExternalsFile(this.getAssociatedExternalsFile());
        copy.setDescription(this.getDescription());
        copy.setName(this.getName());
        copy.setReactions(this.getReactions());
        copy.setCompounds(this.getCompounds());
        return copy;
    }
    
    public File getAssociatedSBML() {
        return associatedSBML;
    }

    public File getAssociatedSurreyFormat() {
        return associatedSurreyFormat;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public void setAssociatedSBML(File associatedSBML) {
        this.associatedSBML = associatedSBML;
    }

    public void setAssociatedSurreyFormat(File associatedSurreyFormat) {
        this.associatedSurreyFormat = associatedSurreyFormat;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setAssociatedExternalsFile(File newExternalsFile){
        this.associatedExternalsFile = newExternalsFile;
    }
    private File associatedExternalsFile ;
    public File getAssociatedExternalsFile() {
        return this.associatedExternalsFile;
    }
    @Override
    public String toString(){
        return this.name;
    }
}
