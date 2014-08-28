package com.epam.project.parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.epam.project.composite.Component;
import com.epam.project.composite.Composite;
import com.epam.project.composite.TagType;
import com.epam.project.composite.leaf.TagLeaf;
import com.epam.project.composite.leaf.TextLeaf;
import com.epam.project.exception.DataValidException;

public class ReadFileParser implements JSPParser {

	@Override
	public Composite doPars(String path) throws DataValidException {
		List<String> regexs = initRegex();
		BufferedReader in = readFile(path);
		StringBuilder pageInfo = getPageInfo(in);

		Composite page = new Composite();
		for (int i = 0; i < pageInfo.length(); i++) {
			i = addTag(page, regexs, pageInfo, i);
			i = addText(page, pageInfo, i);
		}
		page = createTree(page);
		return page;
	}

	private BufferedReader readFile(String path) throws DataValidException {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(path));
		} catch (FileNotFoundException e) {
			throw new DataValidException();
		}
		return in;
	}

	private StringBuilder getPageInfo(BufferedReader in) {
		StringBuilder pageInfo = new StringBuilder();
		String line;
		try {
			while ((line = in.readLine()) != null) {
				pageInfo.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pageInfo;
	}

	private int addTag(Composite page, List<String> regexs,
			StringBuilder pageInfo, int i) {
		if (pageInfo.charAt(i) == '<') {
			int j = 0;
			for (j = i + 1; j < pageInfo.length(); j++) {
				if (pageInfo.charAt(j) == '>') {
					break;
				}
			}
			String strTag = new String(pageInfo.toString().getBytes(), i, j - i
					+ 1);
			i = j;
			TagLeaf tag = getTag(regexs, strTag);
			page.add(tag);
		}
		return i;
	}	
	
	private int addText(Composite page, StringBuilder pageInfo, int i) {
		if (isTextLeaf(pageInfo, i)) {
			for (int j = i + 1; j < pageInfo.length(); j++) {
				if (pageInfo.charAt(j) == '<') {
					TextLeaf text = new TextLeaf();
					text.setText(getLine(pageInfo.toString(), i + 1, j - i - 1));
					page.add(text);
					i = j - 1;
					break;
				}
			}
		}
		return i;
	}
	
	private static Composite createTree(Composite page) {
		Composite tree = new Composite();
		List<Component> components = page.getComponents();
		for (int i = 0; i < components.size(); i++) {
			if (components.get(i).getClass().getSimpleName()
					.equalsIgnoreCase("TagLeaf")
					&& ((TagLeaf) components.get(i)).getClose() == false
					&& (((TagLeaf) components.get(i)).getType() == TagType.HTML || ((TagLeaf) components
							.get(i)).getType() == TagType.JSTL)) {
				TagLeaf closeTag = new TagLeaf();
				closeTag.setType(TagType.CLOSETAG);
				if (((TagLeaf) components.get(i)).getType() == TagType.HTML) {
					closeTag.setTagName(((TagLeaf) components.get(i))
							.getTagName());
				} else {
					closeTag.setTagName(((TagLeaf) components.get(i))
							.getTagLib()
							+ ":"
							+ ((TagLeaf) components.get(i)).getTagName());
				}
				tree.add(components.get(i));
				int j = 0;
				Composite brunch = new Composite();
				for (j = i + 1; j < components.size(); j++) {
					if (components.get(j).getClass().getSimpleName()
							.equalsIgnoreCase("TagLeaf")
							&& ((TagLeaf) components.get(j)).getType() == TagType.CLOSETAG
							&& ((TagLeaf) components.get(j)).getTagName()
									.equals(closeTag.getTagName())) {
						break;
					} else {
						brunch.add(components.get(j));
					}
				}
				i = j;
				tree.add(createTree(brunch));
				tree.add(closeTag);
			} else {
				tree.add(components.get(i));
			}
		}
		return tree;
	}

	private boolean isTextLeaf(StringBuilder pageInfo, int i) {
		return ((i + 1) != pageInfo.length())
				&& (pageInfo.charAt(i + 1) != '<');
	}

	private TagLeaf getTag(List<String> regexs, String strTag) {
		TagLeaf tag = new TagLeaf();
		for (int RegexIndex = 0; RegexIndex < regexs.size(); RegexIndex++) {
			if (strTag.matches(regexs.get(RegexIndex))) {
				switch (RegexIndex) {
				case 0:
					tag.setType(TagType.ADS);
					tag.setOtherInfo(getLine(strTag, 3, strTag.length() - 5));
					RegexIndex = regexs.size();
					break;
				case 1:
					tag.setType(TagType.EXPRESSION);
					tag.setOtherInfo(getLine(strTag, 3, strTag.length() - 5));
					RegexIndex = regexs.size();
					break;
				case 2:
					tag.setType(TagType.DIRECTIVE);
					tag.setOtherInfo(getLine(strTag, 3, strTag.length() - 5));
					RegexIndex = regexs.size();
					break;
				case 3:
					tag.setType(TagType.SCRIPTLET);
					tag.setOtherInfo(getLine(strTag, 2, strTag.length() - 4));
					RegexIndex = regexs.size();
					break;
				case 4:
					tag.setType(TagType.DOCTYPE);
					tag.setOtherInfo(getLine(strTag, 9, strTag.length() - 10));
					RegexIndex = regexs.size();
					break;
				case 5:
					tag.setType(TagType.COMMENTS);
					tag.setOtherInfo(getLine(strTag, 5, strTag.length() - 8));
					RegexIndex = regexs.size();
					break;
				case 6:
					tag.setType(TagType.CLOSETAG);
					tag.setTagName(getLine(strTag, 2, strTag.length() - 3));
					RegexIndex = regexs.size();
					break;
				case 7:
					createJSTL(strTag, tag);
					RegexIndex = regexs.size();
					break;
				case 8:
					createHTML(strTag, tag);
					RegexIndex = regexs.size();
					break;
				}
			}
		}
		return tag;
	}

	private void createHTML(String strTag, TagLeaf tag) {
		tag.setType(TagType.HTML);
		
		if (isClose(strTag)) {
			tag.setClose(true);
		}
		String tagName = getTagName(strTag, 1).toString();
		tag.setTagName(tagName);

		if (tagName.length() + 3 <= strTag.length()) {
			int finish = strTag.length() - tagName.length() - 2;
			Map<String, String> attribute = initAttribute(strTag,
					tagName.length() + 2, finish);
			tag.setAttribute(attribute);
		}
	}

	private Map<String, String> initAttribute(String strTag, int i, int j) {
		String strAttribute = getLine(strTag, i, j);
		Map<String, String> attribute = new LinkedHashMap<>();
		Pattern pattern = Pattern.compile("([^ ]+)=\"([^\"]+)\"");
		Matcher matcher = pattern.matcher(strAttribute);
		while (matcher.find()) {
			attribute.put(matcher.group(1), matcher.group(2));
		}
		return attribute;
	}

	private StringBuilder getTagName(String strTag, int j) {
		StringBuilder tagName = new StringBuilder();
		for (int i = j; i < strTag.length(); i++) {
			if (strTag.charAt(i) == ' ' || strTag.charAt(i) == '>' || strTag.charAt(i) == '/') {
				break;
			} else {
				tagName.append(strTag.charAt(i));
			}
		}
		return tagName;
	}

	private void createJSTL(String strTag, TagLeaf tag) {
		tag.setType(TagType.JSTL);
		if (isClose(strTag)) {
			tag.setClose(true);
		}
		int nameIndex = 0;
		if (strTag.charAt(1) == 'c') {
			tag.setTagLib("c");
			nameIndex = 3;
		} else {
			tag.setTagLib("fmt");
			nameIndex = 5;
		}
		StringBuilder tagName = getTagName(strTag, nameIndex);
		tag.setTagName(tagName.toString());

		if (tagName.length() + 2 != strTag.length()) {
			int finish = strTag.length() - tagName.length() - 3;
			Map<String, String> attribute = initAttribute(strTag,
					tagName.length() + 2, finish);
			tag.setAttribute(attribute);
		}
	}

	private boolean isClose(String strTag) {
		return strTag.charAt(strTag.length() - 2) == '/';
	}

	private String getLine(String strTag, int i, int j) {
		return new String(strTag.getBytes(), i, j);
	}

	private List<String> initRegex() {
		List<String> regexs = new ArrayList<>();
		regexs.add("<%!.+%>");
		regexs.add("<%=.+%>");
		regexs.add("<%@ .+%>");
		regexs.add("<%.+%>");
		regexs.add("<!DOCTYPE.+>");
		regexs.add("<!-- .+ -->");
		regexs.add("</.+>");
		regexs.add("<c:.+>");
		regexs.add("<.+>");
		return regexs;
	}

}
