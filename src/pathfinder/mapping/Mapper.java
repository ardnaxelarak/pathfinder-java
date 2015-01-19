package pathfinder.mapping;

public interface Mapper<T1, T2>
{
	public T2 getValue(T1 o);
}
