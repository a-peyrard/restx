package samplest.alternative;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.inject.Named;
import restx.factory.Module;
import restx.factory.Provides;

/**
 * @author apeyrard
 */
@Module
public class CollectionsModule {

	@Provides @Named("the.list")
	public List newList() {
		return new ArrayList<>();
	}

	@Provides
	public Set Set() {
		return new HashSet();
	}
}
