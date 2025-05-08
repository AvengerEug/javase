package leetcode;

import leetcode.model.ListNode;
import leetcode.model.TreeNode;

import java.util.*;

/**
 * 给定一个排序链表，删除所有含有重复数字的节点，只保留原始链表中 没有重复出现 的数字。
 *
 * 示例1:
 * 输入: 1->2->3->3->4->4->5
 * 输出: 1->2->5
 *
 * 示例2:
 * 输入: 1->1->1->2->3
 * 输出: 2->3
 *
 * 方法一：
 *   最简单的方案：使用hash表，key为node的值，value为出现的次数
 *
 *   遍历完链表后，再将hash表中value为1的节点按顺序组成个链表
 *
 */
public class DeleteDuplicates {


    /**
     * 最简单的方案：使用hash表，key为node的值，value为出现的次数
     * 遍历完链表后，再将hash表中value为1的节点按顺序组成个链表
     *
     * 使用此种方式效率很低，但不可否认它是最容易想到的。
     * @param root
     * @return
     */
    public static ListNode deleteDuplForHash(ListNode root) {
        Map<Integer, Integer> hash = new LinkedHashMap<>();

        while (root != null) {
            if (hash.containsKey(root.val)) {
                int val = hash.get(root.val);
                hash.put(root.val, ++val);
            } else {
                hash.put(root.val, 1);
            }
            root = root.next;
        }

        ListNode prevNode = new ListNode(-1);
        ListNode tmp = prevNode;
        Iterator<Integer> iterator = hash.keySet().iterator();
        while (iterator.hasNext()) {
            int key = iterator.next();
            if (hash.get(key) == 1) {
                ListNode newNode = new ListNode(key);
                tmp.next = newNode;
                tmp = newNode;
            }
        }

        return prevNode.next;
    }


    /**
     * ListNode container = new ListNode(-1);
     * ListNode head = container;
     * ListNode preNode = null;
     *
     * prevNode    head   newNode
     *   null       -1      null
     *    -1         1       1
     *    1          2       2
     *    2          3       3
     *    2          3       3 ---- continue
     *    2          4       4 ---- continue
     *    2          4       4 ---- continue
     *    2          5       5
     *
     * @param root
     * @return
     */
    public static ListNode deleteDupl(ListNode root) {
        if (root == null) {
            return null;
        }

        if (root.next == null) {
            return root;
        }

        // 使用root.val来创建head指针，防止出现元素都相同的链表
        ListNode container = new ListNode(root.val);
        ListNode head = container;
        ListNode preNode = container;

        boolean isDupl = false;

        while (root != null) {
            if (head.val != root.val) {
                if (!isDupl) {
                    preNode = head;
                }

                head = root;
                preNode.next = head;

                isDupl = false;
            } else {
                isDupl = true;
            }

            root = root.next;
        }

        return container.next;
    }

    public static void main(String[] args) {
        ListNode node1 = new ListNode(1);
        ListNode node2 = new ListNode(1);
        node1.next = node2;

        ListNode node3 = new ListNode(1);
        node2.next = node3;

        ListNode node33 = new ListNode(2);
        node3.next = node33;

        ListNode node4 = new ListNode(2);
        node33.next = node4;

        ListNode node44 = new ListNode(3);
        node4.next = node44;

        ListNode node55 = new ListNode(4);
        node44.next = node55;

        //System.out.println(deleteDuplForHash(node1));
        System.out.println(deleteDuplForHash(node1));
    }
}
