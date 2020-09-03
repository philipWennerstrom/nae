package ai.instance.esoterrace;

import ai.AggressiveNpcAI2;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.knownlist.Visitor;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author xTz
 */
@AIName("kexkraprototype")
public class KexkraPrototypeAI2 extends AggressiveNpcAI2 {

	private AtomicBoolean isStartEvent = new AtomicBoolean(false);

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		checkPercentage(getLifeStats().getHpPercentage());
	}

	private void checkPercentage(int hpPercentage) {
		if (hpPercentage <= 75) {
			if (isStartEvent.compareAndSet(false, true)) {
				getKnownList().doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						if (player.isOnline() && !player.getLifeStats().isAlreadyDead()) {
							PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 472));
						}
					}

				});
				spawn(217206, 1320.639282f, 1171.063354f, 51.494003f, (byte) 0);
				AI2Actions.deleteOwner(this);
			}
		}
	}
}