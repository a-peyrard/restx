package restx.config;

import com.google.common.base.Charsets;
import com.google.common.base.Optional;
import com.google.common.io.Files;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import restx.common.ConfigElement;
import restx.common.RestxConfig;
import restx.common.StdRestxConfig;
import restx.config.ConfigSupplier;
import restx.config.ConsolidatedConfigFactoryMachine;
import restx.config.ElementsFromConfigFactoryMachine;
import restx.factory.*;

import java.io.File;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static restx.factory.Factory.LocalMachines.overrideComponents;
import static restx.factory.Factory.LocalMachines.threadLocal;

/**
 * User: xavierhanin
 * Date: 9/24/13
 * Time: 10:05 PM
 */
public class ConfigMachineTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @BeforeClass
    public static void deactivateElementsFromConfig() {
        System.setProperty("restx.activation::java.lang.String::mandatory.dep.result1", "false");
        System.setProperty("restx.activation::java.lang.String::mandatory.dep.result2", "false");
    }

    @Test
    public void should_load_configs_from_system_properties() throws Exception {
        System.setProperty("restx.test.system.property", "v1");
        Factory factory = Factory.builder()
                // we add system property machine just to make sure it doesn't break anything, it isn't required
                .addMachine(new SystemPropertyFactoryMachine())

                .addMachine(new ConsolidatedConfigFactoryMachine())
                .build();

        Optional<RestxConfig> configOptional = factory.queryByClass(RestxConfig.class).findOneAsComponent();

        assertThat(configOptional.isPresent()).isTrue();
        assertThat(configOptional.get().getString("restx.test.system.property")).isEqualTo(Optional.of("v1"));
        assertThat(configOptional.get().getElement("restx.test.system.property").get().getOrigin()).isEqualTo("system");
    }

    @Test
    public void should_load_configs_from_suppliers() throws Exception {
        Factory factory = Factory.builder()
                .addMachine(new ConsolidatedConfigFactoryMachine())
                .addMachine(configSupplierMachine(0, "s1", ConfigElement.of("key1", "val1")))
                .addMachine(configSupplierMachine(0, "s2", ConfigElement.of("key2", "val2")))
                .build();

        Optional<RestxConfig> configOptional = factory.queryByClass(RestxConfig.class).findOneAsComponent();

        assertThat(configOptional.isPresent()).isTrue();
        assertThat(configOptional.get().getString("key1")).isEqualTo(Optional.of("val1"));
        assertThat(configOptional.get().getString("key2")).isEqualTo(Optional.of("val2"));
    }

    @Test
    public void should_provide_config_elements_as_named_strings() throws Exception {
        Factory factory = Factory.builder()
                .addMachine(new ConsolidatedConfigFactoryMachine())
                .addMachine(new ElementsFromConfigFactoryMachine())
                .addMachine(configSupplierMachine(0, "s1", ConfigElement.of("key1", "val1")))
                .addMachine(configSupplierMachine(0, "s2", ConfigElement.of("key2", "val2")))
                .build();

        Optional<String> s = factory.queryByName(Name.of(String.class, "key1")).findOneAsComponent();

        assertThat(s.isPresent()).isTrue();
        assertThat(s).isEqualTo(Optional.of("val1"));
    }

    @Test
    public void should_provide_individual_config_elements() throws Exception {
        Factory factory = Factory.builder()
                .addMachine(new ConsolidatedConfigFactoryMachine())
                .addMachine(new ElementsFromConfigFactoryMachine())
                .addMachine(configSupplierMachine(0, "s1", ConfigElement.of("o1", "d1", "key1", "val1")))
                .build();

        Optional<ConfigElement> s = factory.queryByName(Name.of(ConfigElement.class, "key1")).findOneAsComponent();

        assertThat(s.isPresent()).isTrue();
        assertThat(s.get()).isEqualToComparingFieldByField(ConfigElement.of("o1", "d1", "key1", "val1"));
    }

    @Test
    public void should_include_named_string_in_config() throws Exception {
        Factory factory = Factory.builder()
                .addMachine(new ConsolidatedConfigFactoryMachine())
                .addMachine(configSupplierMachine(0, "s1", ConfigElement.of("key1", "val1")))
                .addMachine(new SingletonFactoryMachine<>(0, NamedComponent.of(String.class, "key2", "val2")))
                .build();

        Optional<RestxConfig> configOptional = factory.queryByClass(RestxConfig.class).findOneAsComponent();

        assertThat(configOptional.isPresent()).isTrue();
        assertThat(configOptional.get().getString("key1")).isEqualTo(Optional.of("val1"));
        assertThat(configOptional.get().getString("key2")).isEqualTo(Optional.of("val2"));
    }

    @Test
    public void should_use_doc_from_supplied_config() throws Exception {
        Factory factory = Factory.builder()
                .addMachine(new ConsolidatedConfigFactoryMachine())
                .addMachine(configSupplierMachine(0, "s1", ConfigElement.of("s1", "doc1", "key1", "val1")))
                .addMachine(new SingletonFactoryMachine<>(0, NamedComponent.of(String.class, "key1", "val2")))
                .build();

        Optional<RestxConfig> configOptional = factory.queryByClass(RestxConfig.class).findOneAsComponent();

        assertThat(configOptional.isPresent()).isTrue();
        assertThat(configOptional.get().getString("key1")).isEqualTo(Optional.of("val2"));
        assertThat(configOptional.get().getElement("key1").get())
                .isEqualToComparingFieldByField(ConfigElement.of("factory", "doc1", "key1", "val2"));
    }

    @Test
    public void should_load_from_loader() throws Exception {

        Factory factory = Factory.builder()
                .addFromServiceLoader()
                .build();

        Optional<RestxConfig> configOptional = factory.queryByClass(RestxConfig.class).findOneAsComponent();

        assertThat(configOptional.isPresent()).isTrue();
        assertThat(configOptional.get().getString("key1")).isEqualTo(Optional.of("val1"));
        assertThat(configOptional.get().getElement("key1").get())
                .isEqualToComparingFieldByField(ConfigElement.of(
                        "classpath:restx/common/config.properties", "Doc 1", "key1", "val1"));
    }

    @Test
    public void should_override_loaded_configs_from_loader() throws Exception {
        overrideComponents().set("key1", "foo");

        Factory factory = Factory.builder()
                .addLocalMachines(threadLocal())
                .addFromServiceLoader()
                .build();

        Optional<RestxConfig> configOptional = factory.queryByClass(RestxConfig.class).findOneAsComponent();

        assertThat(configOptional.isPresent()).isTrue();
        assertThat(configOptional.get().getString("key1")).isEqualTo(Optional.of("foo"));
    }

    @Test
    public void should_loader_load_settings_from_file_setup_with_system_property() throws Exception {
        String key = "restx.common.config.location";
        try {
            File file = folder.newFile("config.properties");
            Files.append("key1=myval1", file, Charsets.UTF_8);
            System.setProperty(key, file.getAbsolutePath());

            Factory factory = Factory.builder()
                    .addFromServiceLoader()
                    .build();

            RestxConfig config = factory.getComponent(RestxConfig.class);

            assertThat(config.getString("key1")).isEqualTo(Optional.of("myval1"));
            assertThat(config.getElement("key1").get())
                    .isEqualToComparingFieldByField(ConfigElement.of(
                            "file://" + file.getAbsolutePath(), "Doc 1", "key1", "myval1"));

        } finally {
            System.clearProperty(key);
        }
    }

    @Test
    public void should_loader_load_settings_from_file() throws Exception {
        File file = folder.newFile("config.properties");
        Files.append("key1=myval1", file, Charsets.UTF_8);

        RestxConfig config = new ConfigLoader(Optional.<String>absent())
                .fromFile(file.getAbsolutePath()).get();

        assertThat(config.getString("key1")).isEqualTo(Optional.of("myval1"));
        assertThat(config.getElement("key1").get())
                .isEqualToComparingFieldByField(ConfigElement.of(
                        "file://" + file.getAbsolutePath(), "", "key1", "myval1"));
    }

    private SingletonFactoryMachine<ConfigSupplier> configSupplierMachine(
            int priority, String name, final ConfigElement... config) {
        return new SingletonFactoryMachine<>(priority,
                NamedComponent.of(ConfigSupplier.class, name, new ConfigSupplier() {
                    @Override
                    public RestxConfig get() {
                        return StdRestxConfig.of(asList(config));
                    }
                }));
    }
}
