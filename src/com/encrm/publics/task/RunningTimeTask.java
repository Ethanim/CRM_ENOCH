package com.encrm.publics.task;

/**
 * 任务调用框架使用
 */
public class RunningTimeTask {

	private int i = 0;

	public void runningTime() {
		System.out.println("任务调度已运行：" + i++ + "次");
	}
}
