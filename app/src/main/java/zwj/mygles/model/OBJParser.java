package zwj.mygles.model;

/**
 * Created by student on 2016/12/3.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import android.content.Context;
import android.util.Log;

import zwj.mygles.Mesh;

public class OBJParser {

    private Context mContext;

    private Vector<Integer> faces=new Vector<>();
    private Vector<Integer> vtPointer=new Vector<>();
    private Vector<Integer> vnPointer=new Vector<>();
    private Vector<Float> v=new Vector<>();
    private Vector<Float> vn=new Vector<>();
    private Vector<Float> vt=new Vector<>();
    private Vector<Material> materials=null;

    public OBJParser(Context context){
        mContext=context;
    }

    public Vector<Mesh> parseOBJ(String fileName) {

        Vector<Mesh> Scene=new Vector<>();

        InputStream is = null;
        try {
            is = mContext.getAssets().open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        String line = null;
        Material m = null;

        try { //try to read lines of the file
            while((line = reader.readLine()) != null) {
               /// Log.v("obj", line);
                if (line.startsWith("f")) {//a polygonal face
                    processFLine(line);
                } else if (line.startsWith("vn")) {
                    processVNLine(line);
                } else if (line.startsWith("vt")) {
                    processVTLine(line);
                } else if (line.startsWith("v")) { //line having geometric position of single vertex
                    processVLine(line);
                } else if (line.startsWith("usemtl")) {
                    if (faces.size() != 0) {    //if not this is not the start of the first group
                       // TDModelPart model = new TDModelPart(faces, vtPointer, vnPointer, m, vn);
                        Mesh model=new Mesh(faces,vtPointer,vnPointer,m,v,vt,vn);
                        Scene.add(model);
                       // parts.add(model);
                    }
                    String mtlName = line.split("[ ]+", 2)[1]; //get the name of the material
                    for (int i = 0; i < materials.size(); i++) {//suppose .mtl file already parsed
                        m = materials.get(i);
                        if (m.getName().equals(mtlName)) {//if found, return from loop
                            break;
                        }
                        m = null;//if material not found, set to null
                    }
                    faces = new Vector<Integer>();
                    vtPointer = new Vector<Integer>();
                    vnPointer = new Vector<Integer>();

                } else if (line.startsWith("mtllib")) {
                    materials = MTLParser.loadMTL(mContext, line.split("[ ]+")[1]);
                    for (int i = 0; i < materials.size(); i++) {
                        materials.get(i).GenOGLTexture(mContext);
                        //Material mat = materials.get(i);
                       // Log.v("materials", mat.toString());
                    }
                }
            }
        }
        catch(IOException e){
          //  System.out.println("wtf...");
        }

        if(faces.size()!= 0){//if not this is not the start of the first group
          //  TDModelPart model=new TDModelPart(faces, vtPointer, vnPointer, m,vn);
          //  parts.add(model);
            Mesh model=new Mesh(faces,vtPointer,vnPointer,m,v,vt,vn);
            Scene.add(model);
        }
       // TDModel t=new TDModel(v,vn,vt,parts);
       // t.buildVertexBuffer();
       // Log.v("models",t.toString());
        return Scene;
    }

    public Vector<Integer> triangulate(Vector<Integer> polygon){
        Vector<Integer> triangles=new Vector<Integer>();
        for(int i=1; i<polygon.size()-1; i++){
            triangles.add(polygon.get(0));
            triangles.add(polygon.get(i));
            triangles.add(polygon.get(i+1));
        }
        return triangles;
    }

    private void processVLine(String line){
        String [] tokens=line.split("[ ]+"); //split the line at the spaces
        int c=tokens.length;
        for(int i=1; i<c; i++){ //add the vertex to the vertex array
            v.add(Float.valueOf(tokens[i]));
        }
    }
    private void processVNLine(String line){
        String [] tokens=line.split("[ ]+"); //split the line at the spaces
        int c=tokens.length;
        for(int i=1; i<c; i++){ //add the vertex to the vertex array
            vn.add(Float.valueOf(tokens[i]));
        }
    }
    private void processVTLine(String line){
        String [] tokens=line.split("[ ]+"); //split the line at the spaces
        int c=tokens.length;
        for(int i=1; i<3; i++){ //add the vertex to the vertex array
            vt.add(Float.valueOf(tokens[i]));
        }
    }
    private void processFLine(String line){
        String [] tokens=line.split("[ ]+");
        int c=tokens.length;

        if(tokens[1].matches("[0-9]+")){ //f: v
            if (c==4){ //3 faces
                for(int i=1; i<c; i++){
                    Integer s=Integer.valueOf(tokens[i]);
                    s--;
                    faces.add(s);
                }
            }
            else{ //more faces
                Vector<Integer> polygon=new Vector<Integer>();
                for(int i=1; i<tokens.length; i++){
                    Integer s=Integer.valueOf(tokens[i]);
                    s--;
                    polygon.add(s);
                }
                faces.addAll(triangulate(polygon));//triangulate the polygon and add the resulting faces
            }
        }

        if(tokens[1].matches("[0-9]+/[0-9]+")){//if: v/vt
            if (c==4){ //3 faces
                for(int i=1; i<c; i++){
                    Integer s=Integer.valueOf(tokens[i].split("/")[0]);
                    s--;
                    faces.add(s);
                    s=Integer.valueOf(tokens[i].split("/")[1]);
                    s--;
                    vtPointer.add(s);
                }
            }
            else {//triangulate
                Vector<Integer> tmpFaces=new Vector<Integer>();
                Vector<Integer> tmpVt=new Vector<Integer>();
                for(int i=1; i<tokens.length; i++){
                    Integer s=Integer.valueOf(tokens[i].split("/")[0]);
                    s--;
                    tmpFaces.add(s);
                    s=Integer.valueOf(tokens[i].split("/")[1]);
                    s--;
                    tmpVt.add(s);
                }
                faces.addAll(triangulate(tmpFaces));
                vtPointer.addAll(triangulate(tmpVt));
            }
        }

        if(tokens[1].matches("[0-9]+//[0-9]+")){//f: v//vn
            if(c==4){//3 faces
                for(int i=1; i<c; i++){
                    Integer s=Integer.valueOf(tokens[i].split("//")[0]);
                    s--;
                    faces.add(s);
                    s=Integer.valueOf(tokens[i].split("//")[1]);
                    s--;
                    vnPointer.add(s);
                }
            }
            else {//triangulate
                Vector<Integer> tmpFaces=new Vector<Integer>();
                Vector<Integer> tmpVn=new Vector<Integer>();
                for(int i=1; i<tokens.length; i++){
                    Integer s=Integer.valueOf(tokens[i].split("//")[0]);
                    s--;
                    tmpFaces.add(s);
                    s=Integer.valueOf(tokens[i].split("//")[1]);
                    s--;
                    tmpVn.add(s);
                }
                faces.addAll(triangulate(tmpFaces));
                vnPointer.addAll(triangulate(tmpVn));
            }
        }

        if(tokens[1].matches("[0-9]+/[0-9]+/[0-9]+")){//f: v/vt/vn

            if(c==4){//3 faces
                for(int i=1; i<c; i++){
                    Integer s=Integer.valueOf(tokens[i].split("/")[0]);
                    s--;
                    faces.add(s);
                    s=Integer.valueOf(tokens[i].split("/")[1]);
                    s--;
                    vtPointer.add(s);
                    s=Integer.valueOf(tokens[i].split("/")[2]);
                    s--;
                    vnPointer.add(s);
                }
            }
            else{//triangulate
                Vector<Integer> tmpFaces=new Vector<Integer>();
                Vector<Integer> tmpVn=new Vector<Integer>();
                //MyVector<Integer> tmpVt=new MyVector<Integer>();
                for(int i=1; i<tokens.length; i++){
                    Integer s=Integer.valueOf(tokens[i].split("/")[0]);
                    s--;
                    tmpFaces.add(s);
                    //s=Integer.valueOf(tokens[i].split("/")[1]);
                    //s--;
                    //tmpVt.add(s);
                    //s=Integer.valueOf(tokens[i].split("/")[2]);
                    //s--;
                    //tmpVn.add(s);
                }
                faces.addAll(triangulate(tmpFaces));
                vtPointer.addAll(triangulate(tmpVn));
                vnPointer.addAll(triangulate(tmpVn));
            }
        }
    }
}
