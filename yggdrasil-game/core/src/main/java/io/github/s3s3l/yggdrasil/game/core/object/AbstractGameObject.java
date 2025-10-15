package io.github.s3s3l.yggdrasil.game.core.object;

import java.util.HashSet;
import java.util.Set;

import io.github.s3s3l.yggdrasil.game.core.basic.AbstractGameAtom;
import io.github.s3s3l.yggdrasil.game.core.object.props.Visible;
import lombok.Getter;
import lombok.Setter;

public abstract class AbstractGameObject extends AbstractGameAtom<GameObjectConfig> implements GameObject {

    protected final Set<GameObject> children = new HashSet<>();

    protected GameObject parent;

    /**
     * 对象的可见性
     * SHOW: 可见，参与preRender和Render计算
     * HIDE: 隐藏，不参与preRender和Render计算
     */
    @Setter
    @Getter
    protected Visible visible = Visible.SHOW;
    /**
     * 对象是否启用
     * 启用的对象会参与calculate计算
     * 禁用的对象不会参与calculate、preRender和Render计算
     */
    @Setter
    @Getter
    protected boolean enable = true;

    public AbstractGameObject(GameObjectConfig config) {
        super(config);
    }

    @Override
    public void addChild(GameObject child) {
        children.add(child);
    }

    @Override
    public Set<GameObject> children() {
        return children;
    }

    @Override
    public GameObject parent() {
        return parent;
    }

    @Override
    public void setParent(GameObject parent) {
        this.parent = parent;
    }

    @Override
    public void calculate(float delta) {
        super.calculate(delta);

        children.forEach(child -> child.calculate(delta));
    }

    @Override
    public void preRender() {
        super.preRender();

        children.forEach(GameObject::preRender);
    }

    @Override
    public void render() {
        super.render();
        children.forEach(GameObject::render);
    }

}
