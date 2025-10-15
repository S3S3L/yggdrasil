package io.github.s3s3l.yggdrasil.game.core.object;

import io.github.s3s3l.yggdrasil.game.core.basic.GameAtom;
import io.github.s3s3l.yggdrasil.game.core.basic.TreeNode;
import io.github.s3s3l.yggdrasil.game.core.object.props.Visible;

/**
 * 游戏对象接口，所有游戏中的对象都应实现此接口
 */
public interface GameObject extends GameAtom, TreeNode<GameObject> {
    Visible getVisible();

    void setVisible(Visible visible);

    boolean isEnable();

    void setEnable(boolean enable);

}
