package me.jellysquid.mods.sodium.client.render.chunk.multidraw;

import java.util.Optional;

import me.jellysquid.mods.sodium.client.gl.shader.GlShader;
import me.jellysquid.mods.sodium.client.gl.shader.ShaderConstants;
import me.jellysquid.mods.sodium.client.gl.shader.ShaderLoader;
import me.jellysquid.mods.sodium.client.gl.shader.ShaderType;
import me.jellysquid.mods.sodium.client.model.vertex.type.ChunkVertexType;
import me.jellysquid.mods.sodium.client.render.chunk.ChunkGraphicsState;
import me.jellysquid.mods.sodium.client.render.chunk.passes.BlockRenderPass;
import me.jellysquid.mods.sodium.client.render.chunk.shader.ChunkFogMode;
import me.jellysquid.mods.sodium.client.render.chunk.shader.ChunkRenderShaderBackend;
import net.coderbot.iris.pipeline.SodiumTerrainPipeline;

import net.minecraft.util.Identifier;

public abstract class ChunkRenderBackendMultiDraw<T extends ChunkGraphicsState> extends ChunkRenderShaderBackend<T, ChunkProgramMultiDraw> {
    private final SodiumTerrainPipeline pipeline = SodiumTerrainPipeline.create();

    public ChunkRenderBackendMultiDraw(ChunkVertexType format) {
        super(format);
    }

    @Override
    protected ChunkProgramMultiDraw createShaderProgram(Identifier name, int handle, ChunkFogMode fogMode, BlockRenderPass pass) {
        return new ChunkProgramMultiDraw(name, handle, fogMode.getFactory(), pipeline.initUniforms(handle));
    }

    @Override
    protected GlShader createVertexShader(ChunkFogMode fogMode, BlockRenderPass pass) {
        Optional<String> irisVertexShader = pass == BlockRenderPass.TRANSLUCENT ? pipeline.getTranslucentVertexShaderSource() : pipeline.getTerrainVertexShaderSource();

        if (irisVertexShader.isPresent()) {
            return new GlShader(ShaderType.VERTEX, new Identifier("iris", "sodium-terrain.vsh"), irisVertexShader.get(), this.createShaderConstants(fogMode));
        }

        return ShaderLoader.loadShader(ShaderType.VERTEX, new Identifier("sodium", "chunk_gl20.v.glsl"),
            this.createShaderConstants(fogMode));
    }

    @Override
    protected GlShader createFragmentShader(ChunkFogMode fogMode, BlockRenderPass pass) {
        Optional<String> irisFragmentShader = pass == BlockRenderPass.TRANSLUCENT ? pipeline.getTranslucentFragmentShaderSource() : pipeline.getTerrainFragmentShaderSource();

        if (irisFragmentShader.isPresent()) {
            return new GlShader(ShaderType.FRAGMENT, new Identifier("iris", "sodium-terrain.fsh"), irisFragmentShader.get(), this.createShaderConstants(fogMode));
        }

        return ShaderLoader.loadShader(ShaderType.FRAGMENT, new Identifier("sodium", "chunk_gl20.f.glsl"),
            this.createShaderConstants(fogMode));
    }

    private ShaderConstants createShaderConstants(ChunkFogMode fogMode) {
        ShaderConstants.Builder constants = ShaderConstants.builder();
        constants.define("USE_MULTIDRAW");

        fogMode.addConstants(constants);

        return constants.build();
    }
}
