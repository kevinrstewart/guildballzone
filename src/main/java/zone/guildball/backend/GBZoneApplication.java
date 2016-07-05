package zone.guildball.backend;

import java.util.Set;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.hubspot.dropwizard.guice.GuiceBundle;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import zone.guildball.backend.config.DynamicConfigModule;
import zone.guildball.backend.resources.Resource;
import zone.guildball.backend.resources.ResourceModule;
import zone.guildball.backend.twitter.TwitterModule;

public class GBZoneApplication extends Application<GBZoneApplicationConfiguration> {

	private GuiceBundle<GBZoneApplicationConfiguration> guiceBundle;

	public static void main(String[] args) throws Exception {
		new GBZoneApplication().run(args);
	}

	@Override
	public String getName() {
		return "guildball-zone";
	}

	@Override
	public void run(final GBZoneApplicationConfiguration configuration, Environment environment) {

		Injector injector = Guice.createInjector(
				new ResourceModule(),
				new DynamicConfigModule(),
				new TwitterModule()
		);

		
		final TypeLiteral<Set<Resource>> setOfResources = new TypeLiteral<Set<Resource>>() {
		};
		final Set<Resource> resources = injector.getInstance(Key.get(setOfResources));
		resources.stream().forEach(r -> environment.jersey().register(r));
	}
}
