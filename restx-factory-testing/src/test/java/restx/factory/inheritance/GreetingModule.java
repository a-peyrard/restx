package restx.factory.inheritance;

import javax.inject.Named;
import restx.factory.Module;
import restx.factory.Provides;

/**
 * @author apeyrard
 */
@Module
public class GreetingModule {

	@Provides
	@Named("french-greeting-provides")
	public FrenchGreeting frenchGreeting() {
		return new FrenchGreeting();
	}
}
