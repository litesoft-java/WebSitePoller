package org.litesoft.websitepoller.gui;

import org.litesoft.commonfoundation.typeutils.*;

import java.util.*;

public class AddOnlyWindowedChunkedList<E> extends AbstractList<E> {
    private static final int MINIMUM_MINIMUM_TO_KEEP_WHEN_FULL = 4096;
    private static final int MAX_OLD_LISTS = 16;
    private static final int INITIAL_CHUNK_SIZE = MINIMUM_MINIMUM_TO_KEEP_WHEN_FULL / MAX_OLD_LISTS;

    private final int mChunkSize, mChunkMask;
    private final List<List<E>> mOlderLists = Lists.newArrayList();
    private List<E> mCurrentList = Lists.newArrayList();

    public AddOnlyWindowedChunkedList( int pMinimumToKeepWhenFull ) {
        mChunkSize = calcChunkSize( Math.max( pMinimumToKeepWhenFull, MINIMUM_MINIMUM_TO_KEEP_WHEN_FULL ) );
        mChunkMask = mChunkSize - 1;
    }

    @SuppressWarnings("Convert2Diamond")
    public static <T> List<T> create( int pMinimumToKeepWhenFull ) {
        return new AddOnlyWindowedChunkedList<T>( pMinimumToKeepWhenFull );
    }

    private int calcChunkSize( int pMinimumToKeepWhenFull ) {
        int zChunkSize = INITIAL_CHUNK_SIZE;
        for ( int zSize = MINIMUM_MINIMUM_TO_KEEP_WHEN_FULL; zSize < pMinimumToKeepWhenFull; zSize += zSize ) {
            zChunkSize += zChunkSize;
        }
        return zChunkSize;
    }

    @Override
    public void clear() {
        mOlderLists.clear();
        mCurrentList.clear();
    }

    @Override
    public int size() {
        return (mOlderLists.size() * mChunkSize) + mCurrentList.size();
    }

    @Override
    public E get( int index ) {
        if ( (index < 0) || (size() <= index) ) {
            throw new IndexOutOfBoundsException( "NOT (0 <= " + index + " < " + size() + ")" );
        }
        int zListOffset = index & mChunkMask;
        int zList = index / mChunkSize; // Could just do a shift right, but too much work right now!
        return getList( zList ).get( zListOffset );
    }

    private List<E> getList( int pList ) {
        return (pList < mOlderLists.size()) ? mOlderLists.get( pList ) : mCurrentList;
    }

    @Override
    public boolean add( E e ) {
        return growList().add( e );
    }

    private List<E> growList() {
        return (mCurrentList.size() == mChunkSize) ? rollInCurrentList() : mCurrentList;
    }

    private List<E> rollInCurrentList() {
        mOlderLists.add( mCurrentList );
        if ( mOlderLists.size() > MAX_OLD_LISTS ) { // Too Many?  then Drop Oldest
            mOlderLists.remove( 0 );
        }
        return mCurrentList = Lists.newArrayList();
    }
}
