package hu.fourdsoft.memorygame.common.jaxb.bindings;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DateDataTypeAdapter extends XmlAdapter<String, Date> {

	private static final SimpleDateFormat DF = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public String marshal(Date value) throws Exception {
		String retVal = null;
		try {
			retVal = DF.format(value);

		} catch (Exception e) {
			throw new RuntimeException("Nem megfelelő dátum: " + value);
		}
		return retVal;
	}

	@Override
	public Date unmarshal(String value) throws Exception {
		Date retVal = null;
		try {
			retVal = DF.parse(value);

		} catch (Exception e) {
			throw new RuntimeException("Nem megfelelő dátum string: " + value);
		}
		return retVal;
	}
}
