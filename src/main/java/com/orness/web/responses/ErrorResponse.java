package com.orness.web.responses;

import java.util.UUID;

public record ErrorResponse(String description, UUID stacktraceId, String title) {
}
