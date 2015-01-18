package samplest.alternative;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import restx.factory.Module;
import restx.factory.Provides;

/**
 * @author apeyrard
 */
@Module
public class ListsModule {

	@Provides @Named("the.list")
	public List newList() {
		return new ArrayList<>();
	}
}
