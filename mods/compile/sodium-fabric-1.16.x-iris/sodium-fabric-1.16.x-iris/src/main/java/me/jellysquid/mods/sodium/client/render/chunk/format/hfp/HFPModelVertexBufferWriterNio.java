package me.jellysquid.mods.sodium.client.render.chunk.format.hfp;

import me.jellysquid.mods.sodium.client.model.vertex.buffer.VertexBufferView;
import me.jellysquid.mods.sodium.client.model.vertex.buffer.VertexBufferWriterNio;
import me.jellysquid.mods.sodium.client.render.chunk.format.DefaultModelVertexFormats;
import me.jellysquid.mods.sodium.client.render.chunk.format.ModelVertexSink;
import me.jellysquid.mods.sodium.client.render.chunk.format.ModelVertexUtil;

import java.nio.ByteBuffer;

public class HFPModelVertexBufferWriterNio extends VertexBufferWriterNio implements ModelVertexSink {
    public HFPModelVertexBufferWriterNio(VertexBufferView backingBuffer) {
        super(backingBuffer, DefaultModelVertexFormats.MODEL_VERTEX_HFP);
    }

    @Override
    public void writeQuad(float x, float y, float z, int color, float u, float v, int light) {
        this.writeQuadInternal(
                ModelVertexUtil.denormalizeFloatAsShort(x),
                ModelVertexUtil.denormalizeFloatAsShort(y),
                ModelVertexUtil.denormalizeFloatAsShort(z),
                color,
                ModelVertexUtil.denormalizeFloatAsShort(u),
                ModelVertexUtil.denormalizeFloatAsShort(v),
                ModelVertexUtil.encodeLightMapTexCoord(light)
        );
    }

    @Override
    public void writeQuad(float x, float y, float z, int color, float u, float v, int light, short blockId) {
        writeQuad(x, y, z, color, u, v, light);
    }

    private void writeQuadInternal(short x, short y, short z, int color, short u, short v, int light) {
        int i = this.writeOffset;

        ByteBuffer buffer = this.byteBuffer;
        buffer.putShort(i, x);
        buffer.putShort(i + 2, y);
        buffer.putShort(i + 4, z);
        buffer.putInt(i + 8, color);
        buffer.putShort(i + 12, u);
        buffer.putShort(i + 14, v);
        buffer.putInt(i + 16, light);

        this.advance();
    }
}
