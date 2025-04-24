package leetcode;

import leetcode.model.ListNode;

/**
 * 两两交换链表中的节点
 *
 * 给定一个链表，两两交换其中相邻的节点，并返回交换后的链表。
 *
 * 你不能只是单纯的改变节点内部的值，而是需要实际的进行节点交换。
 *
 * 示例:
 *
 * 给定 1->2->3->4, 你应该返回 2->1->4->3.
 *
 * 分析:
 * 当链表长度是偶数时，最后一对节点也要进行交换
 * 当链表长度为奇数时，最后一个节点不需要交换
 *
 *
 * 定义一个新链表，存储反转后的结果
 * 定义一个临时链表1，专门操作新链表的最后一个节点
 * 定义一个临时链表2，专门处理要反转的链表对
 */
public class SwapPairs {


    /**
     * 暴力解法
     * @param head
     * @return
     */
    public static ListNode swapPairs(ListNode head) {
        // 边界条件
        if (head == null) {
            return null;
        }

        // 边界条件 --> 只有一个元素
        if (head != null && head.next == null) {
            return head;
        }

        // 新链表
        ListNode newNode = null;

        // 操作新链表最后一个节点
        ListNode preNode = null;

        // 临时连表，操作要反转的链表对
        ListNode temporaryNode = null;

        int count = 0;
        while(head != null) {
            if (temporaryNode == null) {
                temporaryNode = new ListNode(head.val);
            } else {
                // 反转的代码
                ListNode node = new ListNode(head.val);
                node.next = temporaryNode;
                temporaryNode = node;
            }

            // 循环了两次，对2求整，则链表对反转完成
            if (++count >> 1 == 1) {

                if (newNode == null) {
                    newNode = temporaryNode;
                } else {
                    preNode.next = temporaryNode;
                }

                preNode = temporaryNode.next;
                count = 0;
                temporaryNode = null;
            } else if (head.next == null) {
                // 链表长度为奇数的情况
                preNode.next = head;
            }

            head = head.next;
        }

        return newNode;
    }

    /**
     * 优化
     * 因为是两两交换嘛，
     * 我可以循环链表的一半长度
     * 因为我在循环第一个元素时，我就能拿到它的next，
     * 然后再把自己放在它的next元素的next属性上，完成反转
     *
     * 然后再获取到上一次反转后的结果的最后一个元素，并把这次反转
     * 的结果放在它的next属性上
     *
     * @return
     */
    public static ListNode swapPairs2(ListNode head) {
        if (head == null) {
            return null;
        }

        // 边界条件 --> 只有一个元素
        if (head != null && head.next == null) {
            return head;
        }

        ListNode newNode = null;

        // 存储上一次反转后的最后一个节点的引用
        ListNode prevNode = null;

        // 临时链表，操作要反转的链表对
        ListNode temporaryNode = null;

        while (head != null) {
            // 完成反转
            ListNode node = new ListNode(head.val);
            ListNode nextNode = new ListNode(head.next.val);
            nextNode.next = node;

            temporaryNode = nextNode;

            if (newNode == null) {
                newNode = temporaryNode;
            } else {
                prevNode.next = temporaryNode;
            }

            prevNode = temporaryNode.next;

            head = head.next.next;

            if (head == null || head.next == null) {
                prevNode.next = head;
                break;
            }
        }

        return newNode;
    }

    /**
     * 参考他人的算法结果，看下执行效率和自己写的差别有多大
     * 发现和自己写的差不多。。
     *
     * 而且觉得自己写的思路还清晰点，因为每个变量名有自己的用途
     * 他人写的这一串，着实看着眼花，对能力要求高啊！！！
     *
     * @param head
     * @return
     */
    public static ListNode swapPairs3(ListNode head) {
        if (head == null) {
            return null;
        }

        // 边界条件 --> 只有一个元素
        if (head != null && head.next == null) {
            return head;
        }

        ListNode node = new ListNode(-1);
        ListNode res = node;
        while (head != null && head.next != null) {
            node.next = head.next;
            head.next = head.next.next;
            node.next.next = head;

            node = node.next.next;
            head = head.next;

        }

        return res.next;
    }

    /**
     * dummy -> 1 -> 2 -> 3 -> 4
     * prev = dummy
     *
     * 第一步：交换 1 和 2
     * first = 1, second = 2
     * 修改指针：
     * prev.next = second → dummy -> 2
     * first.next = second.next → 1 -> 3
     * second.next = first → 2 -> 1
     * 结果链表：dummy -> 2 -> 1 -> 3 -> 4
     * 更新 prev = first → prev = 1
     *
     * 第二步：交换 3 和 4
     * first = 3, second = 4
     * 修改指针：
     * prev.next = second → 1 -> 4
     * first.next = second.next → 3 -> null
     * second.next = first → 4 -> 3
     * 结果链表：dummy -> 2 -> 1 -> 4 -> 3
     * 更新 prev = first → prev = 3
     *
     * 结束循环：
     * prev.next == null 或 prev.next.next == null，退出循环。
     *
     * 返回结果：
     * 返回 dummy.next，即 2 -> 1 -> 4 -> 3。
     * @param head
     * @return
     */
    public static ListNode swapPairs4Final(ListNode head) {
        // 创建一个虚拟头节点
        ListNode dummy = new ListNode(0);
        dummy.next = head;

        // prev 指向当前要交换的两个节点的前一个节点
        ListNode prev = dummy;

        while (prev.next != null && prev.next.next != null) {
            // 找到要交换的两个节点
            ListNode first = prev.next;
            ListNode second = first.next;

            // 修改指针，完成交换
            prev.next = second;       // prev 指向 second
            first.next = second.next; // first 指向 second 的下一个节点
            second.next = first;      // second 指向 first

            // 移动 prev 到交换后的第二个节点
            prev = first;
        }

        // 返回新的链表头
        return dummy.next;
    }


    public static void main(String[] args) {
        ListNode node4 = new ListNode(4);
        ListNode node3 = new ListNode(3);
        ListNode node2 = new ListNode(2);
        ListNode node1 = new ListNode(1);
        node3.next = node4;
        node2.next = node3;
        node1.next = node2;

        System.out.println(swapPairs3(node1));
    }

}
