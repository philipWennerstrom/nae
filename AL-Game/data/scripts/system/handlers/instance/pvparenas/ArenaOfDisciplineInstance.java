package instance.pvparenas;

import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.instance.playerreward.InstancePlayerReward;
import com.aionemu.gameserver.model.instance.playerreward.PvPArenaPlayerReward;

/**
 *
 * @author xTz
 */
@InstanceID(300360000)
public class ArenaOfDisciplineInstance extends DisciplineTrainingGroundsInstance {

	@Override
	protected void reward() {
		int totalPoints = instanceReward.getTotalPoints();
		int size = instanceReward.getInstanceRewards().size();
		// 100 * (rate * size) * (playerScore / playersScore)
		float totalAP = (3.292f * size) * 100; // to do config
		float totalCrucible = (1.964f * size) * 100; // to do config
		float totalCourage = (0.174f * size) * 100; // to do config
		for (InstancePlayerReward playerReward : instanceReward.getInstanceRewards()) {
			PvPArenaPlayerReward reward = (PvPArenaPlayerReward) playerReward;
			if (!reward.isRewarded()) {
				float playerRate = 1;
				Player player = instance.getPlayer(playerReward.getOwner());
				if (player != null) {
					playerRate = player.getRates().getDisciplineRewardRate();
				}
				int score = reward.getScorePoints();
				float scoreRate = ((float) score / (float) totalPoints);
				int rank = instanceReward.getRank(score);
				float percent = reward.getParticipation();
				int basicAP = 200;
				// to do other formula
				int rankingAP = 431;
				if (size > 1) {
					rankingAP = rank == 0 ? 1108 : 431;
				}
				int scoreAP = (int) (totalAP * scoreRate);
				basicAP *= percent;
				rankingAP *= percent;
				rankingAP *= playerRate;
				reward.setBasicAP(basicAP);
				reward.setRankingAP(rankingAP);
				reward.setScoreAP(scoreAP);
				int basicCrI = 195;
				basicCrI *= percent;
				// to do other formula
				int rankingCrI = 256;
				if (size > 1) {
					rankingCrI = rank == 0 ? 660 : 256;
				}
				rankingCrI *= percent;
				rankingCrI *= playerRate;
				int scoreCrI = (int) (totalCrucible * scoreRate);
				reward.setBasicCrucible(basicCrI);
				reward.setRankingCrucible(rankingCrI);
				reward.setScoreCrucible(scoreCrI);
				int basicCoI = 0;
				basicCoI *= percent;
				// to do other formula
				int rankingCoI = 23;
				if (size > 1) {
					rankingCoI = rank == 0 ? 59 : 23;
				}
				rankingCoI *= percent;
				rankingCoI *= playerRate;
				int scoreCoI = (int) (totalCourage * scoreRate);
				reward.setBasicCourage(basicCoI);
				reward.setRankingCourage(rankingCoI);
				reward.setScoreCourage(scoreCoI);
				if (instanceReward.canRewardOpportunityToken(reward)) {
					reward.setOpportunity(4);
				}
			}
		}
		super.reward();
	}
}