package io.github.s3s3l.yggdrasil.game.core.event;

import io.github.s3s3l.yggdrasil.game.core.object.GameObject;

/**
 * 基础事件接口，支持传播阶段与停止控制。
 */
public interface Event {

	EventType getType();

	EventPhase getPhase();

	void setPhase(EventPhase phase);

	long getTimestamp();

	GameObject getSource();

	void setSource(GameObject source);

	GameObject getTarget();

	void setTarget(GameObject target);

	GameObject getCurrent();

	void setCurrent(GameObject current);

	boolean isPropagationStopped();

	void stopPropagation();

	boolean isDefaultPrevented();

	void preventDefault();
}
