/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package meteval.SurreyWrapper;

import java.util.Calendar;
import java.util.Date;
import meteval.types.MetModel;
import meteval.types.Objective;

/**
 *
 * @author gieku
 */
public abstract  class Result {
    public Result(){
        this.date = Calendar.getInstance().getTime();
    }
    private MetModel associatedModel;
    private Protocol associatedProtocol;
    private final Date date;
    
    private Objective objective;

    public Objective getObjective() {
        return objective;
    }

    public void setObjective(Objective objective) {
        this.objective = objective;
    }
    
    public MetModel getAssociatedModel() {
        return associatedModel;
    }
    
    public void setAssociatedModel(MetModel associatedModel) {
        this.associatedModel = associatedModel;
    }

    public Protocol getAssociatedProtocol() {
        return associatedProtocol;
    }

    public void setAssociatedProtocol(Protocol associatedProtocol) {
        this.associatedProtocol = associatedProtocol;
    }

    public Date getDate() {
        return date;
    }
    
    
}
