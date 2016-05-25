package com.victormiranda.mani.core.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Converter(autoApply = true)
public class LocalTimeAttributeConverter implements AttributeConverter<LocalDateTime, Date> {

	@Override
	public Date convertToDatabaseColumn(final LocalDateTime locDate) {
		return locDate == null ? null : Date.valueOf(locDate.toLocalDate());
	}

	@Override
	public LocalDateTime convertToEntityAttribute(final Date sqlDate) {
		return sqlDate == null ? null : Instant.ofEpochMilli(sqlDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
	}
}