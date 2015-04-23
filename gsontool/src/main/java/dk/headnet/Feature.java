package dk.headnet;

import java.util.List;

public class Feature {
		
	String type;
	Geometry geometry;
	Properties properties;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	@Override
	public String toString() {
		return "Feature [type=" + type + ", geometry=" + geometry.toString() + ", properties=" + properties.toString() + "]";
	}
	
	
}


/*

{
  "type":"Feature",
  "geometry": {
    "type":"Point",
    "coordinates":[-91.399944,32.830043]
  },
  "properties": {
    "om":"484",
    "time":"18:00:00",
    "tz":"3",
    "st":"LA",
    "stf":"22",
    "stn":"9",
    "mag":"1",
    "inj":"0",
    "fat":"0",
    "loss":"4.0",
    "closs":"0.0",
    "slat":"32.83",
    "slon":"-91.4",
    "elat":"0.0",
    "elon":"0.0",
    "len":"1.0","wid":"33","created_at":"2015-03-03T16:31:22Z","updated_at":"2015-03-03T20:16:21Z","date_fix":"1954-08-23T00:00:00Z","cartodb_id":1606
    }
}

*/