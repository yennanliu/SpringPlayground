package com.yen.clusterAdmin.exception;

import java.util.UUID;

public class NodeNotFoundException extends RuntimeException {

    public NodeNotFoundException(UUID id) {
        super("Node not found with id: " + id);
    }

    public NodeNotFoundException(String instanceId) {
        super("Node not found with instanceId: " + instanceId);
    }
}
