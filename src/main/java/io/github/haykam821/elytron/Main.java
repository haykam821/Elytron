package io.github.haykam821.elytron;

import java.util.ArrayList;
import java.util.List;

import io.github.haykam821.elytron.game.ElytronConfig;
import io.github.haykam821.elytron.game.phase.ElytronWaitingPhase;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import xyz.nucleoid.plasmid.api.game.GameType;

public class Main implements ModInitializer {
	private static final String MOD_ID = "elytron";

	private static final List<Block> TRAIL_BLOCKS = new ArrayList<>();

	private static final Identifier ELYTRON_ID = Main.identifier("elytron");
	public static final GameType<ElytronConfig> ELYTRON_TYPE = GameType.register(ELYTRON_ID, ElytronConfig.CODEC, ElytronWaitingPhase::open);

	@Override
	public void onInitialize() {
		return;
	}

	public static boolean isTrailBlock(Block block) {
		return TRAIL_BLOCKS.contains(block);
	}

	public static Block getTrailBlock(int index) {
		return TRAIL_BLOCKS.get(index % TRAIL_BLOCKS.size());
	}

	public static Identifier identifier(String path) {
		return Identifier.of(MOD_ID, path);
	}

	static {
		TRAIL_BLOCKS.add(Blocks.RED_STAINED_GLASS);
		TRAIL_BLOCKS.add(Blocks.BLUE_STAINED_GLASS);
		TRAIL_BLOCKS.add(Blocks.YELLOW_STAINED_GLASS);
		TRAIL_BLOCKS.add(Blocks.LIME_STAINED_GLASS);
		TRAIL_BLOCKS.add(Blocks.PURPLE_STAINED_GLASS);
		TRAIL_BLOCKS.add(Blocks.ORANGE_STAINED_GLASS);
		TRAIL_BLOCKS.add(Blocks.BLUE_STAINED_GLASS);
		TRAIL_BLOCKS.add(Blocks.PINK_STAINED_GLASS);
		TRAIL_BLOCKS.add(Blocks.MAGENTA_STAINED_GLASS);
	}
}