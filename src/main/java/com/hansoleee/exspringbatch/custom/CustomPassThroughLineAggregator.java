package com.hansoleee.exspringbatch.custom;

import com.hansoleee.exspringbatch.dto.OneDTO;
import org.springframework.batch.item.file.transform.LineAggregator;

public class CustomPassThroughLineAggregator<T> implements LineAggregator<T> {

    @Override
    public String aggregate(T item) {
        if (item instanceof OneDTO) {
            return "OneDTO " + item;
        }

        return item.toString();
    }
}
