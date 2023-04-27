package io.github.s3s3l.yggdrasil.boot.bean;

public enum BeanLifecycle {
	DEFINED("已定义"),
	RESOLVED("解析完成"),
	CREATED("创建完成"),
	REFRESHED("刷新完成"),
	DESTROYED("已销毁"),

	// processable in pipline
	RESOLVING("解析中", BeanLifecycle.RESOLVED),
	CREATING("创建中", BeanLifecycle.CREATED),
	REFRESHING("刷新中", BeanLifecycle.REFRESHED),
	DESTROYING("销毁中", BeanLifecycle.DESTROYED),;

	private String info;
	private BeanLifecycle next;
	private boolean stable;

	private BeanLifecycle(String info) {
		this.info = info;
		this.next = null;
		this.stable = true;
	}

	private BeanLifecycle(String info, BeanLifecycle next) {
		this.info = info;
		this.next = next;
		this.stable = false;
	}

	public String info() {
		return this.info;
	}

	public BeanLifecycle next() {
		return this.next;
	}

	public boolean isStable() {
		return this.stable;
	}
}
