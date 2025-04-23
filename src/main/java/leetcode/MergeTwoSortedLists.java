package leetcode;

import leetcode.model.ListNode;

/**
 * 题目: 合并两个有序链表
 * 分析: 题目中已经标明了是两个有序的链表了
 * 所以每个链表肯定是按照升序或者降序排列的
 *
 * 将两个升序链表合并为一个新的升序链表并返回。新链表是通过拼接给定的两个链表的所有节点组成的。
 *
 * 实例:
 * 输入：1->2->4, 1->3->4
 * 输出：1->1->2->3->4->4
 *
 */
public class MergeTwoSortedLists {

    public static void main(String[] args) {
        // 构建两个链表
        ListNode l13 = new ListNode(4);
        ListNode l12 = new ListNode(2);
        ListNode l11 = new ListNode(1);
        l12.next = l13;
        l11.next = l12;

        ListNode l23 = new ListNode(4);
        ListNode l22 = new ListNode(3);
        ListNode l21 = new ListNode(1);
        l22.next = l23;
        l21.next = l22;

        // System.out.println(merge(l11, l21));
        // System.out.println(iterate(l11, l21));
        ListNode merged = merged(l11, l21);
        System.out.println(merged);
    }

    /**
     * 递规的基本思想:
     *   把一个大型复杂的问题层层转化为一个与原问题相似的规模较小的问题来求解
     *
     * 递归解决:
     *   因为两个链表的合并，在大范围而言，是两个链表合并，将大的链表放在小的链表后面
     *   而链表是由一个个节点组成的，所以可以拆解成若干个子问题, 即把每个节点进行合并，
     *   将大的节点放在小的节点后面
     *
     * 时间复杂度 o(m + n)  m, n为链表的长度
     * 空间复杂度 o(m + n)  因为递归会产生方法调用栈，最终也取决于两个链表的长度
     *
     *
     * 递归的本指就是一个单一的思想：
     *   我们来分析下这个题目：将两个有序链表合并，返回的新链表仍然有序
     *
     *   链表这个数据结构不像数组，是连续的空间。它是分布在内存中的各个角落的，
     *   它是节点与节点之间的关联。
     *   其实链表就是一个对象：比如ListNode，只不过它内部有一些特殊的属性，比如
     *   next。next属性维护的其实是另外一个ListNode对象，也就是通过这些引用
     *   来维护了链表这个数据结构。
     *
     *   我们来分析下这两个链表
     *    * 链表1：1->2->4
     *    * 链表2：1->3->4
     *   说它是两个链表，还不如说它是两个对象，
     *   分别是listNode1和listNode2，只不过listNode1内部维护了一个属性listNode12，它的val为2
     *
     *
     *   于是我们来拆解这道题，两个链表排序其实就是将listNode1和listNode2进行排序
     *   也就是1和1进行排序
     *
     *   于是，我们定义：根据当前链表的val的比较大小，做出对应的引用修改
     *   if (listNode1.val == listNode2.val) {
     *       listNode1.next = listNode2;
     *   } else if (listNode1.val > listNode2.val) {
     *       listNode1.next = listNode2;
     *   } else {
     *       listNode2.next = listNode1;
     *   }
     *
     *   但是直接这么修改引用会出错，假设在listNode1.val == listNode2.val的情况下
     *   这就导致了listNode1的next直接指向了listNode2
     *   于是链表会变成 1 -> 1 -> 3 -> 4
     *   即listNode1中的2和4都被丢失了
     *
     *   所以我们应该定义一个方法：比较两个链表，然后升序返回
     *   即继续按照上面的逻辑，我们应该把代码修改成如下样子：
     *   if (listNode1.val == listNode2.val) {
     *       // 将listNode1的next指向 listNode.next和listNode2的比较结果
     *       listNode1.next = sortNode(listNode1.next, listNode2);
     *   } else if (listNode1.val > listNode2.val) {
     *       listNode1.next = sortNode(listNode1.next, listNode2);
     *   } else {
     *       // 将listNode2的next指向，listNode2.next和listNode1的比较结果
     *       listNode2.next = sortNode(listNode2.next, listNode1);
     *   }
     *
     *   我们在比较listNode1和listNode2时，
     *   如果他们相等，那么我就把listNode1的next指向一个排序后的链表，
     *   这个链表是什么呢？这个链表就是listNode1的next节点与listNode2的排序结果。
     *   我们假设此时listNode1和listNode2的链表结构仅为如下所示：
     *   listNode1: 1 -> 2
     *   listNode2: 1
     *   此时的listNode1.val == listNode2.val
     *   于是：listNode1.next = sortNode(listNode1.next, listNode2);
     *   还是用上面的说法来说：如果直接使用listNode1.next = listNode2
     *   即listNode1的next属性指向listNode2，那么listNode1之前的next属性
     *   就会被丢失，最终链表变成了 1 -> 1
     *
     *   所以我们的listNode1.next应该要指向listNode1.next与listNode2的比较结果
     *   listNode1.next与listNode2的比较结果为：listNode2的next要指向listNode1的next
     *   所以最终链表会变成 1 -> 1 -> 2
     *
     *   同时，因为使用会拿某个节点的next做比较，那么难免会出现链表尾部，即next为null的情况
     *   此时我们直接返回另外一个listNode即可。
     *   于是，就出现了下面的merge方法
     * @param listNode1
     * @param listNode2
     * @return
     */
    public static ListNode merge(ListNode listNode1, ListNode listNode2) {

        if (listNode1 == null) {
            return listNode2;
        } else if (listNode2 == null) {
            return listNode1;
        } else if (listNode1.val < listNode2.val) {
            // listNode1比listNode2小，所以此时将listNode2和listNode1.next的比较结果
            // 放在listNode1.next属性下
            listNode1.next = merge(listNode1.next, listNode2);
            return listNode1;
        } else {
            listNode2.next = merge(listNode2.next, listNode1);
            return listNode2;
        }
    }


    /**
     *
     * 再次温故算法，按照自己思路实现迭代解法
     *
     * 迭代解决：
     *
     *  * 输入：1->2->4, 1->3->4
     *  * 输出：1->1->2->3->4->4
     *
     *  创建一个虚拟节点，virtualListNode
     *
     *  比较两个节点，
     *  当listNode1和listNode2相比，
     *  将小的那个放在virtualListNode的next下，
     *  假设listNode1.val > listNode2
     *
     *  那么virtualListNode.next = listNode1.val
     *
     *  同时再将listNode1.next 与listNode2相比较
     *
     *  以此循环，这样的话，就能比较到链表中的每一个元素了
     *
     * @param listNode1
     * @param listNode2
     * @return
     */
    public static ListNode iterateAgain(ListNode listNode1, ListNode listNode2) {
        ListNode virtualListNode = new ListNode(-999);

        ListNode tmpListNode = virtualListNode;

        while (listNode1 != null && listNode2 != null) {
            if (listNode1.val > listNode2.val) {
                tmpListNode.next = listNode2;
                listNode2 = listNode2.next;
            } else if (listNode1.val <= listNode2.val) {
                tmpListNode.next = listNode1;
                listNode1 = listNode1.next;
            }

            tmpListNode = tmpListNode.next;
        }

        // 有可能出现listNode1 != null 或者 listNode2 != null的情况
        tmpListNode.next = listNode1 != null ? listNode1 : listNode2;


        return virtualListNode.next;
    }

    /**
     * 迭代解决:
     *   启动一个哑结点(用来连接迭代后的节点)
     *   启动一个preNode, 作为迭代器, 后续将使用它来
     *
     * @param listNode1
     * @param listNode2
     * @return
     */
    public static ListNode iterate(ListNode listNode1, ListNode listNode2) {
        // 用来存放排序后的链表
        ListNode preHead = new ListNode(-1);

        // 用来做比较和迭代, 指向上述new出来的preHead，用preNode来操作preHead
        ListNode preNode = preHead;

        while (listNode1 != null && listNode2 != null) {
            // 将小的一个节点移动至preNode的下一个节点
            if (listNode1.val > listNode2.val) {
                preNode.next = listNode2;

                // 后续将开始处理listNode2的next节点
                listNode2 = listNode2.next;

            } else {
                // listNode1.val <= listNode2.val的情况
                preNode.next = listNode1;
                listNode1 = listNode1.next;
            }

            // 获取preNode的下一个节点(当前比较后比较小的那个节点)，后续将对比较小的一个节点放在
            // preNode的下一个节点的next属性上
            // 这里修改了preNode的引用，我之前还困扰着说，preNode的引用都不指向PreHead了
            // 怎么还能修改preHead的值呢？
            // 后来还是自己基础不够，因为preHead是一个链表，preNode.next标识链表next属性指向的
            // 节点。preNode的确是不指向preHead了，但是它指向的是preHead的next属性的对象
            // 以此类推
            preNode = preNode.next;
        }

        // 上述while循环遍历完后，至少有一个节点没有被处理,
        // 假设遍历到listNode1的最后一个节点了，处理完后，发现它的next为null
        // 进而退出了循环，此时listNode2的最后一个节点还未处理，此时把它假如到preNode下即可
        preNode.next = listNode1 == null ? listNode2 : listNode1;

        // 获取preHead的下一个，因为第一个为-1，用来连接节点时使用的
        return preHead.next;
    }


    /**
     * 合并两个有序链表，把listNode2合并到listNode1中
     * @param listNode1
     * @param listNode2
     */
    public static ListNode merged(ListNode listNode1, ListNode listNode2) {
        if (listNode1 == null) {
            return listNode2;
        } else if (listNode2 == null) {
            return listNode1;
        } else if (listNode1.val > listNode2.val) {
            // 2 > 1  ==> 2要放在1后面。  所以为listNode2.next = merge(listNode2.next, listNode1)
            listNode2.next = merge(listNode2.next, listNode1);
            return listNode2;
        } else {
            // 大于等于的case
            listNode1.next = merge(listNode1.next, listNode2);
            return listNode1;
        }
    }

}
