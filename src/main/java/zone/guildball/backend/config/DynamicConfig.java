package zone.guildball.backend.config;

import java.util.concurrent.TimeUnit;

import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3Client;
import com.google.inject.Inject;
import com.netflix.config.ConfigurationManager;
import com.netflix.config.DynamicConfiguration;
import com.netflix.config.FixedDelayPollingScheduler;
import com.netflix.config.sources.S3ConfigurationSource;

public class DynamicConfig {

	@Inject
	public DynamicConfig() {
		AmazonS3Client s3 = new AmazonS3Client(new AWSCredentialsProviderChain(new ProfileCredentialsProvider("gbzone"),
				new InstanceProfileCredentialsProvider()));
		S3ConfigurationSource source = new S3ConfigurationSource(s3, "gbzone", "twitter.props");
		FixedDelayPollingScheduler scheduler = new FixedDelayPollingScheduler(0,
				Math.toIntExact(TimeUnit.SECONDS.toMillis(60)), false);
		DynamicConfiguration config = new DynamicConfiguration(source, scheduler);
		ConfigurationManager.loadPropertiesFromConfiguration(config);
	}
	
}
