package pathfinder.format;

public interface Formatter<T1, T2>
{
	public T2 getValue(T1 o);
}
