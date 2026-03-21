package io.github.s3s3l.yggdrasil.game.core.demo.scene;

import io.github.s3s3l.yggdrasil.game.core.demo.config.DemoConfig;
import io.github.s3s3l.yggdrasil.game.core.demo.entity.EnemyEntity;
import io.github.s3s3l.yggdrasil.game.core.demo.entity.PlayerEntity;
import io.github.s3s3l.yggdrasil.game.core.demo.input.DefaultInputConfig;
import io.github.s3s3l.yggdrasil.game.core.demo.input.GameInputProcessor;
import io.github.s3s3l.yggdrasil.game.core.flow.GameManager;
import io.github.s3s3l.yggdrasil.game.core.flow.scene.GameScene;
import io.github.s3s3l.yggdrasil.game.core.scene.SceneManager;
import io.github.s3s3l.yggdrasil.game.core.ui.scene.UIScene;

import java.util.ArrayList;
import java.util.List;

/**
 * Demo 游戏场景
 * 包含 Demo 游戏的主要逻辑
 */
public class DemoGameScene extends GameScene {

    /**
     * 玩家实体
     */
    private PlayerEntity player;

    /**
     * 敌人列表
     */
    private final List<EnemyEntity> enemies;

    /**
     * 输入处理器
     */
    private GameInputProcessor inputProcessor;

    /**
     * 敌人生成计时器
     */
    private float enemySpawnTimer;

    /**
     * 游戏时间
     */
    private float gameTimer;

    /**
     * 是否游戏结束
     */
    private boolean gameOver;

    /**
     * 是否胜利
     */
    private boolean isVictory;

    /**
     * 生存目标时间（秒）
     */
    private final float survivalGoalTime;

    /**
     * 创建 Demo 游戏场景
     */
    public DemoGameScene() {
        this(120); // 默认生存 120 秒
    }

    /**
     * 创建 Demo 游戏场景
     *
     * @param survivalTime 生存目标时间（秒）
     */
    public DemoGameScene(float survivalTime) {
        super("DemoGame");
        this.enemies = new ArrayList<>();
        this.enemySpawnTimer = 0;
        this.gameTimer = 0;
        this.gameOver = false;
        this.isVictory = false;
        this.survivalGoalTime = survivalTime;
    }

    @Override
    protected void onEnter() {
        super.onEnter();

        // 初始化输入处理器
        inputProcessor = new GameInputProcessor();
        DefaultInputConfig.applyDefaultBindings(inputProcessor.getInputActionMap());
        setupActionListeners();

        // 初始化玩家
        player = new PlayerEntity("Hero");
        player.initialize();
        player.setPosition(0, 0);

        // 清空敌人列表
        enemies.clear();
        enemySpawnTimer = 0;
        gameTimer = 0;
        gameOver = false;

        onGameStart();
    }

    /**
     * 设置动作监听器
     */
    private void setupActionListeners() {
        // 暂停
        inputProcessor.setActionListener(io.github.s3s3l.yggdrasil.game.core.demo.entity.GameAction.PAUSE, () -> {
            if (!gameOver) {
                pause();
            }
        });

        // 攻击
        inputProcessor.setActionListener(io.github.s3s3l.yggdrasil.game.core.demo.entity.GameAction.ATTACK, () -> {
            if (!gameOver && !isPaused() && player != null && player.isActive()) {
                playerAttack();
            }
        });

        // 技能 1
        inputProcessor.setActionListener(io.github.s3s3l.yggdrasil.game.core.demo.entity.GameAction.SKILL_1, () -> {
            if (!gameOver && !isPaused() && player != null && player.isActive()) {
                playerUseSkill(1);
            }
        });

        // 技能 2
        inputProcessor.setActionListener(io.github.s3s3l.yggdrasil.game.core.demo.entity.GameAction.SKILL_2, () -> {
            if (!gameOver && !isPaused() && player != null && player.isActive()) {
                playerUseSkill(2);
            }
        });

        // 技能 3
        inputProcessor.setActionListener(io.github.s3s3l.yggdrasil.game.core.demo.entity.GameAction.SKILL_3, () -> {
            if (!gameOver && !isPaused() && player != null && player.isActive()) {
                playerUseSkill(3);
            }
        });
    }

    /**
     * 游戏开始回调
     */
    protected void onGameStart() {
        // 子类可重写
    }

    @Override
    public void update(float deltaTime) {
        if (!isActive() || isPaused() || gameOver) {
            return;
        }

        gameTimer += deltaTime;
        enemySpawnTimer += deltaTime;

        // 生成敌人
        if (enemySpawnTimer >= io.github.s3s3l.yggdrasil.game.core.demo.config.DemoConfig.ENEMY_SPAWN_INTERVAL) {
            spawnEnemy();
            enemySpawnTimer = 0;
        }

        // 处理玩家输入
        handlePlayerInput(deltaTime);

        // 更新玩家
        if (player != null) {
            player.update(deltaTime);
            updateGameTime();
        }

        // 更新敌人
        for (EnemyEntity enemy : new ArrayList<>(enemies)) {
            enemy.update(deltaTime);

            // 敌人 AI：追踪玩家
            updateEnemyAI(enemy, deltaTime);

            // 检查敌人是否死亡
            if (enemy.isDead()) {
                onEnemyDefeated(enemy);
                enemies.remove(enemy);
            }
        }

        // 检查碰撞
        checkCollisions();

        // 检查游戏结束条件
        checkGameOver();

        super.update(deltaTime);
    }

    /**
     * 处理玩家输入
     */
    protected void handlePlayerInput(float deltaTime) {
        if (player == null || !player.isActive()) {
            return;
        }

        // 获取移动方向
        float[] direction = inputProcessor.getMoveDirection();
        if (direction[0] != 0 || direction[1] != 0) {
            player.moveTo(
                    player.getX() + direction[0],
                    player.getY() + direction[1]
            );
        } else {
            player.stopMoving();
        }

        // 边界检查
        clampPlayerToBoundary();
    }

    /**
     * 敌人 AI：追踪玩家
     */
    protected void updateEnemyAI(EnemyEntity enemy, float deltaTime) {
        if (enemy == null || !enemy.isActive() || enemy.isDead()) {
            return;
        }

        if (player == null || !player.isActive()) {
            return;
        }

        float distance = enemy.getMovementComponent().distanceTo(player.getX(), player.getY());

        // 如果在攻击范围内，攻击玩家
        if (distance <= enemy.getAttackComponent().getAttackRange()) {
            enemyAIAttack(enemy);
        } else {
            // 否则向玩家移动
            float dx = player.getX() - enemy.getX();
            float dy = player.getY() - enemy.getY();

            // 归一化
            float length = (float) Math.sqrt(dx * dx + dy * dy);
            if (length > 0) {
                dx /= length;
                dy /= length;
            }

            // 移动
            enemy.moveTo(
                    enemy.getX() + dx * deltaTime,
                    enemy.getY() + dy * deltaTime
            );
        }
    }

    /**
     * 敌人 AI 攻击
     */
    protected void enemyAIAttack(EnemyEntity enemy) {
        if (enemy == null || !enemy.isActive() || enemy.isDead()) {
            return;
        }

        if (player == null || !player.isActive()) {
            return;
        }

        // 尝试攻击
        if (enemy.tryAttack()) {
            float attackDamage = enemy.getAttackComponent().getAttackDamage();
            player.takeDamage(attackDamage);
            onEnemyAttackHit(enemy, player);
        }
    }

    /**
     * 敌人攻击命中回调
     *
     * @param enemy  敌人
     * @param player 被命中的玩家
     */
    protected void onEnemyAttackHit(EnemyEntity enemy, PlayerEntity player) {
        // 子类可重写
    }

    /**
     * 将玩家限制在边界内
     */
    protected void clampPlayerToBoundary() {
        if (player == null) {
            return;
        }

        float x = player.getX();
        float y = player.getY();

        x = Math.max(DemoConfig.BOUNDARY_LEFT, Math.min(DemoConfig.BOUNDARY_RIGHT, x));
        y = Math.max(DemoConfig.BOUNDARY_BOTTOM, Math.min(DemoConfig.BOUNDARY_TOP, y));

        player.setPosition(x, y);
    }

    /**
     * 玩家与敌人碰撞回调
     */
    protected void onPlayerEnemyCollision(PlayerEntity player, EnemyEntity enemy) {
        // 子类可重写
    }

    /**
     * 生成敌人
     */
    protected void spawnEnemy() {
        if (enemies.size() >= DemoConfig.MAX_ENEMIES) {
            return;
        }

        // 随机生成敌人类型
        float rand = (float) Math.random();
        EnemyEntity.EnemyType type;
        if (rand < 0.1f) {
            type = EnemyEntity.EnemyType.BOSS;
        } else if (rand < 0.3f) {
            type = EnemyEntity.EnemyType.ELITE;
        } else {
            type = EnemyEntity.EnemyType.NORMAL;
        }

        EnemyEntity enemy = new EnemyEntity("Enemy_" + enemies.size(), type);
        enemy.initialize();

        // 随机位置生成（在边界内）
        float x = (float) ((Math.random() * 2 - 1) * DemoConfig.BOUNDARY_RIGHT * 0.8);
        float y = (float) ((Math.random() * 2 - 1) * DemoConfig.BOUNDARY_TOP * 0.8);
        enemy.setPosition(x, y);

        enemies.add(enemy);
    }

    /**
     * 检查碰撞
     */
    protected void checkCollisions() {
        if (player == null || !player.isActive()) {
            return;
        }

        // 检查玩家与敌人的碰撞
        for (EnemyEntity enemy : enemies) {
            if (!enemy.isActive() || enemy.isDead()) {
                continue;
            }

            float distance = player.getMovementComponent().distanceTo(enemy.getX(), enemy.getY());

            // 简单的圆形碰撞检测
            if (distance < 30) { // 碰撞半径
                onPlayerEnemyCollision(player, enemy);
            }
        }
    }

    /**
     * 玩家攻击
     */
    protected void playerAttack() {
        if (player == null || !player.isActive()) {
            return;
        }

        // 尝试攻击
        if (!player.tryAttack()) {
            return; // 冷却中
        }

        // 获取攻击范围和伤害
        float attackRange = player.getAttackComponent().getAttackRange();
        float attackDamage = player.getAttackComponent().getAttackDamage();

        // 检查范围内的敌人
        for (EnemyEntity enemy : enemies) {
            if (!enemy.isActive() || enemy.isDead()) {
                continue;
            }

            float distance = player.getMovementComponent().distanceTo(enemy.getX(), enemy.getY());
            if (distance <= attackRange) {
                // 命中敌人
                enemy.takeDamage(attackDamage);
                onPlayerAttackHit(player, enemy);
            }
        }

        onPlayerAttack(player);
    }

    /**
     * 玩家使用技能
     *
     * @param skillIndex 技能索引（1-3）
     */
    protected void playerUseSkill(int skillIndex) {
        // 基础技能实现，子类可扩展
        switch (skillIndex) {
            case 1:
                // 技能 1：快速治疗
                if (player != null) {
                    player.heal(20);
                    onPlayerUseSkill(player, 1);
                }
                break;
            case 2:
                // 技能 2：范围攻击（攻击所有敌人）
                if (player != null) {
                    float attackDamage = player.getAttackComponent().getAttackDamage() * 0.5f;
                    for (EnemyEntity enemy : enemies) {
                        if (enemy.isActive() && !enemy.isDead()) {
                            enemy.takeDamage(attackDamage);
                        }
                    }
                    onPlayerUseSkill(player, 2);
                }
                break;
            case 3:
                // 技能 3：临时增益（这里简单实现为重置攻击冷却）
                if (player != null) {
                    player.getAttackComponent().resetCooldown();
                    onPlayerUseSkill(player, 3);
                }
                break;
        }
    }

    /**
     * 玩家攻击命中回调
     *
     * @param player 玩家
     * @param enemy  被命中的敌人
     */
    protected void onPlayerAttackHit(PlayerEntity player, EnemyEntity enemy) {
        // 子类可重写
    }

    /**
     * 玩家攻击回调
     *
     * @param player 玩家
     */
    protected void onPlayerAttack(PlayerEntity player) {
        // 子类可重写
    }

    /**
     * 玩家使用技能回调
     *
     * @param player     玩家
     * @param skillIndex 技能索引
     */
    protected void onPlayerUseSkill(PlayerEntity player, int skillIndex) {
        // 子类可重写
    }

    /**
     * 更新游戏时间
     */
    protected void updateGameTime() {
        setGameTime(getGameTime() + 1.0f / io.github.s3s3l.yggdrasil.game.core.demo.config.DemoConfig.TARGET_FPS);
    }

    /**
     * 敌人被击败回调
     */
    protected void onEnemyDefeated(EnemyEntity enemy) {
        if (player != null) {
            player.addScore(enemy.getBounty());
        }
    }

    /**
     * 检查游戏结束条件
     */
    protected void checkGameOver() {
        if (player == null || player.isDead()) {
            gameOver = true;
            isVictory = false;
            onGameLost();
            return;
        }

        // 检查胜利条件：生存时间达到目标
        if (gameTimer >= survivalGoalTime) {
            gameOver = true;
            isVictory = true;
            onGameVictory();
        }
    }

    /**
     * 游戏胜利回调
     */
    protected void onGameVictory() {
        victory();
    }

    /**
     * 游戏失败回调
     */
    protected void onGameLost() {
        defeat();
    }

    /**
     * 获取玩家实体
     *
     * @return 玩家实体
     */
    public PlayerEntity getPlayer() {
        return player;
    }

    /**
     * 获取敌人列表
     *
     * @return 敌人列表
     */
    public List<EnemyEntity> getEnemies() {
        return enemies;
    }

    /**
     * 获取输入处理器
     *
     * @return 输入处理器
     */
    public GameInputProcessor getInputProcessor() {
        return inputProcessor;
    }

    /**
     * 获取游戏时间
     *
     * @return 游戏时间（秒）
     */
    public float getGameTimer() {
        return gameTimer;
    }

    /**
     * 检查是否游戏结束
     *
     * @return true 如果游戏结束
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * 检查是否胜利
     *
     * @return true 如果胜利
     */
    public boolean isVictory() {
        return isVictory;
    }

    /**
     * 手动初始化场景（用于测试）
     */
    public void initializeForTest() {
        onEnter();
    }
}
