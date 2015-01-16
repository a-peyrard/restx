package samplest.alternative;

import restx.factory.Alternative;
import restx.factory.When;

/**
 * @author apeyrard
 */
@Alternative(to = GreetingsMessenger.class)
@When(name = "restx.locale", value = "fr")
public class FrenchGreetingsMessenger extends GreetingsMessenger {

	public String greet(String name) {
		return "bonjour "+name;
	}
}
