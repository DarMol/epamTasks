package com.epam.project.composite;

import com.epam.project.exception.LogicException;
import com.epam.project.exception.DataValidException;

public interface Component {
	void print();
	boolean add(Component component) throws LogicException;
	boolean remove(Component component) throws LogicException;
	Component getChild (int index) throws DataValidException;
	int getCount();
}
