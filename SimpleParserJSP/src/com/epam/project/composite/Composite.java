package com.epam.project.composite;

import java.util.ArrayList;
import java.util.List;

public class Composite implements Component {
	private List<Component> components = new ArrayList<Component>();

	@Override
	public void read() {
		for (int i = 0; i < components.size(); i++) {
			components.get(i).read();
		}
	}

	@Override
	public boolean add(Component component) {
		return components.add(component)?true:false;

	}

	@Override
	public boolean remove(Component component) {
		return components.remove(component)?true:false;

	}

	@Override
	public Component getChild(int index) {
		return components.get(index);
	}

	public List<Component> getComponents() {
		return components;
	}

	@Override
	public int getCount() {
		int count = 0;
		for (int i = 0; i < components.size(); i++) {
			count += components.get(i).getCount();
		}
		return count;
	}

}
