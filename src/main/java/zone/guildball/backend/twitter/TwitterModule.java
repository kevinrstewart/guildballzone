package zone.guildball.backend.twitter;

import com.google.inject.AbstractModule;

public class TwitterModule extends AbstractModule {


	@Override
	protected void configure() {
		bind(TwitterClient.class).asEagerSingleton();
	}

}
