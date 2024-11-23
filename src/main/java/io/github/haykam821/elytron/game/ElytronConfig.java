package io.github.haykam821.elytron.game;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.github.haykam821.elytron.game.map.ElytronMapConfig;
import net.minecraft.SharedConstants;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.IntProvider;
import xyz.nucleoid.plasmid.api.game.common.config.WaitingLobbyConfig;

public class ElytronConfig {
	public static final MapCodec<ElytronConfig> CODEC = RecordCodecBuilder.mapCodec(instance -> {
		return instance.group(
			ElytronMapConfig.CODEC.fieldOf("map").forGetter(ElytronConfig::getMapConfig),
			WaitingLobbyConfig.CODEC.fieldOf("players").forGetter(ElytronConfig::getPlayerConfig),
			IntProvider.NON_NEGATIVE_CODEC.optionalFieldOf("ticks_until_close", ConstantIntProvider.create(SharedConstants.TICKS_PER_SECOND * 5)).forGetter(ElytronConfig::getTicksUntilClose),
			Codec.INT.optionalFieldOf("height", 2).forGetter(ElytronConfig::getHeight),
			Codec.INT.optionalFieldOf("delay", 15).forGetter(ElytronConfig::getDelay),
			Codec.INT.optionalFieldOf("decay", 200).forGetter(ElytronConfig::getDecay)
		).apply(instance, ElytronConfig::new);
	});

	private final ElytronMapConfig mapConfig;
	private final WaitingLobbyConfig playerConfig;
	private final IntProvider ticksUntilClose;
	private final int height;
	private final int delay;
	private final int decay;

	public ElytronConfig(ElytronMapConfig mapConfig, WaitingLobbyConfig playerConfig, IntProvider ticksUntilClose, int height, int delay, int decay) {
		this.mapConfig = mapConfig;
		this.playerConfig = playerConfig;
		this.ticksUntilClose = ticksUntilClose;
		this.height = height;
		this.delay = delay;
		this.decay = decay;
	}

	public ElytronMapConfig getMapConfig() {
		return this.mapConfig;
	}

	public WaitingLobbyConfig getPlayerConfig() {
		return this.playerConfig;
	}

	public IntProvider getTicksUntilClose() {
		return this.ticksUntilClose;
	}

	public int getHeight() {
		return this.height;
	}

	public int getDelay() {
		return this.delay;
	}

	public int getDecay() {
		return this.decay;
	}
}