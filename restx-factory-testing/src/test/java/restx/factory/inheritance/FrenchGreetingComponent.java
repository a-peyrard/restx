package restx.factory.inheritance;

import javax.inject.Named;
import restx.factory.Component;

/**
 * @author apeyrard
 */
@Component
@Named("french-greeting-component")
public class FrenchGreetingComponent extends FrenchGreeting {
}
