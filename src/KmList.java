import java.lang.reflect.Array;
import java.util.*;

/**
 * 양방향(이중) 연결 리스트
 */
public class KmList<E> implements List<E> {
    private class Node<E> {
        E data;
        Node<E> prev;
        Node<E> next;

        public Node(E data) {
            this.data = data;
            this.prev = null;
            this.next = null;
        }

        public Node(E data, Node<E> prev, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node<E> first;
    private Node<E> end;
    private int size;

    public KmList() {
        this.first = null;
        this.end = null;
        this.size = 0;
    }

    /**
     * 해당 인덱스 노드 검색
     */
    private Node<E> search(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        Node<E> target;

        // 찾고자 하는 index가 사이즈 절반보다 크면 뒤에서부터 탐색
        if (index > size / 2) {
            target = end;
            for (int i = size - 1; i > index; i--) {
                target = target.prev;
            }
        } else {
            target = first;
            for (int i = 0; i < index; i++) {
                target = target.next;
            }
        }
        return target;
    }

    /**
     * 리스트 사이즈 반환
     */
    @Override
    public int size() {
        return this.size;
    }

    /**
     * 빈 리스트 유/무 반환
     *
     * @return 비었으면 true
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * 한 데이터가 리스트의 존재 여부
     *
     * @return 데이터가 리스트 내에 존재하면 true
     */
    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    /**
     * 한 데이터가 리스트 내에 존재하면 그 index 반환
     *
     * @return 데이터의 index, 없으면 -1
     */
    @Override
    public int indexOf(Object o) {
        int index = 0;

        // 노드 data가 같은 객체를 찾을 때 까지 loop
        for (Node<E> target = first; target != null; target = target.next) {
            if (Objects.equals(o, target.data)) {
                return index;
            }
            index++;
        }

        return -1;
    }

    /**
     * 리스트를 Array로 바꿈
     *
     * @return copied Array
     */
    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        int index = 0;

        for (Node<E> target = first; target != null; target = target.next) {
            array[index] = target.data;
            index++;
        }
        return array;
    }

    /**
     * 리스트를 Array로 바꿈
     *
     * @return copied Array
     */
    @Override
    public <T> T[] toArray(T[] a) {
        // 배열에 목록보다 많은 요소가 있는 경우
        if (a.length > size) {
            a[size] = null;
        }
        // List가 param 배열보다 큰 경우
        if (size > a.length) {
            // 원하는 Class로 배열 생성
            a = (T[]) Array.newInstance(a.getClass().getComponentType(), size);
        }
        int index = 0;
        // 얕은 복사 (주소 값까지 동일)
        Object[] result = a;

        for (Node<E> target = first; target != null; target = target.next) {
            result[index] = target.data;
            index++;
        }
        return a;
    }

    /**
     * 첫 인덱스에 삽입
     */
    private void addFirst(E element) {
        Node<E> newNode = new Node<>(element);
        // newNode의 next 링크에 first 노드 저장
        newNode.next = first;

        // first 노드가 null 이 아니면 prev 링크에 newNode 저장
        if (first != null) {
            first.prev = newNode;
        }

        // first 노드로 newNode 임명
        first = newNode;
        size++;

        // first의 next 링크가 null이면 first == end (size == 1)
        if (first.next == null) {
            end = first;
        }
    }

    /**
     * 리스트 내 마지막 인덱스에 요소 추가
     *
     * @return 요소가 정상적으로 추가되면 true
     */
    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    /**
     * 마지막 인덱스에 삽입
     */
    private void addLast(E element) {
        Node<E> newNode = new Node<>(element);

        if (size == 0) {
            addFirst(element);
            return;
        }

        // 링크 초기화 & end 노드 초기화
        end.next = newNode;
        newNode.prev = end;
        end = newNode;

        size++;
    }

    /**
     * 리스트 내 해당하는 요소 삭제
     *
     * @return 요소가 정상적으로 삭제되면 true
     */
    @Override
    public boolean remove(Object o) {
        Node<E> prevNode = first;
        Node<E> removeNode = first;

        while (removeNode != null) {
            // 같은 객체 찾기
            if (Objects.equals(o, removeNode.data)) {
                break;
            }
            prevNode = removeNode;
            removeNode = removeNode.next;
        }

        // 일치 객체 없음
        if (removeNode == null) {
            return false;
        }

        if (Objects.equals(removeNode, first)) {
            remove();
        } else {
            Node<E> nextNode = removeNode.next;

            // 링크 초기화 & 타겟 노드 삭제
            prevNode.next = null;
            removeNode.data = null;
            removeNode.next = removeNode.prev = null;

            // 제거 노드 다음 노드가 있다면
            if (nextNode != null) {
                // 링크 초기화
                nextNode.prev = prevNode;
                prevNode.next = nextNode;
            } else {
                end = prevNode;
            }
            size--;
        }
        return true;
    }

    /**
     * 리스트 내 요소 모두 제거
     */
    @Override
    public void clear() {
        // first 노드부터 next, next ...
        // node 값이 null 나올 때까지(모든 노드를 제거하고 마지막 노드를 넘을 때 까지) null로 초기화
        for (Node<E> target = first; target != null; ) {
            // 다음 노드 임의 저장 (다음 노드로 포인트 이동용)
            Node<E> nextNode = target.next;

            // null로 초기화
            target.data = null;
            target.prev = target.next = null;

            // 이동
            target = nextNode;
        }
        first = end = null;
        size = 0;
    }

    /**
     * 리스트 내 해당 인덱스의 데이터 조회
     *
     * @return 해당 인덱스의 데이터
     */
    @Override
    public E get(int index) {
        return search(index).data;
    }

    /**
     * 리스트 내 해당 인덱스의 데이터 변경
     *
     * @return 변경 전 요소
     */
    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        Node<E> replaceNode = search(index);

        // 반환할 원래 요소
        E originalData = replaceNode.data;

        // 값 교체
        replaceNode.data = element;

        return originalData;
    }

    /**
     * 리스트 내 해당 인덱스에 요소 추가
     */
    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }

        // add
        if (index == 0) {
            addFirst(element);
            return;
        } else if (index == size) {
            addLast(element);
            return;
        }

        // 링크 초기화
        Node<E> prevNode = search(index - 1);
        Node<E> newNode = new Node<>(element);
        Node<E> nextNode = prevNode.next;

        // 끊기
        nextNode.prev = null;

        // 연결
        prevNode.next = newNode;
        newNode.prev = prevNode;
        newNode.next = nextNode;
        nextNode.prev = newNode;

        size++;
    }

    /**
     * 첫 노드 제거 (remove first)
     *
     * @return 삭제된 데이터
     */
    private E remove() {
        Node<E> firstNode = first;

        // node 존재 x
        if (firstNode == null) {
            throw new NoSuchElementException();
        }

        // 반환(return) 데이터 (삭제될 노드)
        E element = firstNode.data;

        Node<E> nextNode = firstNode.next;

        // 노드 삭제
        first.data = null;
        first.next = null;

        // first 다음 노드가 있으면 다음 노드의 prev = null
        if (nextNode != null) {
            nextNode.prev = null;
        }

        // first 노드 초기화
        first = nextNode;

        size--;

        if (size == 0) {
            // 1개 남은 노드가 삭제된 경우 end = 0
            end = null;
        }

        return element;
    }

    /**
     * 해당 인덱스 요소 삭제
     *
     * @return 삭제된 데이터
     */
    @Override
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        // first 인덱스 삭제
        if (index == 0) {
            return remove();
        }

        // search로 해당 인덱스 전 노드 가져옴
        Node<E> prevNode = search(index - 1);
        // 삭제할 노드
        Node<E> removeNode = prevNode.next;
        // 삭제할 노드의 다음 노드
        Node<E> nextNode = removeNode.next;

        // 반환할 노드
        E element = removeNode.data;

        // 링크 초기화
        prevNode.next = null;
//        removeNode.data = null;
        removeNode.prev = removeNode.next = null;

        // 삭제한 다음 노드가 존재하는 경우 링크 연결
        if (nextNode != null) {
            nextNode.prev = prevNode;
            prevNode.next = nextNode;
        } else {
            end = prevNode;
        }

        size--;

        return element;
    }

    /**
     * 해당 데이터가 나타나는 인덱스 반환 (끝에서부터 탐색)
     *
     * @return 해당 데이터의 인덱스 (끝에서부터 탐색), 없으면 -1
     */
    @Override
    public int lastIndexOf(Object o) {
        int index = this.size;

        for (Node<E> target = end; target != null; target = target.prev) {
            index--;
            if (Objects.equals(o, target.data)) {
                return index;
            }
        }

        return -1;
    }

    /**
     * 해당 Collection 객체의 데이터를 본 리스트 마지막 인덱스부터 모두 추가
     *
     * @return 정상 추가 시 true
     */
    @Override
    public boolean addAll(Collection<? extends E> c) {
        return addAll(size, c);
    }

    /**
     * 해당 Collection 객체의 데이터를 본 리스트의 해당 인덱스부터 모두 추가
     *
     * @return 정상 추가 시 true
     */
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }

        Object[] newListArray = c.toArray();
        if (newListArray.length == 0)
            return false;

        // 배열의 데이터를 각각 노드 데이터로 만듬
        for (Object element : newListArray) {
            add(index, (E) element);
            index++;
        }

        return true;
    }

    /**
     * 해당 Collection 객체의 데이터를 본 리스트에서 제거
     *
     * @return 제거로 인해 리스트의 변화가 생기면 true
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        if (c.isEmpty())
            return false;
        Object[] paramListArray = c.toArray();

        boolean change = false;
        Node<E> currentNode;

        // 배열의 데이터를 각각 노드로 만듬
        for (int i = 0; i < paramListArray.length; i++) {
            E element = (E) paramListArray[i];

            // element 와 동일한 data의 노드가 있으면
            if (contains(element)) {
                // 현재 노드를 찾아 제거
                currentNode = search(indexOf(element));
                remove(currentNode.data);
                change = true;
                i = -1;
            }
        }

        return change;
    }

    /**
     * 해당 Collection 객체의 데이터가 본 리스트에 모두 포함돼있는지 확인
     *
     * @return 모든 요소가 포함돼있으면 true
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        Object[] newListArray = c.toArray();
        int collectionLength = newListArray.length;
        if (this.size == 0 && c.size() == 0)
            return true;
        else if (c.isEmpty())
            return false;

        int count = 0;

        // 배열의 데이터를 각각 노드 데이터로 만듬
        for (Object newElement : newListArray) {
            E element = (E) newElement;

            // element 와 동일한 data의 노드가 있으면
            if (contains(element)) {
                count++;
            }
        }
        return collectionLength == count;
    }

    /**
     * collection 에 포함된 데이터만 리스트에서 남긴다.
     *
     * @return 리스트 내 삭제된 요소가 있으면 true
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        if (c.isEmpty()) {
            if (this.size == 0) {
                return false;
            } else {
                clear();
                return true;
            }
        }

        Object[] elements = this.toArray();
        boolean change = false;
        int writeIndex = 0;

        for (int readIndex = 0; readIndex < size; readIndex++) {
            if (c.contains(elements[readIndex])) {
                elements[writeIndex++] = elements[readIndex];
            }
        }

        if (writeIndex > 0) {
            clear();
            change = true;

            for (int i = 0; i < writeIndex; i++) {
                add((E) elements[i]);
            }
        }
        return change;
    }

    /**
     * 본 리스트의 iterator 객체를 생성
     *
     * @return 본 리스트 iterator 객체
     */
    @Override
    public Iterator<E> iterator() {
        return listIterator(0);
    }

    /**
     * 본 리스트의 listIterator 객체를 생성
     *
     * @return 본 리스트 listIterator 객체
     */
    @Override
    public ListIterator<E> listIterator() {
        return listIterator(0);
    }

    /**
     * 본 리스트의 listIterator 객체를 생성
     *
     * @return 본 리스트 listIterator 객체
     */
    @Override
    public ListIterator<E> listIterator(int index) {
        return new ListIterator<E>() {
            /**
             * 포인트 다음 요소
             * */
            private Node<E> next = (index == size) ? null : search(index);
            /**
             * next, prev 등에서 반환할 노드 (또는 이전에 반환했던 노드)
             * */
            private Node<E> lastReturned;
            /**
             * 포인트 (인덱스)
             * */
            private int nextIndex = index;

            /**
             * 다음 노드의 존재 유무
             *
             * @return 다음 요소가 존재하면 true
             * */
            @Override
            public boolean hasNext() {
                return nextIndex < size();
            }

            /**
             * 포인트를 옮기기 전의 현재 요소를 반환
             * 다음 노드로 포인트를 옮김
             *
             * @return 포인트를 옮기기 전의 요소 반환
             * */
            @Override
            public E next() {
                if (!hasNext())
                    throw new NoSuchElementException();

                lastReturned = next;
                next = next.next;
                nextIndex++;
                return lastReturned.data;
            }

            /**
             * 이전 노드의 존재 유무
             *
             * @return 이전 요소가 존재하면 true
             * */
            @Override
            public boolean hasPrevious() {
                return nextIndex > 0;
            }

            /**
             * 리스트의 이전 요소를 반환
             * 이전 노드로 포인트를 옮김
             *
             * @return 이전 요소
             * */
            @Override
            public E previous() {
                if (!hasPrevious())
                    throw new NoSuchElementException();

                lastReturned = next = (next == null) ? end : next.prev;
                nextIndex--;
                return lastReturned.data;
            }

            /**
             * @return next 함수에서 반환 될 요소의 인덱스 (와 동일)
             * */
            @Override
            public int nextIndex() {
                return nextIndex;
            }

            /**
             * @return previous 함수에서 반환 될 요소의 인덱스 (와 동일) <br /> -1: 포인트가 시작 부분에 있는 경우
             * */
            @Override
            public int previousIndex() {
                return nextIndex - 1;
            }

            /**
             * 현재 표인트 요소 다음 요소를 삭제
             * */
            @Override
            public void remove() {
                if (lastReturned == null)
                    throw new IllegalStateException();

                Node<E> lastNext = lastReturned.next;
                KmList.this.remove(lastReturned.data);
                if (next == lastReturned)
                    next = lastNext;
                else
                    nextIndex--;
                lastReturned = null;
            }

            /**
             * 현재 포인트 요소를 해당 요소로 바꿈
             * */
            @Override
            public void set(E e) {
                if (lastReturned == null)
                    throw new IllegalStateException();
                lastReturned.data = e;
            }

            /**
             * sub 리스트의 마지막 부분에 데이터 추가
             * */
            @Override
            public void add(E e) {
                lastReturned = null;
                if (next == null)
                    addLast(e);
                else
                    KmList.this.add(nextIndex, e);
                nextIndex++;
            }
        };
    }

    /**
     * 본 리스트를 참조하는 subList 객체 생성 / 반환
     *
     * @return 본 리스트를 참조하는 List
     */
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return new SubList(this, fromIndex, toIndex);
    }

    public class SubList extends KmList<E> {
        private final KmList<E> parent;
        private int fromIndex;
        private int toIndex;
        private int size;

        public SubList(KmList<E> parent, int startPosition, int endPosition) {
            // 빈 리스트를 참조한 경우
            if (parent.size() == 0) {
                startPosition = 0;
                endPosition = 0;
            } else if (endPosition > parent.size()) {
                throw new IndexOutOfBoundsException();
            }
            this.parent = parent;
            this.fromIndex = startPosition;
            this.toIndex = endPosition;
            this.size = toIndex - fromIndex;
        }

        @Override
        public int size() {
            return this.size;
        }

        @Override
        public boolean isEmpty() {
            return this.size == 0;
        }

        @Override
        public boolean add(E element) {
            this.add(fromIndex + size, element);
            return true;
        }

        @Override
        public void add(int index, E element) {
            parent.add(index, element);
            this.size++;
            this.toIndex++;
        }

        @Override
        public boolean contains(Object o) {
            return this.indexOf(o) >= 0;
        }

        @Override
        public int indexOf(Object o) {
            int index = 0;

            // 노드 data가 같은 객체를 찾을 때 까지 loop
            for (Node<E> target = search(fromIndex); index != toIndex - fromIndex; target = target.next) {
                if (Objects.equals(o, target.data)) {
                    return index;
                }
                index++;
            }

            return -1;
        }

        @Override
        public void clear() {
            if (this.size == parent.size) {
                parent.clear();
                this.size = 0;
                this.fromIndex = 0;
                this.toIndex = 0;
            } else {
                while (this.size > 0) {
                    parent.remove(fromIndex);
                    this.size--;
                }
                this.toIndex = fromIndex;
            }
        }

        @Override
        public E get(int index) {
            return parent.get(index + fromIndex);
        }

        @Override
        public E set(int index, E e) {
            return parent.set(index + fromIndex, e);
        }

        @Override
        public E remove(int index) {
            E result = parent.remove(index + fromIndex);
            this.size--;
            this.toIndex--;
            return result;
        }

        @Override
        public boolean remove(Object o) {
            boolean result = parent.remove(o);
            if (result)
                this.size--;
            this.toIndex--;
            return result;
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            if (c.isEmpty())
                return false;

            Object[] removeArray = c.toArray();
            boolean change = false;

            // sublist 사이즈 만큼 loop
            for (int i = 0; i < this.size; i++) {
                // param 사이즈 만큼 loop
                for (Object o : removeArray) {
                    if (Objects.equals(this.get(i), o)) {
                        parent.remove(o);
                        this.size--;
                        this.toIndex--;
                        change = true;
                        i = -1;
                        break;
                    }
                }
            }
            return change;
        }

        @Override
        public Object[] toArray() {
            Object[] array = new Object[this.size];
            int index = 0;

            for (Node<E> target = search(fromIndex); index != toIndex - fromIndex; target = target.next) {
                array[index++] = target.data;
            }
            return array;
        }

        @Override
        public int lastIndexOf(Object o) {
            int index = toIndex;

            for (Node<E> target = search(toIndex - 1); index != 0; target = target.prev) {
                index--;
                if (Objects.equals(o, target.data)) {
                    return index;
                }
            }

            return -1;
        }

        @Override
        public boolean addAll(int index, Collection<? extends E> c) {
            int cSize = c.size();
            if (cSize == 0)
                return false;

            parent.addAll(fromIndex + index, c);
            this.size += cSize;
            toIndex += cSize;

            return true;
        }

        @Override
        public boolean addAll(Collection<? extends E> c) {
            return this.addAll(this.size, c);
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            Object[] newListArray = c.toArray();
            int collectionLength = newListArray.length;
            if (this.size == 0 && c.size() == 0)
                return true;
            else if (c.isEmpty())
                return false;

            int count = 0;

            // 배열의 데이터를 각각 노드 데이터로 만듬
            for (Object newElement : newListArray) {
                E element = (E) newElement;

                // element 와 동일한 data의 노드가 있으면
                if (this.contains(element)) {
                    count++;
                }
            }
            return collectionLength == count;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            if (c.isEmpty()) {
                if (this.size == 0) {
                    return false;
                } else {
                    this.clear();
                    return true;
                }
            }

            Object[] elements = this.toArray();
            boolean change = false;
            int writeIndex = 0;

            for (int readIndex = 0; readIndex < size; readIndex++) {
                if (c.contains(elements[readIndex])) {
                    elements[writeIndex++] = elements[readIndex];
                }
            }

            if (writeIndex > 0) {
                this.clear();
                change = true;

                for (int i = 0; i < writeIndex; i++) {
                    this.add((E) elements[i]);
                }
            }
            return change;
        }

        /******************************************* 구현 중 **********************************************************/

        // 미테스트
        @Override
        public <T> T[] toArray(T[] a) {
            // 배열에 목록보다 많은 요소가 있는 경우
            if (a.length > this.size) {
                a[this.size] = null;
            }
            // List가 param 배열보다 큰 경우
            if (this.size > a.length) {
                // 원하는 Class로 배열 생성
                a = (T[]) Array.newInstance(a.getClass().getComponentType(), this.size);
            }
            int index = 0;
            // 얕은 복사 (주소 값까지 동일)
            Object[] result = a;

            for (Node<E> target = first; target != null; target = target.next) {
                result[index] = target.data;
                index++;
            }
            return a;
        }
    }
}