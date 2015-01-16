package samplest.alternative;

import javax.inject.Named;
import restx.factory.Component;

/**
 * @author apeyrard
 */
@Component
public class GreetingsMessenger {

	public String greet(String name) {
		return "hello "+name;
	}
}
