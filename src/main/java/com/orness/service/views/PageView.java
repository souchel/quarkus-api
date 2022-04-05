package com.orness.service.views;

import java.util.List;

public record PageView<T>(int count, int pageSize, int pageIndex, List<T> data) {
}