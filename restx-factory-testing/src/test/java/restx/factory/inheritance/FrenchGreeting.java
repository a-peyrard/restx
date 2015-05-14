package restx.factory.inheritance;

/**
 * @author apeyrard
 */
public class FrenchGreeting implements Greeting {
	@Override
	public String greet(String name) {
		return "bonjour " + name;
	}
}
