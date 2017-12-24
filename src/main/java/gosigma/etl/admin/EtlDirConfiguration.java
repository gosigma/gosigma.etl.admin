package gosigma.etl.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class EtlDirConfiguration extends WebMvcConfigurerAdapter {
	public static Logger log = LoggerFactory.getLogger(EtlDirConfiguration.class);

	@Value("${etl.dir}")
	private String _etlDir;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		log.info("register etl_files/** to " + _etlDir);
		registry.addResourceHandler("/etl_files/**").addResourceLocations("file:///" + _etlDir + "/");
	}

	//	// setup default index view for /
	//	// index.html must put in /template folder
	//	@Controller
	//	public static class Routes {
	//
	//		@RequestMapping({ "/", "/default", "/index" })
	//		public String index() {
	//			return "index";
	//		}
	//	}
	
	// setup default index.html for /
	// index.html must put in /static folder
	@Override
	public void addViewControllers( ViewControllerRegistry registry ) {
		registry.addViewController( "/" ).setViewName( "forward:/index.html" );

		registry.setOrder( Ordered.HIGHEST_PRECEDENCE );
		super.addViewControllers( registry );
	}
}
