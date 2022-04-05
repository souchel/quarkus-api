package com.orness.web.requests;


import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

@Data
public class PageRequest {
    @QueryParam("page")
    @DefaultValue("0")
    @PositiveOrZero
    @Parameter(
        description = "0 based",
        required = true
    )
    private int index;

    @QueryParam("size")
    @DefaultValue("3")
    @Positive
    private int size;

}
