package core.problem;

/**
 * ����ģ�͵�״̬
 */
public abstract class State {

	public abstract void draw(); // ��Console�ϣ������״̬

	/**
	 * ��ǰ״̬�£�����ĳ��action���������һ��״̬
	 * 
	 * @param action ��ǰ״̬�£�ĳ�����е�action
	 * @return ���״̬
	 */
	public abstract State next(Action action);

	/**
	 * ��ǰ״̬�����п��ܵ�Action������һ��������
	 * 
	 * @return ���п��ܵ�Action��List
	 */
	public abstract Iterable<? extends Action> actions();
}