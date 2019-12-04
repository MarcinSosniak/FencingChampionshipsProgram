package model.command;

import model.Fight;
import model.Participant;
import model.enums.FightScore;

public class CommandAddBattleResult implements Command {

    private static final ValidInvocationChecker validInvocationChecker = ValidInvocationChecker.getChecker();

    private FightScore scoreToSet;
    private final Fight fight;
    private FightScore oldScore= FightScore.NULL_STATE;

    public CommandAddBattleResult(Fight fight, Participant winner)
    {
        this.fight=fight;
        scoreToSet=fight.getScoreWithWinner(validInvocationChecker, winner) ;
    }

    public CommandAddBattleResult(Fight fight, FightScore score)
    {
        this.fight=fight;
        scoreToSet=score;
    }

    /**creates fight command add battle result using loser particpent instead of winner**/
    public CommandAddBattleResult(Fight fight, Participant looser, boolean DUMMY_ARGUMENT_USE_LOOSER_NOT_WINNER)
    {
        this.fight=fight;
        scoreToSet=fight.getScoreWithLoser(validInvocationChecker, looser);
    }


    @Override
    public void execute() {
        oldScore=fight.getScore();
        if(oldScore!= FightScore.NULL_STATE) {
            fight.updateScore(validInvocationChecker,true); // reverse previous change
        }
        fight.setFightScore(validInvocationChecker, scoreToSet);

        if(fight.getScore()!= FightScore.NULL_STATE)
            fight.updateScore(validInvocationChecker,false);
    }

    @Override
    public void undo() {
        if(fight.getScore()!=scoreToSet)
            throw new IllegalStateException("This error means that there is a bug. Something was changed outside " +
                    "of command stack and/or element is missing from stack");
        if(fight.getScore() != FightScore.NULL_STATE)
            fight.updateScore(validInvocationChecker,true); // if we changed
        fight.setFightScore(validInvocationChecker, oldScore);
        if(fight.getScore() != FightScore.NULL_STATE)
            fight.updateScore(validInvocationChecker,false);
    }

    @Override
    public void redo() {
        execute();
    }
}
