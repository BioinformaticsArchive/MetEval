/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package meteval.types;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

/**
 *
 * @author gieku
 */
public class Reaction implements Serializable{
    public Reaction(String tag){
        this.tag = tag;
    }
    @Override
    public String toString(){
        String representation = this.tag+": ";
        representation += compoundMapToString(substrates);
        representation += "<=> ";
        representation += compoundMapToString(products);
        return representation;
    }
    public void addProduct(String nameTag,double  stoichiometry){
        this.involvedCompounds.add(nameTag);
        this.products.put(nameTag, stoichiometry);
    }
    public void addSubstrate(String nameTag,double  stoichiometry){
        this.involvedCompounds.add(nameTag);
        this.substrates.put(nameTag, stoichiometry);
    }
    public boolean isInvolved(String compoundtag){
        if (involvedCompounds.contains(compoundtag))return  true;
        else return false;
    }
    public String getName(){
        return tag;
    }
    private String compoundMapToString(HashMap<String,Double> compoundlist){
        String representation = "";
        int size = compoundlist.keySet().size();
        int i = 0; 
        for (String substrate:compoundlist.keySet()){
            i++;
            double stoichiometry = compoundlist.get(substrate);
            String sign = "+";
            if(i == size )sign ="";
            representation += String.format("%f %s %s ",stoichiometry,substrate,sign );
        }
        return representation;
    }
    private HashMap<String,Double> substrates = new HashMap<String,Double>();
    private HashMap<String,Double> products = new HashMap<String,Double>();
    private LinkedList<String> involvedCompounds = new LinkedList<String>();
    private  String tag;

    public LinkedList<String> getInvolved() {
        return (LinkedList<String>) this.involvedCompounds.clone();
    }

    public HashMap<String,Double> getProducts() {
        return (HashMap<String, Double>) this.products.clone();
    }

    public HashMap<String,Double> getSubstrates() {
        return (HashMap<String, Double>) this.substrates.clone();
    }
}
