package zone.guildball.backend;

import io.dropwizard.Configuration;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class GBZoneApplicationConfiguration extends Configuration {

	private String version;
}
