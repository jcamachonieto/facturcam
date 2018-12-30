package com.efactura.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.efactura.bill.model.BillEntity;
import com.efactura.bill.model.ConceptEntity;
import com.efactura.client.model.ClientEntity;
import com.efactura.user.model.UserEntity;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Component
public class PdfGenaratorUtil {

	@Autowired
	FileUtils fileUtils;
	
	@Autowired
	HttpSession session;
	
	BigDecimal totalConcepts;

	public byte[] createPDf(ClientEntity client, BillEntity bill, List<ConceptEntity> concepts)
			throws DocumentException {
		byte[] data = null;
		try {
			totalConcepts = new BigDecimal(0);
			
			Document document = new Document(PageSize.A4, 80, 80, 50, 50);
			ByteArrayOutputStream salida = new ByteArrayOutputStream();
			PdfWriter writer = PdfWriter.getInstance(document, salida);
			writer.setInitialLeading(0);

			document.open();

			PdfPTable tableHeader = new PdfPTable(2);
			tableHeader.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			tableHeader.setWidthPercentage(100);
			tableHeader.setWidths(new int[] { 1, 2 });
			tableHeader.addCell(logoTable());
			
			// bill num data
			PdfPTable tableNum = new PdfPTable(1);
			tableNum.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			tableNum.setWidthPercentage(100);
			tableNum.setSpacingBefore(100);
			SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");
			tableNum.addCell(createTextFormCell("Número de factura: ", bill.getYear() + "/" + bill.getNumber(),
					Element.ALIGN_RIGHT, Element.ALIGN_TOP, Rectangle.NO_BORDER));
			tableNum.addCell(createTextFormCell("Fecha de Emisión: ", spf.format(bill.getBroadCast()),
					Element.ALIGN_RIGHT, Element.ALIGN_TOP, Rectangle.NO_BORDER));
			tableNum.addCell(createTextFormCell("Fecha de Vencimiento: ", spf.format(bill.getExpiration()),
					Element.ALIGN_RIGHT, Element.ALIGN_TOP, Rectangle.NO_BORDER));
			
			// client data
			tableNum.addCell(clientTable(client));
			tableHeader.addCell(tableNum);
			
			document.add(tableHeader);
			
			document.add(new Paragraph(" "));
			
			document.add(conceptsTable(concepts));
			
			document.add(new Paragraph(" "));
			
			document.add(conceptsTotal(bill));

			document.close();

			data = salida.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}

	private PdfPTable logoTable() throws DocumentException, IOException {
		PdfPTable tableLogo = new PdfPTable(1);
		tableLogo.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		tableLogo.setWidthPercentage(100);
		tableLogo.setSpacingBefore(100);
		
		Image logo = Image.getInstance(fileUtils.getLogoPath());
		logo.scaleAbsolute(150, 150);
		PdfPCell cell = new PdfPCell(logo, true);
		tableLogo.addCell(cell);
		
		UserEntity userSession = (UserEntity) session.getAttribute("user");
		
		tableLogo.addCell(createTextCell(userSession.getName(),
				Element.ALIGN_JUSTIFIED, Element.ALIGN_TOP, Rectangle.NO_BORDER, FontFactory.getFont("Courier", 8, Font.NORMAL)));
		tableLogo.addCell(createTextCell("NIF/CIF: " + userSession.getCif(),
				Element.ALIGN_JUSTIFIED, Element.ALIGN_TOP, Rectangle.NO_BORDER, FontFactory.getFont("Courier", 8, Font.NORMAL)));
		tableLogo.addCell(createTextCell(userSession.getAddress(),
				Element.ALIGN_JUSTIFIED, Element.ALIGN_TOP, Rectangle.NO_BORDER, FontFactory.getFont("Courier", 8, Font.NORMAL)));
		tableLogo.addCell(createTextCell(userSession.getPostalCode() + " " + userSession.getLocation(),
				Element.ALIGN_JUSTIFIED, Element.ALIGN_TOP, Rectangle.NO_BORDER, FontFactory.getFont("Courier", 8, Font.NORMAL)));
		tableLogo.addCell(createTextCell(userSession.getProvince() + " " + userSession.getCountry(),
				Element.ALIGN_JUSTIFIED, Element.ALIGN_TOP, Rectangle.NO_BORDER, FontFactory.getFont("Courier", 8, Font.NORMAL)));
		
		return tableLogo;
	}
	
	private PdfPTable clientTable(ClientEntity client) throws DocumentException, IOException {
		PdfPTable tableClient = new PdfPTable(2);
		tableClient.setWidthPercentage(100);
		tableClient.setWidths(new int[] { 1, 2 });
		tableClient.addCell(emptyCell());
		
		tableClient.addCell(createTextCell("Cliente",
				Element.ALIGN_RIGHT, Element.ALIGN_TOP, Rectangle.NO_BORDER, FontFactory.getFont(FontFactory.COURIER, 12, Font.BOLD)));
		tableClient.addCell(emptyCell());
		tableClient.addCell(createTextCell(client.getName(),
				Element.ALIGN_RIGHT, Element.ALIGN_TOP, Rectangle.NO_BORDER, FontFactory.getFont(FontFactory.COURIER, 8, Font.NORMAL)));
		tableClient.addCell(emptyCell());
		tableClient.addCell(createTextCell("NIF/CIF:" + client.getCif(),
				Element.ALIGN_RIGHT, Element.ALIGN_TOP, Rectangle.NO_BORDER, FontFactory.getFont(FontFactory.COURIER, 8, Font.NORMAL)));
		tableClient.addCell(emptyCell());
		tableClient.addCell(createTextCell(client.getAddress(),
				Element.ALIGN_RIGHT, Element.ALIGN_TOP, Rectangle.NO_BORDER, FontFactory.getFont(FontFactory.COURIER, 8, Font.NORMAL)));
		tableClient.addCell(emptyCell());
		tableClient.addCell(createTextCell(client.getPostalCode() + " " + client.getLocation(),
				Element.ALIGN_RIGHT, Element.ALIGN_TOP, Rectangle.NO_BORDER, FontFactory.getFont(FontFactory.COURIER, 8, Font.NORMAL)));
		tableClient.addCell(emptyCell());
		tableClient.addCell(createTextCell(client.getProvince() + ", " + client.getCountry(),
				Element.ALIGN_RIGHT, Element.ALIGN_TOP, Rectangle.NO_BORDER, FontFactory.getFont(FontFactory.COURIER, 8, Font.NORMAL)));
		return tableClient;
	}

	private PdfPCell createTextCell(String text, int alignment, int verticalAlignment, int border, Font font)
			throws DocumentException, IOException {
		PdfPCell cell = new PdfPCell();
		Paragraph p = new Paragraph(text, font);
		p.setAlignment(alignment);
		cell.addElement(p);
		cell.setVerticalAlignment(verticalAlignment);
		cell.setBorder(border);
		return cell;
	}
	
	private PdfPCell createTextFormCell(String normalText, String boldtext, int alignment, int verticalAlignment, int border)
			throws DocumentException, IOException {
		PdfPCell cell = new PdfPCell();
		
		Phrase phrase = new Phrase(normalText,  FontFactory.getFont(FontFactory.COURIER, 8, Font.NORMAL));
		phrase.add(new Chunk(boldtext, FontFactory.getFont(FontFactory.COURIER, 8, Font.BOLD)));
		
		Paragraph p = new Paragraph(phrase);
		p.setAlignment(alignment);
		cell.addElement(p);
		cell.setVerticalAlignment(verticalAlignment);
		cell.setBorder(border);
		return cell;
	}
	
	private PdfPCell emptyCell() {
		PdfPCell cell = new PdfPCell(new Phrase(""));
		cell.setBorder(Rectangle.NO_BORDER);
		return cell;
	}
	
	private PdfPTable conceptsTable(List<ConceptEntity> concepts) throws DocumentException, IOException {
		PdfPTable table = new PdfPTable(4);
		table.setWidthPercentage(100);
		table.setWidths(new int[] { 5, 3 , 2, 2});
		table.addCell(createTextCell("Concepto", Element.ALIGN_LEFT, Element.ALIGN_TOP, Rectangle.BOX, FontFactory.getFont(FontFactory.COURIER, 10, Font.BOLDITALIC)));
		table.addCell(createTextCell("Base Imponible", Element.ALIGN_LEFT, Element.ALIGN_TOP, Rectangle.BOX, FontFactory.getFont(FontFactory.COURIER, 10, Font.BOLDITALIC)));
		table.addCell(createTextCell("Cantidad", Element.ALIGN_LEFT, Element.ALIGN_TOP, Rectangle.BOX, FontFactory.getFont(FontFactory.COURIER, 10, Font.BOLDITALIC)));
		table.addCell(createTextCell("Total", Element.ALIGN_LEFT, Element.ALIGN_TOP, Rectangle.BOX, FontFactory.getFont(FontFactory.COURIER, 10, Font.BOLDITALIC)));
		for (ConceptEntity concept : concepts) {
			table.addCell(createTextCell(concept.getDescription(), Element.ALIGN_LEFT, Element.ALIGN_TOP, Rectangle.NO_BORDER, FontFactory.getFont(FontFactory.COURIER, 8, Font.NORMAL)));
			table.addCell(createTextCell(concept.getTaxBase().toString() + "€", Element.ALIGN_LEFT, Element.ALIGN_TOP, Rectangle.NO_BORDER, FontFactory.getFont(FontFactory.COURIER, 8, Font.NORMAL)));
			table.addCell(createTextCell(concept.getQuantity().toString(), Element.ALIGN_LEFT, Element.ALIGN_TOP, Rectangle.NO_BORDER, FontFactory.getFont(FontFactory.COURIER, 8, Font.NORMAL)));
			BigDecimal totalConcept = concept.getTaxBase().multiply(concept.getQuantity());
			totalConcept = totalConcept.setScale(2);
			totalConcepts = totalConcepts.add(totalConcept);
			table.addCell(createTextCell(totalConcept.toString() + "€", Element.ALIGN_LEFT, Element.ALIGN_TOP, Rectangle.NO_BORDER, FontFactory.getFont(FontFactory.COURIER, 8, Font.NORMAL)));
		}
		return table;
	}
	
	private PdfPTable conceptsTotal(BillEntity bill) throws DocumentException, IOException {
		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100);
		table.setWidths(new int[] { 8, 2});
		
		table.addCell(createTextCell("Total Base Imponible:", Element.ALIGN_RIGHT, Element.ALIGN_TOP, Rectangle.NO_BORDER, FontFactory.getFont(FontFactory.COURIER, 10, Font.NORMAL)));
		table.addCell(createTextCell(totalConcepts.toString()  + "€", Element.ALIGN_RIGHT, Element.ALIGN_TOP, Rectangle.NO_BORDER, FontFactory.getFont(FontFactory.COURIER, 10, Font.NORMAL)));
		
		table.addCell(createTextCell("I.V.A:", Element.ALIGN_RIGHT, Element.ALIGN_TOP, Rectangle.NO_BORDER, FontFactory.getFont(FontFactory.COURIER, 10, Font.NORMAL)));
		table.addCell(createTextCell(bill.getTax().toString() + "%", Element.ALIGN_RIGHT, Element.ALIGN_TOP, Rectangle.NO_BORDER, FontFactory.getFont(FontFactory.COURIER, 10, Font.NORMAL)));
		
		table.addCell(createTextCell("Total:", Element.ALIGN_RIGHT, Element.ALIGN_TOP, Rectangle.BOX, FontFactory.getFont(FontFactory.COURIER, 14, Font.BOLDITALIC)));
		
		BigDecimal tax = BigDecimal.valueOf(totalConcepts.doubleValue() * bill.getTax() / 100);
		
		BigDecimal totalPrint = totalConcepts.add(tax).setScale(2, BigDecimal.ROUND_HALF_UP);	
		
		table.addCell(createTextCell(totalPrint.toString(), Element.ALIGN_RIGHT, Element.ALIGN_TOP, Rectangle.BOX, FontFactory.getFont(FontFactory.COURIER, 14, Font.BOLDITALIC)));
		return table;
	}
}
