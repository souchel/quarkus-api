package com.orness.data;

import java.util.List;

public record Page<T>(long count, int pageSize, int pageIndex, List<T> data) {
}