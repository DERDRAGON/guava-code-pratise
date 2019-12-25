package com.der.codepratise.CollectionsUtil;

import com.der.codepratise.entity.MapTestEntity;
import com.google.common.collect.*;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @program: guava-code-pratise
 * @description: ${description}
 * @author: long
 * @create: 2019-12-25 09:42
 */
public class CollectionHelpersExplained {

    private static final Ordering natural = Ordering.natural();

    private static final Ordering reverse = natural.reverse();

    private static final List<MapTestEntity> list = org.assertj.core.util.Lists.newArrayList(new MapTestEntity(6, "six"),
            new MapTestEntity(7, "seven"), new MapTestEntity(8, "eight"), new MapTestEntity(9, "nine"),
            new MapTestEntity(10, "ten"), new MapTestEntity(11, "eleven"), new MapTestEntity(12, "twelve"),
            new MapTestEntity(13, "thirteen"), new MapTestEntity(14, "fourteen"), new MapTestEntity(15, "fitteen"));

    @Slf4j
    static class AddLoggingList<E> extends ForwardingList<E> {

        private List<E> delegate;

        @Override
        protected List<E> delegate() {
            if (delegate == null) {
                delegate = new ArrayList<E>();
            }
            return delegate;
        }

        @Override
        public boolean add(E element) {
            log.info("add ele {}", element);
            return super.add(element);
        }

        @Override
        public void add(int index, E element) {
            log.info("add index {} with {}", index, element);
            super.add(index, element);
        }

        @Override
        public boolean addAll(int index, Collection<? extends E> elements) {
            return super.addAll(index, elements);
        }

        @Override
        public E get(int index) {
            return super.get(index);
        }
    }

    public static void main(String[] args) {
        testForwarding();
        testPeekingIterator();
        testAbstractIterator();
        testAbstractSequentialIterator();
    }

    private static void testAbstractSequentialIterator() {
        AbstractSequentialIterator<Integer> abstractSequentialIterator = new AbstractSequentialIterator<Integer>(1) {
            @Nullable
            @Override
            protected Integer computeNext(Integer previous) {
                return previous == 30? null:(previous += 1);
            }
        };
    }

    private static void testAbstractIterator() {
        skipNulls(list.stream().map(mapTestEntity -> mapTestEntity.getName()).iterator());
    }

    private static Iterator<String> skipNulls(final Iterator<String> in) {
        return new AbstractIterator<String>() {
            protected String computeNext() {
                while (in.hasNext()) {
                    String s = in.next();
                    if (s != null) {
                        return s;
                    }
                }
                return endOfData();
            }
        };
    }

    private static void testPeekingIterator() {
        ArrayList<MapTestEntity> mapTestEntities = Lists.<MapTestEntity>newArrayList();
        PeekingIterator<MapTestEntity> peekingIterator = Iterators.peekingIterator(list.iterator());
        while (peekingIterator.hasNext()) {
            MapTestEntity next = peekingIterator.next();
            while (peekingIterator.hasNext() && peekingIterator.peek().equals(next)) {
                peekingIterator.next();
            }
            mapTestEntities.add(next);
        }
    }

    private static void testForwarding() {
        AddLoggingList addLoggingList = new AddLoggingList();
        addLoggingList.add(333);
        Assert.assertTrue(Integer.valueOf(333).equals(addLoggingList.get(0)));
        /**
         * 其他抽象实现
         * ForwardingCollection
         * ForwardingConcurrentMap
         * ForwardingDeque
         * ForwardingIterator
         * ForwardingList
         * ForwardingListIterator
         * ForwardingListMultimap
         * ForwardingMap
         * ForwardingMapEntry
         * ForwardingMultimap
         * ForwardingMultiset
         * ForwardingNavigableMap
         * ForwardingNavigableSet
         * ForwardingObject
         * ForwardingQueue
         * ForwardingSet
         * ForwardingSetMultimap
         * ForwardingSortedMap
         * ForwardingSortedMultiset
         * ForwardingSortedSet
         * ForwardingSortedSetMultimap
         * ForwardingTable
         */
    }
}
