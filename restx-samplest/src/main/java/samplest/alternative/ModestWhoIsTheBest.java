package samplest.alternative;

import restx.factory.Component;

/**
 * @author apeyrard
 */
@Component
public class ModestWhoIsTheBest implements WhoIsTheBest {

	@Override
	public String answer() {
		return "you";
	}
}
