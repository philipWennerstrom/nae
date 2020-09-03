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
public class _30409StoriesofthePast extends QuestHandler 
{

	private static final int questId = 30409;

	public _30409StoriesofthePast()
	{
		super(questId);
	}

	@Override
	public void register() 
	{
		qe.registerQuestNpc(799539).addOnQuestStart(questId);
		qe.registerQuestNpc(799539).addOnTalkEvent(questId);
		qe.registerQuestNpc(798116).addOnTalkEvent(questId);
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
			if (targetId == 799539) 
			{
				if (dialog == QuestDialog.START_DIALOG) 
					return sendQuestDialog(env, 1011); 
				else 
					return sendQuestStartDialog(env);
			}
		}
		else if (qs.getStatus() == QuestStatus.START)
		{
			int var = qs.getQuestVarById(0);
			switch (targetId)
			{
				case 798116: 
				{
					switch (dialog)
					{
						case START_DIALOG: 
						{
							if (var == 0) 
								return sendQuestDialog(env, 1352);
						}
						case STEP_TO_1:
						{
							return defaultCloseDialog(env, 0, 1);
						}
					}
				}

				case 799539:
					switch (dialog)
					{
						case START_DIALOG: 
						{
							if (var == 1) 
								return sendQuestDialog(env, 2375);

						}
						case SELECT_REWARD: 
						{
							changeQuestStep(env, 1, 1, true);	
							return sendQuestDialog(env, 5);
						}
					}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) 
		{
			if (targetId == 799539) 
				return sendQuestEndDialog(env);
		}
		return false;
	}
}