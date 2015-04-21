package restx.config;

import javax.inject.Named;
import restx.config.ConfigLoader;
import restx.config.ConfigSupplier;
import restx.factory.Module;
import restx.factory.Provides;

/**
 * User: xavierhanin
 * Date: 9/25/13
 * Time: 12:11 AM
 */
@Module
public class ConfigLoaderTestModule {

    @Provides @Named("key1")
    public String key1() {
        return "bar";
    }

    @Provides
    public ConfigSupplier commonConfig(ConfigLoader loader) {
        return loader.fromResource("restx/common/config");
    }
}
