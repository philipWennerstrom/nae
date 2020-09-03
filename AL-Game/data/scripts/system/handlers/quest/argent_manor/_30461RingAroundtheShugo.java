package quest.argent_manor;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * 
 * @author Ritsu
 */
public class _30461RingAroundtheShugo extends QuestHandler 
{

	private static final int questId = 30461;

	public _30461RingAroundtheShugo()
	{
		super(questId);
	}

	@Override
	public void register() 
	{
		qe.registerQuestNpc(204108).addOnQuestStart(questId);
		qe.registerQuestNpc(204108).addOnTalkEvent(questId);
		qe.registerQuestNpc(799546).addOnTalkEvent(questId);
		qe.registerQuestNpc(799547).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();

		if(qs == null || qs.getStatus() == QuestStatus.NONE)
		{
			if (targetId == 204108) 
			{
				if (dialog == QuestDialog.START_DIALOG) 
					return sendQuestDialog(env, 4762); 
				if (dialog == QuestDialog.ACCEPT_QUEST_SIMPLE) 
				{
					if (giveQuestItem(env, 182213032, 1))
						return sendQuestStartDialog(env);
					else
						return true;
				}
				else 
					return sendQuestStartDialog(env);
			}
		}
		else if (qs.getStatus() == QuestStatus.START)
		{
			int var = qs.getQuestVarById(0);
			switch (targetId)
			{
				case 799547: 
				{
					switch (dialog)
					{
						case START_DIALOG: 
						{
							if (var == 0) 
								return sendQuestDialog(env, 1011);
						}
						case STEP_TO_1:
						{
							removeQuestItem(env, 182213032, 1);
							return defaultCloseDialog(env, 0, 0, true, false);
						}
					}
				}

			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) 
		{
			if (targetId == 799546) 
				switch (dialog)
				{
					case USE_OBJECT: 
					{
						return sendQuestDialog(env, 10002);
					}
					case SELECT_REWARD: 
					{
						return sendQuestDialog(env, 5);
					}
					default:
						return sendQuestEndDialog(env);
				}
		}
		return false;
	}
}