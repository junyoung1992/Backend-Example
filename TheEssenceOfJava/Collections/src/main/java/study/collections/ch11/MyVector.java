package study.collections.ch11;

import java.util.*;

/**
 * Vector 클래스의 실제 코드를 바탕으로 이해하기 쉽게 재구성
 */
public class MyVector implements List {

    private Object[] data = null;   // 객체를 담기 위한 객체 배열을 선언
    private int capacity = 0;       // 용량
    private int size = 0;           // 크기

    public MyVector(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("유효하지 않은 값입니다. :" + capacity);
        }

        this.capacity = capacity;
        data = new Object[capacity];
    }

    /**
     * 크기를 지정하지 않으면 크기를 10으로 한다.
     */
    public MyVector() {
        this(10);
    }

    /**
     * 최소한의 저장공간(capacity)를 확보하는 메서드
     * @param minCapacity 저장공간
     */
    public void ensureCapacity(int minCapacity) {
        if (minCapacity - data.length > 0) {
            setCapacity(minCapacity);
        }
    }

    public void trimToSize() {
        setCapacity(size);
    }

    public void setCapacity(int capacity) {
        if (this.capacity == capacity) {    // 크기가 같으면 변경하지 않는다.
            return;
        }

        Object[] tmp = new Object[capacity];
        System.arraycopy(data, 0, tmp, 0, size);
        data = tmp;
        this.capacity = capacity;
    }

    public int capacity() {
        return capacity;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public Iterator iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        System.arraycopy(data, 0, result, 0, size);
        return result;
    }

    @Override
    public Object[] toArray(Object[] a) {
        return new Object[0];
    }

    @Override
    public boolean add(Object o) {
        // 새로운 객체를 저장하기 전에 저장할 공간을 확보한다.
        ensureCapacity(size + 1);
        data[size++] = o;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (o.equals(data[i])) {
                remove(i);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean containsAll(Collection c) {
        return false;
    }

    @Override
    public boolean addAll(Collection c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection c) {
        return false;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            data[i] = null;
        }

        size = 0;
    }

    @Override
    public Object get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("범위를 벗어났습니다.");
        }

        return data[index];
    }

    @Override
    public Object set(int index, Object element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("범위를 벗어났습니다.");
        }

        Object oldValue = data[index];
        data[index] = element;

        return oldValue;
    }

    @Override
    public void add(int index, Object element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("범위를 벗어났습니다.");
        }

        Object[] tmp = new Object[++capacity];
        if (index != 0) {
            System.arraycopy(data, 0, tmp, 0, index + 1);
        }
        tmp[index] = element;
        System.arraycopy(data, index, tmp, index + 1, size - index - 1);
        data = tmp;
        size++;
    }

    @Override
    public Object remove(int index) {
        Object oldObj = null;

        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("범위를 벗어났습니다.");
        }
        oldObj = data[index];

        // 삭제하고자 하는 객체가 마지막 객체가 아니라면, 배열 복사를 통해 빈자리를 채워야 한다.
        if (index != size - 1) {
            System.arraycopy(data, index + 1, data, index, size - index - 1);
        }

        // 마지막 데이터를 null로 한다. 배열은 0부터 시작하므로 마지막 요소는 index가 size - 1이다.
        data[size - 1] = null;
        size--;

        return oldObj;
    }

    @Override
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (data[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(data[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        if (o == null) {
            for (int i = size - 1; i >= 0; i--) {
                if (data[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = size - 1; i >= 0; i--) {
                if (o.equals(data[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public ListIterator listIterator() {
        return null;
    }

    @Override
    public ListIterator listIterator(int index) {
        return null;
    }

    @Override
    public List subList(int fromIndex, int toIndex) {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyVector myVector = (MyVector) o;
        return capacity == myVector.capacity && size == myVector.size && Arrays.equals(data, myVector.data);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(capacity, size);
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }

}
