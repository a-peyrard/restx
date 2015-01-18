package samplest.alternative;

import javax.inject.Named;
import restx.factory.Component;

/**
 * @author apeyrard
 */
@Component
@Named("who.is.the.best")
public class ModestWhoIsTheBest implements WhoIsTheBest {

	@Override
	public String answer() {
		return "you";
	}
}
