package com.orness.web.responses;

import java.util.List;

public record PageResponse<T>(int count, int pageSize, int pageIndex, List<T> data) {
}
