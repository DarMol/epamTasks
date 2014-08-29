package com.epam.project.composite.leaf;

import org.apache.log4j.Logger;
import com.epam.project.composite.Component;
import com.epam.project.exception.LogicException;
import com.epam.project.exception.DataValidException;
import com.epam.project.main.ParserExample;

public class TextLeaf implements Component {
	private static Logger logger = Logger.getLogger(ParserExample.class);
	private String text;

	@Override
	public void print() {
		logger.info(text);
	}

	@Override
	public boolean add(Component c) throws LogicException {
		throw new LogicException();
	}

	@Override
	public boolean remove(Component c) throws LogicException {
		throw new LogicException();
	}

	@Override
	public Component getChild(int index) throws DataValidException {
		throw new DataValidException();
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public int getCount() {
		return 1;
	}

}
