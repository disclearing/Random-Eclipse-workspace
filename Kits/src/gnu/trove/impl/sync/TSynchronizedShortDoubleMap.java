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

package gnu.trove.impl.sync;


//////////////////////////////////////////////////
// THIS IS A GENERATED CLASS. DO NOT HAND EDIT! //
//////////////////////////////////////////////////

////////////////////////////////////////////////////////////
// THIS IS AN IMPLEMENTATION CLASS. DO NOT USE DIRECTLY!  //
// Access to these methods should be through TCollections //
////////////////////////////////////////////////////////////


import gnu.trove.TDoubleCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.iterator.TShortDoubleIterator;
import gnu.trove.map.TShortDoubleMap;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TShortDoubleProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TShortSet;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;


public class TSynchronizedShortDoubleMap implements TShortDoubleMap, Serializable {
	private static final long serialVersionUID = 1978198479659022715L;
	final Object mutex;    // Object on which to synchronize
	private final TShortDoubleMap m;     // Backing Map
	private transient TShortSet keySet = null;
	private transient TDoubleCollection values = null;

	public TSynchronizedShortDoubleMap(TShortDoubleMap m) {
		if (m == null) throw new NullPointerException();
		this.m = m;
		mutex = this;
	}

	public TSynchronizedShortDoubleMap(TShortDoubleMap m, Object mutex) {
		this.m = m;
		this.mutex = mutex;
	}

	public int size() {
		synchronized (mutex) {
			return m.size();
		}
	}

	public boolean isEmpty() {
		synchronized (mutex) {
			return m.isEmpty();
		}
	}

	public boolean containsKey(short key) {
		synchronized (mutex) {
			return m.containsKey(key);
		}
	}

	public boolean containsValue(double value) {
		synchronized (mutex) {
			return m.containsValue(value);
		}
	}

	public double get(short key) {
		synchronized (mutex) {
			return m.get(key);
		}
	}

	public double put(short key, double value) {
		synchronized (mutex) {
			return m.put(key, value);
		}
	}

	public double remove(short key) {
		synchronized (mutex) {
			return m.remove(key);
		}
	}

	public void putAll(Map<? extends Short, ? extends Double> map) {
		synchronized (mutex) {
			m.putAll(map);
		}
	}

	public void putAll(TShortDoubleMap map) {
		synchronized (mutex) {
			m.putAll(map);
		}
	}

	public void clear() {
		synchronized (mutex) {
			m.clear();
		}
	}

	public TShortSet keySet() {
		synchronized (mutex) {
			if (keySet == null) keySet = new TSynchronizedShortSet(m.keySet(), mutex);
			return keySet;
		}
	}

	public short[] keys() {
		synchronized (mutex) {
			return m.keys();
		}
	}

	public short[] keys(short[] array) {
		synchronized (mutex) {
			return m.keys(array);
		}
	}

	public TDoubleCollection valueCollection() {
		synchronized (mutex) {
			if (values == null) values = new TSynchronizedDoubleCollection(m.valueCollection(), mutex);
			return values;
		}
	}

	public double[] values() {
		synchronized (mutex) {
			return m.values();
		}
	}

	public double[] values(double[] array) {
		synchronized (mutex) {
			return m.values(array);
		}
	}

	public TShortDoubleIterator iterator() {
		return m.iterator(); // Must be manually synched by user!
	}

	// these are unchanging over the life of the map, no need to lock
	public short getNoEntryKey() {
		return m.getNoEntryKey();
	}

	public double getNoEntryValue() {
		return m.getNoEntryValue();
	}

	public double putIfAbsent(short key, double value) {
		synchronized (mutex) {
			return m.putIfAbsent(key, value);
		}
	}

	public boolean forEachKey(TShortProcedure procedure) {
		synchronized (mutex) {
			return m.forEachKey(procedure);
		}
	}

	public boolean forEachValue(TDoubleProcedure procedure) {
		synchronized (mutex) {
			return m.forEachValue(procedure);
		}
	}

	public boolean forEachEntry(TShortDoubleProcedure procedure) {
		synchronized (mutex) {
			return m.forEachEntry(procedure);
		}
	}

	public void transformValues(TDoubleFunction function) {
		synchronized (mutex) {
			m.transformValues(function);
		}
	}

	public boolean retainEntries(TShortDoubleProcedure procedure) {
		synchronized (mutex) {
			return m.retainEntries(procedure);
		}
	}

	public boolean increment(short key) {
		synchronized (mutex) {
			return m.increment(key);
		}
	}

	public boolean adjustValue(short key, double amount) {
		synchronized (mutex) {
			return m.adjustValue(key, amount);
		}
	}

	public double adjustOrPutValue(short key, double adjust_amount, double put_amount) {
		synchronized (mutex) {
			return m.adjustOrPutValue(key, adjust_amount, put_amount);
		}
	}

	public boolean equals(Object o) {
		synchronized (mutex) {
			return m.equals(o);
		}
	}

	public int hashCode() {
		synchronized (mutex) {
			return m.hashCode();
		}
	}

	public String toString() {
		synchronized (mutex) {
			return m.toString();
		}
	}

	private void writeObject(ObjectOutputStream s) throws IOException {
		synchronized (mutex) {
			s.defaultWriteObject();
		}
	}
}
