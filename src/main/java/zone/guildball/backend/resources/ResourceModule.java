package zone.guildball.backend.resources;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

public class ResourceModule extends AbstractModule {

	@Override
	protected void configure() {
		Multibinder<Resource> resourceBinder = Multibinder.newSetBinder(this.binder(), Resource.class);
		resourceBinder.addBinding().to(Display.class).asEagerSingleton();
	}

}