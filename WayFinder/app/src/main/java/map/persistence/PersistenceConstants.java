package map.persistence;

/**
 * Created by roberto on 6/7/16.
 */
public class PersistenceConstants {

    //DBHelper Constants
    public static final String DATABASE_NAME   = "MyDBName.db";
    public static final String NODE_TABLE_NAME = "Nodes";
    public static final String EDGE_TABLE_NAME = "Edges";

    public static final String NODE_COLUMN_MAJOR    = "Major";
    public static final String NODE_COLUMN_MINOR    = "Minor";
    public static final String NODE_COLUMN_CATEGORY = "Category";
    public static final String NODE_COLUMN_AUDIO    = "Audio";
    public static final String NODE_COLUMN_STEPS    = "Steps";

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

    //JSON handling constants
    public static final String NODES_LIST_NAME = "nodes";
    public static final String EDGES_LIST_NAME = "edges";
}
