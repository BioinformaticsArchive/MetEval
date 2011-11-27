/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package meteval.SurreyWrapper;

import java.util.LinkedHashMap;

/**
 *
 * @author gieku
 */
public class TradeoffResult extends Result{
    public TradeoffResult(LinkedHashMap<Double,Double> resultTable){
        this.resultTable = resultTable;
    }

    public LinkedHashMap<Double, Double> getResultTable() {
        return (LinkedHashMap<Double, Double>) resultTable.clone();
    }
    
    private LinkedHashMap<Double,Double> resultTable = new LinkedHashMap<Double,Double>();
}
