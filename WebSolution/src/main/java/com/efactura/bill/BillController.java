package com.efactura.bill;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.efactura.bill.model.BillEntity;
import com.efactura.bill.service.BillDataProvider;
import com.efactura.client.ClientController;
import com.efactura.message.model.MessageConstants;
import com.efactura.message.model.MessageDto;

@RestController
public class BillController {

	@Autowired
	private BillDataProvider billDataProvider;
	
	@Autowired
	private ClientController clientController;

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
			return MessageDto.builder().text("Cliente eliminado correctamente").type(MessageConstants.TYPE_SUCCESS)
					.build();
		} catch (Exception e) {
			return MessageDto.builder().text("Se ha producido un error al eliminar la factura")
					.type(MessageConstants.TYPE_ERROR).build();
		}
	}

	@PostMapping("/bill")
	public ModelAndView createBill(@ModelAttribute("bill") BillEntity bill, Model model) {
		int year = Calendar.getInstance().get(Calendar.YEAR);
		bill.setYear(year);
		bill.setNumber(billDataProvider.countByYear(year) + 1);
		billDataProvider.upsert(bill);
		model.addAttribute("message",
				MessageDto.builder().text("Factura creada correctamente").type(MessageConstants.TYPE_SUCCESS).build());
		return homePage(model);
	}
}
