package com.efactura.bill;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.efactura.bill.model.ConceptEntity;
import com.efactura.bill.service.ConceptDataProvider;

@RestController
public class ConceptController {

	@Autowired
	private ConceptDataProvider conceptDataProvider;

	@RequestMapping(path = "/concept/bill/{idBill}", method = RequestMethod.GET)
	public List<ConceptEntity> list(Model model, @PathVariable int idBill) {
		return conceptDataProvider.getList(idBill);
	}

}
