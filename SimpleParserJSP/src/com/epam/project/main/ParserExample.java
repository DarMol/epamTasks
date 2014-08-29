package com.epam.project.main;

import org.apache.log4j.LogManager;
import org.apache.log4j.xml.DOMConfigurator;

import com.epam.project.composite.Composite;
import com.epam.project.exception.DataValidException;
import com.epam.project.service.ParserService;

public class ParserExample {
	static {
		new DOMConfigurator().doConfigure("log4j.xml",
				LogManager.getLoggerRepository());
	}

	public static void main(String[] args) {
		try {
			Composite page = new ParserService().doPars("index.jsp");
			page.print();

		} catch (DataValidException e) {
			e.printStackTrace();
		}
	}

}
