package leetcode;


import leetcode.model.ListNode;

import java.util.HashSet;
import java.util.Set;

/**
 * 给定一个链表，判断链表中是否有环。
 *
 * 为了表示给定链表中的环，我们使用整数 pos 来表示链表尾连接到链表中的位置（索引从 0 开始）。 如果 pos 是 -1，则在该链表中没有环。
 *
 */
public class HasCycle {


    public static boolean hasCycle(ListNode head) {
        // 长度 < 1的链表肯定没有环
        if (head == null || head.next == null) {
            return false;
        }

        Set<ListNode> listNodes = new HashSet<>();

        while (head != null) {

            if (listNodes.contains(head)) {
                return true;
            }

            listNodes.add(head);

            head = head.next;
        }

        return false;
    }


    /**
     * 龟兔赛跑的模式
     * 定义两个指针，
     * 一个指针走一步
     * 另外一个指针走两步
     * 当链表遍历结束后，这两个指针没有相遇的话，则表示没有环
     * 若相遇了则表示有环
     *
     * 这里有几个关键点：
     * 1、链表遍历，因此是从head开始遍历，即fast = head; slow = head;
     * 2、快指针是基于自己的链表走，因此都是基于自己引用处理。eg：fast = fast.next.next
     * 3、慢指针是基于自己的链表走，因此都是基于自己引用处理。eg：slow = slow.next
     *
     *
     * 注意点: 快慢指针都是基于自己的那个链表走
     * 所以初始时，需要将head赋值给快慢指针
     * 在遍历head的同时，快慢指针自己想前走
     * 快指针走两步，慢指针走一步
     * 若在head遍历的情况下，快慢指针相遇了(hashCode一样)
     * 那么则表示有环
     *
     * 否则无环
     *
     * @param head
     * @return
     */
    public static boolean hasCycle2(ListNode head) {

        if (head == null || head.next == null) {
            return false; // 空链表或只有一个节点时，不可能有环
        }

        ListNode slow = head; // 慢指针
        ListNode fast = head; // 快指针

        // 遍历快指针
        while (fast != null && fast.next != null) {
            slow = slow.next;           // 慢指针走一步
            fast = fast.next.next;      // 快指针走两步

            if (slow == fast) {         // 如果快慢指针相遇，说明有环
                return true;
            }
        }

        return false; // 快指针到达链表末尾，说明无环
    }


    public static void main(String[] args) {
        ListNode node5 = new ListNode(5);
        ListNode node4 = new ListNode(4);
        ListNode node3 = new ListNode(3);
        ListNode node2 = new ListNode(2);
        ListNode node1 = new ListNode(1);
        node4.next = node5;
        node3.next = node4;
        node2.next = node3;
        node1.next = node2;

        // 创建环:
        node5.next = node3;

        System.out.println(hasCycle2(node1));
    }
}
