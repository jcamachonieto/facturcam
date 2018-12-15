/**
 * 
 */
package es.efactura;

import java.awt.EventQueue;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import es.efactura.principal.PrincipalFrame;

/**
 * @author jcamacho
 *
 */
@SpringBootApplication
public class App {

	public App() {

	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {

		ConfigurableApplicationContext ctx = new SpringApplicationBuilder(App.class).headless(false).run(args);

		EventQueue.invokeLater(() -> {
			PrincipalFrame principalFrame = ctx.getBean(PrincipalFrame.class);
			principalFrame.initialize();
		});
	}

}
