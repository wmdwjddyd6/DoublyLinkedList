import java.util.*;

public class Main {

    public static void main(String[] args) {
        testInteger();
    }

    public static void testInteger() {
        List<Integer> list = new KmList<>();
        assert list.isEmpty() : "list must be empty.";

        List<Integer> intList = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        list.clear();
        list.addAll(intList);
        ListIterator<Integer> it = list.listIterator(4);
        assert it.hasNext() : "must have next.";
        assert it.hasPrevious() : "must have prev.";
        assert it.next() == 4 : "it.next() == 4";
        assert it.next() == 5 : "it.next() == 5";
        assert it.next() == 6 : "it.next() == 6";
        assert it.next() == 7 : "it.next() == 7";
        assert it.next() == 8 : "it.next() == 8";
        assert it.next() == 9 : "it.next() == 9";
        assert !it.hasNext() : "must not have next.";
        assert it.hasPrevious() : "must have prev.";
        assert it.previous() == 9 : "it.previous() == 9";
        assert it.previousIndex() == 8 : "it.previousIndex() == 8";
        while (it.hasPrevious())
            it.previous();
        assert !it.hasPrevious() : "must not have prev.";
        assert it.next() == 0 : "it.next() == 0";

        list.clear();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        assert !list.isEmpty() : "list must not be empty.";
        assert list.size() == 4 : "size() == 4";
        assert list.get(0) == 1 : "get(0) == 1";
        assert list.get(1) == 2 : "get(0) == 2";
        assert list.get(2) == 3 : "get(0) == 3";
        assert list.get(3) == 4 : "get(0) == 4";

        list.remove(0);
        assert list.size() == 3 : "size() == 3";
        assert list.get(0) == 2 : "get(0) == 2";
        list.remove(0);
        assert list.size() == 2 : "size() == 2";
        list.remove(0);
        assert list.size() == 1 : "size() == 1";
        list.remove(0);
        assert list.size() == 0 : "size() == 0";

        list.addAll(intList);
        assert list.size() == 10 : "size() == 10";
        assert list.get(5) == 5 : "get(5) == 5";

        List<Integer> oddList = Arrays.asList(1, 3, 5, 7, 9);
        list.retainAll(oddList);
        assert list.size() == 5 : "size() == 5";
        assert list.get(0) == 1 : "get(0) == 1";
        assert list.get(1) == 3 : "get(1) == 3";

        assert !list.contains(2) : "must not contain 2";
        assert list.containsAll(oddList) : "must contain all odd numbers.";

        list.removeAll(oddList);
        assert list.isEmpty() : "list must be empty. 2";

        list.addAll(0, oddList);
        assert list.size() == 5 : "size() == 5";
        list.addAll(list.size(), oddList);
        assert list.size() == 10 : "size() == 10";
        list.addAll(5, intList);
        assert list.size() == 20 : "size() == 20";

        List<Integer> sublist = list.subList(0, list.size());
        assert list.size() == sublist.size() : "list.size() == sublist.size()";
        // remove에서 first end 모두 null 됨
        sublist.remove(0);
        assert list.size() == sublist.size() : "list.size() == sublist.size()";
        sublist.remove(18);
        assert list.size() == sublist.size() : "list.size() == sublist.size()";
        assert list.size() == 18 : "list.size() == 18";
        sublist.add(9);
        assert list.size() == sublist.size() : "list.size() == sublist.size()";
        assert list.size() == 19 : "list.size() == 19";
        sublist.add(0, 1);

        Number[] n = new Number[1];
        n = list.toArray(n);
        assert n[0].intValue() == 1 : "n[0].intValue() == 1";
        assert n[18].intValue() == 7 : "n[17].intValue() == 7";

        sublist.clear();
        assert list.size() == sublist.size() : "list.size() == sublist.size()";
        assert list.size() == 0 : "list.size() == 0"; // sublist, list size: 0

        sublist.add(3);
        assert list.size() == sublist.size() : "list.size() == sublist.size()";
        assert list.size() == 1 : "list.size() == 1";
        sublist.add(0, 1);
        assert list.size() == sublist.size() : "list.size() == sublist.size()";
        assert list.size() == 2 : "list.size() == 2";

        sublist.remove(0);
        sublist.remove(0);
        assert list.size() == sublist.size() : "list.size() == sublist.size()";
        assert list.size() == 0 : "list.size() == 0"; // sublist, list size: 0

        list.addAll(intList); // list: 10 (0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
        sublist = list.subList(1, 9); // subList: 8 (1, 2, 3, 4, 5, 6, 7, 8)
        assert sublist.size() == 8 : "sublist.size() == 8";

        sublist.clear();
        assert sublist.size() == 0 : "sublist.size() == 0"; // list: 2 (0, 9)
        assert list.size() == sublist.size() + 2: "list.size() == sublist.size() + 2";

        sublist.add(1);
        assert sublist.size() == 1 : "sublist.size() == 1";
        assert list.size() == sublist.size() + 2: "list.size() == sublist.size() + 2";
        sublist.add(2);
        sublist.add(3);
        sublist.add(4);
        sublist.add(5);
        sublist.add(6);
        sublist.add(7);
        sublist.add(8);
        assert sublist.size() == 8 : "sublist.size() == 8"; // list: 10 (0, 1, 2, 3, 4, 5, 6, 7, 8, 9), subList: 8 (1, 2, 3, 4, 5, 6, 7, 8)
        assert list.size() == sublist.size() + 2: "list.size() == sublist.size() + 2";
        assert list.get(1) == 1 : "list.get(1) == 1";
        assert list.get(8) == 8 : "list.get(8) == 8";

        sublist.removeAll(oddList);
        assert list.size() == sublist.size() + 2: "list.size() == sublist.size() + 2"; // list: 5, subList: 4
        assert sublist.size() == 4 : "sublist.size() == 4";

        List<Integer> subsublist = sublist.subList(0, 2);
        subsublist.remove(0);
        assert (list.size() == sublist.size() + 2) && (sublist.size() == subsublist.size() + 2) : "all same size";
        subsublist.add(0, 2);
        assert (list.size() == sublist.size() + 2) && (sublist.size() == subsublist.size() + 2) : "all same size";
        assert subsublist.get(0) == 2 : "subsublist.get(0) == 2";
        subsublist.clear();
        assert (list.size() == sublist.size() + 2) && (sublist.size() == subsublist.size() + 2) : "all same size";
        assert list.get(2) == 8 : "list.get(2) == 8";

        System.out.println("OK.");
    }
}