package com.aionemu.gameserver.model.team2.league.events;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RequestResponseHandler;
import com.aionemu.gameserver.model.team2.league.League;
import com.aionemu.gameserver.model.team2.league.LeagueService;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author ATracer
 */
public class LeagueInvite extends RequestResponseHandler {

	private final Player inviter;
	private final Player invited;

	public LeagueInvite(Player inviter, Player invited) {
		super(inviter);
		this.inviter = inviter;
		this.invited = invited;
	}

	@Override
	public void acceptRequest(Creature requester, Player responder) {
		if (LeagueService.canInvite(inviter, invited)) {

			League league = inviter.getPlayerAlliance2().getLeague();

			if (league == null) {
				league = LeagueService.createLeague(inviter, invited);
			}
			else if (league.size() == 8) {
				PacketSendUtility.sendMessage(invited, "That league is already full.");
				PacketSendUtility.sendMessage(inviter, "Your league is already full.");
				return;
			}

			if (!invited.isInLeague()) {
				LeagueService.addAlliance(league, invited.getPlayerAlliance2());
			}
		}
	}

	@Override
	public void denyRequest(Creature requester, Player responder) {
		// TODO correct message
		PacketSendUtility.sendPacket(inviter,
			SM_SYSTEM_MESSAGE.STR_PARTY_ALLIANCE_HE_REJECT_INVITATION(responder.getName()));
	}
}