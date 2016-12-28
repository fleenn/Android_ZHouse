package com.lemon.util.sdCard;

/**
 * 存储空间不足，此时写入操作失败
 * @author geolo
 *
 */
public class LimitSpaceUnwriteException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -360543214883668013L;

	public LimitSpaceUnwriteException() {
        super("存储空间不足，无法写入");
    }
}
