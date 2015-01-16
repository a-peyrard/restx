package samplest.alternative;

import com.sun.javafx.sg.PGShape;

import restx.factory.Alternative;
import restx.factory.When;

/**
 * @author apeyrard
 */
@Alternative(to = WhoIsTheBest.class)
@When(name = "restx.mood", value = "megalomaniac")
public class MegalomaniacWhoIsTheBest extends ModestWhoIsTheBest {
	@Override
	public String answer() {
		return "me";
	}
}
