package samplest.alternative;

import static org.assertj.core.api.Assertions.assertThat;


import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import restx.factory.Factory;

/**
 * @author apeyrard
 */
public class CollectionsTest {

	/*
		This test does not work if we uncomment the second part of the test,
		so it's not possible to use alternative there.
	 */
	@Test
	public void should_use_alternative_of_provided_component() {
		Factory factory = Factory.builder().addFromServiceLoader().build();
		List component = factory.getComponent(List.class);
		assertThat(component instanceof ArrayList).isTrue();

		System.setProperty("restx.test.list.impl", "LinkedList");

		// make the factory throw an exception: "more than one component is available for query QueryByClass{componentClass=java.util.List[]}."
//		factory = Factory.builder().addFromServiceLoader().build();
//		component = factory.getComponent(List.class);
//		assertThat(component instanceof LinkedListAlternative).isTrue();
	}

	/*
		This test use a hack to create an alternative for a provided component,
		the component has to be provided using a method having the same name as
		the single name of the component's class.
 	*/
	@Test
	public void should_allow_alternative_of_provided_component_with_a_hack() {
		Factory factory = Factory.builder().addFromServiceLoader().build();
		Set component = factory.getComponent(Set.class);
		assertThat(component instanceof HashSet).isTrue();

		System.setProperty("restx.test.set.impl", "TreeSet");

		// make the factory throw an exception: "more than one component is available for query QueryByClass{componentClass=java.util.List[]}."
		factory = Factory.builder().addFromServiceLoader().build();
		component = factory.getComponent(Set.class);
		assertThat(component instanceof TreeSetAlternative).isTrue();
	}
}
