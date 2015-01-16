package samplest.alternative;

import static org.assertj.core.api.Assertions.assertThat;


import org.junit.Test;

import restx.factory.Factory;

/**
 * Simple Alternative test, a component {@link GreetingsMessenger} is defined,
 * and an extension is done {@link FrenchGreetingsMessenger} and use as alternative
 * to greetings messenger when "restx.locale" is equal to "fr".
 *
 * @author apeyrard
 */
public class GreetingsMessengerTest {

	@Test
	public void should_use_alternative() throws Exception {
		Factory factory = Factory.builder().addFromServiceLoader().build();
		String greet = factory.getComponent(GreetingsMessenger.class).greet("augustin");
		assertThat(greet).isEqualTo("hello augustin");

		System.setProperty("restx.locale", "fr");
		factory = Factory.builder().addFromServiceLoader().build();
		greet = factory.getComponent(GreetingsMessenger.class).greet("augustin");
		assertThat(greet).isEqualTo("bonjour augustin");
	}
}
