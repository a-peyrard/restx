package samplest.alternative;

import static org.assertj.core.api.Assertions.assertThat;


import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import restx.factory.Factory;

/**
 * @author apeyrard
 */
public class ListsTest {

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
}
