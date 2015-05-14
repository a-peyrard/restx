package restx.factory.inheritance;

import com.google.common.base.Optional;
import org.assertj.core.api.JUnitSoftAssertions;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import restx.factory.Factory;
import restx.factory.Name;
import restx.factory.NamedComponent;

/**
 * @author apeyrard
 */
public class GreetingInheritanceTest {

	/**
	 * ElementsFromConfig component can not be build, because of module TestMandatoryDependency
	 * which use a missing dependency.
	 */
	@BeforeClass
	public static void deactivateElementsFromConfig() {
		System.setProperty("restx.activation::restx.factory.FactoryMachine::ElementsFromConfig", "false");
	}

	@Rule
	public JUnitSoftAssertions softly = new JUnitSoftAssertions();

	@Test
	public void should_query_annotated_components_with_full_type_hierarchy() {
		Factory factory = Factory.newInstance();

		Optional<NamedComponent<FrenchGreetingComponent>> component1 =
				factory.queryByName(Name.of(FrenchGreetingComponent.class, "french-greeting-component")).findOne();
		softly.assertThat(component1.isPresent()).isTrue();

		Optional<NamedComponent<FrenchGreeting>> component2 =
				factory.queryByName(Name.of(FrenchGreeting.class, "french-greeting-component")).findOne();
		softly.assertThat(component2.isPresent()).isTrue();

		Optional<NamedComponent<Greeting>> component3 =
				factory.queryByName(Name.of(Greeting.class, "french-greeting-component")).findOne();
		softly.assertThat(component3.isPresent()).isTrue();
	}

	@Test
	public void should_query_provided_components_with_full_type_hierarchy() {
		Factory factory = Factory.newInstance();

		Optional<NamedComponent<FrenchGreeting>> component1 =
				factory.queryByName(Name.of(FrenchGreeting.class, "french-greeting-provides")).findOne();
		softly.assertThat(component1.isPresent()).isTrue();

		Optional<NamedComponent<Greeting>> component2 =
				factory.queryByName(Name.of(Greeting.class, "french-greeting-provides")).findOne();
		softly.assertThat(component2.isPresent()).isTrue();
	}
}
