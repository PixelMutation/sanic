package me.jellysquid.mods.sodium.client.gl.arena;

public class GlBufferRegion {
    private final GlBufferArena arena;
    private final int start;
    private final int len;

    GlBufferRegion(GlBufferArena arena, int start, int len) {
        this.arena = arena;
        this.start = start;
        this.len = len;
    }

    public int getStart() {
        return this.start;
    }

    public int getLength() {
        return this.len;
    }

    public void delete() {
        this.arena.free(this);
    }
}
