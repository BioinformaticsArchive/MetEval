/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package meteval.SurreyWrapper;

import meteval.types.MetModelCollection;
import meteval.SurreyWrapper.SurreyFbaWrapper;

/**
 *
 * @author gieku
 */
 abstract class Protocol {
    private  SurreyFbaWrapper sfbaWrapperInstance;
    public Protocol(String displayableName,SurreyFbaWrapper sfbaWrapperInstance){
        this.displayableName = displayableName;
        this.sfbaWrapperInstance = sfbaWrapperInstance;
    }
    public SurreyFbaWrapper getSfbaWrapper(){
        return sfbaWrapperInstance;
    }
    public abstract void configure();
        
    
    public abstract Result execute(MetModelCollection querriedCollection);
    private String displayableName;
    public String getName(){
        return this.displayableName;
    }
}
