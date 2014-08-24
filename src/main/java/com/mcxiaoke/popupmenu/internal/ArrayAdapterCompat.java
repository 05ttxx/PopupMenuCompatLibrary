/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mcxiaoke.popupmenu.internal;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * A concrete BaseAdapter that is backed by an array of arbitrary
 * objects.  By default this class expects that the provided resource id references
 * a single TextView.  If you want to use a more complex layout, use the constructors that
 * also takes a field id.  That field id should reference a TextView in the larger layout
 * resource.
 * <p/>
 * <p>However the TextView is referenced, it will be filled with the getPrintString() of each object in
 * the array. You can add lists or arrays of custom objects. Override the getPrintString() method
 * of your objects to determine what text will be displayed for the item in the list.
 * <p/>
 * <p>To use something other than TextViews for the array display, for instance, ImageViews,
 * or to have some of data besides getPrintString() results fill the views,
 * override {@link #getView(int, android.view.View, android.view.ViewGroup)} to return the type of view you want.
 */
abstract class ArrayAdapterCompat<T> extends BaseAdapter implements Filterable {


    /**
     * Lock used to modify the content of {@link #mObjects}. Any write operation
     * performed on the array should be synchronized on this lock. This lock is also
     * used by the filter (see {@link #getFilter()} to make a synchronized copy of
     * the original array of data.
     */
    protected final Object mLock = new Object();

    /**
     * Indicates whether or not {@link #notifyDataSetChanged()} must be called whenever
     * {@link #mObjects} is modified.
     */
    private boolean mNotifyOnChange = true;

    private Context mContext;

    /**
     * Contains the list of objects that represent the data of this ArrayAdapter.
     * The content of this list is referred to as "the array" in the documentation.
     */
    protected List<T> mObjects;
    // A copy of the original mObjects array, initialized from and then used instead as soon as
    // the mFilter ArrayFilter is used. mObjects will then only contain the filtered values.
    protected List<T> mOriginalValues;
    protected Filter mFilter;

    /**
     * Constructor
     *
     * @param context The current context.
     */
    public ArrayAdapterCompat(Context context) {
        init(context, new ArrayList<T>());
    }

    /**
     * Constructor
     *
     * @param context The current context.
     * @param objects The objects to represent in the ListView.
     */
    public ArrayAdapterCompat(Context context, T[] objects) {
        init(context, Arrays.asList(objects));
    }

    /**
     * Constructor
     *
     * @param context The current context.
     * @param objects The objects to represent in the ListView.
     */
    public ArrayAdapterCompat(Context context, List<T> objects) {
        init(context, objects);
    }

    /**
     * Adds the specified object at the end of the array.
     *
     * @param object The object to add at the end of the array.
     */
    public void add(T object) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.add(object);
            } else {
                mObjects.add(object);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * Adds the specified object at the index
     *
     * @param object The object to add at the end of the array.
     */
    public void add(int index, T object) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.add(index, object);
            } else {
                mObjects.add(index, object);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * set the specified object at index
     *
     * @param object The object to add at the end of the array.
     */
    public void set(int index, T object) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.set(index, object);
            } else {
                mObjects.set(index, object);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * Adds the specified Collection at the end of the array.
     *
     * @param collection The Collection to add at the end of the array.
     */
    public void addAll(Collection<? extends T> collection) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.addAll(collection);
            } else {
                mObjects.addAll(collection);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * Adds the specified items at the end of the array.
     *
     * @param items The items to add at the end of the array.
     */
    public void addAll(T... items) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                Collections.addAll(mOriginalValues, items);
            } else {
                Collections.addAll(mObjects, items);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * Inserts the specified objects at the specified index in the array.
     *
     * @param collection The objects to insert into the array.
     * @param index      The index at which the object must be inserted.
     */
    public void addAll(int index, Collection<? extends T> collection) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.addAll(index, collection);
            } else {
                mObjects.addAll(index, collection);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * Inserts the specified objects at the specified index in the array.
     *
     * @param items The objects to insert into the array.
     * @param index The index at which the object must be inserted.
     */
    public void addAll(int index, T... items) {
        List<T> collection = Arrays.asList(items);
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.addAll(index, collection);
            } else {
                mObjects.addAll(index, collection);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public List<T> getAllItems() {
        if (mOriginalValues != null) {
            return mOriginalValues;
        } else {
            return mObjects;
        }
    }

    /**
     * Inserts the specified object at the specified index in the array.
     *
     * @param object The object to insert into the array.
     * @param index  The index at which the object must be inserted.
     */
    public void insert(T object, int index) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.add(index, object);
            } else {
                mObjects.add(index, object);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * check contains the object.
     *
     * @param object The object to remove.
     */
    public boolean contains(T object) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                return mOriginalValues.contains(object);
            } else {
                return mObjects.contains(object);
            }
        }
    }

    /**
     * check contains the object.
     *
     * @param object The object to remove.
     */
    public int indexOf(T object) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                return mOriginalValues.indexOf(object);
            } else {
                return mObjects.indexOf(object);
            }
        }
    }

    /**
     * Removes the specified object from the array.
     *
     * @param object The object to remove.
     */
    public void remove(T object) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.remove(object);
            } else {
                mObjects.remove(object);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * Removes the specified object in index from the array.
     *
     * @param index The index to remove.
     */
    public void removeAt(int index) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.remove(index);
            } else {
                mObjects.remove(index);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * Removes the specified objects.
     *
     * @param collection The collection to remove.
     */
    public boolean removeAll(Collection<?> collection) {
        boolean result = false;
        synchronized (mLock) {
            Iterator<?> it;
            if (mOriginalValues != null) {
                it = mOriginalValues.iterator();

            } else {
                it = mObjects.iterator();
            }
            while (it.hasNext()) {
                if (collection.contains(it.next())) {
                    it.remove();
                    result = true;
                }
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
        return result;
    }

    /**
     * Remove all elements from the list.
     */
    public void clear() {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.clear();
            } else {
                mObjects.clear();
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * Sorts the content of this adapter using the specified comparator.
     *
     * @param comparator The comparator used to sort the objects contained
     *                   in this adapter.
     */
    public void sort(Comparator<? super T> comparator) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                Collections.sort(mOriginalValues, comparator);
            } else {
                Collections.sort(mObjects, comparator);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        mNotifyOnChange = true;
    }

    /**
     * Control whether methods that change the list ({@link #add},
     * {@link #insert}, {@link #remove}, {@link #clear}) automatically call
     * {@link #notifyDataSetChanged}.  If set to false, caller must
     * manually call notifyDataSetChanged() to have the changes
     * reflected in the attached view.
     * <p/>
     * The default is true, and calling notifyDataSetChanged()
     * resets the flag to true.
     *
     * @param notifyOnChange if true, modifications to the list will
     *                       automatically call {@link
     *                       #notifyDataSetChanged}
     */
    public void setNotifyOnChange(boolean notifyOnChange) {
        mNotifyOnChange = notifyOnChange;
    }

    private void init(Context context, List<T> objects) {
        mContext = context;
        mObjects = objects;
    }

    /**
     * Returns the context associated with this array adapter. The context is used
     * to create views from the resource passed to the constructor.
     *
     * @return The Context associated with this adapter.
     */
    public Context getContext() {
        return mContext;
    }

    /**
     * {@inheritDoc}
     */
    public int getCount() {
        return mObjects.size();
    }

    /**
     * {@inheritDoc}
     */
    public T getItem(int position) {
        return mObjects.get(position);
    }

    /**
     * Returns the position of the specified item in the array.
     *
     * @param item The item to retrieve the position of.
     * @return The position of the specified item.
     */
    public int getPosition(T item) {
        return mObjects.indexOf(item);
    }

    /**
     * {@inheritDoc}
     */
    public long getItemId(int position) {
        return position;
    }

    /**
     * {@inheritDoc}
     */
    public Filter getFilter() {
        return mFilter;
    }

    public void setFilter(Filter filter) {
        mFilter = filter;
    }
}
