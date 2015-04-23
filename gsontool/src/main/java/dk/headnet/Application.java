package dk.headnet;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class Application {
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		List<Feature> features = Gsontools.readGeoJson(new FileInputStream(args[0]));
		
		/*for (Feature f : features) {
			System.out.println(f.toString());
		}*/
	}
}
