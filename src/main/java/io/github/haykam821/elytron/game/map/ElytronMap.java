package io.github.haykam821.elytron.game.map;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import xyz.nucleoid.map_templates.BlockBounds;
import xyz.nucleoid.map_templates.MapTemplate;
import xyz.nucleoid.plasmid.api.game.world.generator.TemplateChunkGenerator;

public class ElytronMap {
	private final MapTemplate template;
	private final Box innerBox;
	private final Box innerInnerBox;
	private final Vec3d waitingSpawnPos;

	public ElytronMap(MapTemplate template, BlockBounds bounds) {
		this.template = template;
		this.innerBox = bounds.asBox().expand(-1, -1, -1);
		this.innerInnerBox = this.innerBox.expand(-1, -1, -1);

		Vec3d center = this.innerBox.getCenter();
		this.waitingSpawnPos = new Vec3d(center.getX(), this.innerBox.minY, center.getZ());
	}

	public Box getInnerBox() {
		return this.innerBox;
	}

	public Box getInnerInnerBox() {
		return this.innerInnerBox;
	}

	public Vec3d getWaitingSpawnPos() {
		return this.waitingSpawnPos;
	}

	public ChunkGenerator createGenerator(MinecraftServer server) {
		return new TemplateChunkGenerator(server, this.template);
	}
}