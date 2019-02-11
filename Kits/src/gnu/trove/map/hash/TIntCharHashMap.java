///////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2001, Eric D. Friedman All Rights Reserved.
// Copyright (c) 2009, Rob Eden All Rights Reserved.
// Copyright (c) 2009, Jeff Randall All Rights Reserved.
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
///////////////////////////////////////////////////////////////////////////////

package gnu.trove.map.hash;


//////////////////////////////////////////////////
// THIS IS A GENERATED CLASS. DO NOT HAND EDIT! //
//////////////////////////////////////////////////

import gnu.trove.TCharCollection;
import gnu.trove.TIntCollection;
import gnu.trove.function.TCharFunction;
import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.THashPrimitiveIterator;
import gnu.trove.impl.hash.TIntCharHash;
import gnu.trove.impl.hash.TPrimitiveHash;
import gnu.trove.iterator.TCharIterator;
import gnu.trove.iterator.TIntCharIterator;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.map.TIntCharMap;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TIntCharProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.set.TIntSet;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Map;

/**
 * An open addressed Map implementation for int keys and char values.
 *
 * @author Eric D. Friedman
 * @author Rob Eden
 * @author Jeff Randall
 * @version $Id: _K__V_HashMap.template,v 1.1.2.16 2010/03/02 04:09:50 robeden Exp $
 */
public class TIntCharHashMap extends TIntCharHash implements TIntCharMap, Externalizable {
	static final long serialVersionUID = 1L;

	/**
	 * the values of the map
	 */
	protected transient char[] _values;


	/**
	 * Creates a new <code>TIntCharHashMap</code> instance with the default
	 * capacity and load factor.
	 */
	public TIntCharHashMap() {
		super();
	}


	/**
	 * Creates a new <code>TIntCharHashMap</code> instance with a prime
	 * capacity equal to or greater than <tt>initialCapacity</tt> and
	 * with the default load factor.
	 *
	 * @param initialCapacity an <code>int</code> value
	 */
	public TIntCharHashMap(int initialCapacity) {
		super(initialCapacity);
	}


	/**
	 * Creates a new <code>TIntCharHashMap</code> instance with a prime
	 * capacity equal to or greater than <tt>initialCapacity</tt> and
	 * with the specified load factor.
	 *
	 * @param initialCapacity an <code>int</code> value
	 * @param loadFactor      a <code>float</code> value
	 */
	public TIntCharHashMap(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}


	/**
	 * Creates a new <code>TIntCharHashMap</code> instance with a prime
	 * capacity equal to or greater than <tt>initialCapacity</tt> and
	 * with the specified load factor.
	 *
	 * @param initialCapacity an <code>int</code> value
	 * @param loadFactor      a <code>float</code> value
	 * @param noEntryKey      a <code>int</code> value that represents
	 *                        <tt>null</tt> for the Key set.
	 * @param noEntryValue    a <code>char</code> value that represents
	 *                        <tt>null</tt> for the Value set.
	 */
	public TIntCharHashMap(int initialCapacity, float loadFactor, int noEntryKey, char noEntryValue) {
		super(initialCapacity, loadFactor, noEntryKey, noEntryValue);
	}


	/**
	 * Creates a new <code>TIntCharHashMap</code> instance containing
	 * all of the entries in the map passed in.
	 *
	 * @param keys   a <tt>int</tt> array containing the keys for the matching values.
	 * @param values a <tt>char</tt> array containing the values.
	 */
	public TIntCharHashMap(int[] keys, char[] values) {
		super(Math.max(keys.length, values.length));

		int size = Math.min(keys.length, values.length);
		for (int i = 0; i < size; i++) {
			this.put(keys[i], values[i]);
		}
	}


	/**
	 * Creates a new <code>TIntCharHashMap</code> instance containing
	 * all of the entries in the map passed in.
	 *
	 * @param map a <tt>TIntCharMap</tt> that will be duplicated.
	 */
	public TIntCharHashMap(TIntCharMap map) {
		super(map.size());
		if (map instanceof TIntCharHashMap) {
			TIntCharHashMap hashmap = (TIntCharHashMap) map;
			this._loadFactor = hashmap._loadFactor;
			this.no_entry_key = hashmap.no_entry_key;
			this.no_entry_value = hashmap.no_entry_value;
			//noinspection RedundantCast
			if (this.no_entry_key != (int) 0) {
				Arrays.fill(_set, this.no_entry_key);
			}
			//noinspection RedundantCast
			if (this.no_entry_value != (char) 0) {
				Arrays.fill(_values, this.no_entry_value);
			}
			setUp((int) Math.ceil(DEFAULT_CAPACITY / _loadFactor));
		}
		putAll(map);
	}


	/**
	 * initializes the hashtable to a prime capacity which is at least
	 * <tt>initialCapacity + 1</tt>.
	 *
	 * @param initialCapacity an <code>int</code> value
	 * @return the actual capacity chosen
	 */
	protected int setUp(int initialCapacity) {
		int capacity;

		capacity = super.setUp(initialCapacity);
		_values = new char[capacity];
		return capacity;
	}


	/**
	 * rehashes the map to the new capacity.
	 *
	 * @param newCapacity an <code>int</code> value
	 */
	/**
	 * {@inheritDoc}
	 */
	protected void rehash(int newCapacity) {
		int oldCapacity = _set.length;

		int oldKeys[] = _set;
		char oldVals[] = _values;
		byte oldStates[] = _states;

		_set = new int[newCapacity];
		_values = new char[newCapacity];
		_states = new byte[newCapacity];

		for (int i = oldCapacity; i-- > 0; ) {
			if (oldStates[i] == FULL) {
				int o = oldKeys[i];
				int index = insertKey(o);
				_values[index] = oldVals[i];
			}
		}
	}


	/**
	 * {@inheritDoc}
	 */
	public char put(int key, char value) {
		int index = insertKey(key);
		return doPut(key, value, index);
	}


	/**
	 * {@inheritDoc}
	 */
	public char putIfAbsent(int key, char value) {
		int index = insertKey(key);
		if (index < 0) return _values[-index - 1];
		return doPut(key, value, index);
	}


	private char doPut(int key, char value, int index) {
		char previous = no_entry_value;
		boolean isNewMapping = true;
		if (index < 0) {
			index = -index - 1;
			previous = _values[index];
			isNewMapping = false;
		}
		_values[index] = value;

		if (isNewMapping) {
			postInsertHook(consumeFreeSlot);
		}

		return previous;
	}


	/**
	 * {@inheritDoc}
	 */
	public void putAll(Map<? extends Integer, ? extends Character> map) {
		ensureCapacity(map.size());
		// could optimize this for cases when map instanceof THashMap
		for (Map.Entry<? extends Integer, ? extends Character> entry : map.entrySet()) {
			this.put(entry.getKey().intValue(), entry.getValue().charValue());
		}
	}


	/**
	 * {@inheritDoc}
	 */
	public void putAll(TIntCharMap map) {
		ensureCapacity(map.size());
		TIntCharIterator iter = map.iterator();
		while (iter.hasNext()) {
			iter.advance();
			this.put(iter.key(), iter.value());
		}
	}


	/**
	 * {@inheritDoc}
	 */
	public char get(int key) {
		int index = index(key);
		return index < 0 ? no_entry_value : _values[index];
	}


	/**
	 * {@inheritDoc}
	 */
	public void clear() {
		super.clear();
		Arrays.fill(_set, 0, _set.length, no_entry_key);
		Arrays.fill(_values, 0, _values.length, no_entry_value);
		Arrays.fill(_states, 0, _states.length, FREE);
	}


	/**
	 * {@inheritDoc}
	 */
	public boolean isEmpty() {
		return 0 == _size;
	}


	/**
	 * {@inheritDoc}
	 */
	public char remove(int key) {
		char prev = no_entry_value;
		int index = index(key);
		if (index >= 0) {
			prev = _values[index];
			removeAt(index);    // clear key,state; adjust size
		}
		return prev;
	}


	/**
	 * {@inheritDoc}
	 */
	protected void removeAt(int index) {
		_values[index] = no_entry_value;
		super.removeAt(index);  // clear key, state; adjust size
	}


	/**
	 * {@inheritDoc}
	 */
	public TIntSet keySet() {
		return new TKeyView();
	}


	/**
	 * {@inheritDoc}
	 */
	public int[] keys() {
		int[] keys = new int[size()];
		if (keys.length == 0) {
			return keys;        // nothing to copy
		}
		int[] k = _set;
		byte[] states = _states;

		for (int i = k.length, j = 0; i-- > 0; ) {
			if (states[i] == FULL) {
				keys[j++] = k[i];
			}
		}
		return keys;
	}


	/**
	 * {@inheritDoc}
	 */
	public int[] keys(int[] array) {
		int size = size();
		if (size == 0) {
			return array;       // nothing to copy
		}
		if (array.length < size) {
			array = new int[size];
		}

		int[] keys = _set;
		byte[] states = _states;

		for (int i = keys.length, j = 0; i-- > 0; ) {
			if (states[i] == FULL) {
				array[j++] = keys[i];
			}
		}
		return array;
	}


	/**
	 * {@inheritDoc}
	 */
	public TCharCollection valueCollection() {
		return new TValueView();
	}


	/**
	 * {@inheritDoc}
	 */
	public char[] values() {
		char[] vals = new char[size()];
		if (vals.length == 0) {
			return vals;        // nothing to copy
		}
		char[] v = _values;
		byte[] states = _states;

		for (int i = v.length, j = 0; i-- > 0; ) {
			if (states[i] == FULL) {
				vals[j++] = v[i];
			}
		}
		return vals;
	}


	/**
	 * {@inheritDoc}
	 */
	public char[] values(char[] array) {
		int size = size();
		if (size == 0) {
			return array;       // nothing to copy
		}
		if (array.length < size) {
			array = new char[size];
		}

		char[] v = _values;
		byte[] states = _states;

		for (int i = v.length, j = 0; i-- > 0; ) {
			if (states[i] == FULL) {
				array[j++] = v[i];
			}
		}
		return array;
	}


	/**
	 * {@inheritDoc}
	 */
	public boolean containsValue(char val) {
		byte[] states = _states;
		char[] vals = _values;

		for (int i = vals.length; i-- > 0; ) {
			if (states[i] == FULL && val == vals[i]) {
				return true;
			}
		}
		return false;
	}


	/**
	 * {@inheritDoc}
	 */
	public boolean containsKey(int key) {
		return contains(key);
	}


	/**
	 * {@inheritDoc}
	 */
	public TIntCharIterator iterator() {
		return new TIntCharHashIterator(this);
	}


	/**
	 * {@inheritDoc}
	 */
	public boolean forEachKey(TIntProcedure procedure) {
		return forEach(procedure);
	}


	/**
	 * {@inheritDoc}
	 */
	public boolean forEachValue(TCharProcedure procedure) {
		byte[] states = _states;
		char[] values = _values;
		for (int i = values.length; i-- > 0; ) {
			if (states[i] == FULL && !procedure.execute(values[i])) {
				return false;
			}
		}
		return true;
	}


	/**
	 * {@inheritDoc}
	 */
	public boolean forEachEntry(TIntCharProcedure procedure) {
		byte[] states = _states;
		int[] keys = _set;
		char[] values = _values;
		for (int i = keys.length; i-- > 0; ) {
			if (states[i] == FULL && !procedure.execute(keys[i], values[i])) {
				return false;
			}
		}
		return true;
	}


	/**
	 * {@inheritDoc}
	 */
	public void transformValues(TCharFunction function) {
		byte[] states = _states;
		char[] values = _values;
		for (int i = values.length; i-- > 0; ) {
			if (states[i] == FULL) {
				values[i] = function.execute(values[i]);
			}
		}
	}


	/**
	 * {@inheritDoc}
	 */
	public boolean retainEntries(TIntCharProcedure procedure) {
		boolean modified = false;
		byte[] states = _states;
		int[] keys = _set;
		char[] values = _values;


		// Temporarily disable compaction. This is a fix for bug #1738760
		tempDisableAutoCompaction();
		try {
			for (int i = keys.length; i-- > 0; ) {
				if (states[i] == FULL && !procedure.execute(keys[i], values[i])) {
					removeAt(i);
					modified = true;
				}
			}
		} finally {
			reenableAutoCompaction(true);
		}

		return modified;
	}


	/**
	 * {@inheritDoc}
	 */
	public boolean increment(int key) {
		return adjustValue(key, (char) 1);
	}


	/**
	 * {@inheritDoc}
	 */
	public boolean adjustValue(int key, char amount) {
		int index = index(key);
		if (index < 0) {
			return false;
		} else {
			_values[index] += amount;
			return true;
		}
	}


	/**
	 * {@inheritDoc}
	 */
	public char adjustOrPutValue(int key, char adjust_amount, char put_amount) {
		int index = insertKey(key);
		final boolean isNewMapping;
		final char newValue;
		if (index < 0) {
			index = -index - 1;
			newValue = (_values[index] += adjust_amount);
			isNewMapping = false;
		} else {
			newValue = (_values[index] = put_amount);
			isNewMapping = true;
		}

		byte previousState = _states[index];

		if (isNewMapping) {
			postInsertHook(consumeFreeSlot);
		}

		return newValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof TIntCharMap)) {
			return false;
		}
		TIntCharMap that = (TIntCharMap) other;
		if (that.size() != this.size()) {
			return false;
		}
		char[] values = _values;
		byte[] states = _states;
		char this_no_entry_value = getNoEntryValue();
		char that_no_entry_value = that.getNoEntryValue();
		for (int i = values.length; i-- > 0; ) {
			if (states[i] == FULL) {
				int key = _set[i];
				char that_value = that.get(key);
				char this_value = values[i];
				if ((this_value != that_value) && (this_value != this_no_entry_value) && (that_value != that_no_entry_value)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		int hashcode = 0;
		byte[] states = _states;
		for (int i = _values.length; i-- > 0; ) {
			if (states[i] == FULL) {
				hashcode += HashFunctions.hash(_set[i]) ^ HashFunctions.hash(_values[i]);
			}
		}
		return hashcode;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		final StringBuilder buf = new StringBuilder("{");
		forEachEntry(new TIntCharProcedure() {
			private boolean first = true;

			public boolean execute(int key, char value) {
				if (first) first = false;
				else buf.append(", ");

				buf.append(key);
				buf.append("=");
				buf.append(value);
				return true;
			}
		});
		buf.append("}");
		return buf.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	public void writeExternal(ObjectOutput out) throws IOException {
		// VERSION
		out.writeByte(0);

		// SUPER
		super.writeExternal(out);

		// NUMBER OF ENTRIES
		out.writeInt(_size);

		// ENTRIES
		for (int i = _states.length; i-- > 0; ) {
			if (_states[i] == FULL) {
				out.writeInt(_set[i]);
				out.writeChar(_values[i]);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		// VERSION
		in.readByte();

		// SUPER
		super.readExternal(in);

		// NUMBER OF ENTRIES
		int size = in.readInt();
		setUp(size);

		// ENTRIES
		while (size-- > 0) {
			int key = in.readInt();
			char val = in.readChar();
			put(key, val);
		}
	}

	/**
	 * a view onto the keys of the map.
	 */
	protected class TKeyView implements TIntSet {

		/**
		 * {@inheritDoc}
		 */
		public TIntIterator iterator() {
			return new TIntCharKeyHashIterator(TIntCharHashMap.this);
		}


		/**
		 * {@inheritDoc}
		 */
		public int getNoEntryValue() {
			return no_entry_key;
		}


		/**
		 * {@inheritDoc}
		 */
		public int size() {
			return _size;
		}


		/**
		 * {@inheritDoc}
		 */
		public boolean isEmpty() {
			return 0 == _size;
		}


		/**
		 * {@inheritDoc}
		 */
		public boolean contains(int entry) {
			return TIntCharHashMap.this.contains(entry);
		}


		/**
		 * {@inheritDoc}
		 */
		public int[] toArray() {
			return TIntCharHashMap.this.keys();
		}


		/**
		 * {@inheritDoc}
		 */
		public int[] toArray(int[] dest) {
			return TIntCharHashMap.this.keys(dest);
		}


		/**
		 * Unsupported when operating upon a Key Set view of a TIntCharMap
		 * <p/>
		 * {@inheritDoc}
		 */
		public boolean add(int entry) {
			throw new UnsupportedOperationException();
		}


		/**
		 * {@inheritDoc}
		 */
		public boolean remove(int entry) {
			return no_entry_value != TIntCharHashMap.this.remove(entry);
		}


		/**
		 * {@inheritDoc}
		 */
		public boolean containsAll(Collection<?> collection) {
			for (Object element : collection) {
				if (element instanceof Integer) {
					int ele = ((Integer) element).intValue();
					if (!TIntCharHashMap.this.containsKey(ele)) {
						return false;
					}
				} else {
					return false;
				}
			}
			return true;
		}


		/**
		 * {@inheritDoc}
		 */
		public boolean containsAll(TIntCollection collection) {
			TIntIterator iter = collection.iterator();
			while (iter.hasNext()) {
				if (!TIntCharHashMap.this.containsKey(iter.next())) {
					return false;
				}
			}
			return true;
		}


		/**
		 * {@inheritDoc}
		 */
		public boolean containsAll(int[] array) {
			for (int element : array) {
				if (!TIntCharHashMap.this.contains(element)) {
					return false;
				}
			}
			return true;
		}


		/**
		 * Unsupported when operating upon a Key Set view of a TIntCharMap
		 * <p/>
		 * {@inheritDoc}
		 */
		public boolean addAll(Collection<? extends Integer> collection) {
			throw new UnsupportedOperationException();
		}


		/**
		 * Unsupported when operating upon a Key Set view of a TIntCharMap
		 * <p/>
		 * {@inheritDoc}
		 */
		public boolean addAll(TIntCollection collection) {
			throw new UnsupportedOperationException();
		}


		/**
		 * Unsupported when operating upon a Key Set view of a TIntCharMap
		 * <p/>
		 * {@inheritDoc}
		 */
		public boolean addAll(int[] array) {
			throw new UnsupportedOperationException();
		}


		/**
		 * {@inheritDoc}
		 */
		@SuppressWarnings({"SuspiciousMethodCalls"})
		public boolean retainAll(Collection<?> collection) {
			boolean modified = false;
			TIntIterator iter = iterator();
			while (iter.hasNext()) {
				if (!collection.contains(Integer.valueOf(iter.next()))) {
					iter.remove();
					modified = true;
				}
			}
			return modified;
		}


		/**
		 * {@inheritDoc}
		 */
		public boolean retainAll(TIntCollection collection) {
			if (this == collection) {
				return false;
			}
			boolean modified = false;
			TIntIterator iter = iterator();
			while (iter.hasNext()) {
				if (!collection.contains(iter.next())) {
					iter.remove();
					modified = true;
				}
			}
			return modified;
		}


		/**
		 * {@inheritDoc}
		 */
		public boolean retainAll(int[] array) {
			boolean changed = false;
			Arrays.sort(array);
			int[] set = _set;
			byte[] states = _states;

			for (int i = set.length; i-- > 0; ) {
				if (states[i] == FULL && (Arrays.binarySearch(array, set[i]) < 0)) {
					removeAt(i);
					changed = true;
				}
			}
			return changed;
		}


		/**
		 * {@inheritDoc}
		 */
		public boolean removeAll(Collection<?> collection) {
			boolean changed = false;
			for (Object element : collection) {
				if (element instanceof Integer) {
					int c = ((Integer) element).intValue();
					if (remove(c)) {
						changed = true;
					}
				}
			}
			return changed;
		}


		/**
		 * {@inheritDoc}
		 */
		public boolean removeAll(TIntCollection collection) {
			if (this == collection) {
				clear();
				return true;
			}
			boolean changed = false;
			TIntIterator iter = collection.iterator();
			while (iter.hasNext()) {
				int element = iter.next();
				if (remove(element)) {
					changed = true;
				}
			}
			return changed;
		}


		/**
		 * {@inheritDoc}
		 */
		public boolean removeAll(int[] array) {
			boolean changed = false;
			for (int i = array.length; i-- > 0; ) {
				if (remove(array[i])) {
					changed = true;
				}
			}
			return changed;
		}


		/**
		 * {@inheritDoc}
		 */
		public void clear() {
			TIntCharHashMap.this.clear();
		}


		/**
		 * {@inheritDoc}
		 */
		public boolean forEach(TIntProcedure procedure) {
			return TIntCharHashMap.this.forEachKey(procedure);
		}


		@Override
		public boolean equals(Object other) {
			if (!(other instanceof TIntSet)) {
				return false;
			}
			final TIntSet that = (TIntSet) other;
			if (that.size() != this.size()) {
				return false;
			}
			for (int i = _states.length; i-- > 0; ) {
				if (_states[i] == FULL) {
					if (!that.contains(_set[i])) {
						return false;
					}
				}
			}
			return true;
		}


		@Override
		public int hashCode() {
			int hashcode = 0;
			for (int i = _states.length; i-- > 0; ) {
				if (_states[i] == FULL) {
					hashcode += HashFunctions.hash(_set[i]);
				}
			}
			return hashcode;
		}


		@Override
		public String toString() {
			final StringBuilder buf = new StringBuilder("{");
			forEachKey(new TIntProcedure() {
				private boolean first = true;


				public boolean execute(int key) {
					if (first) {
						first = false;
					} else {
						buf.append(", ");
					}

					buf.append(key);
					return true;
				}
			});
			buf.append("}");
			return buf.toString();
		}
	}

	/**
	 * a view onto the values of the map.
	 */
	protected class TValueView implements TCharCollection {

		/**
		 * {@inheritDoc}
		 */
		public TCharIterator iterator() {
			return new TIntCharValueHashIterator(TIntCharHashMap.this);
		}


		/**
		 * {@inheritDoc}
		 */
		public char getNoEntryValue() {
			return no_entry_value;
		}


		/**
		 * {@inheritDoc}
		 */
		public int size() {
			return _size;
		}


		/**
		 * {@inheritDoc}
		 */
		public boolean isEmpty() {
			return 0 == _size;
		}


		/**
		 * {@inheritDoc}
		 */
		public boolean contains(char entry) {
			return TIntCharHashMap.this.containsValue(entry);
		}


		/**
		 * {@inheritDoc}
		 */
		public char[] toArray() {
			return TIntCharHashMap.this.values();
		}


		/**
		 * {@inheritDoc}
		 */
		public char[] toArray(char[] dest) {
			return TIntCharHashMap.this.values(dest);
		}


		public boolean add(char entry) {
			throw new UnsupportedOperationException();
		}


		/**
		 * {@inheritDoc}
		 */
		public boolean remove(char entry) {
			char[] values = _values;
			int[] set = _set;

			for (int i = values.length; i-- > 0; ) {
				if ((set[i] != FREE && set[i] != REMOVED) && entry == values[i]) {
					removeAt(i);
					return true;
				}
			}
			return false;
		}


		/**
		 * {@inheritDoc}
		 */
		public boolean containsAll(Collection<?> collection) {
			for (Object element : collection) {
				if (element instanceof Character) {
					char ele = ((Character) element).charValue();
					if (!TIntCharHashMap.this.containsValue(ele)) {
						return false;
					}
				} else {
					return false;
				}
			}
			return true;
		}


		/**
		 * {@inheritDoc}
		 */
		public boolean containsAll(TCharCollection collection) {
			TCharIterator iter = collection.iterator();
			while (iter.hasNext()) {
				if (!TIntCharHashMap.this.containsValue(iter.next())) {
					return false;
				}
			}
			return true;
		}


		/**
		 * {@inheritDoc}
		 */
		public boolean containsAll(char[] array) {
			for (char element : array) {
				if (!TIntCharHashMap.this.containsValue(element)) {
					return false;
				}
			}
			return true;
		}


		/**
		 * {@inheritDoc}
		 */
		public boolean addAll(Collection<? extends Character> collection) {
			throw new UnsupportedOperationException();
		}


		/**
		 * {@inheritDoc}
		 */
		public boolean addAll(TCharCollection collection) {
			throw new UnsupportedOperationException();
		}


		/**
		 * {@inheritDoc}
		 */
		public boolean addAll(char[] array) {
			throw new UnsupportedOperationException();
		}


		/**
		 * {@inheritDoc}
		 */
		@SuppressWarnings({"SuspiciousMethodCalls"})
		public boolean retainAll(Collection<?> collection) {
			boolean modified = false;
			TCharIterator iter = iterator();
			while (iter.hasNext()) {
				if (!collection.contains(Character.valueOf(iter.next()))) {
					iter.remove();
					modified = true;
				}
			}
			return modified;
		}


		/**
		 * {@inheritDoc}
		 */
		public boolean retainAll(TCharCollection collection) {
			if (this == collection) {
				return false;
			}
			boolean modified = false;
			TCharIterator iter = iterator();
			while (iter.hasNext()) {
				if (!collection.contains(iter.next())) {
					iter.remove();
					modified = true;
				}
			}
			return modified;
		}


		/**
		 * {@inheritDoc}
		 */
		public boolean retainAll(char[] array) {
			boolean changed = false;
			Arrays.sort(array);
			char[] values = _values;
			byte[] states = _states;

			for (int i = values.length; i-- > 0; ) {
				if (states[i] == FULL && (Arrays.binarySearch(array, values[i]) < 0)) {
					removeAt(i);
					changed = true;
				}
			}
			return changed;
		}


		/**
		 * {@inheritDoc}
		 */
		public boolean removeAll(Collection<?> collection) {
			boolean changed = false;
			for (Object element : collection) {
				if (element instanceof Character) {
					char c = ((Character) element).charValue();
					if (remove(c)) {
						changed = true;
					}
				}
			}
			return changed;
		}


		/**
		 * {@inheritDoc}
		 */
		public boolean removeAll(TCharCollection collection) {
			if (this == collection) {
				clear();
				return true;
			}
			boolean changed = false;
			TCharIterator iter = collection.iterator();
			while (iter.hasNext()) {
				char element = iter.next();
				if (remove(element)) {
					changed = true;
				}
			}
			return changed;
		}


		/**
		 * {@inheritDoc}
		 */
		public boolean removeAll(char[] array) {
			boolean changed = false;
			for (int i = array.length; i-- > 0; ) {
				if (remove(array[i])) {
					changed = true;
				}
			}
			return changed;
		}


		/**
		 * {@inheritDoc}
		 */
		public void clear() {
			TIntCharHashMap.this.clear();
		}


		/**
		 * {@inheritDoc}
		 */
		public boolean forEach(TCharProcedure procedure) {
			return TIntCharHashMap.this.forEachValue(procedure);
		}


		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			final StringBuilder buf = new StringBuilder("{");
			forEachValue(new TCharProcedure() {
				private boolean first = true;

				public boolean execute(char value) {
					if (first) {
						first = false;
					} else {
						buf.append(", ");
					}

					buf.append(value);
					return true;
				}
			});
			buf.append("}");
			return buf.toString();
		}
	}

	class TIntCharKeyHashIterator extends THashPrimitiveIterator implements TIntIterator {

		/**
		 * Creates an iterator over the specified map
		 *
		 * @param hash the <tt>TPrimitiveHash</tt> we will be iterating over.
		 */
		TIntCharKeyHashIterator(TPrimitiveHash hash) {
			super(hash);
		}

		/**
		 * {@inheritDoc}
		 */
		public int next() {
			moveToNextIndex();
			return _set[_index];
		}

		/**
		 * @{inheritDoc}
		 */
		public void remove() {
			if (_expectedSize != _hash.size()) {
				throw new ConcurrentModificationException();
			}

			// Disable auto compaction during the remove. This is a workaround for bug 1642768.
			try {
				_hash.tempDisableAutoCompaction();
				TIntCharHashMap.this.removeAt(_index);
			} finally {
				_hash.reenableAutoCompaction(false);
			}

			_expectedSize--;
		}
	}

	class TIntCharValueHashIterator extends THashPrimitiveIterator implements TCharIterator {

		/**
		 * Creates an iterator over the specified map
		 *
		 * @param hash the <tt>TPrimitiveHash</tt> we will be iterating over.
		 */
		TIntCharValueHashIterator(TPrimitiveHash hash) {
			super(hash);
		}

		/**
		 * {@inheritDoc}
		 */
		public char next() {
			moveToNextIndex();
			return _values[_index];
		}

		/**
		 * @{inheritDoc}
		 */
		public void remove() {
			if (_expectedSize != _hash.size()) {
				throw new ConcurrentModificationException();
			}

			// Disable auto compaction during the remove. This is a workaround for bug 1642768.
			try {
				_hash.tempDisableAutoCompaction();
				TIntCharHashMap.this.removeAt(_index);
			} finally {
				_hash.reenableAutoCompaction(false);
			}

			_expectedSize--;
		}
	}

	class TIntCharHashIterator extends THashPrimitiveIterator implements TIntCharIterator {

		/**
		 * Creates an iterator over the specified map
		 *
		 * @param map the <tt>TIntCharHashMap</tt> we will be iterating over.
		 */
		TIntCharHashIterator(TIntCharHashMap map) {
			super(map);
		}

		/**
		 * {@inheritDoc}
		 */
		public void advance() {
			moveToNextIndex();
		}

		/**
		 * {@inheritDoc}
		 */
		public int key() {
			return _set[_index];
		}

		/**
		 * {@inheritDoc}
		 */
		public char value() {
			return _values[_index];
		}

		/**
		 * {@inheritDoc}
		 */
		public char setValue(char val) {
			char old = value();
			_values[_index] = val;
			return old;
		}

		/**
		 * @{inheritDoc}
		 */
		public void remove() {
			if (_expectedSize != _hash.size()) {
				throw new ConcurrentModificationException();
			}
			// Disable auto compaction during the remove. This is a workaround for bug 1642768.
			try {
				_hash.tempDisableAutoCompaction();
				TIntCharHashMap.this.removeAt(_index);
			} finally {
				_hash.reenableAutoCompaction(false);
			}
			_expectedSize--;
		}
	}
} // TIntCharHashMap
