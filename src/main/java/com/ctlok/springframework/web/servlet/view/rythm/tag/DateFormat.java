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
	public String getName() {
		return "dateFormat";
	}

	@Override
	protected void call(ParameterList params, Body body) {
		final Date date = (Date) (params.getByName("date") == null ? params.getDefault() : params.getByName("date"));
		final String format = (String) (params.getByName("format") == null ? "dd-MM-yyyy" : params.getByName("format"));
		final SimpleDateFormat dateFormat = this.createDateFormat(format);
		
		this.p(dateFormat.format(date));
	}
	
	protected SimpleDateFormat createDateFormat(final String format){
		return new SimpleDateFormat(format);
	}

}
