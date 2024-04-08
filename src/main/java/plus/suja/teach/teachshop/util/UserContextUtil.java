package plus.suja.teach.teachshop.util;

import plus.suja.teach.teachshop.entity.Member;

public class UserContextUtil {
    private static ThreadLocal<Member> currentUser = new ThreadLocal<Member>();

    public static Member getCurrentUser() {
        return currentUser.get();
    }

    public static void setCurrentUser(Member currentUser) {
        UserContextUtil.currentUser.set(currentUser);
    }
}