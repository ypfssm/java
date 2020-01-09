package com.ypf.offer;

public class LinkedQuestion {

    static class ListNode {
        int val;
        ListNode next;

        public ListNode(int val) {
            this.val = val;
        }
    }

    /**
     * 两个有序列表合并
     * @param l1
     * @param l2
     * @return
     */
    public ListNode mergeTwoSortedLists(ListNode l1, ListNode l2){
        ListNode dummy = new ListNode(-1), p = dummy;

        while (l1 != null && l2 != null) {
            if (l1.val < l2.val) {
                p.next = l1;
                l1 = l1.next;
            } else {
                p.next = l2;
                l2 = l2.next;
            }
        }

        if (l1 != null) {
            p.next = l1;
        }

        if (l2 != null) {
            p.next = l2;
        }

        return dummy.next;
    }



}
