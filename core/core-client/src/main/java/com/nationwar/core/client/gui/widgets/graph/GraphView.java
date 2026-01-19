package com.nationwar.core.client.gui.widgets.graph;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * A generic, reusable widget for displaying node-based graphs.
 * This is a placeholder implementation. Full implementation will include
 * node/edge rendering, panning, zooming, and interaction.
 */
public class GraphView extends AbstractWidget {

    private final List<GraphNode> nodes = new ArrayList<>();
    private final List<GraphEdge> edges = new ArrayList<>();

    public GraphView(int x, int y, int width, int height) {
        super(x, y, width, height, Component.literal("Graph View"));
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // Fill background
        guiGraphics.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), 0xFF202020);

        // Render placeholder text
        Font font = net.minecraft.client.Minecraft.getInstance().font;
        guiGraphics.drawCenteredString(font, "GraphView (WIP)", getX() + getWidth() / 2, getY() + getHeight() / 2 - 4, 0xFFFFFFFF);

        // In the future, we will render nodes and edges here.
    }

    public void setGraphData(List<GraphNode> nodes, List<GraphEdge> edges) {
        this.nodes.clear();
        this.edges.clear();
        this.nodes.addAll(nodes);
        this.edges.addAll(edges);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        // Accessibility feature, can be implemented later
    }
}
