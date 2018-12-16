/**
 * 
 */
package es.efactura;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JProgressBar;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import es.efactura.license.model.LicenseDataProvider;
import es.efactura.principal.PrincipalFrame;

/**
 * @author jcamacho
 *
 */
@SpringBootApplication
public class App {

	@SuppressWarnings("unused")
	public static void main(String[] args) {

		ConfigurableApplicationContext ctx = new SpringApplicationBuilder(App.class).headless(false).run(args);

		EventQueue.invokeLater(() -> {
			PrincipalFrame principalFrame = ctx.getBean(PrincipalFrame.class);
			
			LicenseDataProvider licenseDataProvider = ctx.getBean(LicenseDataProvider.class);
			
			JFrame frame = new JFrame("Inicializando");
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			frame.setBounds(0, 0, (int) dim.getWidth(), (int) dim.getHeight());
		    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    frame.setLocationRelativeTo(null);
		    final JProgressBar aJProgressBar = new JProgressBar(JProgressBar.HORIZONTAL);
		    aJProgressBar.setIndeterminate(true);

		    frame.add(aJProgressBar, BorderLayout.NORTH);
		    frame.setSize(300, 200);
		    frame.setVisible(true);
		    
		    if (licenseDataProvider.isValidLicense()) {
		    	principalFrame.initialize();	
		    	frame.setVisible(false);
		    } else {
		    	System.exit(0);
		    }
		});
	}

}
