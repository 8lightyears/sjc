package com.eightlightyears.sjc.service.data_access;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.StringJoiner;

@AllArgsConstructor
@Getter
public class DataGroupDesc implements Comparable<DataGroupDesc> {
    private final List<Comparable> fields;
    public String getName() {
        String result = "";
        if (fields != null) {
            StringJoiner joiner = new StringJoiner("");
            for (Object field : fields) {
                joiner.add(field.toString());
            }
            result = joiner.toString();
        }
        return result;
    }

    @Override
    public int compareTo(DataGroupDesc o) {
        for (int i = 0; i < getFields().size(); i++) {
            int compareResult = getFields().get(i).compareTo(o.getFields().get(i));
            if (compareResult != 0)
                return compareResult;
        }
        return 0;
    }
}
