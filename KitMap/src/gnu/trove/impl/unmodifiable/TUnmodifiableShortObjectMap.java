///////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2008, Robert D. Eden All Rights Reserved.
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

package gnu.trove.impl.unmodifiable;


//////////////////////////////////////////////////
// THIS IS A GENERATED CLASS. DO NOT HAND EDIT! //
//////////////////////////////////////////////////

////////////////////////////////////////////////////////////
// THIS IS AN IMPLEMENTATION CLASS. DO NOT USE DIRECTLY!  //
// Access to these methods should be through TCollections //
////////////////////////////////////////////////////////////


import gnu.trove.TCollections;
import gnu.trove.function.TObjectFunction;
import gnu.trove.iterator.TShortObjectIterator;
import gnu.trove.map.TShortObjectMap;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.procedure.TShortObjectProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TShortSet;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;


public class TUnmodifiableShortObjectMap<V> implements TShortObjectMap<V>, Serializable {
	private static final long serialVersionUID = -1034234728574286014L;

	private final TShortObjectMap<V> m;
	private transient TShortSet keySet = null;
	private transient Collection<V> values = null;

	public TUnmodifiableShortObjectMap(TShortObjectMap<V> m) {
		if (m == null) throw new NullPointerException();
		this.m = m;
	}

	public int size() {
		return m.size();
	}

	public boolean isEmpty() {
		return m.isEmpty();
	}

	public boolean containsKey(short key) {
		return m.containsKey(key);
	}

	public boolean containsValue(Object val) {
		return m.containsValue(val);
	}

	public V get(short key) {
		return m.get(key);
	}

	public V put(short key, V value) {
		throw new UnsupportedOperationException();
	}

	public V remove(short key) {
		throw new UnsupportedOperationException();
	}

	public void putAll(TShortObjectMap<? extends V> m) {
		throw new UnsupportedOperationException();
	}

	public void putAll(Map<? extends Short, ? extends V> map) {
		throw new UnsupportedOperationException();
	}

	public void clear() {
		throw new UnsupportedOperationException();
	}

	public TShortSet keySet() {
		if (keySet == null) keySet = TCollections.unmodifiableSet(m.keySet());
		return keySet;
	}

	public short[] keys() {
		return m.keys();
	}

	public short[] keys(short[] array) {
		return m.keys(array);
	}

	public Collection<V> valueCollection() {
		if (values == null) values = Collections.unmodifiableCollection(m.valueCollection());
		return values;
	}

	public Object[] values() {
		return m.values();
	}

	public V[] values(V[] array) {
		return m.values(array);
	}

	public boolean equals(Object o) {
		return o == this || m.equals(o);
	}

	public int hashCode() {
		return m.hashCode();
	}

	public String toString() {
		return m.toString();
	}

	public short getNoEntryKey() {
		return m.getNoEntryKey();
	}

	public boolean forEachKey(TShortProcedure procedure) {
		return m.forEachKey(procedure);
	}

	public boolean forEachValue(TObjectProcedure<? super V> procedure) {
		return m.forEachValue(procedure);
	}

	public boolean forEachEntry(TShortObjectProcedure<? super V> procedure) {
		return m.forEachEntry(procedure);
	}

	public TShortObjectIterator<V> iterator() {
		return new TShortObjectIterator<V>() {
			TShortObjectIterator<V> iter = m.iterator();

			public short key() {
				return iter.key();
			}

			public V value() {
				return iter.value();
			}

			public void advance() {
				iter.advance();
			}

			public boolean hasNext() {
				return iter.hasNext();
			}

			public V setValue(V val) {
				throw new UnsupportedOperationException();
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	public V putIfAbsent(short key, V value) {
		throw new UnsupportedOperationException();
	}

	public void transformValues(TObjectFunction<V, V> function) {
		throw new UnsupportedOperationException();
	}

	public boolean retainEntries(TShortObjectProcedure<? super V> procedure) {
		throw new UnsupportedOperationException();
	}
}