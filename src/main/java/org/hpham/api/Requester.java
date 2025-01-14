package org.hpham.api;

import java.util.List;

public interface Requester {
    List<Server.ApiResponse> makeRequest(int numberOfRequests);
}
