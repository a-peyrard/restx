package samplest.alternative;

import static org.assertj.core.api.Assertions.assertThat;


import org.junit.Test;

import restx.factory.Factory;

/**
 * Test to show use of alternative, of components based on interface.
 * An interface have been created {@link WhoIsTheBest} and depending on
 * "restx.mood" value, either {@link ModestWhoIsTheBest} or {@link MegalomaniacWhoIsTheBest}
 * will be returned by the factory.
 *
 * @author apeyrard
 */
public class WhoIsTheBestTest {

	@Test
	public void should_use_alternative() throws Exception {
		Factory factory = Factory.builder().addFromServiceLoader().build();
//		String greet = factory.getComponent(WhoIsTheBest.class).answer();
		String greet = factory.getComponent(ModestWhoIsTheBest.class).answer();
		assertThat(greet).isEqualTo("you");

		System.setProperty("restx.mood", "megalomaniac");
		factory = Factory.builder().addFromServiceLoader().build();
//		greet = factory.getComponent(WhoIsTheBest.class).answer();
		greet = factory.getComponent(ModestWhoIsTheBest.class).answer();
		assertThat(greet).isEqualTo("you");
	}
}
