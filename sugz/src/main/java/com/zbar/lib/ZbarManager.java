package com.zbar.lib;

/**
 * 作者: 陈涛(1076559197@qq.com)
 * 
 * 时间: 2014年5月9日 下午12:25:46
 *
 * 版本: V_1.0.0
 *
 * 描述: zbar调用类
 */
public class ZbarManager {

	static {
		System.loadLibrary("zbar");
	}

	/**
	 *
	 * @param data
	 * @param width 照相机宽
	 * @param height 照相机高
	 * @param isCrop
	 * @param x x轴裁剪区域起始位置
	 * @param y y轴裁剪区域起始位置
	 * @param cwidth 裁剪区域宽
	 * @param cheight 裁剪区域高
	 * @return
	 */
	public native String decode(byte[] data, int width, int height, boolean isCrop, int x, int y, int cwidth, int cheight);
}
