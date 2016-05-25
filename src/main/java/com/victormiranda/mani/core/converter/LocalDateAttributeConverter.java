package com.victormiranda.mani.core.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Date;
import java.time.LocalDate;

@Converter(autoApply = true)
public class LocalDateAttributeConverter implements AttributeConverter<LocalDate, Date> {

	@Override
	public Date convertToDatabaseColumn(final LocalDate locDate) {
		return locDate == null ? null : java.sql.Date.valueOf(locDate);
	}

	@Override
	public LocalDate convertToEntityAttribute(final Date sqlDate) {
		return sqlDate == null ? null : sqlDate.toLocalDate();
	}
}