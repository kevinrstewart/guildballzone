package zone.guildball.backend.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;
import com.netflix.config.DynamicPropertyFactory;

import lombok.SneakyThrows;

@Path("/display")
@Produces(MediaType.APPLICATION_JSON)
public class Display implements Resource {
	
	public Display() {
		
	}

	@GET
	@Timed
	@SneakyThrows
	public String sayHello(@QueryParam("name") Optional<String> name) {
		return "hello, " + DynamicPropertyFactory.getInstance().getStringProperty("access.token", "none").get() + "!";
	}
}
