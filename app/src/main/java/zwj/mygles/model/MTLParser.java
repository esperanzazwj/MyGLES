package zwj.mygles.model;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

public class MTLParser {

    public  static Vector<Material> loadMTL(Context context, String fileName){

        InputStream is = null;
        try {
            is = context.getAssets().open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        Vector<Material> materials=new Vector<Material>();
        String line;
        Material currentMtl=null;

        if(reader!=null){
            try {//try to read lines of the file
                while((line = reader.readLine()) != null) {
                    line=line.trim();
                    if(line.startsWith("newmtl")){
                        if(currentMtl!=null)
                            materials.add(currentMtl);
                        String mtName=line.split("[ ]+",2)[1];
                        currentMtl=new Material(mtName);
                    }
                    else if(line.startsWith("Ka")){
                        String[] str=line.split("[ ]+");
                        currentMtl.setAmbientColor(Float.parseFloat(str[1]), Float.parseFloat(str[2]), Float.parseFloat(str[3]));
                    }
                    else if(line.startsWith("Kd")){
                        String[] str=line.split("[ ]+");
                        currentMtl.setDiffuseColor(Float.parseFloat(str[1]), Float.parseFloat(str[2]), Float.parseFloat(str[3]));
                    }
                    else if(line.startsWith("Ks")){
                        String[] str=line.split("[ ]+");
                        currentMtl.setSpecularColor(Float.parseFloat(str[1]), Float.parseFloat(str[2]), Float.parseFloat(str[3]));
                    }
                    else if(line.startsWith("Tr") || line.startsWith("d")){
                        String[] str=line.split("[ ]+");
                        currentMtl.setAlpha(Float.parseFloat(str[1]));
                    }
                    else if(line.startsWith("Ns")){
                        String[] str=line.split("[ ]+");
                        currentMtl.setShine(Float.parseFloat(str[1]));
                    }
                    else if(line.startsWith("illum")){
                        String[] str=line.split("[ ]+");
                        currentMtl.setIllum(Integer.parseInt(str[1]));
                    }
                    else if(line.startsWith("map_Kd")){
                        String[] str=line.split("[ ]+");
                        currentMtl.setDiffuseTextureFile(str[1]);
                    }
                    else if(line.startsWith("map_Ks")) {
                        String[] str = line.split("[ ]+");
                        currentMtl.setSpecularTextureFile(str[1]);
                    }
                }
            }
            catch (Exception e) {
                // TODO: handle exception
            }
        }
        if(currentMtl!=null)
            materials.add(currentMtl);
        return materials;
    }
}
