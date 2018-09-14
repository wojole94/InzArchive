package pl.polsl.woleszko.fuels.model.types;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class Tube {
	Integer length = 0;
	Double slope;
	Double intercept;
	Double tolerance;

	public Tube(Double slope, Double intercept, Double tolerance) {
		this.slope = slope;
		this.intercept = intercept;
		this.tolerance = tolerance;
	}
	
	public Boolean isInTube(Integer point, Double value) {
		Double tubeMax = point * slope + intercept + tolerance;
		Double tubeMin = point * slope + intercept - tolerance;

		if (value >= tubeMax || value <= tubeMin)
			return false;
		else
			return true;
	}
	
	public void incrementLength() {
		length++;
	}
}
