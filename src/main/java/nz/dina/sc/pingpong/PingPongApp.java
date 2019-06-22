package nz.dina.sc.pingpong;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Expose REST endpoints to test the app
 * 
 * URL HTTP Verb Description 1. /ping GET Returns a string "pong" to show the
 * app is running. 2. /version GET Returns git commit details in JSON. 3. /echo
 * GET Returns client ip, server host / ip in JSON.
 */
@SpringBootApplication
@RestController
public class PingPongApp {

	@Autowired
	private HttpServletRequest request;

	@Value("${git.commit.id}")
	private String commitId;

	@Value("${git.branch}")
	private String branch;

	@Value("${git.commit.message.short}")
	private String commitMessage;

	@Bean
	public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
		PropertySourcesPlaceholderConfigurer cfg = new PropertySourcesPlaceholderConfigurer();
		cfg.setLocation(new ClassPathResource("git.properties"));
		cfg.setIgnoreResourceNotFound(true);
		cfg.setIgnoreUnresolvablePlaceholders(true);
		return cfg;
	}

	public static void main(String[] args) {
		SpringApplication.run(PingPongApp.class, args);
	}

	@RequestMapping(value = "/ping", method = RequestMethod.GET)
	public String ping() {
		return "pong";
	}

	@RequestMapping(value = "/version", method = RequestMethod.GET)
	public Map<String, String> getCodeVersion() {
		Map<String, String> result = new HashMap<>();
		result.put("Commit id", commitId);
		result.put("Commit branch", branch);
		result.put("Commit message", commitMessage);
		return result;
	}

	@RequestMapping(value = "/whoami", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, String> peformEcho() {
		Map<String, String> result = new HashMap<>();
		result.put("Request IP", request.getRemoteAddr());
		result.put("Host", request.getLocalName());
		result.put("IP", request.getLocalAddr());
		return result;
	}

}
