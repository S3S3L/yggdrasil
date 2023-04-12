package io.github.s3s3l.yggdrasil.boot.old.enumerations;

public enum ApplicationLifecycle {
	CREATED(0, "已创建"),
	INITIALIZING(1, "初始化中"),
	INITIALIZED(2, "初始化完成"),
	PREPARING(3, "准备中"),
	PREPARED(4, "准备完成"),
	STARTING(5, "启动中"),
	STARTED(6, "启动完成"),
	STOPPING(7, "停止中"),
	STOPPED(8, "已停止"),
	DESTROYING(9, "销毁中"),
	DESTROYED(10, "已销毁"),
	TERMINATED(11, "异常终止");

	private int value;
	private String info;

	private ApplicationLifecycle(int value, String info) {
		this.value = value;
		this.info = info;
	}

	public int value() {
		return this.value;
	}

	public String info() {
		return this.info;
	}

	public static ApplicationLifecycle parse(int value) {
		ApplicationLifecycle[] enums = values();

		for (ApplicationLifecycle currentEnum : enums) {
			if (currentEnum.value == value) {
				return currentEnum;
			}
		}
		throw new IllegalArgumentException(String.format("No matching constant for [%s]", value));
	}
}
