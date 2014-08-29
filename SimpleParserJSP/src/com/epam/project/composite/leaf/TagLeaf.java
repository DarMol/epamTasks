package com.epam.project.composite.leaf;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import com.epam.project.composite.Component;
import com.epam.project.composite.TagType;
import com.epam.project.exception.LogicException;
import com.epam.project.exception.DataValidException;
import com.epam.project.main.ParserExample;

public class TagLeaf implements Component {

	private static Logger logger = Logger.getLogger(ParserExample.class);
	private TagType type;
	private boolean closed;
	private String tagLib;
	private String tagName;
	private String otherInfo;
	private Map<String, String> attribute;

	public TagLeaf() {
		closed = false;
		attribute = new LinkedHashMap<>();
	}

	@Override
	public String toString() {
		StringBuilder tagOut = new StringBuilder();
		switch (type) {
		case HTML:
			createGoodTag(tagOut);
			break;
		case JSTL:
			createGoodTag(tagOut);
			break;
		case COMMENTS:
			createBadPracticeTag(tagOut);
			break;
		case DOCTYPE:
			createBadPracticeTag(tagOut);
			break;
		case DIRECTIVE:
			createBadPracticeTag(tagOut);
			break;
		case SCRIPTLET:
			createBadPracticeTag(tagOut);
			break;
		case ADS:
			createBadPracticeTag(tagOut);
			break;
		case EXPRESSION:
			createBadPracticeTag(tagOut);
			break;
		case CLOSETAG:
			createBadPracticeTag(tagOut);
			break;
		}
		return tagOut.toString();
	}

	private void createGoodTag(StringBuilder tagOut) {
		tagOut.append("<");
		if (tagLib != null) {
			tagOut.append(tagLib + ":");
		}
		tagOut.append(tagName);
		if (attribute != null) {
			for (Map.Entry<String, String> element : attribute.entrySet()) {
				tagOut.append(" " + element.getKey());
				tagOut.append("=\"" + element.getValue() + "\"");
			}
		}
		if (closed) {
			tagOut.append(" /");
		}
		tagOut.append(">");
	}

	private void createBadPracticeTag(StringBuilder tagOut) {
		tagOut.append(getOpenTag());
		if (type.equals(TagType.CLOSETAG)) {
			tagOut.append(tagName);
		} else {
			tagOut.append(otherInfo);
		}
		tagOut.append(getCloseTag());
	}

	private String getCloseTag() {
		switch (type) {
		case COMMENTS:
			return " -->";
		case DOCTYPE:
			return ">";
		case CLOSETAG:
			return ">";
		default:
			return "%>";
		}
	}

	private String getOpenTag() {
		switch (type) {
		case COMMENTS:
			return "<!-- ";
		case DOCTYPE:
			return "<!DOCTYPE ";
		case DIRECTIVE:
			return "<%@ ";
		case SCRIPTLET:
			return "<% ";
		case ADS:
			return "<%! ";
		case CLOSETAG:
			return "</";
		default:
			return "<%= ";
		}
	}

	@Override
	public void print() {
		logger.info(this.toString());
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

	public TagType getType() {
		return type;
	}

	public void setType(TagType type) {
		this.type = type;
	}

	public String getTagLib() {
		return tagLib;
	}

	public void setTagLib(String tagLib) {
		this.tagLib = tagLib;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getOtherInfo() {
		return otherInfo;
	}

	public void setOtherInfo(String otherInfo) {
		this.otherInfo = otherInfo;
	}

	public Map<String, String> getAttribute() {
		return attribute;
	}

	public void setAttribute(Map<String, String> attribute) {
		this.attribute = attribute;
	}

	public boolean getClose() {
		return closed;
	}

	public void setClose(boolean close) {
		this.closed = close;
	}

	@Override
	public int getCount() {
		return 1;
	}

}
