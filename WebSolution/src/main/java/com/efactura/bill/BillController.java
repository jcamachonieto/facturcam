package com.efactura.bill;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.efactura.bill.model.BillEntity;
import com.efactura.bill.model.ConceptEntity;
import com.efactura.bill.service.BillDataProvider;
import com.efactura.bill.service.ConceptDataProvider;
import com.efactura.client.ClientController;
import com.efactura.client.model.ClientEntity;
import com.efactura.client.service.ClientDataProvider;
import com.efactura.message.model.MessageConstants;
import com.efactura.message.model.MessageDto;
import com.efactura.user.model.UserEntity;
import com.efactura.utils.DropBoxBackupUtil;
import com.efactura.utils.ItextPdfGenerator;
import com.efactura.utils.PdfGenaratorUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class BillController {

	@Autowired
	private BillDataProvider billDataProvider;
	
	@Autowired
	private ClientController clientController;
	
	@Autowired
	private ConceptDataProvider conceptDataProvider;
	
	@Autowired
	ClientDataProvider clientDataProvider;
	
	@Autowired
	PdfGenaratorUtil pdfGenaratorUtil;
	
	@Autowired
	ItextPdfGenerator itextPdfGenerator;
	
	@Autowired
    DropBoxBackupUtil dropBoxBackupUtil;
	
	@Autowired
	HttpSession session;

	@GetMapping("/bill")
	public ModelAndView homePage(Model model) {
		model.addAttribute("bill", BillEntity.builder().build());
		model.addAttribute("clients", clientController.list(model));
		return new ModelAndView("bill");
	}

	@RequestMapping(path = "/bill/list", method = RequestMethod.GET)
	public List<BillEntity> list(Model model) {
		return billDataProvider.getList();
	}

	@DeleteMapping(value = "/bill/{id}")
	public MessageDto deleteBill(@PathVariable int id) {
		try {
			billDataProvider.delete(id);
			return MessageDto.builder().text("Factura eliminada correctamente").type(MessageConstants.TYPE_SUCCESS)
					.build();
		} catch (Exception e) {
			return MessageDto.builder().text("Se ha producido un error al eliminar la factura")
					.type(MessageConstants.TYPE_ERROR).build();
		}
	}

	@PostMapping("/bill")
	public MessageDto createBill(@RequestParam Map<String, String> body, Model model) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			mapper.setDateFormat(df);
			
			BillEntity bill = mapper.readValue(body.get("bill"), BillEntity.class);
			List<ConceptEntity> concepts = mapper.readValue(body.get("concepts"), mapper.getTypeFactory().constructCollectionType(List.class, ConceptEntity.class));
			
			int year = Calendar.getInstance().get(Calendar.YEAR);
			bill.setYear(year);
			if (bill.getNumber() == null) {
				bill.setNumber(billDataProvider.countByYear(year) + 1);
			}
			billDataProvider.upsert(bill, concepts);
			
			return MessageDto.builder().text("Factura guardada correctamente").type(MessageConstants.TYPE_SUCCESS).build();
		} catch (IOException e) {
			return MessageDto.builder().text("Error al guardar factura").type(MessageConstants.TYPE_ERROR).build();
		}
	}
	
	@PostMapping("/bill/download/{idBill}")
	public ResponseEntity<byte[]> download(Model model, @PathVariable int idBill) {
		ResponseEntity<byte[]> response = null;
		try {
			BillEntity bill = billDataProvider.load(idBill);
			ClientEntity client = clientDataProvider.load(bill.getIdClient());
			List<ConceptEntity> concepts = conceptDataProvider.getList(idBill);
			
			// byte[] contents = pdfGenaratorUtil.createPDf(client, bill, concepts);
			byte[] contents = itextPdfGenerator.createPDf(client, bill, concepts);
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.parseMediaType("application/pdf"));
			// Here you have to set the actual filename of your pdf
			String filename = bill.getYear() + "/" + bill.getNumber() + ".pdf";
			headers.setContentDispositionFormData(filename, filename);
			headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
			response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
			
			try {
				UserEntity userSession = (UserEntity) session.getAttribute("user");
				dropBoxBackupUtil.backupBill(bill, contents, userSession);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
			response = new ResponseEntity<>(null, null, HttpStatus.BAD_REQUEST);
		}
	    
	    return response;
	}
}
