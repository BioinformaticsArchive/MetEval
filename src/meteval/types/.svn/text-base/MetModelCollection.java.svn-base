/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package meteval.types;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;

/**
 *
 * @author gieku
 */
 public class MetModelCollection implements Serializable{

     public MetModelCollection(){
         
     }
     public MetModelCollection(MetModel[] models){
        this.modelList.addAll(Arrays.asList(models));
     }
     public MetModel getModelByName(String name){
         for (MetModel model:modelList){
             if(model.getName().equals(name))return model;
         }
         return null;
     }
     private String name;
     private String descripction;
     private LinkedList<MetModel> modelList = new LinkedList<MetModel>();

    public String getDescripction() {
        return descripction;
    }

    public void setDescripction(String descripction) {
        this.descripction = descripction;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void addModel(MetModel model){
        modelList.add(model);
    }
    public void removeModel(MetModel model){
        modelList.remove(model);
    }
    public LinkedList<MetModel>getModels(){
        return (LinkedList<MetModel>) modelList.clone();
    }
    @Override
    public String toString(){
        return this.name;
    }
}
