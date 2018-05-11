package cn.financial.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.financial.service.DataModuleService;

@Controller
@RequestMapping("/dataModule")
public class DataModuleController {
	
	protected Logger logger = LoggerFactory.getLogger(DataModuleController.class);

	@Autowired
	private DataModuleService dataModuleService;
	
	
}
