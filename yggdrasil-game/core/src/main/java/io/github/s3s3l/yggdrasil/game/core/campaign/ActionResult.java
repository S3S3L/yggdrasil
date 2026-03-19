package io.github.s3s3l.yggdrasil.game.core.campaign;

/**
 * 行动结果
 */
public class ActionResult {

    /**
     * 是否成功
     */
    private final boolean success;

    /**
     * 结果描述
     */
    private final String message;

    /**
     * 资源消耗
     */
    private final int resourceCost;

    /**
     * 造成的伤害
     */
    private final int damageDealt;

    /**
     * 获得的资源
     */
    private final int resourceGained;

    public ActionResult(boolean success, String message) {
        this(success, message, 0, 0, 0);
    }

    public ActionResult(boolean success, String message, int resourceCost, int damageDealt, int resourceGained) {
        this.success = success;
        this.message = message;
        this.resourceCost = resourceCost;
        this.damageDealt = damageDealt;
        this.resourceGained = resourceGained;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public int getResourceCost() {
        return resourceCost;
    }

    public int getDamageDealt() {
        return damageDealt;
    }

    public int getResourceGained() {
        return resourceGained;
    }

    public static ActionResult success(String message) {
        return new ActionResult(true, message);
    }

    public static ActionResult failure(String message) {
        return new ActionResult(false, message);
    }

    public static ActionResult success(String message, int resourceCost, int damageDealt, int resourceGained) {
        return new ActionResult(true, message, resourceCost, damageDealt, resourceGained);
    }

    public static ActionResult failure(String message, int resourceCost) {
        return new ActionResult(false, message, resourceCost, 0, 0);
    }
}
