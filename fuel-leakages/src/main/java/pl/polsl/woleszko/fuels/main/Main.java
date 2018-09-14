package pl.polsl.woleszko.fuels.main;

import java.util.ArrayList;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import pl.polsl.woleszko.fuels.route.RouteConfig;

public class Main {
	public static ArrayList<Long> dateIds = new ArrayList<>();
	public static void main(String[] args) {
		
	    //Logback configuration - package level grained
        ((Logger) LoggerFactory.getLogger("pl.polsl.woleszko.fuels.route")).setLevel(Level.INFO);
        ((Logger) LoggerFactory.getLogger("org.apache.camel")).setLevel(Level.INFO);

        

	    RouteConfig app = new RouteConfig();


	}


}
