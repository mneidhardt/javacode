package dk.headnet;

import java.util.List;

public class Geometry {

	String type;
	List<Double> coordinates;

	@Override
	public String toString() {
		return "Geometry [type=" + type + ", coordinates=" + coordinates.toString() + "]";
	}
}
