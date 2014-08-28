package com.epam.project.parser;

import com.epam.project.composite.Composite;
import com.epam.project.exception.DataValidException;

public interface JSPParser {
	public Composite doPars(String path) throws DataValidException;
}
