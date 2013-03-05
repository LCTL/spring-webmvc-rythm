package com.ctlok.springframework.web.servlet.view.rythm.tag;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.greenlaw110.rythm.template.JavaTagBase;

/**
 * @author Lawrence Cheung
 *
 */
public class DateFormat extends JavaTagBase {
	
	@Override
	public String __getName() {
		return "dateFormat";
	}

	@Override
	protected void call(__ParameterList params, __Body body) {
		final Date date = (Date) (params.getByName("date") == null ? params.getDefault() : params.getByName("date"));
		final String format = (String) (params.getByName("format") == null ? "dd-MM-yyyy" : params.getByName("format"));

		if (date != null){
		    this.p(this.createDateFormat(format).format(date));
		}
	}
	
	protected SimpleDateFormat createDateFormat(final String format){
		return new SimpleDateFormat(format);
	}

}
