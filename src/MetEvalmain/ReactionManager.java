/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetEvalmain;

import java.util.LinkedList;
import meteval.types.Reaction;

/**
 *
 * @author gieku
 */
public class ReactionManager {
    public void addReaction(Reaction reaction){
        reactions.add(reaction);
    }
    public void remove(Reaction reaction){
        reactions.remove(reaction);
    }
    public LinkedList<Reaction> getReactions(){
        return (LinkedList<Reaction>) reactions.clone();
    }
    public LinkedList<Reaction> getReactionByCompound(String compound){
        LinkedList<Reaction> ret = new LinkedList<Reaction>();
        for (Reaction reaction:reactions){
            if(reaction.getInvolved().contains(compound))ret.add(reaction);
        }
        return  ret;
    }
    public LinkedList<Reaction> getReactionByProduct(String compound){
        LinkedList<Reaction> ret = new LinkedList<Reaction>();
        for (Reaction reaction:reactions){
            if(reaction.getProducts().containsKey(compound))ret.add(reaction);
        }
        return  ret;
    }
    public LinkedList<Reaction> getReactionBySubstrate(String compound){
        LinkedList<Reaction> ret = new LinkedList<Reaction>();
        for (Reaction reaction:reactions){
            if(reaction.getSubstrates().containsKey(compound))ret.add(reaction);
        }
        return  ret;
    }
    private  LinkedList<Reaction> reactions = new LinkedList<Reaction>();
    
}
