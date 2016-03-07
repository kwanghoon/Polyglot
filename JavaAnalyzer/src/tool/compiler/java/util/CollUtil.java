package tool.compiler.java.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;

public class CollUtil {
	
	// String conversion
	
	/**
	 * Returns a string representation of the collection. The string
	 * representation consists of a list of the collection's elements in the
	 * order they are returned by its iterator. Adjacent elements are
	 * separated by the characters <tt>", "</tt> (comma and space).
	 * Elements are converted to strings as by {@link String#valueOf(Object)}.
	 *
	 * @return a string representation of collection
	 */
	public static <E> String getStringOf(Collection<E> collection) {
		Iterator<E> it = collection.iterator();
		if (!it.hasNext())
			return "";
		
		StringBuilder sb = new StringBuilder();
		for (;;) {
			E e = it.next();
			sb.append(e == collection ? "(this Collection)" : e);
			if (!it.hasNext())
				return sb.toString();
			sb.append(',').append(' ');
		}
	}
	
	/**
	 * Returns a string representation of the collection. The string
	 * representation consists of a list of the collection's elements in the
	 * order they are returned by its iterator, enclosed in set brackets (
	 * <tt>e.g., "[]"</tt>). Adjacent elements are separated by the characters
	 * <tt>", "</tt> (comma and space). Elements are converted to strings as by
	 * {@link String#valueOf(Object)}.
	 *
	 * @return a string representation of collection
	 */
	public static <E> String getStringOf(Collection<E> collection, char leftBracket, char rightBracket) {
		
		try {
			return leftBracket + getStringOf(collection) + rightBracket;
		} catch (NullPointerException e) {
			return String.valueOf(leftBracket) + rightBracket;
		}
	}
	
	/**
	 * Returns a string representation of the collection. The string
	 * representation consists of a list of the collection's elements in the
	 * order they are returned by its iterator. Adjacent elements are
	 * separated by the characters <tt>"\n "</tt> (new line).
	 * Elements are converted to strings as by {@link String#valueOf(Object)}.
	 *
	 * @return a string representation of collection
	 */
	public static <E> String getNLStringOf(Collection<E> collection) {
		Iterator<E> it = collection.iterator();
		if (!it.hasNext())
			return "";
		
		StringBuilder sb = new StringBuilder();
		for (;;) {
			E e = it.next();
			sb.append(e == collection ? "(this Collection)" : e);
			if (!it.hasNext())
				return sb.toString();
			sb.append('\n');
		}
	}
	
	public static <E> void writeToOutputStream(OutputStream oStream, Collection<E> collection) throws IOException {
		for(E ft: collection) {
			oStream.write((ft + "\n").getBytes());
		}
	}
}