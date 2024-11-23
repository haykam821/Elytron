package io.github.haykam821.elytron.game.phase;

import java.util.Set;

import io.github.haykam821.elytron.game.ElytronConfig;
import io.github.haykam821.elytron.game.map.ElytronMap;
import io.github.haykam821.elytron.game.map.ElytronMapBuilder;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import xyz.nucleoid.fantasy.RuntimeWorldConfig;
import xyz.nucleoid.plasmid.api.game.GameOpenContext;
import xyz.nucleoid.plasmid.api.game.GameOpenProcedure;
import xyz.nucleoid.plasmid.api.game.GameResult;
import xyz.nucleoid.plasmid.api.game.GameSpace;
import xyz.nucleoid.plasmid.api.game.common.GameWaitingLobby;
import xyz.nucleoid.plasmid.api.game.event.GameActivityEvents;
import xyz.nucleoid.plasmid.api.game.event.GamePlayerEvents;
import xyz.nucleoid.plasmid.api.game.player.JoinAcceptor;
import xyz.nucleoid.plasmid.api.game.player.JoinAcceptorResult;
import xyz.nucleoid.plasmid.api.game.player.JoinOffer;
import xyz.nucleoid.stimuli.event.EventResult;
import xyz.nucleoid.stimuli.event.player.PlayerDeathEvent;

public class ElytronWaitingPhase {
	private final GameSpace gameSpace;
	private final ServerWorld world;
	private final ElytronMap map;
	private final ElytronConfig config;

	public ElytronWaitingPhase(GameSpace gameSpace, ServerWorld world, ElytronMap map, ElytronConfig config) {
		this.gameSpace = gameSpace;
		this.world = world;
		this.map = map;
		this.config = config;
	}

	public static GameOpenProcedure open(GameOpenContext<ElytronConfig> context) {
		ElytronMapBuilder mapBuilder = new ElytronMapBuilder(context.config());
		ElytronMap map = mapBuilder.create(context.server().getOverworld().getRandom());

		RuntimeWorldConfig worldConfig = new RuntimeWorldConfig()
			.setGenerator(map.createGenerator(context.server()));

		return context.openWithWorld(worldConfig, (activity, world) -> {
			ElytronWaitingPhase phase = new ElytronWaitingPhase(activity.getGameSpace(), world, map, context.config());

			GameWaitingLobby.addTo(activity, context.config().getPlayerConfig());
			ElytronActivePhase.setRules(activity);

			// Listeners
			activity.listen(GamePlayerEvents.ACCEPT, phase::onAcceptPlayers);
			activity.listen(GamePlayerEvents.OFFER, JoinOffer::accept);
			activity.listen(PlayerDeathEvent.EVENT, phase::onPlayerDeath);
			activity.listen(GameActivityEvents.REQUEST_START, phase::requestStart);
		});
	}

	private GameResult requestStart() {
		ElytronActivePhase.open(this.gameSpace, this.world, this.map, this.config);
		return GameResult.ok();
	}

	private JoinAcceptorResult onAcceptPlayers(JoinAcceptor acceptor) {
		return acceptor.teleport(this.world, this.map.getWaitingSpawnPos()).thenRunForEach(player -> {
			player.changeGameMode(GameMode.ADVENTURE);
		});
	}

	private EventResult onPlayerDeath(ServerPlayerEntity player, DamageSource source) {
		// Respawn player at the start
		this.spawnPlayer(player);
		return EventResult.ALLOW;
	}

	private void spawnPlayer(ServerPlayerEntity player) {
		Vec3d spawnPos = this.map.getWaitingSpawnPos();
		player.teleport(this.world, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), Set.of(), 0, 0, true);
	}
}
