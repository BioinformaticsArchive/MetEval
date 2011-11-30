/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetEvalmain;

import java.io.File;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;

/**
 *
 * @author gieku
 */
public class OpenJDKBugOverrides {
    public File getClassDir(){
     String path = getClass().getClassLoader().getResource("parseSbml.py").getPath();
     //path = purge(path,"file:");
     //path = purge(path,".jar");
     //path = purge(path,"!");
     //path = purge(path,"parseSbml.py");
     path = cutBefore(path,"file:");
     path = cutAfter(path,".jar");
     path = cutAfter(path,"/");
     path = path.replaceAll("%20", " ");
     return new File(path);
 }
    private String cutBefore(String path,String token){
        int  cutPos = path.lastIndexOf(token);
        if(cutPos != -1){
            path = path.substring(cutPos);
        }
        path = path.replaceAll(token, "");
        return path;
    }
    private String cutAfter(String path, String token){
        int cutPos = path.lastIndexOf(token);
        if(cutPos != -1){
            path = path.substring(0, cutPos);
        }
        return path;
    }
 private String purge(String path,String shit){
     int pos = path.lastIndexOf(shit);
     if(pos != -1){
         path = path.substring(0, pos);
     }
     return path;
 }
    public static File getCurrentDir(){
        return instance.getClassDir();
    }
    private static OpenJDKBugOverrides instance = new OpenJDKBugOverrides();
}
