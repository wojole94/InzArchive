package pl.polsl.woleszko.fuels_dsmock.main;

import org.mortbay.log.Log;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import pl.polsl.woleszko.fuels_dsmock.route.RouteConfig;

public class Main {

	public static void main(String[] args) {

		// Logback configuration - package level grained
		((Logger) LoggerFactory.getLogger("pl.polsl.woleszko")).setLevel(Level.INFO);

		if (args.length==2 && (args[0] != null && args[1] != null)) {
			RouteConfig app = new RouteConfig(args[0], args[1]);
		}
		else Log.info("Cannot start because some of args are empty");
	}

}
