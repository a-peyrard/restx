package samplest.alternative;

import javax.inject.Named;
import restx.factory.Alternative;
import restx.factory.When;

/**
 * @author apeyrard
 */
@Alternative(to = ModestWhoIsTheBest.class)
@When(name = "restx.mood", value = "megalomaniac")
public class MegalomaniacWhoIsTheBest extends ModestWhoIsTheBest {
	@Override
	public String answer() {
		return "me";
	}
}
