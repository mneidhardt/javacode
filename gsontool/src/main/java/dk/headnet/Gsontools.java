package dk.headnet;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;


/**
 * @author mine
 *
 */
public class Gsontools {

    public static List<Feature> readGeoJson(InputStream in) throws IOException {
    	Gson gson = new Gson();
    	
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        List<Feature> features = new ArrayList<Feature>();
        reader.beginArray();
        while (reader.hasNext()) {
            Feature feature = gson.fromJson(reader, Feature.class);
            transformFeature(gson, feature);
            features.add(feature);
        }
        reader.endArray();
        reader.close();
        return features;
    }

    
    private static void transformFeature(Gson gson, Feature feature) {
        double lon = feature.geometry.coordinates.get(0);
        lon = (lon + 180.0)%180;
        double lat = feature.geometry.coordinates.get(1);
        String createdat = feature.properties.getCreated_at();
        
        System.out.println(lon + "," + lat + "," + createdat);
    }

    /*    public static Simplejson readJsonStream(InputStream in) throws IOException {
    	Gson gson = new Gson();
    	
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        reader.beginArray();
        while (reader.hasNext()) {
            Feature feature = gson.fromJson(reader, Feature.class);
            features.add(feature);
        }
        reader.endArray();
        reader.close();
        return features;
    }*/
}
