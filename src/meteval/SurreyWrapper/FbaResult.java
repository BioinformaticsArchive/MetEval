/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package meteval.SurreyWrapper;

/**
 *
 * @author gieku
 */
public class FbaResult extends Result implements Comparable{
    private final Double result;

    public FbaResult(Double resultValue) {
        this.result = resultValue;
    }

    public double getResult() {
        return result;
    }

    public int compareTo(Object t) {
        FbaResult r = (FbaResult) t;
        return (int) (r.getResult() - this.getResult());
    }
    
}
