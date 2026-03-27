package com.bmstu_bureau_1440.accounting;

import com.bmstu_bureau_1440.accounting.models.Operation;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Storage {

    @Getter
    @Setter
    private List<Operation> operations;

    public Storage(@NonNull @JsonProperty("operations") List<Operation> operations) {
        this.operations = operations.isEmpty() ? new ArrayList<>() : operations;
    }

}
