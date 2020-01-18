package wanderingMiniBosses.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import wanderingMiniBosses.util.WanderingBossHelper;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractWanderingBoss extends CustomMonster {
    public static final int RUNTIMER = 3;
    public static final Byte RUN = Byte.MIN_VALUE;

    protected Map<Byte, EnemyMoveInfo> moves;
    protected int runTimer;

    public AbstractWanderingBoss(String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl, float offsetX, float offsetY) {
        super(name, id, maxHealth, hb_x, hb_y, hb_w, hb_h, imgUrl, offsetX, offsetY);
        setupMisc();
    }

    public AbstractWanderingBoss(String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl, float offsetX, float offsetY, boolean ignoreBlights) {
        super(name, id, maxHealth, hb_x, hb_y, hb_w, hb_h, imgUrl, offsetX, offsetY, ignoreBlights);
        setupMisc();
    }

    public AbstractWanderingBoss(String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl) {
        super(name, id, maxHealth, hb_x, hb_y, hb_w, hb_h, imgUrl);
        setupMisc();
    }
    private void setupMisc() {
        this.type = EnemyType.BOSS;
        this.moves = new HashMap<>();
        this.runTimer = RUNTIMER;

        this.moves.put(RUN, new EnemyMoveInfo(RUN, Intent.ESCAPE, -1, 0, false));
    }

    @Override
    public void takeTurn() {
        if(this.nextMove == RUN) {
            onEscape();
            AbstractDungeon.actionManager.addToBottom(new EscapeAction(this));
        } else {
            takeCustomTurn();
        }
        runTimer--;
    }
    public abstract void takeCustomTurn();

    @Override
    public void rollMove() {
        if(this.runTimer-- <= 0) {
            this.setMoveShortcut(RUN);
        } else {
            super.rollMove();
        }
    }

    public void onEscape() {

    }

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);
        WanderingBossHelper.getMonster().currentHealth = this.currentHealth;
    }

    public void setMoveShortcut(byte next, int moveIndex) {
        EnemyMoveInfo info = this.moves.get(next);
        this.setMove(MOVES[moveIndex], next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
    }
    public void setMoveShortcut(byte next) {
        EnemyMoveInfo info = this.moves.get(next);
        this.setMove(next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
    }
}
