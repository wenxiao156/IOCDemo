package com.cmsz.upay.ioc.beans.exception;

/**
 * Bean相关的异常<br>
 * <b>答题者无需修改此类</b>
 * @author liyuanchang
 *
 */
public class BeansException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2937581172934285567L;

	/**
	 * 使用指定的异常信息创建BeansException实例
	 * @param msg 详细的异常信息
	 */
	public BeansException(String msg) {
		super(msg);
	}

	/**
	 * 使用指定的异常信息和导致异常的根异常创建BeansException实例
	 * @param msg 详细的异常信息
	 * @param cause 导致异常的根异常
	 */
	public BeansException(String msg, Throwable cause) {
		super(msg, cause);
	}

	/**
	 * getMessage()以及getCause()都equals则两个异常对象equals
	 */
	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof BeansException)) {
			return false;
		}
		BeansException otherBe = (BeansException) other;
		return getMessage().equals(otherBe.getMessage()) && 
				nullSafeEquals(this.getCause(), otherBe.getCause());
	}

	@Override
	public int hashCode() {
		return getMessage().hashCode();
	}
	
	private boolean nullSafeEquals(Object o1, Object o2) {
		if (o1 == o2) {
			return true;
		}
		if (o1 == null || o2 == null) {
			return false;
		}
		if (o1.equals(o2)) {
			return true;
		}
		return false;
	}
}
