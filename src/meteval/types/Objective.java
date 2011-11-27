/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package meteval.types;

/**
 *
 * @author gieku
 */
public class Objective {
    public Objective(String reactiontag){
        reaction = reactiontag;
    }
    private String reaction;
    public String getReaction(){
        return reaction;
    }
}
