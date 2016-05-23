package map.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.UUID;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.estimote.sdk.Region;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import graph.Edge;
import graph.Node;

/**
 * Created by roberto on 5/23/16.
 */
public class DBHelper extends SQLiteOpenHelper {

    private final String TAG_DEBUG_APP = "BLIND_";
    private final String TAG_DEBUG = "DBHELPER";

    public static final String DATABASE_NAME     = "MyDBName.db";
    public static final String NODE_TABLE_NAME   = "Nodes";
    public static final String EDGE_TABLE_NAME   = "Edges";

    public static final String NODE_COLUMN_MAJOR = "Major";
    public static final String NODE_COLUMN_MINOR = "Minor";
    public static final String NODE_COLUMN_TYPE  = "Type";
    public static final String NODE_COLUMN_AUDIO = "Audio";
    public static final String NODE_COLUMN_STEPS = "Steps";

    public static final String EDGE_COLUMN_FROM_MAJOR = "From_Major";
    public static final String EDGE_COLUMN_FROM_MINOR = "From_Minor";
    public static final String EDGE_COLUMN_TO_MAJOR   = "To_Major";
    public static final String EDGE_COLUMN_TO_MINOR   = "To_Minor";
    public static final String EDGE_COLUMN_DEGREE     = "Degree";
    public static final String EDGE_COLUMN_DISTANCE   = "Distance";

    public static final String UUID_String = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
    public static final String ROOM_LABEL    = "ROOM";
    public static final String OUTDOOR_LABEL = "OUTDOOR";
    public static final String STAIRS_LABEL  = "STAIRS";


    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table Nodes (" +
                    "Major integer, Minor integer, Type text, Audio text, Steps integer, "+
                    "unique(Major,Minor))"
        );
        db.execSQL(
                "create table Edges (" +
                    "From_Major integer, From_Minor integer, To_Major integer, To_Minor integer, " +
                    "Degree integer, Distance integer, "+
                    "unique(From_Major,From_Minor,To_Major, To_Minor))"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS Nodes");
        db.execSQL("DROP TABLE IF EXISTS Edges");
        onCreate(db);
    }

    public boolean insertNode(int major, int minor, String type, String audio, int steps){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NODE_COLUMN_MAJOR, major);
        contentValues.put(NODE_COLUMN_MINOR, minor);
        contentValues.put(NODE_COLUMN_AUDIO, audio);
        contentValues.put(NODE_COLUMN_TYPE, type);
        contentValues.put(NODE_COLUMN_STEPS, steps);
        db.insert("Nodes", null, contentValues);
        return true;
    }

    public boolean insertEdge(int fromMajor, int fromMinor, int toMajor, int toMinor, int degree, int distance){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EDGE_COLUMN_FROM_MAJOR, fromMajor);
        contentValues.put(EDGE_COLUMN_FROM_MINOR, fromMinor);
        contentValues.put(EDGE_COLUMN_TO_MINOR, toMajor);
        contentValues.put(EDGE_COLUMN_TO_MINOR, toMinor);
        contentValues.put(EDGE_COLUMN_DEGREE, degree);
        contentValues.put(EDGE_COLUMN_DISTANCE, distance);
        db.insert("Edges", null, contentValues);
        return true;
    }

    public Cursor getNodes(int major){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from Nodes where Major="+major+"", null );
        return res;
    }
    public Cursor getEdges(int major){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from Edges where From_Major="+major+"", null );
        return res;
    }

    // Given a major related to a particular building, this method return an HashMap with the shape
    // used into GraphMap class
    public LinkedHashMap<Region, Node> getMap(int mapMajor) {
        LinkedHashMap<Region, Node> graphMap = new LinkedHashMap<Region, Node>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor resNodes =  this.getNodes(mapMajor);
        Cursor resEdges =  this.getEdges(mapMajor);
        resEdges.moveToFirst();
        resNodes.moveToFirst();

        // Initializing Nodes
        while(resNodes.isAfterLast() == false){
            //TODO
            // I already know it: it's mapMajor
            // int major    = resNodes.getInt(resNodes.getColumnIndex(NODE_COLUMN_MAJOR));
            int minor    = resNodes.getInt(resNodes.getColumnIndex(NODE_COLUMN_MINOR));
            String audio = resNodes.getString(resNodes.getColumnIndex(NODE_COLUMN_AUDIO));
            String type  = resNodes.getString(resNodes.getColumnIndex(NODE_COLUMN_TYPE));
            int steps    = resNodes.getInt(resNodes.getColumnIndex(NODE_COLUMN_STEPS));

            Region region = new Region(audio, UUID.fromString(UUID_String), mapMajor, minor);
            Node node = new Node();
            node.setAudio(audio);
            node.setSteps(steps);
            if(type.equals(STAIRS_LABEL)) node.setCategory(Node.CATEGORY.STAIRS);
            else if(type.equals(OUTDOOR_LABEL)) node.setCategory(Node.CATEGORY.OUTDOOR);
            else{
                node.setCategory(Node.CATEGORY.ROOM);
                if(!type.equals(ROOM_LABEL))
                    Log.d(TAG_DEBUG_APP+TAG_DEBUG, "Tipo non previsto: "+type);
            }
            graphMap.put(region, node);
            resNodes.moveToNext();
        }

        // Initializing Edges
        while(resEdges.isAfterLast() == false){
            // I already know it: it's mapMajor
            // int fromMajor = resEdges.getInt(resEdges.getColumnIndex(EDGE_COLUMN_FROM_MAJOR));
            int fromMinor = resEdges.getInt(resEdges.getColumnIndex(EDGE_COLUMN_FROM_MINOR));
            // I already know it: it's mapMajor
            // int toMajor   = resEdges.getInt(resEdges.getColumnIndex(EDGE_COLUMN_TO_MAJOR));
            int toMinor   = resEdges.getInt(resEdges.getColumnIndex(EDGE_COLUMN_TO_MINOR));
            int distance  = resEdges.getInt(resEdges.getColumnIndex(EDGE_COLUMN_DISTANCE));
            int degree    = resEdges.getInt(resEdges.getColumnIndex(EDGE_COLUMN_DEGREE));

            Cursor resFromNode = db.rawQuery( "select * from Nodes " +
                    "where From_Major="+mapMajor+" and From_Minor="+fromMinor+"", null );
            Cursor resToNode = db.rawQuery( "select * from Nodes " +
                    "where To_Major="+mapMajor+" and To_Minor="+toMinor+"", null );
            resFromNode.moveToFirst(); resToNode.moveToFirst();

            String fromAudio = resFromNode.getString(resFromNode.getColumnIndex(NODE_COLUMN_AUDIO));
            String toAudio   = resFromNode.getString(resToNode.getColumnIndex(NODE_COLUMN_AUDIO));

            Region fromRegion = new Region(fromAudio, UUID.fromString(UUID_String), mapMajor, fromMinor);
            Region toRegion   = new Region(toAudio,   UUID.fromString(UUID_String), mapMajor,   toMinor);
            Node fromNode = graphMap.get(fromRegion);
            if( fromNode == null ){
                Log.d(TAG_DEBUG_APP+TAG_DEBUG, "fromNode non trovato. FromMajor="+mapMajor+", fromMinor="+fromMinor);
            }
            Node toNode = graphMap.get(toRegion);
            if( toNode == null ){
                Log.d(TAG_DEBUG_APP+TAG_DEBUG, "toNode non trovato. toMajor="+mapMajor+", toMinor="+toMinor);
            }
            Edge edge = new Edge(fromNode, toNode, degree, distance);
            if(fromNode != null) fromNode.addEdge(edge);
            else Log.d(TAG_DEBUG_APP+TAG_DEBUG, "Dato che fromNode è null, non posso inserire l'arco");
            graphMap.put(fromRegion, fromNode);

            resEdges.moveToNext();
        }

        // Once I have initialized the graph properly, I can return it.
        return graphMap;
    }

    public void insertMaps(JSONArray maps){
        //TODO
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS Nodes");
        db.execSQL("DROP TABLE IF EXISTS Edges");
        onCreate(db);

        //Once I dropped all the content of the database, I can put the new maps into it
        for(int i=0; i<maps.length(); i++) {
            try {
                JSONObject map = maps.getJSONObject(i);
                JSONArray nodes = map.getJSONArray("nodes");
                JSONArray edges = map.getJSONArray("edges");

                for(int j=0; j<nodes.length(); j++){
                    JSONObject node = nodes.getJSONObject(j);
                    String audio = node.getString("audio");
                    int major    = node.getInt("major");
                    int minor    = node.getInt("major");
                    String type  = node.getString("category");
                    int steps    = node.getInt("steps");
                    this.insertNode(major, minor, type, audio, steps);
                }

                for(int j=0; j<edges.length(); j++){
                    JSONObject edge = edges.getJSONObject(j);
                    String[] nodeFrom = edge.getString("nodeFrom").split(" ");
                    String[] nodeTo   = edge.getString("nodeTo").split(" ");
                    int direction = edge.getInt("direction");
                    int distance  = edge.getInt("distance");
                    int fromMajor = Integer.parseInt(nodeFrom[0]);
                    int fromMinor = Integer.parseInt(nodeFrom[1]);
                    int toMajor   = Integer.parseInt(nodeTo[0]);
                    int toMinor   = Integer.parseInt(nodeTo[1]);
                    this.insertEdge(fromMajor, fromMinor, toMajor, toMinor, direction, distance);
                }
            }
            catch (JSONException e){
                Log.d(TAG_DEBUG_APP+TAG_DEBUG, "Bad format exception in map at index "+i+".");
            }
        }
    }
}