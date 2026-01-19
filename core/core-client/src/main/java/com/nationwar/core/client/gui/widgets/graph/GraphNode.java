package com.nationwar.core.client.gui.widgets.graph;

/**
 * Represents a node in the GraphView.
 * This is a simple data record.
 *
 * @param id The unique ID of the node.
 * @param x  The fixed x-coordinate of the node.
 * @param y  The fixed y-coordinate of the node.
 */
public record GraphNode(String id, int x, int y) {
}
