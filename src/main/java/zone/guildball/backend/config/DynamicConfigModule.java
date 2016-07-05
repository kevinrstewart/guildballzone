package zone.guildball.backend.config;

import com.google.inject.AbstractModule;

public class DynamicConfigModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(DynamicConfig.class).asEagerSingleton();
	}

}
