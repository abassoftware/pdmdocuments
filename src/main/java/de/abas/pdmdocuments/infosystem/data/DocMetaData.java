package de.abas.pdmdocuments.infosystem.data;

import java.math.BigDecimal;
import java.util.Date;

public class DocMetaData {

	private String name;
	private String value;
	private MetaDataTyp typ;

	public DocMetaData(String name, Object value) {

		super();
		this.name = name;

		if (value instanceof Integer) {
			Integer temp = (Integer) value;
			this.value = temp.toString();
			this.typ = MetaDataTyp.INTEGER;
		} else if (value instanceof String) {
			this.value = (String) value;
			this.typ = MetaDataTyp.STRING;
		} else if (value instanceof Date) {
			Date temp = (Date) value;
			this.value = temp.toString();
			this.typ = MetaDataTyp.DATE;
		} else if (value instanceof BigDecimal) {
			BigDecimal temp = (BigDecimal) value;
			this.value = temp.toString();
			this.typ = MetaDataTyp.BIGDEZIMAL;
		} else if (value instanceof String[]) {
			String[] temp = (String[]) value;

			StringBuilder bld = new StringBuilder();
			for (String string : temp) {
				bld.append(string);

			}
			this.value = bld.toString();
			this.typ = MetaDataTyp.STRINGLIST;
		}

	}

	public DocMetaData(String name, Integer value) {

		super();
		this.name = name;
		this.value = value.toString();
		this.typ = MetaDataTyp.INTEGER;

	}

	public DocMetaData(String name, Date value) {

		super();
		this.name = name;
		this.value = value.toString();
		this.typ = MetaDataTyp.DATE;

	}

	public DocMetaData(String name, BigDecimal value) {

		super();
		this.name = name;
		this.value = value.toString();
		this.typ = MetaDataTyp.BIGDEZIMAL;

	}

	public DocMetaData(String name, String value) {
		super();
		this.name = name;
		this.value = value;
		this.typ = MetaDataTyp.STRING;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public MetaDataTyp getType() {
		return typ;
	}

}
