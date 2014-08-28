package com.epam.project.service;

import com.epam.project.composite.Composite;
import com.epam.project.exception.DataValidException;
import com.epam.project.parser.JSPParser;
import com.epam.project.parser.ReadFileParser;

public class ParserService {
	private JSPParser parser;
	
	public ParserService() {
		parser = new ReadFileParser();
	}
	public Composite doPars(String path) throws DataValidException {
		return parser.doPars(path);
	}
}
