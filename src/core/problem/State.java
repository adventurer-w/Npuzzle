package core.problem;

/**
 * 问题模型的状态
 */
public abstract class State {

	public abstract void draw(); // 在Console上，输出该状态

	/**
	 * 当前状态下，采用某个action而进入的下一个状态
	 * 
	 * @param action 当前状态下，某个可行的action
	 * @return 后继状态
	 */
	public abstract State next(Action action);

	/**
	 * 当前状态下所有可能的Action，但不一定都可行
	 * 
	 * @return 所有可能的Action的List
	 */
	public abstract Iterable<? extends Action> actions();
}