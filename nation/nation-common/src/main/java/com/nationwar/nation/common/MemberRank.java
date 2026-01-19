package com.nationwar.nation.common;

/**
 * Represents the rank of a member within a nation.
 */
public enum MemberRank {
    /**
     * The leader of the nation, with full permissions.
     */
    LEADER,
    /**
     * An officer, with elevated permissions.
     */
    OFFICER,
    /**
     * A regular member of the nation.
     */
    MEMBER,
    /**
     * A new recruit, with limited permissions.
     */
    RECRUIT;
}
