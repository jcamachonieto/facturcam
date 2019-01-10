package com.efactura.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.efactura.bill.model.BillEntity;
import com.efactura.bill.model.ConceptEntity;
import com.efactura.client.model.ClientEntity;
import com.efactura.user.model.UserEntity;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

@Component
public class ItextPdfGenerator {
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	@Autowired
	private FileUtils fileUtils;
	
	@Autowired
	HttpSession session;
	
	public byte[] createPDf(ClientEntity client, BillEntity bill, List<ConceptEntity> concepts) throws JRException, IOException {
		final Resource fileResource = resourceLoader.getResource("classpath:invoice.jasper");
		JasperReport jasReport = (JasperReport) JRLoader.loadObject(fileResource.getFile());
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		// Imagen
		byte[] logo = fileUtils.getLogo();
		InputStream in = new ByteArrayInputStream(logo);
		BufferedImage bImageLogo = ImageIO.read(in);
		params.put("logo", bImageLogo );
		
		// client data
		params.put("clientName", client.getName());
		params.put("clientCif", client.getCif());
		params.put("clientAddress", client.getAddress());
		params.put("clientLocation", client.getLocation());
		params.put("clientPostalCode", client.getPostalCode());
		params.put("clientProvince", client.getProvince());
		params.put("clientCountry", client.getCountry());
		params.put("clientTelephone", client.getTelephone());
		params.put("clientEmail", client.getEmail());
		
		// bill data
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		params.put("billNumber", bill.getYear() + "/" + bill.getNumber());
		params.put("broadcastDate", sdf.format(bill.getBroadCast()));
		params.put("expirationDate", sdf.format(bill.getExpiration()));
		
		// total
		BigDecimal totalConcepts = new BigDecimal(0);
		for (ConceptEntity concept : concepts) {
			totalConcepts = totalConcepts.add(concept.getTaxBase().multiply(concept.getQuantity()));
		}
		params.put("totalBI", NumberFormat.getCurrencyInstance().format(totalConcepts));
		params.put("iva", bill.getTax().toString() + " %");
		BigDecimal totalTax = BigDecimal.valueOf(totalConcepts.doubleValue() * bill.getTax() / 100);
		params.put("totalIva", NumberFormat.getCurrencyInstance().format(totalTax));
		BigDecimal totalPrint = totalConcepts.add(totalTax).setScale(2, BigDecimal.ROUND_HALF_UP);
		params.put("totalValue", NumberFormat.getCurrencyInstance().format(totalPrint));
		
		// user data
		UserEntity userSession = (UserEntity) session.getAttribute("user");
		params.put("userPaymentMethod", userSession.getPaymentMethod());
		params.put("userName", userSession.getName());
		params.put("userCif", userSession.getCif());
		params.put("userAddress", userSession.getAddress());
		params.put("userLocation", userSession.getLocation());
		params.put("userPostalCode", userSession.getPostalCode());
		params.put("userProvince", userSession.getProvince());
		params.put("userCountry", userSession.getCountry());
		params.put("userTelephone", userSession.getTelephone());
		params.put("userEmail", userSession.getEmail());
		
		JRBeanCollectionDataSource con = new JRBeanCollectionDataSource(concepts);
		JasperPrint jasPrint = JasperFillManager.fillReport(jasReport, params, con);
        
		// Export pdf file
		return JasperExportManager.exportReportToPdf(jasPrint);
	}

}
