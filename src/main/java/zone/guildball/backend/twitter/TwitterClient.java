package zone.guildball.backend.twitter;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.netflix.config.DynamicPropertyFactory;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.event.Event;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

import lombok.SneakyThrows;
import zone.guildball.backend.config.DynamicConfig;

public class TwitterClient {

	private Client hosebirdClient;
	private Thread twitterThread;

	@Inject
	public TwitterClient(DynamicConfig config) {
		BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>(100000);
		BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<Event>(1000);

		/**
		 * Declare the host you want to connect to, the endpoint, and
		 * authentication (basic auth or oauth)
		 */
		Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
		StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();
		// Optional: set up some followings and track terms
		List<Long> followings = Lists.newArrayList(1234L, 566788L);
		List<String> terms = Lists.newArrayList("twitter", "api");
		hosebirdEndpoint.followings(followings);
		hosebirdEndpoint.trackTerms(terms);

		// These secrets should be read from a config file
		Authentication hosebirdAuth = new OAuth1(
				DynamicPropertyFactory.getInstance().getStringProperty("api.key", "none").get(),
				DynamicPropertyFactory.getInstance().getStringProperty("api.secret", "none").get(),
				DynamicPropertyFactory.getInstance().getStringProperty("access.token", "none").get(),
				DynamicPropertyFactory.getInstance().getStringProperty("access.token.secret", "none").get());

		ClientBuilder builder = new ClientBuilder().hosts(hosebirdHosts).authentication(hosebirdAuth)
				.endpoint(hosebirdEndpoint).processor(new StringDelimitedProcessor(msgQueue))
				.eventMessageQueue(eventQueue); // optional: use this if you
												// want to process client events

		hosebirdClient = builder.build();
		// Attempts to establish a connection.
		hosebirdClient.connect();

		twitterThread = new Thread(new Runnable() {

			@Override
			@SneakyThrows
			public void run() {
				while (!hosebirdClient.isDone()) {
					String message = msgQueue.take();
					System.out.println(message);
				}
			}
		});

		twitterThread.start();
	}

}
