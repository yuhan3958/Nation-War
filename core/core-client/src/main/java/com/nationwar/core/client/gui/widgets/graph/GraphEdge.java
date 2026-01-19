package com.nationwar.core.client.gui.widgets.graph;

/**
 * Represents an edge connecting two nodes in the GraphView.
 *
 * @param fromNodeId The ID of the starting node.
 * @param toNodeId   The ID of the ending node.
 */
public record GraphEdge(String fromNodeId, String toNodeId) {
}
