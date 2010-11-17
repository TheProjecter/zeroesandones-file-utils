package edu.zao.fire.util;

/**
 * @author dylan
 * 
 */
public interface Filter<T> {
	public boolean accept(T item);
}
