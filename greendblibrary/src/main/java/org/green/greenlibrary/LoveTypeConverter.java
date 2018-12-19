package org.green.greenlibrary;

import org.greenrobot.greendao.converter.PropertyConverter;

public class LoveTypeConverter implements PropertyConverter<LoveType,String> {
    @Override
    public LoveType convertToEntityProperty(String databaseValue) {
        return LoveType.valueOf(databaseValue);
    }

    @Override
    public String convertToDatabaseValue(LoveType entityProperty) {
        return entityProperty.name();
    }
}
